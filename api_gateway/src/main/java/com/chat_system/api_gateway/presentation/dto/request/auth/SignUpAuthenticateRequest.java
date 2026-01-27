package com.chat_system.api_gateway.presentation.dto.request.auth;

public class SignUpAuthenticateRequest {
    public String code;

    public SignUpAuthenticateRequest(String code) {
        this.code = code;
    }
}
