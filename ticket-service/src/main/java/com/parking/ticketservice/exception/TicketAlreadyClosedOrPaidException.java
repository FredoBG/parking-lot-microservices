package com.parking.ticketservice.exception;

import com.parking.common.exception.BaseParkingException;
import org.springframework.http.HttpStatus;
import java.util.UUID;

public class TicketAlreadyClosedOrPaidException extends BaseParkingException {
    public TicketAlreadyClosedOrPaidException(UUID ticketId) {
        super("No active parking ticket found with ID: " + ticketId, HttpStatus.BAD_REQUEST.value());
    }
}
