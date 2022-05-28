package com.microservice.demo.security;

/**
 * Created by: Tharindu Eranga
 * Date: 28 May 2022
 **/

import com.microservice.demo.exception.JwtTokenMalformedException;
import com.microservice.demo.exception.JwtTokenMissingException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> apiEndpoints = List.of("/register", "/login");

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            try {
                jwtUtil.validateToken(token);
            } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                e.printStackTrace();

                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            jwtUtil.getClaims(token).ifPresent(claims -> {
                final String headerId = String.valueOf(claims.get("id"));
                exchange.getRequest().mutate().header("id", headerId).build();
            });
        }

        return chain.filter(exchange);
    }

}
