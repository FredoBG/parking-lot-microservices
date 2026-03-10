package com.parking.ticketservice.entity;

import com.parking.ticketservice.domain.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Double totalFee;

    @Enumerated(EnumType.STRING) // Saves "CAR" or "MOTORCYCLE" as text
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING) // Saves "OPEN" or "CLOSED" as text
    private TicketStatus status;

    // Encapsulation: State mutation happens inside the object
    public void closeTicket(Double fee) {
        this.exitTime = LocalDateTime.now();
        this.totalFee = fee;
        this.status = TicketStatus.CLOSED;
    }
}
