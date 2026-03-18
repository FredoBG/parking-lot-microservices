package com.parking.ticketservice.repository;

import com.parking.common.domain.TicketStatus;
import com.parking.ticketservice.entity.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID; // Import UUID

@Repository
public interface TicketRepository extends JpaRepository<ParkingTicket, UUID> {
    // Custom query to find if a car is currently in the lot
    Optional<ParkingTicket> findByLicensePlateAndStatus(String licensePlate, TicketStatus status);
}