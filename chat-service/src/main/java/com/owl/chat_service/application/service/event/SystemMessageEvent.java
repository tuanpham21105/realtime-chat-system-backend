package com.owl.chat_service.application.service.event;

public class SystemMessageEvent extends Event {
    public SystemMessageEvent() {
        super.setName("ADD SYSTEM MESSAGE");
    }

    private String chatId;
    public String getChatId() {
        return chatId;
    }
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
