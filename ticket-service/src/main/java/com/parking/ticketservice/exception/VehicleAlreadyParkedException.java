package com.parking.ticketservice.exception;

public class VehicleAlreadyParkedException extends RuntimeException {
    public VehicleAlreadyParkedException(String licensePlate) {
        super("Vehicle with license plate " + licensePlate + " is already parked in the lot.");
    }
}
