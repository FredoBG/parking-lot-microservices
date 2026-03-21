package com.parking.bffservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginRedirectController {
    @GetMapping("/login")
    public String forwardToKeycloak() {
        // If someone types /login, send them to the Keycloak trigger
        return "redirect:/oauth2/authorization/keycloak";
    }
}