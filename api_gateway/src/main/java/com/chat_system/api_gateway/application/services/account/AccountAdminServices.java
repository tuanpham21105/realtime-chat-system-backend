package com.chat_system.api_gateway.application.services.account;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chat_system.api_gateway.external_service.client.AccountWebClient;
import com.chat_system.api_gateway.external_service.dto.response.user_service.AccountDto;
import com.chat_system.api_gateway.presentation.dto.request.account.AccountRequest;

@Service
public class AccountAdminServices {
    private final AccountWebClient accountWebClient;

    public AccountAdminServices(AccountWebClient accountWebClient) {
        this.accountWebClient = accountWebClient;
    }

    public List<AccountDto> getAccounts(String keywords, int page, int size, int status, boolean ascSort) {
        return accountWebClient.getAccounts(keywords, page, size, status, ascSort);
    }

    public AccountDto getAccountById(String id) {
        return accountWebClient.getAccountById(id);
    }

    public AccountDto addAccount(AccountRequest newAccountRequest) {
        return accountWebClient.addAccount(newAccountRequest);
    }

    public AccountDto updateAccount(String id, AccountRequest accountRequest) {
        return accountWebClient.updateAccount(id, accountRequest);
    }

    public AccountDto updateAccountStatus(String id, boolean status) {
        return accountWebClient.updateAccountStatus(id, status);
    }

    public void deleteAccount(String id) {
        accountWebClient.deleteAccount(id);
    }
}
