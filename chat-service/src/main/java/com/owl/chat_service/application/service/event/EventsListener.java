package com.owl.chat_service.application.service.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.owl.chat_service.application.service.admin.chat.ControlChatAdminServices;
import com.owl.chat_service.application.service.admin.message.ControlMessageAdminServices;
import com.owl.chat_service.external_service.client.WebSocketGatewayApiClient;

@Component
public class EventsListener {
    private final ControlChatAdminServices controlChatAdminServices;
    private final ControlMessageAdminServices controlMessageAdminServices;
    private final WebSocketGatewayApiClient webSocketGatewayApiClient;

    public EventsListener(ControlChatAdminServices controlChatAdminServices, ControlMessageAdminServices controlMessageAdminServices, WebSocketGatewayApiClient webSocketGatewayApiClient) {
        this.controlChatAdminServices = controlChatAdminServices;
        this.controlMessageAdminServices = controlMessageAdminServices;
        this.webSocketGatewayApiClient = webSocketGatewayApiClient;
    }

    @EventListener(condition = "#event.name == 'ADD MESSAGE'")
    @Async
    public void handleAddMessageEvent(AddMessageEvent event) {
        controlChatAdminServices.updateChatNewestMessage(event.getMessage());
    }

    @EventListener(condition = "#event.name == 'ADD SYSTEM MESSAGE'")
    @Async
    public void handleAddSystemMessageEvent(SystemMessageEvent event) {
        controlMessageAdminServices.addNewSystemMessage(event.getChatId(), event.getContent());
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
