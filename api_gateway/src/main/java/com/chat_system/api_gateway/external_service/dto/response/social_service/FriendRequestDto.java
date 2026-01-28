package com.chat_system.api_gateway.external_service.dto.response.social_service;

import java.time.Instant;

public class FriendRequestDto {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String senderId;

    private String receiverId;

    public enum FriendRequestStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    private FriendRequestStatus status;

    private Instant createdDate;

    private Instant updatedDate;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }
}
