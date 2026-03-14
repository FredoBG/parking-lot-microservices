package com.parking.ticketservice.exception;

import java.util.UUID;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(UUID id) {
        super("No active parking ticket found with ID: " + id);
    }
}
