package com.parking.ticketservice.controller;

import com.parking.ticketservice.dto.TicketRequestDTO;
import com.parking.ticketservice.entity.ParkingTicket;
import com.parking.ticketservice.domain.VehicleType;
import com.parking.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/check-in")
    public ResponseEntity<ParkingTicket> checkIn(@RequestBody TicketRequestDTO request) {
        // We convert the String from JSON into our VehicleType Enum
        VehicleType type = VehicleType.valueOf(request.getType().toUpperCase());

        ParkingTicket ticket = ticketService.checkIn(request.getLicensePlate(), type);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @PostMapping("/{id}/check-out")
    public ResponseEntity<ParkingTicket> checkOut(@PathVariable Long id) {
        ParkingTicket ticket = ticketService.checkOut(id);
        return ResponseEntity.ok(ticket);
    }
}