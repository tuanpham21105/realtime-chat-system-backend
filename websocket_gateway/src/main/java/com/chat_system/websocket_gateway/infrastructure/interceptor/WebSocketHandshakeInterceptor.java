package com.chat_system.websocket_gateway.infrastructure.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String requesterId = request.getHeaders().getFirst("X-User-Id");

        // Allow connection even without requesterId, but store it if provided
        if (requesterId != null) {
            attributes.put("userId", requesterId);
        } else {
            // Set a default user ID for testing/anonymous connections
            attributes.put("userId", "anonymous-" + System.currentTimeMillis());
        }
        
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
    
}
