package com.owl.social_service.application.event;

import com.owl.social_service.external_service.dto.WsMessageDto;

public class NotifyEvent extends Event {
    private String receiverId;
    private WsMessageDto message;
    public NotifyEvent(String name, String receiverId, WsMessageDto message) {
        super(name);
        this.receiverId = receiverId;
        this.message = message;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public WsMessageDto getMessage() {
        return message;
    }
    public void setMessage(WsMessageDto message) {
        this.message = message;
    }
}
