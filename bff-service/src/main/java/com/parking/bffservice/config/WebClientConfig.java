package com.parking.bffservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient gatewayWebClient(ClientRegistrationRepository clientRegistrations,
                                      OAuth2AuthorizedClientRepository authorizedClients) {

        // This filter grabs the JWT from the current user's session
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);

        oauth2.setDefaultClientRegistrationId("keycloak");

        return WebClient.builder()
                .baseUrl("http://localhost:8000") // Points to the Gateway
                .apply(oauth2.oauth2Configuration())
                .build();
    }
}
