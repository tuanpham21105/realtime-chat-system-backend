package com.chat_system.websocket_gateway.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.chat_system.websocket_gateway.infrastructure.decorator.EmaWebSocketHandlerDecorator;
import com.chat_system.websocket_gateway.infrastructure.handler.WebSocketHandshakeHandler;
import com.chat_system.websocket_gateway.infrastructure.interceptor.WebSocketHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;
    private final WebSocketHandshakeHandler webSocketHandshakeHandler;

    public WebSocketConfig(WebSocketHandshakeInterceptor webSocketHandshakeInterceptor, WebSocketHandshakeHandler webSocketHandshakeHandler) {
        this.webSocketHandshakeInterceptor = webSocketHandshakeInterceptor;
        this.webSocketHandshakeHandler = webSocketHandshakeHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws")
            .addInterceptors(webSocketHandshakeInterceptor)
            .setHandshakeHandler(webSocketHandshakeHandler)
            .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {

            @Override
            public WebSocketHandler decorate(WebSocketHandler handler) {
                return new EmaWebSocketHandlerDecorator(handler);
            }
        });
    }
}
