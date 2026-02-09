package com.owl.user_service.persistence.jpa.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    private String id;
    private String userId;
    private Instant expiresDate;
    public RefreshToken() {
    }
    public RefreshToken(String id, String userId, Instant expiresDate) {
        this.id = id;
        this.userId = userId;
        this.expiresDate = expiresDate;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Instant getExpiresDate() {
        return expiresDate;
    }
    public void setExpiresDate(Instant expiresDate) {
        this.expiresDate = expiresDate;
    }    
}
