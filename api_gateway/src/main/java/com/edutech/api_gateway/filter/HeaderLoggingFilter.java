package com.edutech.api_gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
@Order(1)
public class HeaderLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(HeaderLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        // Solo logear si es una petición con encabezados muy grandes
        int headerSize = calculateHeaderSize(request);
        if (headerSize > 8192) { // si el tamaño es mayor a 8KB
            logger.warn("Request with large headers ({} bytes) detected for URI: {}", 
                    headerSize, request.getRequestURI());
            
            // Opcional: logear encabezados específicos que podrían ser problemáticos
            if (request.getHeader("Cookie") != null) {
                logger.warn("Cookie header size: {} bytes", request.getHeader("Cookie").length());
            }
            if (request.getHeader("Authorization") != null) {
                logger.warn("Authorization header present with length: {} bytes", 
                        request.getHeader("Authorization").length());
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private int calculateHeaderSize(HttpServletRequest request) {
        int size = 0;
        Enumeration<String> headerNames = request.getHeaderNames();
        
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headerValues = request.getHeaders(headerName);
                size += headerName.length();
                
                while (headerValues.hasMoreElements()) {
                    String headerValue = headerValues.nextElement();
                    size += headerValue.length();
                }
            }
        }
        
        return size;
    }
}
