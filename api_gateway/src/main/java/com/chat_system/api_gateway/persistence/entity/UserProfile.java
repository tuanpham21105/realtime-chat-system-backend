package com.chat_system.api_gateway.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserProfile {
    public String id;
    public Account account;
    public String name;
    public Boolean gender;
    public LocalDate dateOfBirth;
    public String avatar;
    public String email;
    public String phoneNumber;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}

