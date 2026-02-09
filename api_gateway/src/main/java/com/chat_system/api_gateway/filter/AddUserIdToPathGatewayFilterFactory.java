package com.chat_system.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Order
public class AddUserIdToPathGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static final String PLACEHOLDER = "USERID_TEMP";

    public AddUserIdToPathGatewayFilterFactory() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {

        return (exchange, chain) -> {

            String userId = exchange.getAttributeOrDefault("X-User-Id", null);

            if (userId == null || userId.isBlank()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String originalPath = exchange.getRequest().getURI().getPath();

            if (!originalPath.contains(PLACEHOLDER)) {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

                return exchange.getResponse().setComplete();
            }
            
            String newPath = originalPath.replace(PLACEHOLDER, userId);

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().path(newPath).build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();
                
            return chain.filter(mutatedExchange);
        };
    }
}

