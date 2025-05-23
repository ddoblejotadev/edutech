# Instrucciones para actualizar Spring Boot en el proyecto Edu-Tech

## Problema actual
1. La versión actual de Spring Boot es 3.4.6
2. Se recomienda actualizar a Spring Boot 3.5.0
3. Hay problemas con la compilación del microservicio_evaluacion
4. Posible incompatibilidad entre Java, procesadores de anotaciones (Lombok) y Spring Boot

## Solución paso a paso

### 1. Actualizar las versiones en el POM padre
Editar el archivo `c:\Users\ALUMNO\Documents\Edu-Tech\edutech\pom.xml`:
- Cambiar `<spring-boot.version>3.4.6</spring-boot.version>` a `<spring-boot.version>3.5.0</spring-boot.version>`
- Cambiar `<spring-cloud.version>2023.0.1</spring-cloud.version>` a `<spring-cloud.version>2023.0.1</spring-cloud.version>`

### 2. Mejorar la configuración del compilador Java en el POM padre
Modificar la configuración del plugin maven-compiler-plugin:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <source>${java.version}</source>
        <target>${java.version}</target>
        <parameters>true</parameters>
        <fork>true</fork>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </path>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>${mapstruct.version}</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

### 3. Actualizar la configuración del microservicio_evaluacion
Si el archivo del microservicio tiene su propia configuración de compilador, asegúrate de que sea compatible con la configuración del POM padre:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

### 4. Limpieza y compilación
Ejecutar estos comandos en orden:
```
cd c:\Users\ALUMNO\Documents\Edu-Tech\edutech
mvn clean
mvn compile -DskipTests
```

Si hay errores de compilación específicos, ejecutar:
```
mvn compile -DskipTests -rf :microservicio_evaluacion
```

### 5. Si persisten los problemas
Si continúan los problemas después de aplicar estos cambios, intenta:
1. Volver a la versión anterior de Spring Boot (3.4.6)
2. Revisar si hay incompatibilidades con la versión de Java instalada
3. Actualizar la versión de Lombok a la última versión compatible
4. Revisar y actualizar cualquier otra dependencia que pueda estar causando conflictos

### Nota importante
El error "java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN" suele estar relacionado con incompatibilidades entre versiones de Java y herramientas de anotación como Lombok. Asegúrate de que todas las versiones sean compatibles entre sí.
