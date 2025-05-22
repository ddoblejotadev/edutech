package com.edutech.api_gateway.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        
        // Añadir interceptor para logging o para añadir cabeceras si es necesario
        // interceptors.add(new LoggingInterceptor());
        
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos
        factory.setReadTimeout(30000); // 30 segundos
        
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(interceptors);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        
        return restTemplate;
    }
}
