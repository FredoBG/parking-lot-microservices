package com.parking.ticketservice.service.strategy;

import com.parking.ticketservice.domain.VehicleType;

public interface PricingStrategy {
    VehicleType getVehicleType();
    double calculatePrice(long hours);
}
