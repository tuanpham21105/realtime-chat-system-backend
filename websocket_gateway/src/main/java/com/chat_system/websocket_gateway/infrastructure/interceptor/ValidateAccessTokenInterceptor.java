package com.chat_system.websocket_gateway.infrastructure.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.chat_system.websocket_gateway.external_service.client.ValidateTokenWebClient;
import com.chat_system.websocket_gateway.external_service.dto.ValidateAccessTokenResponseDto;


@Component
public class ValidateAccessTokenInterceptor {
    private final ValidateTokenWebClient validateTokenWebClient;

    public ValidateAccessTokenInterceptor(ValidateTokenWebClient validateTokenWebClient) {
        this.validateTokenWebClient = validateTokenWebClient;
    }
    
    public ValidateAccessTokenResponseDto validateAccessToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing authorization header");

        String token = authHeader.substring(7);

        return validateTokenWebClient.validateAccessToken(token);
    }
}
