# ============================================================================
# EjercicioCopilot - Dockerfile Multi-Stage
# ============================================================================
# 
# Dockerfile optimizado con multi-stage build para producción.
# Stage 1: Compilación con Maven
# Stage 2: Ejecución con JRE minimal
# 
# Build: docker build -t ejerciciocopilot:1.0.0 .
# Run:   docker run -p 8080:8080 ejerciciocopilot:1.0.0
#

# ============================================================================
# STAGE 1: BUILDER - Compilar con Maven
# ============================================================================

FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Metadatos
LABEL maintainer="Equipo EjercicioCopilot"
LABEL description="Builder stage para compilación de EjercicioCopilot"

# Directorio de trabajo
WORKDIR /build

# Copiar POM primero (aprovecha caché de capas)
COPY pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:resolve dependency:resolve-plugins

# Copiar código fuente
COPY src ./src

# Compilar y generar JAR
RUN mvn clean package -DskipTests

# ============================================================================
# STAGE 2: RUNTIME - Ejecutar con JRE minimal
# ============================================================================

FROM eclipse-temurin:21-jre-jammy

# Metadatos
LABEL maintainer="Equipo EjercicioCopilot"
LABEL description="EjercicioCopilot - Generador de Excusas Tech"
LABEL version="1.0.0"

# Variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8080

# Directorio de trabajo
WORKDIR /app

# Crear usuario no-root para seguridad
RUN useradd -m -u 1000 appuser && \
    mkdir -p /app/logs && \
    chown -R appuser:appuser /app

# Copiar JAR desde builder stage
COPY --from=builder --chown=appuser:appuser /build/target/ejerciciocopilot-*.jar app.jar

# Cambiar a usuario no-root
USER appuser

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=40s \
    CMD java -cp /app/app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Punto de entrada - ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]

# ============================================================================
# NOTAS:
# ============================================================================
# - El health check intenta ejecutar la app; en producción, usar curl si está disponible
# - Las dependencias se cachean en la primera ejecución (no se redescarga si src cambia)
# - Usuario appuser ejecuta la app (no root por seguridad)
# - JAVA_OPTS puede ser overrideado: docker run -e JAVA_OPTS="-Xmx1g" app
# ============================================================================
