package com.edutech.microservicio_comunicacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Comunicaciones")
                        .version("1.0")
                        .description("API para gestionar mensajes y notificaciones en la plataforma Edu-Tech")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo Edu-Tech")
                                .email("desarrollo@edutech.com")));
    }
}