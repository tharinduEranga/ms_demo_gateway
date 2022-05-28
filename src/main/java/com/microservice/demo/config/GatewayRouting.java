package com.microservice.demo.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by: Tharindu Eranga
 * Date: 28 May 2022
 **/
@Configuration
public class GatewayRouting {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("AUTH-SERVICE", "/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("PRODUCT-SERVICE", "/product/**")
                        .uri("lb://PRODUCT-SERVICE"))
                .build();
    }

}
