package com.microservice.demo.config;

import com.microservice.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by: Tharindu Eranga
 * Date: 28 May 2022
 **/
@RequiredArgsConstructor
@Configuration
public class GatewayRouting {

    private final JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("AUTH-SERVICE", "/auth/**")
                        .filters(f -> f.filter(filter)).uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("PRODUCT-SERVICE", "/product/**")
                        .filters(f -> f.filter(filter)).uri("lb://PRODUCT-SERVICE"))
                .build();
    }

}
