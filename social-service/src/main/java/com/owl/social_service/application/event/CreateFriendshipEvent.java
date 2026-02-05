package com.owl.social_service.application.event;

import com.owl.social_service.external_service.dto.ChatCreateRequestDto;

public class CreateFriendshipEvent extends Event {
    private String requesterId;
    private ChatCreateRequestDto data;

    public CreateFriendshipEvent(String name, String requesterId, ChatCreateRequestDto data) {
        super(name);
        this.requesterId = requesterId;
        this.data = data;
    }

    public ChatCreateRequestDto getData() {
        return data;
    }

    public void setData(ChatCreateRequestDto data) {
        this.data = data;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }
}
