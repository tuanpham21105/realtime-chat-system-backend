package com.chat_system.api_gateway.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external.service.api")
public class WebClientProperties {
    public String user;
    public String chat;
    public String social;
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getChat() {
        return chat;
    }
    public void setChat(String chat) {
        this.chat = chat;
    }
    public String getSocial() {
        return social;
    }
    public void setSocial(String social) {
        this.social = social;
    }
}
