package com.chat_system.api_gateway.presentation.dto.request.user_profile;

import java.time.LocalDate;

public class UserProfileRequest {
    public String name;
    public Boolean gender;
    public LocalDate dateOfBirth;
    public String email;
    public String phoneNumber;
}
