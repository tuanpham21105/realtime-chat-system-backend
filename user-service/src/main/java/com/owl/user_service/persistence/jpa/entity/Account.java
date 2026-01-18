package com.owl.user_service.persistence.jpa.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account")
public class Account extends EntityBase<Account> {

    protected Account() {
    }

    @Id
    @Column(nullable = false)
    private String id;

    @Column
    private Boolean status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role;

    public enum AccountRole {
        ADMIN,
        USER
    }

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "created_date", nullable = true, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date", nullable = true)
    private LocalDateTime updatedDate;

    public Account(String id, Boolean status, AccountRole role, String username, String password) {
        this.id = id;
        this.status = status;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    public AccountRole getRole() {
        return role;
    }
}
