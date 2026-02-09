package com.chat_system.api_gateway.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.chat_system.api_gateway.external_service.client.ValidateTokenWebClient;

import reactor.core.publisher.Mono;

@Component
@Order
public class ValidateAccessTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final ValidateTokenWebClient validateTokenWebClient;

    public ValidateAccessTokenGatewayFilterFactory(ValidateTokenWebClient validateTokenWebClient) {
        super(Object.class);
        this.validateTokenWebClient = validateTokenWebClient;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                String body = """
                    {
                        "error": "Unauthorized",
                        "message": "Missing Authorization header"
                    }
                    """;

                DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(body.getBytes(StandardCharsets.UTF_8));

                return exchange.getResponse().writeWith(Mono.just(buffer));
            }

            String token = authHeader.substring(7);

            return validateTokenWebClient.validateAccessToken(token)
            .flatMap(response -> {
                String userId = response.userId;

                exchange.getAttributes().put("userId", userId);
                exchange.getAttributes().put("role", response.role);

                return chain.filter(exchange);
            })
            .onErrorResume(ex -> {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                String body = """
                    {
                        "error": "Unauthorized",
                        "message": "%s"
                    }
                    """.formatted(ex.getMessage());

                DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(body.getBytes(StandardCharsets.UTF_8));

                return exchange.getResponse().writeWith(Mono.just(buffer));
            });
        };
    }
    
}
