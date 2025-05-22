package cl.edutech.api_gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio que utiliza Circuit Breaker para gestionar las llamadas a microservicios externos.
 * Este enfoque previene fallos en cascada y mejora la resiliencia del sistema.
 */
@Service
public class MicroserviceProxyService {
    
    private static final Logger logger = LoggerFactory.getLogger(MicroserviceProxyService.class);
    
    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    
    public MicroserviceProxyService(RestTemplate restTemplate, CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }
    
    /**
     * Método para obtener datos de un microservicio con protección de circuit breaker
     * 
     * @param serviceUrl La URL del microservicio
     * @param fallback Respuesta alternativa en caso de fallo
     * @param responseType El tipo de dato esperado
     * @return La respuesta del microservicio o el fallback en caso de error
     */
    public <T> T getWithCircuitBreaker(String serviceUrl, T fallback, Class<T> responseType) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("microserviceCircuitBreaker");
        
        return circuitBreaker.run(
            () -> {
                logger.info("Calling microservice: {}", serviceUrl);
                return restTemplate.getForObject(serviceUrl, responseType);
            },
            throwable -> {
                logger.error("Error calling microservice: {}", serviceUrl, throwable);
                return fallback;
            }
        );
    }
    
    /**
     * Método para enviar datos a un microservicio con protección de circuit breaker
     * 
     * @param serviceUrl La URL del microservicio
     * @param request El objeto a enviar
     * @param fallback Respuesta alternativa en caso de fallo
     * @param responseType El tipo de dato esperado como respuesta
     * @return La respuesta del microservicio o el fallback en caso de error
     */
    public <T, R> R postWithCircuitBreaker(String serviceUrl, T request, R fallback, Class<R> responseType) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("microserviceCircuitBreaker");
        
        return circuitBreaker.run(
            () -> {
                logger.info("Posting to microservice: {}", serviceUrl);
                return restTemplate.postForObject(serviceUrl, request, responseType);
            },
            throwable -> {
                logger.error("Error posting to microservice: {}", serviceUrl, throwable);
                return fallback;
            }
        );
    }
}
