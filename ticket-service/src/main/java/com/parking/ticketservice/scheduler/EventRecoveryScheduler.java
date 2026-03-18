package com.parking.ticketservice.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.ticketservice.entity.FailedEvent;
import com.parking.ticketservice.repository.FailedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventRecoveryScheduler {

    private final FailedEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate; // Send the raw JSON
    private final ObjectMapper objectMapper;

    //@Scheduled(fixedDelay = 30000) // Run every 30 seconds
    @Transactional
    public void retryFailedEvents() {
        List<FailedEvent> failures = repository.findAll();

        if (failures.isEmpty()) return;

        log.info("Found {} failed events. Attempting recovery...", failures.size());

        for (FailedEvent fail : failures) {
            try {
                // Try sending to Kafka again
                kafkaTemplate.send("parking-events", fail.getTicketId().toString(), fail.getPayload());

                // If successful, delete from DB so we don't send it twice
                repository.delete(fail);
                log.info("Successfully recovered event for ticket: {}", fail.getTicketId());
            } catch (Exception e) {
                log.warn("Recovery failed again for {}. Will try in 30s.", fail.getTicketId());
                fail.setRetryCount(fail.getRetryCount() + 1);
                repository.save(fail);
            }
        }
    }
}
