package com.edutech.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir todos los orígenes para desarrollo - en producción deberías limitarlo
        config.addAllowedOrigin("*");
        // Para soportar credenciales, debes especificar orígenes exactos (no usar *)
        // config.addAllowedOrigin("http://localhost:19000"); // Para Expo en desarrollo
        // config.addAllowedOrigin("http://192.168.0.X:19000"); // IP local para desarrollo en dispositivo físico
        // config.addAllowedOrigin("https://app.edutech.com"); // Para producción
        
        // Permitir métodos HTTP comunes
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        
        // Permitir cabeceras comunes
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Authorization");
        
        // Exponer cabeceras de respuesta específicas
        config.addExposedHeader("Authorization");
        
        // Si necesitas enviar credenciales (cookies, encabezados de autorización)
        // config.setAllowCredentials(true);
        
        // Tiempo máximo que el navegador caché el resultado de la pre-solicitud CORS (en segundos)
        config.setMaxAge(3600L);
        
        // Aplicar esta configuración a todas las rutas
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
