package com.parking.common.domain;

/**
 * Represents the types of events that can occur within the parking system.
 * These are used as the 'Contract' for cross-service communication via Kafka.
 */
public enum ParkingActionType {
    CHECK_IN,
    CHECK_OUT,
    PAYMENT_PROCESSED,
    TICKET_CANCELLED
}