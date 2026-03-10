package com.parking.ticketservice.exception;

import com.parking.ticketservice.dto.ParkingErrorResponse; // No more confusion!
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ParkingErrorResponse> handleNotFound(TicketNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ParkingErrorResponse(404, ex.getMessage()));
    }

    @ExceptionHandler(VehicleAlreadyParkedException.class)
    public ResponseEntity<ParkingErrorResponse> handleConflict(VehicleAlreadyParkedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ParkingErrorResponse(400, ex.getMessage()));
    }
}
