package com.parking.ticketservice.controller;

import com.parking.common.domain.VehicleType;
import com.parking.ticketservice.dto.TicketRequestDTO;
import com.parking.ticketservice.entity.ParkingTicket;
import com.parking.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/check-in")
    @PreAuthorize("hasRole('PARKING_USER')") // Only users with this role can get here
    public ResponseEntity<ParkingTicket> checkIn(@Valid @RequestBody TicketRequestDTO request) {
        // We convert the String from JSON into our VehicleType Enum
        VehicleType type = VehicleType.valueOf(request.getType().toUpperCase());

        ParkingTicket ticket = ticketService.checkIn(request.getLicensePlate(), type);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @PostMapping("/{id}/check-out")
    @PreAuthorize("hasRole('PARKING_USER')") // Only users with this role can get here
    public ResponseEntity<ParkingTicket> checkOut(@PathVariable UUID id) {
        ParkingTicket ticket = ticketService.checkOut(id);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('PARKING_USER')") // Only users with this role can get here
    public String testSecurity() {
        return "If you see this, the TokenRelay worked! Hello from Ticket-Service.";
    }

    @GetMapping("/vehicle-types")
    public ResponseEntity<List<String>> getVehicleTypes() {
        List<String> types = Stream.of(VehicleType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }
}