package com.parking.bffservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        // Allow browser preflights
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Allow Vite development static resources
                        .pathMatchers("/src/**", "/@vite/**", "/@id/**", "/node_modules/**", "/assets/**", "/*.js", "/*.css", "/favicon.ico", "/@react-refresh").permitAll()
                        // Allow the frontend to check if a session exists without a 403
                        .pathMatchers("/auth/me").permitAll()
                        // Everything else requires the Keycloak session
                        .anyExchange().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
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
            builder.secure(false); // CRITICAL: Must be false for http://localhost
        });
        return resolver;
    }
}