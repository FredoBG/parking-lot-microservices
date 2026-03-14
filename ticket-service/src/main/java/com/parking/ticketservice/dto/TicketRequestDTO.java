package com.parking.ticketservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.parking.ticketservice.validator.ValidateEnum;
import com.parking.ticketservice.domain.VehicleType;

@Data
public class TicketRequestDTO {
    @NotBlank(message = "License plate is mandatory")
    private String licensePlate;
    @ValidateEnum(enumClass = VehicleType.class) // No hardcoded message needed!
    private String type; // We'll receive "CAR", "MOTORCYCLE", etc.
}
