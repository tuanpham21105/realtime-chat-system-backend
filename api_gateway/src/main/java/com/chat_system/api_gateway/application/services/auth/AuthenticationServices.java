package com.chat_system.api_gateway.application.services.auth;

import org.springframework.stereotype.Service;

import com.chat_system.api_gateway.domain.token.TokenService;
import com.chat_system.api_gateway.external_service.client.AuthWebClient;
import com.chat_system.api_gateway.presentation.dto.request.auth.SignInRequestDto;
import com.chat_system.api_gateway.presentation.dto.request.auth.SignUpRequestDto;
import com.chat_system.api_gateway.presentation.dto.response.auth.SignInResponseDto;

@Service
public class AuthenticationServices {
    private final AuthWebClient authWebClient;
    private final TokenService tokenService;

    public AuthenticationServices(TokenService tokenService, AuthWebClient authWebClient) {
        this.authWebClient = authWebClient;
        this.tokenService = tokenService;
    }

    public SignInResponseDto signIn(SignInRequestDto request) {
        SignInResponseDto response = new SignInResponseDto();
        response.token = tokenService.generateUserToken(authWebClient.signIn(request));

        return response;
    }

    public String signUp(SignUpRequestDto request) {
        return authWebClient.signUp(request).id;
    }

    public void signUpCodeAuthentic(String accountId, String code) {
        authWebClient.authenticateSignUp(accountId, code);
    }

    public void renewSignUpAuthenticateCode(String accountId) {
        authWebClient.renewAuthenticateCode(accountId);
    }
}
