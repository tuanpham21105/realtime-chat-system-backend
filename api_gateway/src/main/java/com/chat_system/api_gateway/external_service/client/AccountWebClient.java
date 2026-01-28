package com.chat_system.api_gateway.external_service.client;


import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.external_service.dto.response.user_service.AccountDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.presentation.dto.request.account.AccountRequest;

import reactor.core.publisher.Mono;

@Service
public class AccountWebClient {
    private final WebClient webClient;

    public AccountWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.user).build();
    }

    public List<AccountDto> getAccounts(
            String keywords,
            int page,
            int size,
            int status,
            boolean ascSort
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account")
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("status", status)
                        .queryParam("ascSort", ascSort)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Account service error")))
                .bodyToMono(new ParameterizedTypeReference<List<AccountDto>>() {})
                .block();
    }

    // ---------------- GET BY ID ----------------
    public AccountDto getAccountById(String id) {

        return webClient.get()
                .uri("/account/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Account service error")))
                .bodyToMono(AccountDto.class)
                .block();
    }

    // ---------------- CREATE ----------------
    public AccountDto addAccount(AccountRequest request) {

        return webClient.post()
                .uri("/account")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Account service error")))
                .bodyToMono(AccountDto.class)
                .block();
    }

    // ---------------- UPDATE ----------------
    public AccountDto updateAccount(String id, AccountRequest request) {

        return webClient.put()
                .uri("/account/{id}", id)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Account service error")))
                .bodyToMono(AccountDto.class)
                .block();
    }

    // ---------------- UPDATE STATUS ----------------
    public AccountDto updateAccountStatus(String id, boolean status) {

        return webClient.patch()
                .uri("/account/{id}/status/{status}", id, status)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Account service error")))
                .bodyToMono(AccountDto.class)
                .block();
    }

    // ---------------- DELETE ----------------
    public String deleteAccount(String id) {

        return webClient.delete()
                .uri("/account/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Account service error")))
                .bodyToMono(String.class)
                .block();
    }
}
