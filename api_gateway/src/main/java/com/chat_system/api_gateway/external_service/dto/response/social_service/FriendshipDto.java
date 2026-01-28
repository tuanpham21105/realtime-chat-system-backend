package com.chat_system.api_gateway.external_service.dto.response.social_service;

import java.time.Instant;

public class FriendshipDto {
    private String id;

    private String firstUserId;
    
    private String secondUserId;

    private Instant createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(String user_one_id) {
        this.firstUserId = user_one_id;
    }

    public String getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(String user_two_id) {
        this.secondUserId = user_two_id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
