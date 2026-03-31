package com.parking.bffservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable()) // Critical for local POST requests
                .authorizeExchange(exchanges -> exchanges
                        // 1. Allow the frontend to load
                        .pathMatchers("/", "/index.html", "/src/**", "/@vite/**", "/@id/**", "/node_modules/**", "/assets/**", "/*.js", "/*.css", "/favicon.ico", "/@react-refresh").permitAll()
                        // 2. Allow the session check to be reached
                        .pathMatchers("/auth/me").permitAll()
                        // 3. Everything else requires login
                        .anyExchange().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, e) -> {
                            // Send 401 instead of redirecting API calls to a login page
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        })
                )
                .build();
    }

    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("SESSION");
        resolver.addCookieInitializer((builder) -> {
            builder.path("/");
            builder.sameSite("Lax");
            builder.httpOnly(true);
            builder.secure(false); // REQUIRED for http://localhost
        });
        return resolver;
    }
}