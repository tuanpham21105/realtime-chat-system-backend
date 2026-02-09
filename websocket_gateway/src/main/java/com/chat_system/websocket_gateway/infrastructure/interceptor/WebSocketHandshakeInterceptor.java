package com.chat_system.websocket_gateway.infrastructure.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final ValidateAccessTokenInterceptor validateAccessTokenInterceptor;

    public WebSocketHandshakeInterceptor(ValidateAccessTokenInterceptor validateAccessTokenInterceptor) {
        this.validateAccessTokenInterceptor = validateAccessTokenInterceptor;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String requesterId = validateAccessTokenInterceptor.validateAccessToken(request.getHeaders().getFirst("Authorization")).userId;

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
