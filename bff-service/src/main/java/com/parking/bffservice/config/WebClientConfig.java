package com.parking.bffservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient gatewayWebClient(ReactiveClientRegistrationRepository clientRegistrations,
                                      ServerOAuth2AuthorizedClientRepository authorizedClients) {

        // Use the SERVER/Reactive version of the filter
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);

        oauth2.setDefaultClientRegistrationId("keycloak");

        return WebClient.builder()
                .baseUrl("http://localhost:8000")
                .filter(oauth2) // Apply the filter directly
                .build();
    }
}