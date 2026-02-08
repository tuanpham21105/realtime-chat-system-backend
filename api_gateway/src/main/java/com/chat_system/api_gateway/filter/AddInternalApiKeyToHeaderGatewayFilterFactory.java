package com.chat_system.api_gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Order
public class AddInternalApiKeyToHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Value("${internal-api-key}")
    private String internalApiKey;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-Internal-Api-Key", internalApiKey)
                .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

            return chain.filter(mutatedExchange);
        };
    }
    
}
