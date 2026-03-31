package com.parking.bffservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
public class AuthController {
    @GetMapping("/auth/me")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            // This is the signal for React to redirect to login
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // This is the signal for React to show the Dashboard
        return Map.of("username", principal.getAttribute("preferred_username"));
    }
}
