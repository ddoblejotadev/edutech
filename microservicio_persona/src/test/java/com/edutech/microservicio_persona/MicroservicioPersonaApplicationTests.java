package com.edutech.microservicio_persona;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Usar un perfil de prueba para evitar dependencias externas
class MicroservicioPersonaApplicationTests {

    @Test
    void contextLoads() {
        // Test vacío para verificar que el contexto se carga correctamente
    }
}
