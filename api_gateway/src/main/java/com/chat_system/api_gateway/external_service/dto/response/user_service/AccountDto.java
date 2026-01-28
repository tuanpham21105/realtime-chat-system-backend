package com.chat_system.api_gateway.external_service.dto.response.user_service;

import java.time.LocalDateTime;

public class AccountDto{
    public String id;
    public Boolean status;
    public AccountRole role;

    public enum AccountRole {
        ADMIN,
        USER
    }
    public String username;
    public String password;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}
