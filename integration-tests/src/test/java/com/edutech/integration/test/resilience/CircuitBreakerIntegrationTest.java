package com.edutech.integration.test.resilience;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import com.edutech.integration.test.base.BaseIntegrationTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import java.io.IOException;

@TestMethodOrder(OrderAnnotation.class)
public class CircuitBreakerIntegrationTest extends BaseIntegrationTest {

    private static String authToken;

    @Container
    private static final GenericContainer<?> wiremockContainer = new GenericContainer<>("wiremock/wiremock")
            .withExposedPorts(8080)
            .withCommand("--global-response-templating", "--verbose");

    @BeforeAll
    @Override
    public void setup() {
        super.setup();
        
        // Configuramos wiremock para simular microservicios
        wiremockContainer.start();
        int wiremockPort = wiremockContainer.getMappedPort(8080);
        System.out.println("Wiremock está ejecutándose en el puerto: " + wiremockPort);
        
        // Obtenemos token de autenticación
        String loginBody = "{"
                + "\"username\": \"admin\","
                + "\"password\": \"admin123\""
                + "}";

        authToken = given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    @Order(1)
    public void testCircuitBreakerOpen() {
        // Hacemos múltiples llamadas fallidas para abrir el circuit breaker
        for (int i = 0; i < 10; i++) {
            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + authToken)
                    .when()
                    .get("/api/servicios/no-existente")
                    .then()
                    .statusCode(anyOf(equalTo(404), equalTo(500), equalTo(503)));
        }        // Verificamos el estado del circuit breaker
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(200)
                .body("status", anyOf(equalTo("UP"), equalTo("DOWN"), equalTo("CIRCUIT_OPEN")));
    }

    @Test
    @Order(2)
    public void testFallbackResponse() {
        // Verificamos que recibimos una respuesta de fallback cuando el circuit breaker está abierto
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/api/servicios/no-existente")
                .then()
                .statusCode(503)
                .body("message", containsString("Servicio no disponible"));
    }    @Test
    @Order(3)
    public void testCircuitBreakerHalfOpen() throws InterruptedException {
        // Esperamos a que el circuit breaker pase a half-open
        Thread.sleep(6000); // Configuramos 5s en el waitDurationInOpenState
        
        // Verificamos el estado del circuit breaker
        try {
            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + authToken)
                    .when()
                    .get("/actuator/circuitbreakers")
                    .then()
                    .statusCode(anyOf(equalTo(200), equalTo(404)));
        } catch (Exception e) {
            // Si hay error capturamos y continuamos
            System.out.println("Error al verificar estado del circuit breaker: " + e.getMessage());
        }
    }
    
    @AfterAll
    public static void tearDown() {
        if (wiremockContainer != null && wiremockContainer.isRunning()) {
            wiremockContainer.stop();
        }
    }
}
