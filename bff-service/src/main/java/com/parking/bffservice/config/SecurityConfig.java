package com.parking.bffservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Add this line for REACT localhost testing!
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // ADD THIS: Allow frames for H2 Console
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeExchange(exchange -> exchange
                        // ADD THIS: Permit all to H2 console paths
                        .pathMatchers("/h2-console/**").permitAll()
                        .pathMatchers("/login/**", "/oauth2/**").permitAll() // Allow auth flows
                        .anyExchange().authenticated()
                )
                // 1. CHANGE: Use oauth2Login instead of oauth2ResourceServer
                // This is what triggers the redirect to Keycloak
                .oauth2Login(Customizer.withDefaults())
                // 2. ADD THIS: It ensures the token is placed where the Gateway filter can find it
                .oauth2Client(Customizer.withDefaults())
                .oauth2Login(oauth2 -> {
                    // We add curly braces so the lambda returns 'void'
                    oauth2.authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("http://localhost:5173/"));
                })
                // --- ADD THIS LINE JUST FOR DEVELOPMENT (Bearer token support) ---
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // 3. ADD: This bypasses the "blue link" page and sends
                // unauthorized users straight to the Keycloak login box
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/keycloak"))
                )
                // Add this exceptionHandling for REACT localhost testing!
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, e) -> {
                            // Instead of redirecting to Keycloak (302), we send 401
                            // This triggers the React 'interceptor' we wrote above
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        })
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow your React app's specific origin
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        // CRITICAL: This allows the browser to send the session cookie to the BFF
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
