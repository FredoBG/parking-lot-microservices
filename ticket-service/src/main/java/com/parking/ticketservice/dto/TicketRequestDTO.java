package com.parking.ticketservice.dto;

import lombok.Data;

@Data
public class TicketRequestDTO {
    private String licensePlate;
    private String type; // We'll receive "CAR", "MOTORCYCLE", etc.
}
