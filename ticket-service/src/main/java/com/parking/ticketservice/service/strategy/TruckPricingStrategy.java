package com.parking.ticketservice.service.strategy;

import com.parking.common.domain.VehicleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class TruckPricingStrategy implements PricingStrategy {

    // Note the path matches your new YAML structure
    @Value("${parking.hourlyRates.truck:8.0}")
    private double truckRate;

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.TRUCK;
    }

    @Override
    public double calculatePrice(long hours) {
        return hours * truckRate;
    }
}

