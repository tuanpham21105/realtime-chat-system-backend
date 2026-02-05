package com.owl.social_service.external_service.dto;

import java.util.List;

public class ChatCreateRequestDto {
    public String name;
    public List<String> chatMembersId;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getChatMembersId() {
        return chatMembersId;
    }
    public void setChatMembersId(List<String> chatMembersId) {
        this.chatMembersId = chatMembersId;
    }
}
