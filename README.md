# 🦈 EjercicioCopilot - Generador de Excusas Tech

<div align="center">

**Generador de excusas tech creativas** combinando fragmentos, memes argentinos y leyes del caos developer.

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=flat-square)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue?style=flat-square)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=flat-square)](https://www.docker.com/)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-blue?style=flat-square)](https://swagger.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](./LICENSE)

Construido con **Spring Boot 3.2.0**, **Java 21** y arquitectura hexagonal (Ports & Adapters).

[📖 Documentación](#-documentación-adicional) • [🚀 Quick Start](#-quick-start) • [📡 API](#-api-endpoints) • [🐳 Docker](#-ejecución-con-docker) • [🔵 Swagger](#-swagger-ui)

</div>

---

## 🎯 Descripción

EjercicioCopilot es una **API REST** divertida y técnicamente sólida que genera excusas tech mezclando:

- **🎯 Fragmentos**: Contexto, Causa, Consecuencia, Recomendación
- **😂 Memes**: Tech argentinos (Tano Pasman, anónimos, etc.)
- **📜 Leyes/Axiomas**: Murphy, Hofstadter, Dilbert, DevOps Principles, Dev Axioms

### Tipos de Excusas Generables

- ✨ **SIMPLE**: Solo fragmentos (contexto + causa + consecuencia + recomendación)
- ✨ **CON_MEME**: Fragmentos + meme tech argentino
- ✨ **CON_LEY**: Fragmentos + ley del caos developer
- ✨ **ULTRA_SHARK**: Fragmentos + meme + ley (modo completo) 🦈

---

## 🚀 Quick Start

### Opción 1: Docker Compose (Recomendado ⭐)

```bash
# Clonar repositorio
git clone https://github.com/sergiolewczuk/EjercicioCopilot.git
cd EjercicioCopilot

# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# Acceder a la API
curl http://localhost:8080/api/excuses/random
```

### Opción 2: Maven Local

```bash
# Compilar
mvn clean package

# Ejecutar
mvn spring-boot:run

# Acceder a la API
curl http://localhost:8080/api/excuses/random
```

### Opción 3: Makefile (Con Docker)

```bash
make build      # Construir imagen
make up         # Iniciar servicios
make logs       # Ver logs
make down       # Detener servicios
make help       # Ver todos los comandos
```

---

## 📋 Requisitos

### Opción 1: Docker (Recomendado)
- Docker 20.10+
- Docker Compose 2.0+
- Git 2.0+

### Opción 2: Local
- Java 21+
- Maven 3.6+
- Git 2.0+

---

## 🔵 Swagger UI

**Documentación interactiva de la API:**

```
http://localhost:8080/swagger-ui.html
```

Características:
- ✅ Exploración interactiva de endpoints
- ✅ Esquemas de DTOs con validaciones
- ✅ Ejemplos de requests/responses
- ✅ Try it out: prueba endpoints directamente
- ✅ Especificación OpenAPI 3.0

**Especificación OpenAPI (JSON):**
```
http://localhost:8080/v3/api-docs
```

---

## 📡 Acceso a Servicios

Una vez ejecutada la aplicación (cualquier opción), acceder a:

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **📡 API REST** | http://localhost:8080 | Endpoints de la API |
| **🔵 Swagger UI** | http://localhost:8080/swagger-ui.html | Documentación interactiva |
| **📊 OpenAPI JSON** | http://localhost:8080/v3/api-docs | Especificación OpenAPI 3.0 |
| **💾 H2 Console** | http://localhost:8080/h2-console | Base de datos (sa / sin contraseña) |
| **❤️ Health Check** | http://localhost:8080/actuator/health | Estado de la aplicación |
| **📈 Métricas** | http://localhost:8080/actuator/metrics | Métricas JVM |
| **ℹ️ Info** | http://localhost:8080/actuator/info | Información de la aplicación |

---

## 📡 API Endpoints

### 🎯 Fragments - CRUD de Fragmentos

**Fragmentos son las partes componentes de una excusa (contexto, causa, consecuencia, recomendación)**

```http
GET    /api/fragments              # Obtener todos los fragmentos
GET    /api/fragments?tipo=CONTEXTO # Filtrar por tipo específico
GET    /api/fragments/{id}         # Obtener por ID
POST   /api/fragments              # Crear fragmento
PUT    /api/fragments/{id}         # Actualizar fragmento
DELETE /api/fragments/{id}         # Eliminar fragmento
```

**Tipos disponibles**: 
- `CONTEXTO` - Situación en la que ocurrió
- `CAUSA` - Razón técnica
- `CONSECUENCIA` - Resultado/impacto
- `RECOMENDACION` - Solución sugerida

**Ejemplo con cURL**:
```bash
# Crear fragmento
curl -X POST http://localhost:8080/api/fragments \
  -H "Content-Type: application/json" \
  -d '{
    "type": "CONTEXTO",
    "text": "Durante el despliegue a producción",
    "role": "DEV"
  }'

# Obtener todos
curl http://localhost:8080/api/fragments

# Filtrar por tipo
curl http://localhost:8080/api/fragments?tipo=CONTEXTO

# Obtener por ID
curl http://localhost:8080/api/fragments/1
```

---

### 😂 Memes - CRUD de Memes

**Memes tech argentinos (Tano Pasman, anónimos, etc.)**

```http
GET    /api/memes                  # Obtener todos los memes
GET    /api/memes/{id}             # Obtener por ID
POST   /api/memes                  # Crear meme
PUT    /api/memes/{id}             # Actualizar meme
DELETE /api/memes/{id}             # Eliminar meme
```

**Ejemplo con cURL**:
```bash
# Crear meme
curl -X POST http://localhost:8080/api/memes \
  -H "Content-Type: application/json" \
  -d '{
    "author": "Tano Pasman",
    "quote": "¿CÓMO QUE FALLÓ EL PIPELINE?"
  }'

# Obtener todos
curl http://localhost:8080/api/memes

# Obtener por ID
curl http://localhost:8080/api/memes/1
```

---

### 📜 Laws - CRUD de Leyes/Axiomas

**Leyes y axiomas del desarrollo (Murphy, Hofstadter, Dilbert, DevOps, Dev Axioms)**

```http
GET    /api/laws                   # Obtener todas las leyes
GET    /api/laws?category=Murphy   # Filtrar por categoría
GET    /api/laws/{id}              # Obtener por ID
POST   /api/laws                   # Crear ley
PUT    /api/laws/{id}              # Actualizar ley
DELETE /api/laws/{id}              # Eliminar ley
```

**Categorías disponibles**:
- `Murphy` - Leyes de Murphy
- `Hofstadter` - Leyes de Hofstadter
- `Dilbert` - Humor corporativo Dilbert
- `DevOps` - Principios DevOps
- `DevAxiom` - Axiomas del desarrollo

**Ejemplo con cURL**:
```bash
# Crear ley
curl -X POST http://localhost:8080/api/laws \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ley de Murphy",
    "description": "Si algo puede salir mal, saldrá mal durante la demo.",
    "category": "Murphy"
  }'

# Obtener todas
curl http://localhost:8080/api/laws

# Filtrar por categoría
curl http://localhost:8080/api/laws?category=Murphy

# Obtener por ID
curl http://localhost:8080/api/laws/1
```

---

### 🦈 Excuses - Generación y Consulta de Excusas

**Generación inteligente de excusas combinando fragmentos, memes y leyes**

#### Endpoints de Generación (GET)

```http
GET /api/excuses/random            # Excusa aleatoria simple
GET /api/excuses/daily             # Excusa del día (reproducible)
GET /api/excuses/meme              # Excusa + meme aleatorio
GET /api/excuses/law               # Excusa + ley aleatoria
GET /api/excuses/ultra             # Excusa ULTRA_SHARK (todo completo) 🦈
GET /api/excuses/role/{rol}        # Excusa para rol específico
```

**Roles disponibles**:
- `DEV` - Desarrollador
- `QA` - Testing/QA
- `DEVOPS` - DevOps/SRE
- `PM` - Project Manager
- `ARCHITECT` - Arquitecto
- `DEVREL` - Developer Relations

#### Endpoints CRUD

```http
GET    /api/excuses                # Historial de excusas generadas
GET    /api/excuses/{id}           # Obtener por ID
POST   /api/excuses                # Crear excusa personalizada
```

**Ejemplos con cURL**:

```bash
# Excusa aleatoria
curl http://localhost:8080/api/excuses/random

# Excusa del día (misma todo el día - reproducible)
curl http://localhost:8080/api/excuses/daily

# Excusa ULTRA_SHARK (fragmentos + meme + ley)
curl http://localhost:8080/api/excuses/ultra

# Excusa para rol específico (ej: DEV)
curl http://localhost:8080/api/excuses/role/DEV

# Excusa para rol (ej: QA)
curl http://localhost:8080/api/excuses/role/QA

# Excusa con meme
curl http://localhost:8080/api/excuses/meme

# Excusa con ley
curl http://localhost:8080/api/excuses/law

# Obtener historial
curl http://localhost:8080/api/excuses

# Obtener por ID
curl http://localhost:8080/api/excuses/1

# Crear excusa personalizada
curl -X POST http://localhost:8080/api/excuses \
  -H "Content-Type: application/json" \
  -d '{
    "contextId": 1,
    "causeId": 2,
    "consequenceId": 3,
    "recommendationId": 4,
    "memeId": 1,
    "lawId": 1,
    "type": "ULTRA_SHARK",
    "role": "DEV"
  }'
```

---

## 📤 Ejemplo de Respuesta Completa

### GET /api/excuses/ultra

```json
{
  "id": 1,
  "context": {
    "id": 1,
    "type": "CONTEXTO",
    "text": "Durante el despliegue del pipeline",
    "role": "DEV",
    "createdAt": "2024-01-20T14:30:45"
  },
  "cause": {
    "id": 2,
    "type": "CAUSA",
    "text": "El token de CI/CD venció sin aviso",
    "role": null,
    "createdAt": "2024-01-20T14:30:45"
  },
  "consequence": {
    "id": 3,
    "type": "CONSECUENCIA",
    "text": "Tuvimos que hacer rollback de emergencia",
    "role": null,
    "createdAt": "2024-01-20T14:30:45"
  },
  "recommendation": {
    "id": 4,
    "type": "RECOMENDACION",
    "text": "Automatizar la rotación de secretos",
    "role": null,
    "createdAt": "2024-01-20T14:30:45"
  },
  "meme": {
    "id": 1,
    "author": "Tano Pasman",
    "quote": "¿CÓMO QUE FALLÓ EL PIPELINE?",
    "createdAt": "2024-01-20T14:30:45"
  },
  "law": {
    "id": 1,
    "name": "Ley de Murphy",
    "description": "Si algo puede salir mal, saldrá mal durante la demo.",
    "category": "Murphy",
    "createdAt": "2024-01-20T14:30:45"
  },
  "type": "ULTRA_SHARK",
  "role": "DEV",
  "seed": 1234567890,
  "createdAt": "2024-01-20T14:30:45"
}
```

---

## 🏗️ Arquitectura

Sigue el patrón **Hexagonal (Ports & Adapters)**:

```
HTTP Request → Controller (Adapter) → Service (Domain) → Repository (Adapter) → H2 Database
             ↓                        ↓                   ↓
            DTOs              Generación Excusas    Persistencia JPA
```

### Estructura de Paquetes

```
com.ejerciciocopilot/
├── controller/     # Adaptadores de entrada (REST)
├── dto/           # Contratos de API (Request/Response)
├── model/         # Entidades de dominio
├── repository/    # Adaptadores de persistencia (JPA)
├── service/       # Núcleo de negocio + Mappers
├── config/        # Configuraciones Spring
├── exception/     # Excepciones personalizadas
└── Application.java # Clase principal
```

### Flujo de una Solicitud

```
1. HTTP Request → Controlador
   ↓
2. Controlador → Mapper DTO → Entidad
   ↓
3. Servicio → Lógica de Negocio
   ↓
4. Repositorio → JPA → H2 Database
   ↓
5. Response → Mapper Entidad → DTO → JSON
```

---

## 🐳 Ejecución con Docker

### Docker Compose (Recomendado)

```bash
# Iniciar
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v
```

### Docker Individual

```bash
# Construir imagen
docker build -t ejerciciocopilot:latest .

# Ejecutar contenedor
docker run -p 8080:8080 ejerciciocopilot:latest
```

### Makefile (Simplificado)

```bash
make build              # Construir imagen
make up                 # Iniciar con docker-compose
make down               # Detener servicios
make logs               # Ver logs
make restart            # Reiniciar
make test               # Ejecutar tests
make health             # Verificar health check
make clean              # Limpiar images
make help               # Ver todos los comandos
```

### Características Docker

- ✅ **Multi-stage build**: Imagen optimizada (~200MB)
- ✅ **JRE minimal**: eclipse-temurin:21-jre-jammy
- ✅ **Usuario no-root**: Ejecución segura
- ✅ **Health checks**: Monitoreo automático
- ✅ **Variables de entorno**: Configuración flexible
- ✅ **Volúmenes**: Persistencia de logs
- ✅ **Redes personalizadas**: Comunicación entre servicios

**Documentación completa:** Ver [`DOCKER.md`](./DOCKER.md)

---

## 🧪 Tests

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Tests con cobertura
mvn jacoco:report

# Tests de un paquete específico
mvn test -Dtest=ExcuseServiceTest

# En Docker
docker-compose exec ejerciciocopilot-app mvn test
```

### Cobertura

El proyecto incluye una suite completa de tests unitarios e integración con **>80% de cobertura**:

- ✅ **ExcuseServiceTest** (13 tests): Generación de excusas, reproducibilidad, roles
- ✅ **ExcuseControllerTest** (11 tests): Endpoints REST, validaciones
- ✅ **FragmentServiceTest** (5 tests): CRUD de fragmentos
- ✅ **FragmentControllerTest** (4 tests): Integración HTTP
- ✅ **ExcuseIntegrationTest** (6 tests): Datos reales, seed reproducibilidad

### Tests Destacados

- **Reproducibilidad con Seed**: `generateDaily()` produce la misma excusa todo el día
- **Datos desde JSONs**: Cargan automáticamente desde `/docs/json`
- **MockMvc**: Tests de controllers con datos reales
- **Mockito**: Mocking de dependencias

---

## 📚 Datos Iniciales (Precarga)

Los JSONs en `/docs/json/` contienen datos iniciales:

| Archivo | Contenido | Cantidad |
|---------|-----------|----------|
| `dev_axioms.json` | Axiomas del desarrollo | 10+ |
| `murphy.json` | Leyes de Murphy | 40+ |
| `hofstadter.json` | Leyes de Hofstadter | 5+ |
| `dilbert.json` | Humor corporativo Dilbert | 20+ |
| `devops_principles.json` | Principios DevOps | 6+ |
| `memes_argentinos.json` | Memes tech locales | 7+ |
| `argento-memes.json` | Más memes argentinos | 40+ |
| `dev-memes.json` | Memes generales dev | 40+ |

---

## 🏆 Enums del Dominio

### FragmentType
```java
CONTEXTO        // La situación en la que ocurrió
CAUSA           // La razón técnica por la que sucedió
CONSECUENCIA    // El resultado o impacto
RECOMENDACION   // La solución sugerida
```

### ExcuseType
```java
SIMPLE          // Solo fragmentos
CON_MEME        // Fragmentos + meme
CON_LEY         // Fragmentos + ley
ULTRA_SHARK     // Fragmentos + meme + ley (completo) 🦈
```

### Role
```java
DEV             // Desarrollador
QA              // Testing/QA
DEVOPS          // DevOps/SRE
PM              // Project Manager
ARCHITECT       // Arquitecto
DEVREL          // Developer Relations
```

---

## 📖 Documentación Adicional

### Arquitectura

Diagramas PlantUML en `/docs/uml/`:

- **EjercicioCopilot-Arquitectura.puml**: Diagrama de clases completo
- **EjercicioCopilot-Secuencia.puml**: Flujo ULTRA_SHARK (caso de uso principal)
- **EjercicioCopilot-Componentes.puml**: Descomposición en componentes
- **EjercicioCopilot-Despliegue.puml**: Diagrama de despliegue
- **EjercicioCopilot-FlujohHexagonal.puml**: Flujo hexagonal completo

### Instrucciones de Desarrollo

- **[`.github/copilot-instructions.md`](./.github/copilot-instructions.md)**: Guía para GitHub Copilot
- **[`.github/git-commit-instructions.md`](./.github/git-commit-instructions.md)**: Convenciones de commits (Conventional Commits)
- **[`.github/prompts/controller.prompt.md`](./.github/prompts/controller.prompt.md)**: Generación de controllers
- **[`.github/prompts/services.prompt.md`](./.github/prompts/services.prompt.md)**: Generación de servicios
- **[`.github/prompts/docker.prompt.md`](./.github/prompts/docker.prompt.md)**: Configuración Docker

### Especificaciones

- **[`swagger.yaml`](./src/main/resources/swagger.yaml)**: Especificación OpenAPI 3.0 completa
- **[`DOCKER.md`](./DOCKER.md)**: Documentación completa de Docker

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| **Java** | 21+ | Lenguaje de programación |
| **Spring Boot** | 3.2.0 | Framework principal |
| **Spring Data JPA** | 3.2.0 | Persistencia ORM |
| **Spring Boot Actuator** | 3.2.0 | Health checks y métricas |
| **H2 Database** | 2.x | Base de datos en memoria |
| **Lombok** | 1.18.30 | Boilerplate reduction |
| **Jakarta Validation** | 3.0 | Validaciones |
| **OpenAPI** | 3.0 | Especificación API |
| **JUnit 5** | 5.10 | Testing unitario |
| **Mockito** | 5.2+ | Mocking en tests |
| **Maven** | 3.6+ | Build tool |
| **Docker** | 20.10+ | Containerización |

---

## 📊 Patrones Aplicados

- ✅ **Arquitectura Hexagonal**: Dominio independiente de infraestructura
- ✅ **SOLID**: Principios aplicados consistentemente
- ✅ **Clean Code**: Código legible y autodocumentado
- ✅ **Conventional Commits**: Historial de git semántico
- ✅ **Lazy Loading**: Evitar N+1 queries
- ✅ **DTOs**: Request/Response separados
- ✅ **Mappers**: Transformación Entity ↔ DTO
- ✅ **Dependency Injection**: Por constructor
- ✅ **Multi-stage Docker**: Builds optimizados

---

## 🚀 Roadmap Futuro (Level White Shark+)

- [x] ✅ Docker: Containerizar la aplicación
- [x] ✅ Docker Compose: Orquestación local
- [x] ✅ Swagger/OpenAPI: Documentación interactiva
- [x] ✅ Actuator: Health checks y métricas
- [ ] Tests de Integración con RestAssured
- [ ] Autenticación OAuth2
- [ ] Caché con Redis
- [ ] Trazabilidad distribuida
- [ ] Generación de excusas con IA (integración LLM)
- [ ] Kubernetes: Despliegue en clusters
- [ ] PostgreSQL: Migrar de H2
- [ ] API Rate Limiting
- [ ] WebSockets para actualizaciones en tiempo real
- [ ] GraphQL: Alternativa a REST
- [ ] HATEOAS: REST avanzado

---

## 📝 Convenciones de Código

### Lombok

Usado en **TODAS** las clases:

```java
// Entidades (@Entity)
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Fragment { ... }

// DTOs
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class FragmentRequestDTO { ... }
```

### Inyección de Dependencias

Siempre por **constructor**:

```java
@Service
public class ExcuseService {
    private final ExcuseRepository excuseRepository;
    
    public ExcuseService(ExcuseRepository excuseRepository) {
        this.excuseRepository = excuseRepository;
    }
}
```

### Validaciones

Solo en **RequestDTOs**:

```java
@Data
public class FragmentRequestDTO {
    @NotBlank(message = "El tipo es obligatorio")
    private String type;
    
    @NotBlank(message = "El texto es obligatorio")
    @Size(min = 10, max = 500)
    private String text;
}
```

### Commits

Seguir **[Conventional Commits](https://www.conventionalcommits.org/)**:

```bash
git commit -m "feat(ExcuseController): agregar endpoint /ultra"
git commit -m "fix(ExcuseService): corregir NPE sin fragmentos"
git commit -m "test(ExcuseServiceTest): aumentar cobertura a 85%"
git commit -m "docs: actualizar README con Swagger"
git commit -m "build(docker): agregar configuración Docker"
```

---

## 🐛 Troubleshooting

### Problema: H2 Console no abre

**Solución:**
```bash
# Verificar que la aplicación está corriendo
curl http://localhost:8080/actuator/health

# Acceder a: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# User: sa (sin contraseña)
```

### Problema: Tests fallan

**Solución:**
```bash
# Limpiar y recompilar
mvn clean test

# Ejecutar con logs
mvn test -X

# En Docker
docker-compose exec ejerciciocopilot-app mvn clean test
```

### Problema: Puerto 8080 ya está en uso

**Solución:**
```bash
# Opción 1: Cambiar puerto en application.properties
# server.port=8081

# Opción 2: Detener proceso que usa puerto 8080
lsof -ti:8080 | xargs kill -9

# Opción 3: Usar puerto diferente en Docker
docker run -p 8081:8080 ejerciciocopilot:latest
```

### Problema: Docker Compose no inicia

**Solución:**
```bash
# Ver logs detallados
docker-compose logs

# Forzar rebuild
docker-compose up -d --build

# Limpiar y reiniciar
docker-compose down -v
docker-compose up -d
```

### Problema: Base de datos está vacía

**Solución:**
```bash
# H2 está en memoria, se reinicia con cada contenedor
# Los datos iniciales se cargan desde JSONs en /docs/json

# Para persistencia, descomenta PostgreSQL en docker-compose.yml:
# - docker-compose.yml: descomentar servicio postgres
# - application-docker.properties: cambiar SPRING_DATASOURCE_URL
# - Reiniciar: docker-compose down -v && docker-compose up -d
```

---

## 📞 Contacto y Contribuciones

Este es un proyecto de la **Tribu Java Sharks** para práctica con **GitHub Copilot**.

### Cómo Contribuir

1. **Fork** el repositorio
2. **Crea rama** para tu feature: `git checkout -b feature/nombre-feature`
3. **Commit** cambios: `git commit -m "feat(scope): descripción"`
4. **Push** a la rama: `git push origin feature/nombre-feature`
5. **Abre Pull Request**

### Reportar Bugs

Usa [GitHub Issues](https://github.com/sergiolewczuk/EjercicioCopilot/issues) para reportar bugs.

---

## 📄 Licencia

MIT License - Ver [`LICENSE`](./LICENSE) para detalles.

---

<div align="center">

**Versión**: 1.0.0  
**Estado**: ✅ Funcional (Level Shark 🦈)  
**Última actualización**: Noviembre 2025  
**Mantenedor**: Equipo EjercicioCopilot

[⬆ Volver al inicio](#-ejerciciocopilot---generador-de-excusas-tech)

</div>
