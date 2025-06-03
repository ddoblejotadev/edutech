package com.edutech.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class DataFakerConfig {

    @Bean
    public Faker faker() {
        // Usar locale español para datos más apropiados para Chile
        return new Faker(new Locale("es", "CL"));
    }
}
