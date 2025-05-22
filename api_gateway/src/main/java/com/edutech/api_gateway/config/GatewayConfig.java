package com.edutech.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
            .GET("/api-test", request -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body("API Gateway está funcionando correctamente"))
            .GET("/api/personas/**", request -> {
                URI uri = URI.create("http://localhost:8081" + request.path());
                return ServerResponse.temporaryRedirect(uri).build();
            })
            .POST("/api/personas/**", request -> {
                URI uri = URI.create("http://localhost:8081" + request.path());
                return ServerResponse.temporaryRedirect(uri).build();
            })
            .PUT("/api/personas/**", request -> {
                URI uri = URI.create("http://localhost:8081" + request.path());
                return ServerResponse.temporaryRedirect(uri).build();
            })
            .DELETE("/api/personas/**", request -> {
                URI uri = URI.create("http://localhost:8081" + request.path());
                return ServerResponse.temporaryRedirect(uri).build();
            })
            .build();
    }
}
