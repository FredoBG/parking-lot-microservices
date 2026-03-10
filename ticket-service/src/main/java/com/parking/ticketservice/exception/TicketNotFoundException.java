package com.parking.ticketservice.exception;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("No active parking ticket found with ID: " + id);
    }
}
