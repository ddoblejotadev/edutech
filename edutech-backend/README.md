# EduTech Backend

## 📋 Requisitos del Sistema

- **Java 17** (obligatorio)
- **Maven 3.6+** (incluido wrapper)

## 🚀 Configuración Rápida para Nuevas Máquinas

### Opción 1: Script automático (recomendado)
```batch
# Windows
setup-env.bat

# Linux/Mac  
chmod +x setup-env.sh
./setup-env.sh
```

### Opción 2: Manual
```batch
# 1. Verificar Java 17
java -version

# 2. Configurar JAVA_HOME (si es necesario)
set JAVA_HOME=C:\Program Files\Java\jdk-17

# 3. Ejecutar con Maven wrapper (recomendado)
mvnw.cmd clean compile
mvnw.cmd spring-boot:run

# O con Maven local
mvn clean compile
mvn spring-boot:run
```

## 🔧 Solución de Problemas Comunes

### ❌ Error "Unsupported Java version" o "class file version"
**Causa**: La máquina está usando Java 24 en lugar de Java 17

**Solución**:
1. Instalar Java 17 si no está instalado
2. Configurar JAVA_HOME específicamente para Java 17
3. Usar Maven wrapper: `mvnw.cmd` en lugar de `mvn`

### ❌ Lombok no funciona / "Cannot find symbol"
**Causa**: Lombok no se está procesando correctamente

**Solución**:
1. Usar Maven wrapper que tiene la configuración correcta
2. Limpiar y recompilar: `mvnw.cmd clean compile`
3. En IDE: Instalar plugin de Lombok y habilitar annotation processing

### ❌ IDE Configuration
- **VS Code**: Usar extensión Java Extension Pack y configurar Java 17
- **IntelliJ**: File → Project Structure → Project SDK → Java 17
- **Eclipse**: Instalar Lombok y configurar Java 17

## 📂 Estructura del Proyecto

```
edutech-backend/
├── src/main/java/com/edutech/
├── src/main/resources/
├── pom.xml (configurado para Java 17 + Lombok)
├── mvnw.cmd (Maven wrapper Windows)
├── mvnw (Maven wrapper Linux/Mac)
└── setup-env.bat (script de configuración)
```

## 🧪 Verificación

```batch
# Verificar configuración
mvnw.cmd clean compile

# Ejecutar tests
mvnw.cmd test

# Ejecutar aplicación
mvnw.cmd spring-boot:run
```

## 💡 Notas Importantes

- ⚠️ **Siempre usar `mvnw.cmd` en lugar de `mvn`** para garantizar compatibilidad
- ⚠️ **Java 17 es obligatorio**, el proyecto no funcionará con otras versiones
- ⚠️ **Lombok requiere annotation processing habilitado** en el IDE