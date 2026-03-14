package com.parking.ticketservice.exception;

import java.util.UUID;

public class TicketAlreadyClosedOrPaidException extends RuntimeException {
    public TicketAlreadyClosedOrPaidException(UUID ticketId) {
        super("No active parking ticket found with ID: " + ticketId);
    }
}
