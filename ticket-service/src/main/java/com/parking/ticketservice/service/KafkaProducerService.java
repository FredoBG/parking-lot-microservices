package com.parking.ticketservice.service;

import com.parking.common.dto.ParkingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, ParkingEvent> kafkaTemplate;

    // It's a best practice to define the topic name as a constant
    private static final String TOPIC = "parking-events";

    public void publishParkingEvent(ParkingEvent event) {
        log.info("📤 Publishing to Kafka Topic [{}]: {}", TOPIC, event.getLicensePlate());

        // Using licensePlate as the key ensures all events for the same car
        // stay in the same Kafka partition (maintaining order).
        kafkaTemplate.send(TOPIC, event.getLicensePlate(), event);
    }
}
