package com.parking.ticketservice.repository;

import com.parking.ticketservice.entity.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FailedEventRepository extends JpaRepository<FailedEvent, UUID> {
    // Standard JpaRepository gives us findAll(), save(), and delete() for free!
}
