package com.chat_system.api_gateway.domain.token;

import org.springframework.stereotype.Service;

import com.chat_system.api_gateway.external_service.dto.response.user_service.AccountDto;

@Service
public class TokenService {
    public TokenService() {}

    public String generateUserToken(AccountDto account) {
        return account.id;
    }
}
