package com.owl.user_service.presentation.dto.request;

public class AccountRequest {
    private String role;
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    private String username;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
