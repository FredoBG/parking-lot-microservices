package com.parking.gatewayservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/ticket-service") // <-- This accepts ALL methods (GET, POST, etc.)
    public Mono<String> ticketFallback() {
        return Mono.just("Ticket service is currently unavailable. Please try again later.");
    }
}
