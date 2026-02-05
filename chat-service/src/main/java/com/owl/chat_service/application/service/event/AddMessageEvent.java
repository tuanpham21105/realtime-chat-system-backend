package com.owl.chat_service.application.service.event;

import com.owl.chat_service.persistence.mongodb.document.Message;

public class AddMessageEvent extends Event {
    private Message message;

    public AddMessageEvent() {
        super.setName("ADD MESSAGE");
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
