package com.chat_system.api_gateway.external_service.client;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;

@Service
public class AccountWebClient {
    private final WebClient webClient;

    public AccountWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.user).build();
    }

    
}
