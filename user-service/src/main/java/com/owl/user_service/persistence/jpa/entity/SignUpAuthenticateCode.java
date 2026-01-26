package com.owl.user_service.persistence.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sign_up_authenticate_code")
public class SignUpAuthenticateCode {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "code", nullable = false, length = 9)
    private String code;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    // ===== Constructors =====

    public SignUpAuthenticateCode() {
    }

    public SignUpAuthenticateCode(String id, String code, LocalDateTime createdDate) {
        this.id = id;
        this.code = code;
        this.createdDate = createdDate;
    }

    // ===== Getters & Setters =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}

