package com.chat_system.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Order
public class AddRequesterIdToHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final String requestUserHeaderName = "requesterId";
    public AddRequesterIdToHeaderGatewayFilterFactory() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(requestUserHeaderName, userId)
                .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

            return chain.filter(mutatedExchange);
        };
    }
    
}
