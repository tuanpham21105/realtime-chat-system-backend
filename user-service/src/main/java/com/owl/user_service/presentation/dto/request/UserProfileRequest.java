package com.owl.user_service.presentation.dto.request;

import java.time.LocalDate;

public class UserProfileRequest {
    private String name;
    private Boolean gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getGender() {
        return gender;
    }
    public void setGender(Boolean gender) {
        this.gender = gender;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
