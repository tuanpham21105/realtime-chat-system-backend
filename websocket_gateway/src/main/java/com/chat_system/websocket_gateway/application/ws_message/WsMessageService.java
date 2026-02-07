package com.chat_system.websocket_gateway.application.ws_message;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.chat_system.websocket_gateway.presentation.dto.WsMessageDto;

@Service
public class WsMessageService {
    private final SimpMessagingTemplate messagingTemplate;

    public WsMessageService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToChat(String chatId, WsMessageDto message) {
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, message);
    }

    public void sendToUser(String userId, WsMessageDto message) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/private", message);
    }
}
