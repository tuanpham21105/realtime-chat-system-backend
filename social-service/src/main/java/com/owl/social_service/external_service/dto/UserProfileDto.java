package com.owl.social_service.external_service.dto;

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
