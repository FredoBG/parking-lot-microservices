package com.parking.ticketservice.service.strategy;

import com.parking.common.domain.VehicleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class MotorcyclePricingStrategy implements PricingStrategy {

    // Note the path matches your new YAML structure
    @Value("${parking.hourlyRates.motorcycle:5.0}")
    private double motorcycleRate;

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.CAR;
    }

    @Override
    public double calculatePrice(long hours) {
        return hours * motorcycleRate;
    }
}

