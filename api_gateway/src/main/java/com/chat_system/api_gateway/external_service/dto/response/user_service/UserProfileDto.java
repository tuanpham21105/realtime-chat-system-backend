package com.chat_system.api_gateway.external_service.dto.response.user_service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserProfileDto {
    public String id;
    public String name;
    public Boolean gender;
    public LocalDate dateOfBirth;
    public String avatar;
    public String email;
    public String phoneNumber;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}

