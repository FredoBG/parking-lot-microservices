package com.parking.ticketservice.service;

import com.parking.ticketservice.dto.ParkingEvent;
import com.parking.ticketservice.domain.VehicleType;       // Domain folder
import com.parking.ticketservice.domain.TicketStatus;      // Moved to Domain folder
import com.parking.ticketservice.domain.ParkingActionType; // New Domain Enum
import com.parking.ticketservice.entity.ParkingTicket;
import com.parking.ticketservice.exception.TicketAlreadyClosedOrPaidException;
import com.parking.ticketservice.exception.TicketNotFoundException;
import com.parking.ticketservice.exception.VehicleAlreadyParkedException;
import com.parking.ticketservice.repository.TicketRepository;
import com.parking.ticketservice.service.strategy.PricingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repository;
    private final List<PricingStrategy> strategies; // Spring injects ALL implementations!
    private final KafkaProducerService producerService;

    @Transactional
    public ParkingTicket checkIn(String licensePlate, VehicleType type) {
        // Logic: Check if vehicle is already in the lot
        log.info("Checking-in license: {}", licensePlate);
        repository.findByLicensePlateAndStatus(licensePlate, TicketStatus.OPEN)
                .ifPresent(t -> { throw new VehicleAlreadyParkedException(licensePlate); });

        ParkingTicket ticket = ParkingTicket.builder()
                .licensePlate(licensePlate)
                .vehicleType(type)
                .entryTime(LocalDateTime.now())
                .status(TicketStatus.OPEN)
                .build();

        ParkingTicket savedTicket = repository.save(ticket);

        // Map to Event using the SAME UUID
        ParkingEvent event = ParkingEvent.builder()
                .ticketId(savedTicket.getId()) // The shared UUID
                .licensePlate(savedTicket.getLicensePlate())
                .vehicleType(savedTicket.getVehicleType().name())
                .actionType(ParkingActionType.CHECK_IN)
                .timestamp(savedTicket.getEntryTime())
                .build();

        producerService.publishParkingEvent(event);
        return savedTicket;
    }

    @Transactional
    public ParkingTicket checkOut(UUID ticketId) {
        // Logic: Check vehicle before checkout
        log.info("Checking-out ticket: {}", ticketId);
        ParkingTicket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        // Business Logic: Ensure we don't check out twice
        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new TicketAlreadyClosedOrPaidException(ticketId);
        }

        // Calculate hours (Minimum 1 hour)
        long hours = Math.max(1, Duration.between(ticket.getEntryTime(), LocalDateTime.now()).toHours());

        // Use Strategy Pattern to find the right price
        double fee = strategies.stream()
                .filter(s -> s.getVehicleType() == ticket.getVehicleType())
                .findFirst()
                .map(s -> s.calculatePrice(hours))
                .orElse(0.0);

        ticket.closeTicket(fee);

        // Save the changes
        ParkingTicket savedTicket = repository.save(ticket);
        log.info("Ticket {} marked as CLOSED", ticketId);

        // Publish the Event
        ParkingEvent event = ParkingEvent.builder()
                .ticketId(savedTicket.getId()) // The shared UUID
                .licensePlate(savedTicket.getLicensePlate())
                .actionType(ParkingActionType.CHECK_OUT)
                .vehicleType(savedTicket.getVehicleType().name())
                .timestamp(savedTicket.getExitTime())
                .build();

        producerService.publishParkingEvent(event);

        return savedTicket;
    }
}
