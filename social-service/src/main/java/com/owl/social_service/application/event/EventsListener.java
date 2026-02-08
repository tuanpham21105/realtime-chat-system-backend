package com.owl.social_service.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.owl.social_service.external_service.client.ChatServiceApiClient;
import com.owl.social_service.external_service.client.WebSocketGatewayApiClient;

@Component
public class EventsListener {
    private final ChatServiceApiClient chatServiceApiClient;
    private final WebSocketGatewayApiClient webSocketGatewayApiClient;

    public EventsListener(ChatServiceApiClient chatServiceApiClient, WebSocketGatewayApiClient webSocketGatewayApiClient) {
        this.chatServiceApiClient = chatServiceApiClient;
        this.webSocketGatewayApiClient = webSocketGatewayApiClient;
    }

    @EventListener(condition = "#event.name == 'CREATE FRIENDSHIP'")
    @Async
    public void handleCreateFriendshipEvent(CreateFriendshipEvent event) {
        chatServiceApiClient.createChat(event.getRequesterId(), event.getData());
    }

    @EventListener(condition = "#event.name == 'NOTIFY USER'")
    @Async
    public void handleNotifyUserEvent(NotifyEvent event) {
        webSocketGatewayApiClient.sendToUser(event.getReceiverId(), event.getMessage());
    }

    @EventListener(condition = "#event.name == 'NOTIFY CHAT'")
    @Async
    public void handleNotifyChatEvent(NotifyEvent event) {
        webSocketGatewayApiClient.sendToChat(event.getReceiverId(), event.getMessage());
    }
}
