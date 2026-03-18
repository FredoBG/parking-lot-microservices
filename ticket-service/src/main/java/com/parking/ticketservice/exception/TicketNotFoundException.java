package com.parking.ticketservice.exception;

import com.parking.common.exception.BaseParkingException;
import org.springframework.http.HttpStatus;
import java.util.UUID;

public class TicketNotFoundException extends BaseParkingException {
    public TicketNotFoundException(UUID id) {
        super("No active parking ticket found with ID: " + id, HttpStatus.NOT_FOUND.value());
    }
}
