package com.chat_system.api_gateway.domain.token;

import org.springframework.stereotype.Service;

import com.chat_system.api_gateway.persistence.entity.Account;

@Service
public class TokenService {
    public TokenService() {}

    public String generateUserToken(Account account) {
        return account.id;
    }
}
