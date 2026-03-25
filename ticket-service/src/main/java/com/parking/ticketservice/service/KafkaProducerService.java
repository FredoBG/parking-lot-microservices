package com.parking.ticketservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.common.dto.ParkingEvent;
import com.parking.ticketservice.entity.FailedEvent;
import com.parking.ticketservice.repository.FailedEventRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, ParkingEvent> kafkaTemplate;
    private final FailedEventRepository failedEventRepository; // New Repository
    private final ObjectMapper objectMapper; // To convert DTO to JSON String

    // It's a best practice to define the topic name as a constant
    private static final String TOPIC = "parking-events";

    // 1. Retry: If the network blips, try 3 times.
    // 2. CircuitBreaker: If Kafka is totally dead, stop hammering it.
    @Retry(name = "kafkaProducer", fallbackMethod = "fallbackKafka")
    @CircuitBreaker(name = "kafkaProducer", fallbackMethod = "fallbackKafka")
    public void publishParkingEvent(ParkingEvent event) {
        log.info("📤 Publishing to Kafka Topic [{}]: {}", TOPIC, event.getLicensePlate());

        // 1. Extract the User ID from the Security Context (Zero Trust source)
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Create the Record with Topic, Key (License Plate), and the Event
        ProducerRecord<String, ParkingEvent> record = new ProducerRecord<>(TOPIC, event.getLicensePlate(), event);

        // 3. Add the Identity Header
        record.headers().add("X-USER-ID", userId.getBytes(StandardCharsets.UTF_8));

        // 4. Send the full record instead of individual parameters
        kafkaTemplate.send(record);
    }


    // This method is called ONLY when all retries fail or the circuit is open.
    public void fallbackKafka(ParkingEvent event, Exception e) {
        log.error("CRITICAL ERROR: Could not send event to Kafka for ticket ID: {}. Reason: {}",
                event.getTicketId(), e.getMessage());
        log.error("KAFKA DOWN. Saving to DB for later: {}", event.getTicketId());

        // 1. Save this to a "failed_messages" table in H2.
        // 2. Have a background task try to re-send it later.
        try {
            FailedEvent failed = FailedEvent.builder()
                    .ticketId(event.getTicketId())
                    .payload(objectMapper.writeValueAsString(event)) // Convert to String
                    .createdAt(LocalDateTime.now())
                    .retryCount(0)
                    .build();
            failedEventRepository.save(failed);
        } catch (Exception jsonEx) {
            log.error("Could not even save to DB!", jsonEx);
        }
    }
}
