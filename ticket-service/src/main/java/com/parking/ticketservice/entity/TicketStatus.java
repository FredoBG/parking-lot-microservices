package com.parking.ticketservice.entity;

public enum TicketStatus {
    OPEN,    // Vehicle is currently in the parking lot
    CLOSED,  // Vehicle has exited and fee has been paid
    VOID     // For cancelled or invalid tickets
}
