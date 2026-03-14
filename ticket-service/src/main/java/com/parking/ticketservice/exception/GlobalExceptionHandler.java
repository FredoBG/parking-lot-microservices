package com.parking.ticketservice.exception;

import com.parking.ticketservice.dto.ParkingErrorResponse; // No more confusion!
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    @ExceptionHandler(TicketAlreadyClosedOrPaidException.class)
    public ResponseEntity<ParkingErrorResponse> handleConflict(TicketAlreadyClosedOrPaidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ParkingErrorResponse(400, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ParkingErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // This gets the dynamic message from our EnumValidator (e.g., "Allowed values are: [CAR, MOTORCYCLE]")
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ParkingErrorResponse(400, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ParkingErrorResponse> handleGlobalException(Exception ex) {
        // This is your safety net for bugs you didn't see coming
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ParkingErrorResponse(500, "An unexpected error occurred."));
    }

}
