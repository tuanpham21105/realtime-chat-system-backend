package com.owl.social_service.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.owl.social_service.external_service.client.ChatServiceApiClient;

@Component
public class EventsListener {
    private final ChatServiceApiClient chatServiceApiClient;

    public EventsListener(ChatServiceApiClient chatServiceApiClient) {
        this.chatServiceApiClient = chatServiceApiClient;
    }

    @EventListener(condition = "#event.name == 'CREATE FRIENDSHIP'")
    @Async
    public void handleCreateFriendshipEvent(CreateFriendshipEvent event) {
        chatServiceApiClient.createChat(event.getRequesterId(), event.getData());
    }
}
