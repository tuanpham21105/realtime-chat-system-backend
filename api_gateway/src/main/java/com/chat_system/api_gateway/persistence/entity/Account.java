package com.chat_system.api_gateway.persistence.entity;

import java.time.LocalDateTime;

public class Account{
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
