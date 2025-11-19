````prompt
---
agent: agent
---

# Prompt para Configuración Docker - EjercicioCopilot

## Contexto del Proyecto

Estamos desarrollando **EjercicioCopilot**, un generador de excusas tech con Spring Boot 3.2.0 y Java 21.
La arquitectura sigue el patrón **Hexagonal (Ports & Adapters)** con Clean Code y SOLID principles.

La aplicación está diseñada para ser **containerizada con Docker** siguiendo las mejores prácticas de seguridad,
rendimiento y portabilidad.

## Objetivo

Configurar Docker para **EjercicioCopilot** de manera profesional, incluyendo:
- Dockerfile multi-stage optimizado
- Docker Compose para orquestación local
- .dockerignore para reducir contexto de build
- Configuración de networks, volumes y variables de entorno
- Integración con bases de datos (H2 en memoria o PostgreSQL opcional)
- Logging y health checks

## Requisitos Principales

### 1. Dockerfile - Multi-Stage Build

El Dockerfile debe seguir el patrón **multi-stage** para optimizar:
- **Stage 1 (Builder)**: Compilar el proyecto con Maven
- **Stage 2 (Runtime)**: Imagen ligera con solo el JAR ejecutable

```dockerfile
# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/ejerciciocopilot-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 2. Características Requeridas

#### 2.1 Base Images
- **Builder**: `maven:3.9.4-eclipse-temurin-21` (incluye Maven + Java 21)
- **Runtime**: `eclipse-temurin:21-jre-jammy` (imagen minimal de Java 21)

#### 2.2 Optimizaciones
- **Caché de dependencias Maven**: Copiar pom.xml antes que src
- **No instalar dependencias innecesarias**: Solo JRE en runtime
- **Reducir tamaño final**: Usar --no-cache-dir, rm -rf /tmp, etc.
- **Usar .dockerignore**: Excluir archivos innecesarios del contexto

#### 2.3 Seguridad
- **Usuario no-root**: Ejecutar como usuario específico (no root)
- **Permisos restrictivos**: Solo los necesarios para ejecutar la app
- **Variables de entorno**: Para configuración sensible (no hardcoded)
- **Health checks**: Implementar endpoint /actuator/health

#### 2.4 Puertos
- **Puerto 8080**: API REST principal
- **Puerto 8081**: Alternativo si es necesario

#### 2.5 Variables de Entorno
```
JAVA_OPTS                  # Opciones JVM
SPRING_PROFILES_ACTIVE     # Perfil Spring (dev, prod, docker)
SERVER_PORT                # Puerto de la aplicación
DATABASE_URL               # URL de la BD (H2 o PostgreSQL)
H2_CONSOLE_ENABLED         # Habilitar/deshabilitar H2 Console
```

#### 2.6 Volumes
- `/app/logs`: Para persistencia de logs (opcional)
- `/app/data`: Para datos H2 (si no está en memoria)

### 3. Docker Compose

Configurar `docker-compose.yml` para orquestación local:

```yaml
version: '3.8'

services:
  # Servicio principal de la aplicación
  ejerciciocopilot:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: ejerciciocopilot-app
    ports:
      - "8080:8080"
    environment:
      JAVA_OPTS: "-Xmx512m -Xms256m"
      SPRING_PROFILES_ACTIVE: docker
      SERVER_PORT: 8080
    networks:
      - ejerciciocopilot-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s

  # Postgres (opcional, para futura expansión)
  # postgres:
  #   image: postgres:15-alpine
  #   container_name: ejerciciocopilot-db
  #   environment:
  #     POSTGRES_DB: ejerciciocopilot
  #     POSTGRES_USER: app
  #     POSTGRES_PASSWORD: secretpassword
  #   ports:
  #     - "5432:5432"
  #   volumes:
  #     - postgres_data:/var/lib/postgresql/data
  #   networks:
  #     - ejerciciocopilot-network
  #   restart: unless-stopped

networks:
  ejerciciocopilot-network:
    driver: bridge

# volumes:
#   postgres_data:
```

### 4. .dockerignore

Excluir archivos innecesarios del contexto de build:

```
.git
.gitignore
.github
.idea
.vscode
*.log
*.iml
*.swp
*.swo
*~
.DS_Store
target/
build/
dist/
node_modules/
.env
.env.local
.env.*.local
docker-compose.override.yml
*.md
docs/
```

### 5. application-docker.properties

Perfil específico para ejecución en Docker:

```properties
# Servidor
server.port=8080
server.servlet.context-path=/
server.shutdown=graceful

# Base de datos H2 (en memoria para desarrollo)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.root=INFO
logging.level.com.ejerciciocopilot=DEBUG

# Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

### 6. Construcción y Ejecución

#### Build de imagen Docker
```bash
# Construir la imagen
docker build -t ejerciciocopilot:1.0.0 .

# Tag adicional para latest
docker tag ejerciciocopilot:1.0.0 ejerciciocopilot:latest

# Verificar imagen
docker images | grep ejerciciocopilot
```

#### Ejecutar contenedor individual
```bash
# Ejecución simple
docker run -p 8080:8080 ejerciciocopilot:latest

# Con variables de entorno
docker run -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m" \
  -e SPRING_PROFILES_ACTIVE=docker \
  ejerciciocopilot:latest

# Con volúmenes para logs
docker run -p 8080:8080 \
  -v $(pwd)/logs:/app/logs \
  ejerciciocopilot:latest
```

#### Orquestación con Docker Compose
```bash
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f ejerciciocopilot

# Detener servicios
docker-compose down

# Forzar rebuild
docker-compose up -d --build
```

### 7. Health Checks

Implementar endpoint de salud en Spring Boot:

```java
// Endpoint para health check
@GetMapping("/health")
public ResponseEntity<Map<String, String>> health() {
    return ResponseEntity.ok(Map.of(
        "status", "UP",
        "app", "EjercicioCopilot",
        "version", "1.0.0"
    ));
}
```

O usar Spring Boot Actuator (ya incluido en dependencias):
```bash
# Verificar salud
curl http://localhost:8080/actuator/health
```

### 8. Configuración de Red

- **Driver bridge**: Conecta contenedores en la misma red
- **DNS interno**: Los contenedores pueden comunicarse por nombre de servicio
- **Aislamiento**: Red separada del host

### 9. Manejo de Logs

Los logs deben ser visibles en consola:

```properties
# En application-docker.properties
logging.file.name=/app/logs/aplicacion.log
logging.level.root=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

Acceso a logs desde Docker:
```bash
docker-compose logs -f ejerciciocopilot
docker logs -f nombre-contenedor
```

### 10. Ciclo de Vida del Contenedor

- **start_period**: Tiempo para que la app inicie antes de health checks
- **restart policy**: unless-stopped (reinicia si falla, no si se detiene)
- **graceful shutdown**: Para transacciones en vuelo (spring.shutdown=graceful)

### 11. Variables Seguras

**NO hacer hardcode de contraseñas o secrets**:

```bash
# Usar variables de entorno
export DB_PASSWORD="secure-password"
docker run -e DATABASE_PASSWORD=$DB_PASSWORD app

# O archivo .env
# DB_PASSWORD=secure-password
docker-compose --env-file .env up
```

### 12. Troubleshooting

#### Imagen no construye
```bash
# Limpiar build cache
docker system prune -a
docker build --no-cache -t ejerciciocopilot .
```

#### Aplicación no inicia en contenedor
```bash
# Ver logs detallados
docker logs -f nombre-contenedor

# Ejecutar contenedor en modo interactivo
docker run -it ejerciciocopilot:latest /bin/sh
```

#### Puerto ya está en uso
```bash
# Usar puerto diferente
docker run -p 8081:8080 ejerciciocopilot:latest

# O buscar proceso que usa puerto
lsof -i :8080
```

#### Base de datos no persiste
```bash
# H2 está en memoria, se pierde al reiniciar
# Para persistencia, usar volumen + base de datos externa (PostgreSQL, MySQL)
docker run -v ejerciciocopilot-data:/app/data app
```

## Archivos a Crear

### 1. Dockerfile
- ubicación: raíz del proyecto
- Multi-stage build
- Optimizado para tamaño y seguridad
- Incluye health checks

### 2. docker-compose.yml
- ubicación: raíz del proyecto
- Servicio principal de app
- Servicio PostgreSQL comentado (para futura expansión)
- Redes y volúmenes

### 3. .dockerignore
- ubicación: raíz del proyecto
- Excluye archivos innecesarios

### 4. application-docker.properties
- ubicación: src/main/resources/
- Perfil específico para Docker
- Configuración de base de datos y logging

### 5. .env.example
- ubicación: raíz del proyecto
- Ejemplo de variables de entorno
- Para documentar qué variables existen

## Estructura de Directorios Final

```
EjercicioCopilot/
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .env.example
├── src/
│   ├── main/
│   │   ├── java/...
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-docker.properties
│   │       └── ...
│   └── test/...
├── .github/
│   ├── prompts/
│   │   ├── controller.prompt.md
│   │   ├── services.prompt.md
│   │   ├── docker.prompt.md
│   │   └── ...
│   └── ...
├── pom.xml
├── README.md
└── ...
```

## Criterios de Éxito

✅ Dockerfile compila exitosamente  
✅ Imagen Docker se crea sin errores  
✅ Contenedor inicia correctamente  
✅ API responde en http://localhost:8080  
✅ Health checks funcionan (`/actuator/health`)  
✅ H2 Console accesible en `/h2-console`  
✅ Logs visibles en `docker logs`  
✅ Docker Compose orquesta correctamente  
✅ Variables de entorno se aplican  
✅ Contenedor se reinicia automáticamente si falla  
✅ Imagen es relativamente pequeña (<500MB)  

## Casos de Uso

### Desarrollo Local
```bash
docker-compose up -d
curl http://localhost:8080/api/health
```

### Testing en CI/CD
```bash
docker build -t ejerciciocopilot:test .
docker run --rm ejerciciocopilot:test mvn test
```

### Despliegue en Producción
```bash
docker build -t ejerciciocopilot:1.0.0 .
docker tag ejerciciocopilot:1.0.0 registry.example.com/ejerciciocopilot:1.0.0
docker push registry.example.com/ejerciciocopilot:1.0.0
```

### Escalado con orquestación
```yaml
# En un Kubernetes cluster o Docker Swarm
# La imagen está lista para ser deployada
services:
  ejerciciocopilot:
    image: ejerciciocopilot:1.0.0
    replicas: 3
    resources:
      limits:
        cpus: '0.5'
        memory: 512M
```

## Referencias

- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Multi-stage builds](https://docs.docker.com/build/building/multi-stage/)
- [Docker Compose file](https://docs.docker.com/compose/compose-file/)
- [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)
- [Health checks](https://docs.docker.com/engine/reference/builder/#healthcheck)

---

**Nota**: Esta documentación asume Spring Boot 3.2.0 y Java 21.
Para cambios en versiones, ajustar base images y dependencias en pom.xml accordingly.
````