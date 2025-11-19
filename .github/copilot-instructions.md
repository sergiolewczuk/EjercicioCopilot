# Instrucciones para GitHub Copilot - Proyecto EjercicioCopilot

## Arquitectura y Contexto

EjercicioCopilot es un **generador de excusas tech** que combina fragmentos, memes y leyes del caos developer. Construido con Spring Boot 3.2.0 y Java 21. La arquitectura sigue el patrón **Hexagonal (Ports & Adapters)**, separando el dominio de la infraestructura, con H2 como base de datos en memoria.

**Principios fundamentales**:
- **Arquitectura Hexagonal**: Dominio independiente de frameworks e infraestructura
- **Clean Code**: Código legible, autodocumentado y mantenible
- **KISS (Keep It Simple, Stupid)**: Soluciones simples antes que complejas
- **SOLID**: Aplicado consistentemente en toda la arquitectura
- **JPA**: Spring Data JPA para persistencia con H2 en memoria
- **Lombok**: Usado en TODAS las clases (entidades y DTOs) para reducir boilerplate

## Estructura de Paquetes (Hexagonal)

```
com.ejerciciocopilot/
├── controller/     # Adaptadores de entrada (REST) - Puerto HTTP
├── dto/           # Contratos de API (Request/Response con Lombok: @Data, @Builder)
├── model/         # Dominio - Entidades de negocio (@Entity con Lombok: @Getter, @Setter, @Builder)
├── repository/    # Adaptadores de salida - Puerto de Persistencia (JpaRepository)
└── service/       # Núcleo del dominio - Casos de uso y lógica de negocio + Mappers
```

**Flujo Hexagonal**:
```
HTTP Request → Controller (Adapter) → Service (Domain) → Repository (Adapter) → Database
             ↓                        ↓                   ↓
            DTOs              Generación Excusas    Persistencia JPA
```

**SOLID aplicado**:
- **S**ingle Responsibility: Cada clase tiene una única razón para cambiar (Controllers solo manejan HTTP, Services solo lógica de negocio)
- **O**pen/Closed: Extensible mediante herencia de repositories y composition en services
- **L**iskov Substitution: Interfaces JpaRepository permiten sustituir implementaciones
- **I**nterface Segregation: DTOs específicos por operación (Request vs Response)
- **D**ependency Inversion: Inyección por constructor, dependemos de abstracciones (Repository interfaces)

## Patrones Críticos del Proyecto

### 1. Relaciones Entity-DTO con IDs
**NO uses entidades anidadas en RequestDTOs**. Usa IDs para referencias:

```java
// ✅ CORRECTO - ExcuseRequestDTO
private Long contextId;
private Long causeId;
private Long consequenceId;

// ❌ INCORRECTO - No uses esto
private Fragment context;
```

**Los ResponseDTOs SÍ contienen objetos anidados** completos:
```java
// ExcuseResponseDTO incluye FragmentResponseDTO completo
private FragmentResponseDTO context;
```

### 2. Mappers Estáticos con Constructor Privado
Todos los mappers son clases utilitarias con constructor privado:

```java
public class TicketMapper {
    private TicketMapper() {} // Evita instanciación
    
    public static Ticket toEntity(TicketRequestDTO dto) { ... }
    public static TicketResponseDTO toResponse(Ticket t) { ... }
}
```

### 3. Service Layer - Dos Métodos por Operación
Los servicios exponen dos métodos para crear/actualizar:

```java
// Para uso directo con entidades
public Excuse create(Excuse excuse) { ... }

// Para uso desde controllers con DTOs - resuelve relaciones por ID
public Excuse createFromDTO(ExcuseRequestDTO dto) {
    Excuse excuse = ExcuseMapper.toEntity(dto);
    if (dto.getContextId() != null) {
        fragmentRepo.findById(dto.getContextId()).ifPresent(excuse::setContext);
    }
    return create(excuse);
}
```

**Controllers DEBEN usar métodos `*FromDTO`** para manejar correctamente las relaciones.

### 4. Lazy Loading en Relaciones
Todas las relaciones `@ManyToOne` usan `FetchType.LAZY`:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "reporter_id")
private Person reporter;
```

Los mappers manejan objetos null de forma segura (ver `PersonMapper.toResponse()`).

### 5. Valores por Defecto en Service Layer
Los defaults se aplican en servicios, NO en constructores:

```java
public Excuse create(Excuse excuse) {
    if (excuse.getType() == null) excuse.setType(ExcuseType.SIMPLE);
    excuse.setCreatedAt(LocalDateTime.now());
    // ...
}
```

### 6. Actualización Parcial con Null-Safe
Los métodos `update` solo modifican campos no-null del DTO:

```java
if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
```

## Convenciones de Código

### Clean Code y Buenas Prácticas
- **Nombres descriptivos**: `createFromDTO()` mejor que `create2()`
- **Métodos pequeños**: Una función = una responsabilidad
- **No Magic Numbers**: Usar constantes o enums (`Status.OPEN` no `"OPEN"`)
- **DRY**: Extraer lógica repetida a métodos privados o utilities

### Uso de Lombok
- **Entidades (@Entity)**: Usar `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- **DTOs**: Usar `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- **Mappers**: Constructor privado manual (no instanciables, sin Lombok)

### JPA y Persistencia
- **Enums**: Usar `@Enumerated(EnumType.STRING)` para persistencia legible
- **IDs**: Siempre `Long` con `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- **Timestamps**: `LocalDateTime` para `createdAt` / `updatedAt`
- **Relaciones**: `FetchType.LAZY` por defecto, evitar N+1 queries
- **Validaciones**: Jakarta Validation solo en RequestDTOs (`@NotBlank`, `@Size`, `@Email`)

### REST y Controllers
- **Responses HTTP**: Usar `ResponseEntity<T>` con códigos apropiados (201 Created, 404 Not Found)
- **Inyección de dependencias**: Siempre por constructor (no `@Autowired` en campos)
- **KISS**: Endpoints simples, lógica compleja en services

## Comandos de Desarrollo

```bash
# Compilar
mvn clean package

# Ejecutar aplicación
mvn spring-boot:run

# Acceder a H2 Console
# URL: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Usuario: sa, Password: (vacía)

# Acceder a Swagger
# URL: http://localhost:8080/swagger-ui.html
```

## Estados y Enums del Dominio

```java
FragmentType: CONTEXTO, CAUSA, CONSECUENCIA, RECOMENDACION
ExcuseType: SIMPLE, CON_MEME, CON_LEY, ULTRA_SHARK
Role: DEV, QA, DEVOPS, PM, ARCHITECT, DEVREL
```

## Endpoints REST

Todos bajo `/api/{recurso}`:
- Fragments: `/api/fragments` (CRUD de fragmentos por tipo)
- Memes: `/api/memes` (CRUD de memes tech argentinos)
- Laws: `/api/laws` (CRUD de leyes/axiomas)
- Excuses: `/api/excuses` (Generación y consulta)
- Roles: `/api/roles` (Gestión de roles - opcional)

**Patrones principales**:
```
GET  /api/fragments?tipo=CONTEXTO
POST /api/fragments
PUT  /api/fragments/{id}
DELETE /api/fragments/{id}

GET /api/excuses/random
GET /api/excuses/role/{rol}
GET /api/excuses/daily
GET /api/excuses/meme
GET /api/excuses/law
GET /api/excuses/ultra

GET /api/health
```

## Tests

Mínimo obligatorio:
- 1 test unitario del combinador de excusas (validar reproducibilidad con seed)
- Tests de controladores con MockMvc
- Tests de servicios con Mockito

Al crear tests:
- Preferir tests unitarios con Mockito para servicios
- Tests de integración con `@SpringBootTest` y H2
- Usar `RestAssured` para tests de API REST (Level White Shark)
- Tests deben ser independientes y reproducibles

## Modelos Principales

### Fragment (Fragmento)
Representa las partes componentes de una excusa:
- **id**: Long (PK)
- **type**: FragmentType (CONTEXTO, CAUSA, CONSECUENCIA, RECOMENDACION)
- **text**: String (contenido del fragmento)
- **role**: Role (DEV, QA, DEVOPS, PM, ARCHITECT, DEVREL) - opcional
- **createdAt**: LocalDateTime

### Meme
Representa memes tech argentinos:
- **id**: Long (PK)
- **author**: String (ej: "Tano Pasman")
- **quote**: String (texto del meme)
- **createdAt**: LocalDateTime

### Law
Representa leyes y axiomas del desarrollo:
- **id**: Long (PK)
- **name**: String (ej: "Ley de Murphy", "Axioma de Hofstadter")
- **description**: String (descripción completa)
- **category**: String (ej: "Murphy", "Hofstadter", "Dilbert", "DevOps")
- **createdAt**: LocalDateTime

### Excuse
Excusa generada (combinación de fragmentos):
- **id**: Long (PK)
- **context**: Fragment (@ManyToOne, LAZY)
- **cause**: Fragment (@ManyToOne, LAZY)
- **consequence**: Fragment (@ManyToOne, LAZY)
- **recommendation**: Fragment (@ManyToOne, LAZY)
- **meme**: Meme (@ManyToOne, LAZY) - opcional
- **law**: Law (@ManyToOne, LAZY) - opcional
- **type**: ExcuseType (SIMPLE, CON_MEME, CON_LEY, ULTRA_SHARK)
- **role**: Role (para excusas específicas por rol) - opcional
- **seed**: Long (para reproducibilidad)
- **createdAt**: LocalDateTime
