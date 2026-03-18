package com.parking.ticketservice.exception;

import com.parking.common.exception.BaseParkingException;
import org.springframework.http.HttpStatus;

public class VehicleAlreadyParkedException extends BaseParkingException {
    public VehicleAlreadyParkedException(String licensePlate) {
        super("Vehicle with license plate " + licensePlate + " is already parked in the lot.",
                HttpStatus.BAD_REQUEST.value());
    }
}
