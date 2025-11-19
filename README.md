# 🦈 EjercicioCopilot - Generador de Excusas Tech

Generador de **excusas tech creativas** que combina fragmentos, memes argentinos y leyes del caos developer. Construido con **Spring Boot 3.2.0**, **Java 21** y arquitectura hexagonal.

## 🎯 Descripción

EjercicioCopilot es una **API REST** divertida y técnicamente sólida que genera excusas tech mezclando:
- **Fragmentos**: Contexto, Causa, Consecuencia, Recomendación
- **Memes**: Tech argentinos (Tano Pasman, anónimos, etc.)
- **Leyes/Axiomas**: Murphy, Hofstadter, Dilbert, DevOps Principles, Dev Axioms

### Tipos de Excusas Generables

- ✨ **SIMPLE**: Solo fragmentos (contexto + causa + consecuencia + recomendación)
- ✨ **CON_MEME**: Fragmentos + meme tech argentino
- ✨ **CON_LEY**: Fragmentos + ley del caos developer
- ✨ **ULTRA_SHARK**: Fragmentos + meme + ley (modo completo)

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

## 📋 Requisitos

- **Java**: 21+
- **Maven**: 3.6+
- **Spring Boot**: 3.2.0
- **H2 Database**: En memoria (desarrollo/testing)

## ⚙️ Instalación y Ejecución

### Opción 1: Ejecución Local (Maven)

#### 1. Clonar el repositorio

```bash
git clone https://github.com/sergiolewczuk/EjercicioCopilot.git
cd EjercicioCopilot
```

#### 2. Compilar el proyecto

```bash
mvn clean package
```

#### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

### Opción 2: Ejecución con Docker (Recomendado)

#### 1. Requisitos

- Docker 20.10+
- Docker Compose 2.0+

#### 2. Construir imagen Docker

```bash
# Construir imagen
docker build -t ejerciciocopilot:latest .

# O con Makefile
make build
```

#### 3. Ejecutar con Docker Compose

```bash
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# O con Makefile
make up
make logs
```

#### 4. Detener servicios

```bash
docker-compose down

# O con Makefile
make down
```

**Ventajas de Docker:**
- ✅ Entorno consistente (Dev = Prod)
- ✅ No requiere instalar dependencias locales
- ✅ Fácil de escalar con Kubernetes
- ✅ Aislamiento de procesos

**Documentación completa:** Ver [`DOCKER.md`](./DOCKER.md)

### 5. Acceder a las consolas

| Herramienta | URL | Usuario | Contraseña |
|-------------|-----|---------|-----------|
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - | - |
| **H2 Console** | http://localhost:8080/h2-console | sa | (vacía) |
| **Health Check** | http://localhost:8080/actuator/health | - | - |
| **JDBC URL (H2)** | jdbc:h2:mem:testdb | - | - |

## 📡 API Endpoints

### Fragment Controller - CRUD de Fragmentos

```http
GET    /api/fragments              # Obtener todos los fragmentos
GET    /api/fragments?tipo=CONTEXTO # Filtrar por tipo
GET    /api/fragments/{id}         # Obtener por ID
POST   /api/fragments              # Crear fragmento
PUT    /api/fragments/{id}         # Actualizar fragmento
DELETE /api/fragments/{id}         # Eliminar fragmento
```

**Tipos disponibles**: CONTEXTO, CAUSA, CONSECUENCIA, RECOMENDACION

**Ejemplo Request (POST)**:
```json
{
  "type": "CONTEXTO",
  "text": "Durante el despliegue del pipeline",
  "role": "DEV"
}
```

---

### Meme Controller - CRUD de Memes

```http
GET    /api/memes                  # Obtener todos los memes
GET    /api/memes/{id}             # Obtener por ID
POST   /api/memes                  # Crear meme
PUT    /api/memes/{id}             # Actualizar meme
DELETE /api/memes/{id}             # Eliminar meme
```

**Ejemplo Request (POST)**:
```json
{
  "author": "Tano Pasman",
  "quote": "¿CÓMO QUE FALLÓ EL PIPELINE?"
}
```

---

### Law Controller - CRUD de Leyes/Axiomas

```http
GET    /api/laws                   # Obtener todas las leyes
GET    /api/laws?category=Murphy   # Filtrar por categoría
GET    /api/laws/{id}              # Obtener por ID
POST   /api/laws                   # Crear ley
PUT    /api/laws/{id}              # Actualizar ley
DELETE /api/laws/{id}              # Eliminar ley
```

**Categorías disponibles**: Murphy, Hofstadter, Dilbert, DevOps, DevAxiom

**Ejemplo Request (POST)**:
```json
{
  "name": "Ley de Murphy",
  "description": "Si algo puede salir mal, saldrá mal durante la demo.",
  "category": "Murphy"
}
```

---

### Excuse Controller - Generación y Consulta de Excusas

#### Endpoints de Generación

```http
GET /api/excuses/random            # Excusa aleatoria simple
GET /api/excuses/daily             # Excusa del día (reproducible)
GET /api/excuses/meme              # Excusa + meme aleatorio
GET /api/excuses/law               # Excusa + ley aleatoria
GET /api/excuses/ultra             # Excusa ULTRA_SHARK (todo completo)
GET /api/excuses/role/{rol}        # Excusa para rol específico
```

**Roles disponibles**: DEV, QA, DEVOPS, PM, ARCHITECT, DEVREL

#### Endpoints CRUD

```http
GET    /api/excuses                # Historial de excusas generadas
GET    /api/excuses/{id}           # Obtener por ID
POST   /api/excuses                # Crear excusa personalizada
```

**Ejemplo Request (POST)**:
```json
{
  "contextId": 1,
  "causeId": 2,
  "consequenceId": 3,
  "recommendationId": 4,
  "memeId": 1,
  "lawId": 1,
  "type": "ULTRA_SHARK"
}
```

---

## 📤 Ejemplo de Respuesta

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
  "role": null,
  "seed": 1234567890,
  "createdAt": "2024-01-20T14:30:45"
}
```

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
ULTRA_SHARK     // Fragmentos + meme + ley (completo)
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

## 📖 Documentación Adicional

### Arquitectura

Diagramas PlantUML en `/docs/uml/`:

- **EjercicioCopilot-Arquitectura.puml**: Diagrama de clases completo
- **EjercicioCopilot-Secuencia.puml**: Flujo ULTRA_SHARK (caso de uso principal)
- **EjercicioCopilot-Componentes.puml**: Descomposición en componentes
- **EjercicioCopilot-Despliegue.puml**: Diagrama de despliegue
- **EjercicioCopilot-FlujohHexagonal.puml**: Flujo hexagonal completo

### Instrucciones de Desarrollo

- `.github/copilot-instructions.md`: Guía para GitHub Copilot
- `.github/git-commit-instructions.md`: Convenciones de commits (Conventional Commits)
- `.github/prompts/controller.prompt.md`: Generación de controllers
- `.github/prompts/services.prompt.md`: Generación de servicios

## 🛠️ Tecnologías

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| Spring Boot | 3.2.0 | Framework principal |
| Java | 21+ | Lenguaje de programación |
| Spring Data JPA | 3.2.0 | Persistencia ORM |
| H2 Database | 2.x | Base de datos en memoria |
| Lombok | 1.18.30 | Boilerplate reduction |
| Jakarta Validation | 3.0 | Validaciones |
| JUnit 5 | 5.10 | Testing unitario |
| Mockito | 5.2 | Mocking en tests |
| Maven | 3.6+ | Build tool |

## 📊 Patrones Aplicados

- ✅ **Arquitectura Hexagonal**: Dominio independiente de infraestructura
- ✅ **SOLID**: Principios aplicados consistentemente
- ✅ **Clean Code**: Código legible y autodocumentado
- ✅ **Conventional Commits**: Historial de git semántico
- ✅ **Lazy Loading**: Evitar N+1 queries
- ✅ **DTOs**: Request/Response separados
- ✅ **Mappers**: Transformación Entity ↔ DTO

## 🚀 Roadmap Futuro (Level White Shark+)

- [x] Docker: Containerizar la aplicación
- [x] Docker Compose: Orquestación local
- [ ] Tests de Integración con RestAssured
- [ ] Autenticación OAuth2
- [ ] Caché con Redis
- [ ] Métricas con Actuator (básico ya incluido)
- [ ] Trazabilidad distribuida
- [ ] Generación de excusas con IA (integración LLM)
- [ ] Kubernetes: Despliegue en clusters
- [ ] PostgreSQL: Migrar de H2
- [ ] API Rate Limiting
- [ ] WebSockets para actualizaciones en tiempo real

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

## 🐛 Troubleshooting

### H2 Console no abre

```
→ Verificar que la aplicación está corriendo en puerto 8080
→ Acceder a: http://localhost:8080/h2-console
→ JDBC URL: jdbc:h2:mem:testdb
```

### Tests fallan

```bash
# Limpiar y recompilar
mvn clean test

# Ejecutar con logs
mvn test -X
```

### Port 8080 ya está en uso

```bash
# Cambiar puerto en application.properties
server.port=8081

# O matar proceso
lsof -ti:8080 | xargs kill -9
```

## 📞 Contacto y Contribuciones

Este es un proyecto de la **Tribu Java Sharks** para práctica con GitHub Copilot.

### Commits

Seguir [Conventional Commits](https://www.conventionalcommits.org/):

```bash
git commit -m "feat(ExcuseController): agregar endpoint /ultra"
git commit -m "fix(ExcuseService): corregir NPE sin fragmentos"
git commit -m "test(ExcuseServiceTest): aumentar cobertura a 85%"
```

## 📄 Licencia

MIT License - Ver LICENSE.md para detalles.

---

**Versión**: 1.0.0  
**Estado**: ✅ Funcional (Level Shark)  
**Última actualización**: Noviembre 2025  
**Mantenedor**: Equipo EjercicioCopilot
