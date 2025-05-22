package com.edutech.microservicio_persona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroservicioPersonaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioPersonaApplication.class, args);
	}

}
