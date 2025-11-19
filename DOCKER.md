# EjercicioCopilot - Documentación Docker

Guía completa para ejecutar EjercicioCopilot en Docker.

## 📋 Tabla de Contenidos

1. [Requisitos](#requisitos)
2. [Construcción de Imagen](#construcción-de-imagen)
3. [Ejecución con Docker](#ejecución-con-docker)
4. [Ejecución con Docker Compose](#ejecución-con-docker-compose)
5. [Variables de Entorno](#variables-de-entorno)
6. [Health Checks](#health-checks)
7. [Acceso a Servicios](#acceso-a-servicios)
8. [Troubleshooting](#troubleshooting)
9. [Comandos Útiles](#comandos-útiles)
10. [Producción](#producción)

## Requisitos

- **Docker**: 20.10+
- **Docker Compose**: 2.0+
- **Git**: para clonar el repositorio

### Instalar Docker

**Linux (Ubuntu/Debian)**:
```bash
sudo apt-get update
sudo apt-get install -y docker.io docker-compose
sudo usermod -aG docker $USER
```

**macOS**:
```bash
brew install docker docker-compose
# O instalar Docker Desktop desde https://www.docker.com/products/docker-desktop
```

**Windows**:
- Descargar e instalar Docker Desktop desde https://www.docker.com/products/docker-desktop
- Habilitar WSL 2 (Windows Subsystem for Linux)

## Construcción de Imagen

### Build estándar

```bash
# Construir imagen con tag 'latest'
docker build -t ejerciciocopilot:latest .

# Construir con versión específica
docker build -t ejerciciocopilot:1.0.0 .

# Verificar imagen construida
docker images | grep ejerciciocopilot
```

### Build sin caché (fuerza rebuilding)

```bash
docker build --no-cache -t ejerciciocopilot:latest .
```

### Build con BuildKit (más rápido)

```bash
DOCKER_BUILDKIT=1 docker build -t ejerciciocopilot:latest .
```

## Ejecución con Docker

### Ejecución básica

```bash
# Ejecutar contenedor
docker run -p 8080:8080 ejerciciocopilot:latest

# Ejecutar en background
docker run -d -p 8080:8080 --name ejerciciocopilot ejerciciocopilot:latest
```

### Ejecución con variables de entorno

```bash
docker run -d \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx1g -Xms512m" \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e LOGGING_LEVEL_ROOT=DEBUG \
  --name ejerciciocopilot \
  ejerciciocopilot:latest
```

### Ejecución con volúmenes para logs

```bash
docker run -d \
  -p 8080:8080 \
  -v $(pwd)/logs:/app/logs \
  --name ejerciciocopilot \
  ejerciciocopilot:latest
```

### Detener contenedor

```bash
docker stop ejerciciocopilot
docker rm ejerciciocopilot
```

## Ejecución con Docker Compose

### Configuración inicial

```bash
# Copiar variables de entorno
cp .env.example .env

# Editar .env si es necesario (contraseñas, puertos, etc.)
nano .env
```

### Iniciar servicios

```bash
# Iniciar en background
docker-compose up -d

# Ver logs durante ejecución
docker-compose up

# Forzar rebuild
docker-compose up -d --build
```

### Ver logs

```bash
# Logs en vivo de todos los servicios
docker-compose logs -f

# Logs solo de la app
docker-compose logs -f ejerciciocopilot-app

# Últimas 100 líneas
docker-compose logs --tail=100 ejerciciocopilot-app
```

### Detener servicios

```bash
# Detener manteniendo volúmenes
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v

# Detener solo un servicio
docker-compose stop ejerciciocopilot-app
```

### Reiniciar servicios

```bash
# Reiniciar todos
docker-compose restart

# Reiniciar uno específico
docker-compose restart ejerciciocopilot-app
```

## Variables de Entorno

### Archivo `.env`

Copiar `.env.example` a `.env` y configurar:

```bash
cp .env.example .env
```

### Variables principales

```env
# JVM
JAVA_OPTS=-Xmx512m -Xms256m

# Spring Boot
SPRING_PROFILES_ACTIVE=docker
SERVER_PORT=8080

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_EJERCICIOCOPILOT=DEBUG

# Actuator
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics
```

### Override en línea de comandos

```bash
# Docker
docker run -e JAVA_OPTS="-Xmx1g" ejerciciocopilot:latest

# Docker Compose
docker-compose -e JAVA_OPTS="-Xmx1g" up -d
```

## Health Checks

### Health check automático

Docker Compose ejecuta health checks automáticos:

```bash
# Ver estado
docker-compose ps

# Ver detalles
docker inspect <container-id> | jq '.State.Health'
```

### Health check manual

```bash
# Comprobar salud de la aplicación
curl http://localhost:8080/actuator/health | jq '.'

# Respuesta esperada:
# {
#   "status": "UP",
#   "components": {...}
# }
```

## Acceso a Servicios

### API Principal

```
http://localhost:8080
```

### Endpoints Principales

```
GET    http://localhost:8080/api/fragments
GET    http://localhost:8080/api/memes
GET    http://localhost:8080/api/laws
GET    http://localhost:8080/api/excuses
GET    http://localhost:8080/api/excuses/random
GET    http://localhost:8080/api/excuses/ultra
```

### Swagger UI (Documentación)

```
http://localhost:8080/swagger-ui.html
```

### H2 Console (Base de Datos)

```
URL:       http://localhost:8080/h2-console
JDBC URL:  jdbc:h2:mem:testdb
User:      sa
Password:  (vacía)
```

### Actuator (Monitoreo)

```
Health:    http://localhost:8080/actuator/health
Metrics:   http://localhost:8080/actuator/metrics
Info:      http://localhost:8080/actuator/info
Env:       http://localhost:8080/actuator/env
```

### Ejemplo de Request

```bash
# Obtener excusa aleatoria
curl http://localhost:8080/api/excuses/random | jq '.'

# Obtener excusa del día (reproducible)
curl http://localhost:8080/api/excuses/daily | jq '.'

# Obtener excusa ULTRA_SHARK
curl http://localhost:8080/api/excuses/ultra | jq '.'
```

## Troubleshooting

### Contenedor no inicia

```bash
# Ver logs detallados
docker-compose logs ejerciciocopilot-app

# Ejecutar en modo interactivo para ver errores
docker-compose up ejerciciocopilot-app
```

### Puerto ya está en uso

```bash
# Encontrar proceso que usa puerto 8080
lsof -i :8080

# O cambiar puerto en docker-compose.yml
# ports:
#   - "8081:8080"
```

### Base de datos no funciona

```bash
# H2 está en memoria, se reinicia al detener el contenedor
# Para persistencia, descomentar PostgreSQL en docker-compose.yml

# Verificar conexión a BD
curl http://localhost:8080/h2-console
```

### Logs de compilación muy grandes

```bash
# Limpiar caché de Docker
docker system prune -a

# Reconstruir sin caché
docker build --no-cache -t ejerciciocopilot .
```

### Permiso denegado

```bash
# En Linux, agregar usuario al grupo docker
sudo usermod -aG docker $USER
newgrp docker
```

## Comandos Útiles

### Inspeccionar Contenedor

```bash
# Ver detalles del contenedor
docker inspect ejerciciocopilot-app

# Ver detalles de la imagen
docker image inspect ejerciciocopilot:latest
```

### Ejecutar Comandos en Contenedor

```bash
# Entrar a shell del contenedor
docker-compose exec ejerciciocopilot-app /bin/sh

# Ejecutar comando specific
docker-compose exec ejerciciocopilot-app curl http://localhost:8080/actuator/health

# Ejecutar tests
docker-compose exec ejerciciocopilot-app mvn test
```

### Copiar Archivos

```bash
# Desde contenedor al host
docker cp ejerciciocopilot-app:/app/logs ./logs-backup

# Desde host al contenedor
docker cp ./data.json ejerciciocopilot-app:/app/data.json
```

### Networking

```bash
# Ver redes
docker network ls

# Inspeccionar red de compose
docker network inspect ejerciciocopilot_ejerciciocopilot-network

# Conectar contenedor a red
docker network connect ejerciciocopilot_ejerciciocopilot-network otro-contenedor
```

## Producción

### Consideraciones de Seguridad

1. **Usuario no-root**: La imagen ejecuta como usuario `appuser` (no root)
2. **Variables sensibles**: Usar secrets de Docker/Kubernetes, no .env
3. **Base de datos**: Usar PostgreSQL/MySQL en contenedor separado
4. **Logs**: Persistir a volumen o servicio externo

### Ejemplo para Producción

```bash
# Build para producción
docker build -t ejerciciocopilot:1.0.0 .

# Tag para registry
docker tag ejerciciocopilot:1.0.0 myregistry.com/ejerciciocopilot:1.0.0

# Push a registry
docker push myregistry.com/ejerciciocopilot:1.0.0

# Ejecutar en producción (con secrets)
docker run -d \
  --name ejerciciocopilot \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx2g -Xms1g" \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://prod-db:5432/app" \
  -e SPRING_DATASOURCE_PASSWORD="$(cat /run/secrets/db_password)" \
  --secret db_password \
  -v ejerciciocopilot-logs:/app/logs \
  --restart unless-stopped \
  myregistry.com/ejerciciocopilot:1.0.0
```

### Docker Swarm / Kubernetes

La imagen está optimizada para orquestación:

```yaml
# Docker Swarm
docker service create \
  --name ejerciciocopilot \
  --publish 8080:8080 \
  --env JAVA_OPTS="-Xmx2g" \
  myregistry.com/ejerciciocopilot:1.0.0

# Kubernetes (ejemplo)
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ejerciciocopilot
spec:
  replicas: 3
  selector:
    matchLabels:
      app: ejerciciocopilot
  template:
    metadata:
      labels:
        app: ejerciciocopilot
    spec:
      containers:
      - name: ejerciciocopilot
        image: myregistry.com/ejerciciocopilot:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: JAVA_OPTS
          value: "-Xmx512m -Xms256m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 40
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 20
          periodSeconds: 5
```

## Usando Makefile

Si tienes `make` instalado, puedes usar comandos simplificados:

```bash
make build              # Construir imagen
make up                 # Iniciar con Docker Compose
make down               # Detener servicios
make logs               # Ver logs
make test               # Ejecutar tests
make clean              # Limpiar
make help               # Ver todos los comandos
```

## Preguntas Frecuentes

**P: ¿La imagen es muy grande?**
A: Multi-stage build reduce tamaño. Alpine base (~200MB) vs JDK completo (~500MB+).

**P: ¿Los datos persisten?**
A: H2 está en memoria, se pierde al reiniciar. Usar PostgreSQL para persistencia.

**P: ¿Puedo cambiar el puerto?**
A: Sí, en docker-compose.yml cambiar `ports: - "8081:8080"` o variable de entorno.

**P: ¿Cómo debuguear la app en Docker?**
A: Ver logs con `docker-compose logs -f` o entrar a shell con `docker-compose exec ejerciciocopilot-app /bin/sh`.

## Recursos

- [Docker Docs](https://docs.docker.com/)
- [Docker Compose Docs](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Best Practices](https://docs.docker.com/develop/dev-best-practices/)

---

**Versión**: 1.0.0  
**Última actualización**: Noviembre 2025  
**Proyecto**: EjercicioCopilot
