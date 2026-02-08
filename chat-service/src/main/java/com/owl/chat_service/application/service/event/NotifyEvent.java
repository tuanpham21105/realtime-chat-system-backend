package com.owl.chat_service.application.service.event;

import com.owl.chat_service.external_service.dto.WsMessageDto;

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
