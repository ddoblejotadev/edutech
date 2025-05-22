package com.edutech.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ApiGatewayApplication {

    public static void main(String[] args) {
        System.setProperty("server.tomcat.max-http-header-size", "262144");
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
