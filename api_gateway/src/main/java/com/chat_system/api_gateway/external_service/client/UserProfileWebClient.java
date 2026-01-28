package com.chat_system.api_gateway.external_service.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.external_service.dto.response.user_service.UserProfileDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.presentation.dto.request.user_profile.UserProfileCreateRequest;
import com.chat_system.api_gateway.presentation.dto.request.user_profile.UserProfileRequest;

import reactor.core.publisher.Mono;


@Service
public class UserProfileWebClient {
    private final WebClient webClient;

    public UserProfileWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.user).build();
    }

        // ---------- GET LIST ----------
    public List<UserProfileDto> getProfiles(
            String keywords,
            int page,
            int size,
            int gender,
            LocalDate dobStart,
            LocalDate dobEnd,
            boolean ascSort) {

        return webClient.get()
                .uri(uri -> uri
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("gender", gender)
                        .queryParam("dateOfBirthStart", dobStart)
                        .queryParam("dateOfBirthEnd", dobEnd)
                        .queryParam("ascSort", ascSort)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("User profile service error")))
                .bodyToMono(new ParameterizedTypeReference<List<UserProfileDto>>() {})
                .block();
    }

    // ---------- GET BY ID ----------
    public UserProfileDto getProfileById(String id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("User profile service error")))
                .bodyToMono(UserProfileDto.class)
                .block();
    }

    // ---------- CREATE ----------
    public UserProfileDto createProfile(UserProfileCreateRequest request) {
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("User profile service error")))
                .bodyToMono(UserProfileDto.class)
                .block();
    }

    // ---------- ADD TO ACCOUNT ----------
    public UserProfileDto addProfileToAccount(String accountId, UserProfileRequest request) {
        return webClient.post()
                .uri("/account/{id}", accountId)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("User profile service error")))
                .bodyToMono(UserProfileDto.class)
                .block();
    }

    // ---------- UPDATE ----------
    public UserProfileDto updateProfile(String id, UserProfileRequest request) {
        return webClient.put()
                .uri("/{id}", id)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("User profile service error")))
                .bodyToMono(UserProfileDto.class)
                .block();
    }

    // ---------- DELETE ----------
    public String deleteProfile(String id) {
        return webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("User profile service error")))
                .bodyToMono(String.class)
                .block();
    }

    // ---------- UPLOAD AVATAR ----------
    public String uploadAvatar(String id, MultipartFile file) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        return webClient.post()
                .uri("/{id}/avatar/upload", id)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("User profile service error")))
                .bodyToMono(String.class)
                .block();
    }

    // ---------- GET AVATAR ----------
    public ResponseEntity<Resource> getAvatar(String id) {
        return webClient.get()
                .uri("/{id}/avatar", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("User profile service error")))
                .toEntity(Resource.class)
                .block();
    }
}
