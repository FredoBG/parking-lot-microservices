package com.parking.ticketservice.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.parking.ticketservice.domain.ParkingActionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingEvent {
    // This ID is both the Database Primary Key and the Kafka Message ID
    private UUID ticketId;

    private String licensePlate;
    private ParkingActionType actionType; // Type-safe Enum
    private String vehicleType;  // Validated String from your Enum
    private LocalDateTime timestamp;
}
