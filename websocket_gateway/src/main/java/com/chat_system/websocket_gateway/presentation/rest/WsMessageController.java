package com.chat_system.websocket_gateway.presentation.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat_system.websocket_gateway.application.ws_message.WsMessageService;
import com.chat_system.websocket_gateway.presentation.dto.WsMessageDto;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/ws-message")
public class WsMessageController {
    private final WsMessageService wsMessageService;
    
    public WsMessageController(WsMessageService wsMessageService) {
        this.wsMessageService = wsMessageService;
    }

    @PostMapping("/user/{userId}")
    public Mono<Void> sendToUser(@PathVariable String userId, @RequestBody WsMessageDto messageDto) {
        wsMessageService.sendToUser(userId, messageDto);
        return Mono.empty();
    }

    @PostMapping("/chat/{chatId}")
    public Mono<Void> sendToChat(@PathVariable String chatId, @RequestBody WsMessageDto messageDto) {
        wsMessageService.sendToChat(chatId, messageDto);
        return Mono.empty();
    }
    
}
