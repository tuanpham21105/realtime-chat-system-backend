package com.chat_system.api_gateway.external_service.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.persistence.entity.Account;
import com.chat_system.api_gateway.persistence.entity.UserProfile;
import com.chat_system.api_gateway.presentation.dto.request.auth.SignInRequestDto;
import com.chat_system.api_gateway.presentation.dto.request.auth.SignUpAuthenticateRequest;
import com.chat_system.api_gateway.presentation.dto.request.auth.SignUpRequestDto;

import reactor.core.publisher.Mono;

@Service
public class AuthWebClient {
    private final WebClient webClient;

    public AuthWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.user).build();
    }

    // ---------------- SIGN IN ----------------
    public Account signIn(SignInRequestDto request) {

        return webClient.post()
                .uri("/auth/signin")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class).map(body -> new RuntimeException(body)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Auth service error")))
                .bodyToMono(Account.class)
                .block();
    }

    // ---------------- SIGN UP ----------------
    public UserProfile signUp(SignUpRequestDto request) {

        return webClient.post()
                .uri("/auth/signup")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class).map(body -> new RuntimeException(body)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Auth service error")))
                .bodyToMono(UserProfile.class)
                .block();
    }

    // -------- AUTHENTICATE SIGNUP CODE --------
    public void authenticateSignUp(String accountId, String code) {

        webClient.post()
                .uri("/auth/authentic/{accountId}", accountId)
                .bodyValue(new SignUpAuthenticateRequest(code))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class).map(body -> new RuntimeException(body)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Auth service error")))
                .toBodilessEntity()
                .block();
    }

    // -------- RENEW SIGNUP CODE --------
    public void renewAuthenticateCode(String accountId) {

        webClient.get()
                .uri("/auth/authentic/renew/{accountId}", accountId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class).map(body -> new RuntimeException(body)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Auth service error")))
                .toBodilessEntity()
                .block();
    }
}
