# Actualización de Spring Boot y Solución de Problemas

## Cambios realizados

1. **Actualización de versiones en el POM padre:**
   - Spring Boot: 3.4.6 -> 3.5.0
   - Spring Cloud: 2023.0.1 -> 2023.0.3

2. **Corrección de advertencias de depreciación en `auth-service`:**
   - Se corrigió el problema con el constructor `DaoAuthenticationProvider()` depreciado
   - Se corrigió el problema con el método `setUserDetailsService()` depreciado
   - Se agregó `@SuppressWarnings("deprecation")` para suprimir las advertencias

## Explicación técnica

En Spring Boot 3.5.0 (que incluye Spring Security 6.1+), varios métodos de configuración de seguridad han sido marcados como deprecados, incluyendo:

- El constructor por defecto de `DaoAuthenticationProvider`
- El método `setUserDetailsService()` de `DaoAuthenticationProvider`

A pesar de que estos métodos están marcados como deprecados, aún no hay una alternativa clara en la API pública. Spring está trabajando en una nueva forma de configuración basada en builders, pero aún no está completamente implementada para todas las clases.

Por esta razón, se decidió mantener el enfoque actual pero agregar la anotación `@SuppressWarnings("deprecation")` para evitar las advertencias en el código.

## Recomendaciones para futuras actualizaciones

1. **Monitorear las notas de lanzamiento de Spring Security:**
   - Verificar si se lanzan nuevas APIs para reemplazar las funcionalidades deprecadas

2. **Considerar el uso de constructores de configuración:**
   - En versiones futuras, posiblemente puedas usar algo como:
   ```java
   DaoAuthenticationProvider.builder()
       .userDetailsService(userDetailsService)
       .passwordEncoder(passwordEncoder())
       .build();
   ```

3. **Mantener actualizadas las dependencias de seguridad:**
   - Las actualizaciones de seguridad son críticas para la protección de la aplicación

## Recursos útiles

- [Documentación de Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [Notas de lanzamiento de Spring Boot 3.5.0](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.5.0-M1-Release-Notes)
- [Guía de migración a Spring Security 6](https://docs.spring.io/spring-security/reference/migration-7/configuration.html)
