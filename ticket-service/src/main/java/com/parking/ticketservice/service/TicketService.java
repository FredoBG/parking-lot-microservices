package com.parking.ticketservice.service;

import com.parking.ticketservice.domain.VehicleType;
import com.parking.ticketservice.entity.ParkingTicket;
import com.parking.ticketservice.entity.TicketStatus;
import com.parking.ticketservice.exception.TicketNotFoundException;
import com.parking.ticketservice.exception.VehicleAlreadyParkedException;
import com.parking.ticketservice.repository.TicketRepository;
import com.parking.ticketservice.service.strategy.PricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repository;
    private final List<PricingStrategy> strategies; // Spring injects ALL implementations!

    @Transactional
    public ParkingTicket checkIn(String licensePlate, VehicleType type) {
        // Logic: Check if vehicle is already in the lot
        repository.findByLicensePlateAndStatus(licensePlate, TicketStatus.OPEN)
                .ifPresent(t -> { throw new VehicleAlreadyParkedException(licensePlate); });

        ParkingTicket ticket = ParkingTicket.builder()
                .licensePlate(licensePlate)
                .vehicleType(type)
                .entryTime(LocalDateTime.now())
                .status(TicketStatus.OPEN)
                .build();

        return repository.save(ticket);
    }

    @Transactional
    public ParkingTicket checkOut(Long ticketId) {
        ParkingTicket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        // Calculate hours (Minimum 1 hour)
        long hours = Math.max(1, Duration.between(ticket.getEntryTime(), LocalDateTime.now()).toHours());

        // Use Strategy Pattern to find the right price
        double fee = strategies.stream()
                .filter(s -> s.getVehicleType() == ticket.getVehicleType())
                .findFirst()
                .map(s -> s.calculatePrice(hours))
                .orElse(0.0);

        ticket.closeTicket(fee);
        return repository.save(ticket);
    }
}
