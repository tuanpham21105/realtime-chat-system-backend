package com.chat_system.api_gateway.external_service.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
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
                .bodyToMono(new ParameterizedTypeReference<List<UserProfileDto>>() {})
                .block();
    }

    // ---------- GET BY ID ----------
    public UserProfileDto getProfileById(String id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(UserProfileDto.class)
                .block();
    }

    // ---------- CREATE ----------
    public UserProfileDto createProfile(UserProfileCreateRequest request) {
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserProfileDto.class)
                .block();
    }

    // ---------- ADD TO ACCOUNT ----------
    public UserProfileDto addProfileToAccount(String accountId, UserProfileRequest request) {
        return webClient.post()
                .uri("/account/{id}", accountId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserProfileDto.class)
                .block();
    }

    // ---------- UPDATE ----------
    public UserProfileDto updateProfile(String id, UserProfileRequest request) {
        return webClient.put()
                .uri("/{id}", id)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserProfileDto.class)
                .block();
    }

    // ---------- DELETE ----------
    public void deleteProfile(String id) {
        webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // ---------- UPLOAD AVATAR ----------
    public void uploadAvatar(String id, MultipartFile file) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        webClient.post()
                .uri("/{id}/avatar/upload", id)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // ---------- GET AVATAR ----------
    public ResponseEntity<Resource> getAvatar(String id) {
        return webClient.get()
                .uri("/{id}/avatar", id)
                .retrieve()
                .toEntity(Resource.class)
                .block();
    }
}
