package com.parking.ticketservice.repository;

import com.parking.ticketservice.entity.ParkingTicket;
import com.parking.ticketservice.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<ParkingTicket, Long> {
    // Custom query to find if a car is currently in the lot
    Optional<ParkingTicket> findByLicensePlateAndStatus(String licensePlate, TicketStatus status);
}