package com.edutech.api_gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest request, 
            @org.springframework.lang.NonNull HttpServletResponse response, 
            @org.springframework.lang.NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Generar ID único para la solicitud
        String requestId = UUID.randomUUID().toString();
        
        // Registrar la solicitud entrante
        log.info("Request: [{}] {} {} from {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());
        
        // Registrar tiempo de inicio
        long startTime = System.currentTimeMillis();
        
        try {
            // Continuar con la cadena de filtros
            filterChain.doFilter(request, response);
        } finally {
            // Calcular tiempo de procesamiento
            long duration = System.currentTimeMillis() - startTime;
            
            // Registrar la respuesta
            log.info("Response: [{}] status: {} completed in {} ms",
                    requestId,
                    response.getStatus(),
                    duration);
        }
    }
}