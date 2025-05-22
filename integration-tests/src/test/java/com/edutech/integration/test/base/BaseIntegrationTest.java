package com.edutech.integration.test.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
public abstract class BaseIntegrationTest {

    private static final int API_GATEWAY_PORT = 8080;
    private static final String API_GATEWAY_HOST = "localhost";

    @Container
    protected static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("integration_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @BeforeAll
    public void setup() {
        RestAssured.baseURI = "http://" + API_GATEWAY_HOST;
        RestAssured.port = API_GATEWAY_PORT;
        
        // Inicialización para tests si es necesario
        initializeTestData();
    }
    
    /**
     * Método para inicializar datos de prueba.
     * Las clases hijas pueden sobrescribir este método si necesitan inicializar datos específicos.
     */
    protected void initializeTestData() {
        // Implementación por defecto está vacía
    }

    @AfterAll
    public void tearDown() {
        if (mysqlContainer != null && mysqlContainer.isRunning()) {
            mysqlContainer.stop();
        }
    }
}
