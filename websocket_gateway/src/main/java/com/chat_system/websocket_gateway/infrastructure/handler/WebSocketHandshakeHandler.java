package com.chat_system.websocket_gateway.infrastructure.handler;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Component
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String userId = (String) attributes.get("userId");
        
        // Ensure we always have a userId
        if (userId == null || userId.isEmpty()) {
            userId = "anonymous-" + System.currentTimeMillis();
        }
        
        String finalUserId = userId;
        return () -> finalUserId;
    }
}
