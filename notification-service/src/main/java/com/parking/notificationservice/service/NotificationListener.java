package com.parking.notificationservice.service;

import com.parking.common.dto.ParkingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationListener {

    @KafkaListener(topics = "parking-events", groupId = "notification-group")
    public void handleParkingEvent(ParkingEvent event, @Header("X-USER-ID") String userId) {
        log.info("🔔 Notification for User [{}]: TicketId {} has checked in.", userId, event.getTicketId());

        switch (event.getActionType()) {
            case CHECK_IN:
                sendWelcomeMessage(event);
                break;
            case CHECK_OUT:
                sendGoodbyeMessage(event);
                break;
            default:
                log.warn("Unknown action type: {}", event.getActionType());
        }
    }

    private void sendWelcomeMessage(ParkingEvent event) {
        log.info("📧 NOTIFICATION: Welcome! Vehicle {} has entered at {}. Ticket ID: {}",
                event.getLicensePlate(), event.getTimestamp(), event.getTicketId());
    }

    private void sendGoodbyeMessage(ParkingEvent event) {
        log.info("📧 NOTIFICATION: Goodbye! Vehicle {} has exited at {}. We hope to see you again!",
                event.getLicensePlate(), event.getTimestamp());
    }
}