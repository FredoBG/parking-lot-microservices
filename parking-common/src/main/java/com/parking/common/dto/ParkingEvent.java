package com.parking.common.dto;

import com.parking.common.domain.ParkingActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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
