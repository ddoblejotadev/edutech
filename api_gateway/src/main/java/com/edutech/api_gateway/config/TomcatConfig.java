package com.edutech.api_gateway.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return (factory) -> {
            factory.addConnectorCustomizers(connector -> {
                connector.setMaxPostSize(10485760); // 2MB 
                connector.setProperty("maxHttpHeaderSize", "262144"); // 64KB
            });
        };
    }
}
