package com.parking.ticketservice.service.strategy;

import com.parking.common.domain.VehicleType;

public interface PricingStrategy {
    VehicleType getVehicleType();
    double calculatePrice(long hours);
}
