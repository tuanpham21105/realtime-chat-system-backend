package com.owl.user_service.persistence.jpa.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_profile")
public class UserProfile extends EntityBase<UserProfile> {

    protected UserProfile() {
    }

    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
    private Account account;

    @Column(nullable = false)
    private String name;

    @Column
    private Boolean gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column
    private String avatar;

    @Column(nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @CreatedDate
    @Column(name = "created_date", nullable = true, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date", nullable = true)
    private LocalDateTime updatedDate;

    public UserProfile(
            Account account,
            String name,
            Boolean gender,
            LocalDate dateOfBirth,
            String avatar,
            String email,
            String phoneNumber
    ) {
        this.account = account;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public Boolean getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
}

