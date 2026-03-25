package com.parking.bffservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/login/**", "/oauth2/**").permitAll() // Allow auth flows
                        .anyExchange().authenticated()
                )
                // 1. CHANGE: Use oauth2Login instead of oauth2ResourceServer
                // This is what triggers the redirect to Keycloak
                .oauth2Login(Customizer.withDefaults())
                // 2. ADD THIS: It ensures the token is placed where the Gateway filter can find it
                .oauth2Client(Customizer.withDefaults())
                // --- ADD THIS LINE JUST FOR DEVELOPMENT (Bearer token support) ---
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // 3. ADD: This bypasses the "blue link" page and sends
                // unauthorized users straight to the Keycloak login box
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/keycloak"))
                )
                .build();
    }
}
