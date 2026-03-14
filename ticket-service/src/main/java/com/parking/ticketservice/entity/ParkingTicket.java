package com.parking.ticketservice.entity;

import com.parking.ticketservice.domain.TicketStatus;
import com.parking.ticketservice.domain.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parking_tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingTicket {

    @Id
    @GeneratedValue // Hibernate will handle the UUID generation
    @Column(updatable = false, nullable = false)
    private UUID id;

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
