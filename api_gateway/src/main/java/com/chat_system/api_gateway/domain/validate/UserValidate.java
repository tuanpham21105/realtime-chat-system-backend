package com.chat_system.api_gateway.domain.validate;

import org.springframework.stereotype.Service;

import com.chat_system.api_gateway.domain.account.AccountRoleService;
import com.chat_system.api_gateway.external_service.client.AccountWebClient;
import com.chat_system.api_gateway.external_service.dto.response.user_service.AccountDto;
import com.chat_system.api_gateway.external_service.dto.response.user_service.AccountDto.AccountRole;

@Service
public class UserValidate {
    private final AccountWebClient accountWebClient;

    public UserValidate(AccountWebClient accountWebClient) {
        this.accountWebClient = accountWebClient;
    }

    public AccountDto validateToken(String token, AccountRole role) {
        AccountDto account = accountWebClient.getAccountById(token);

        if (account == null)
            throw new IllegalArgumentException("Invalid token");

        if (AccountRoleService.compareRole(account.role, role) >= 0)
            throw new SecurityException("User does not have permission to access this API");

        return account;
    }
}
