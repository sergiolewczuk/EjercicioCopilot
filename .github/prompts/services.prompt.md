---
agent: agent
---

# Prompt para Generación de Services - EjercicioCopilot

## Contexto del Proyecto

Estamos desarrollando **EjercicioCopilot**, un generador de excusas tech con Spring Boot 3.2.0 y Java 21.
La arquitectura sigue el patrón **Hexagonal (Ports & Adapters)** con Clean Code y SOLID principles.

## Objetivo

Generar services que implementen la **lógica de negocio** del proyecto, siguiendo patrones de arquitectura hexagonal,
clean code, y buenas prácticas. Los services son el **núcleo del dominio** y contienen toda la lógica compleja.

## Requisitos Principales

### 1. Estructura y Ubicación
- **Paquete**: `com.ejerciciocopilot.service`
- **Patrón de nombres**: `{Recurso}Service` (ej: `FragmentService`, `ExcuseService`)
- **Anotación de clase**: `@Service` + `@Transactional` si es necesario
- **Inyección de dependencias**: Por constructor (nunca `@Autowired` en campos)

### 2. Responsabilidades del Service Layer
- **Lógica de negocio**: Toda la lógica compleja del dominio va aquí
- **Gestión de transacciones**: Usar `@Transactional` cuando sea necesario
- **Validaciones de negocio**: Validar reglas del dominio
- **Orchestración**: Coordinar entre repositorios y otros servicios
- **Mapeo de DTOs**: Dos métodos por operación (`create` y `createFromDTO`)
- **Resolución de relaciones**: Resolver IDs a entidades en métodos `*FromDTO`

### 3. Patrón Dual de Métodos

**Todos los servicios deben exponer dos métodos para operaciones CRUD**:

```java
// Método 1: Para uso interno o directo con entidades
public Fragment create(Fragment fragment) {
    // Aplicar defaults, validaciones, etc.
    if (fragment.getCreatedAt() == null) {
        fragment.setCreatedAt(LocalDateTime.now());
    }
    return fragmentRepository.save(fragment);
}

// Método 2: Para uso desde controllers con DTOs
public Fragment createFromDTO(FragmentRequestDTO dto) {
    Fragment fragment = FragmentMapper.toEntity(dto);
    // Aquí se resuelven las relaciones por ID si es necesario
    return create(fragment);
}

// Igual para update
public Fragment update(Long id, Fragment fragment) { ... }

public Fragment updateFromDTO(Long id, FragmentRequestDTO dto) {
    return findById(id).map(existing -> {
        // Actualizar solo campos no-null
        if (dto.getText() != null) existing.setText(dto.getText());
        // ...
        return update(id, existing);
    }).orElseThrow(() -> new EntityNotFoundException("Fragment no encontrado"));
}
```

### 4. Clean Code y Documentación

**Cada método público DEBE tener JavaDoc completo**:

```java
/**
 * Crea un nuevo fragmento a partir de los datos de la entidad.
 * Aplica valores por defecto (createdAt) y persiste en base de datos.
 *
 * @param fragment entidad a crear (no nulo)
 * @return fragmento creado con ID asignado
 * @throws IllegalArgumentException si el fragment es nulo o inválido
 */
public Fragment create(Fragment fragment) {
    // Implementación...
}

/**
 * Crea un nuevo fragmento a partir de un DTO recibido desde el controller.
 * Convierte el DTO a entidad y delega al método create().
 *
 * @param dto datos del fragmento (validado por controller)
 * @return fragmento creado persistido
 * @throws IllegalArgumentException si el DTO es nulo
 */
public Fragment createFromDTO(FragmentRequestDTO dto) {
    Fragment fragment = FragmentMapper.toEntity(dto);
    return create(fragment);
}
```

### 5. Métodos CRUD Estándar

Todos los services deben implementar estos métodos base:

```java
// Búsqueda
Optional<T> findById(Long id);
List<T> findAll();

// Creación
T create(T entity);
T createFromDTO(RequestDTO dto);

// Actualización
T update(Long id, T entity);
T updateFromDTO(Long id, RequestDTO dto);

// Eliminación
void delete(Long id);
```

### 6. Manejo de Errores

- **Excepciones personalizadas**: Crear `EntityNotFoundException` para recursos no encontrados
- **Validaciones**: Lanzar excepciones con mensajes descriptivos
- **Sin capturar excepciones silenciosamente**: Propagar o loguear apropiadamente

```java
public Fragment delete(Long id) {
    return findById(id)
            .map(fragment -> {
                fragmentRepository.delete(fragment);
                return fragment;
            })
            .orElseThrow(() -> new EntityNotFoundException("Fragment con ID " + id + " no encontrado"));
}
```

### 7. Service Layer Específico: ExcuseService

Este es el **servicio core del proyecto** que genera excusas tech:

```java
@Service
public class ExcuseService {
    private final ExcuseRepository excuseRepository;
    private final FragmentRepository fragmentRepository;
    private final MemeRepository memeRepository;
    private final LawRepository lawRepository;
    private final Random random;

    // Constructor con inyección de todas las dependencias

    /**
     * Genera una excusa aleatoria simple.
     * Selecciona 4 fragmentos aleatorios (contexto, causa, consecuencia, recomendación)
     * y los combina en una excusa tipo SIMPLE.
     *
     * @return excusa generada
     */
    public Excuse generateRandom() {
        Excuse excuse = new Excuse();
        excuse.setContext(getRandomFragment(FragmentType.CONTEXTO));
        excuse.setCause(getRandomFragment(FragmentType.CAUSA));
        excuse.setConsequence(getRandomFragment(FragmentType.CONSECUENCIA));
        excuse.setRecommendation(getRandomFragment(FragmentType.RECOMENDACION));
        excuse.setType(ExcuseType.SIMPLE);
        excuse.setSeed(System.nanoTime());
        excuse.setCreatedAt(LocalDateTime.now());
        return excuseRepository.save(excuse);
    }

    /**
     * Genera una excusa con meme incluido.
     * Igual a generateRandom() pero añade un meme random y tipo CON_MEME.
     *
     * @return excusa con meme
     */
    public Excuse generateWithMeme() {
        Excuse excuse = generateRandom();
        excuse.setMeme(getRandomMeme());
        excuse.setType(ExcuseType.CON_MEME);
        return excuseRepository.save(excuse);
    }

    /**
     * Genera una excusa justificada con una ley del desarrollo.
     * Similar a generateRandom() pero añade una ley y tipo CON_LEY.
     *
     * @return excusa con ley
     */
    public Excuse generateWithLaw() {
        Excuse excuse = generateRandom();
        excuse.setLaw(getRandomLaw());
        excuse.setType(ExcuseType.CON_LEY);
        return excuseRepository.save(excuse);
    }

    /**
     * Genera una excusa ULTRA_SHARK con TODO: fragmentos + meme + ley.
     * Es la versión más completa y entretenida de una excusa.
     *
     * @return excusa ULTRA_SHARK
     */
    public Excuse generateUltraShark() {
        Excuse excuse = generateRandom();
        excuse.setMeme(getRandomMeme());
        excuse.setLaw(getRandomLaw());
        excuse.setType(ExcuseType.ULTRA_SHARK);
        return excuseRepository.save(excuse);
    }

    /**
     * Genera una excusa específica para un rol (DEV, QA, DEVOPS, etc.).
     * Selecciona fragmentos que aplican a ese rol específico.
     *
     * @param role rol del desarrollador
     * @return excusa personalizada para el rol
     * @throws IllegalArgumentException si el rol no es válido
     */
    public Excuse generateByRole(String role) {
        Role roleEnum = Role.valueOf(role.toUpperCase());
        List<Fragment> contextFragments = fragmentRepository.findByTypeAndRole(
            FragmentType.CONTEXTO, roleEnum);
        // Seleccionar fragmentos específicos del rol...
        return buildExcuse(contextFragments, roleEnum);
    }

    /**
     * Genera la excusa del día de manera reproducible.
     * Usa la fecha actual como seed para asegurar la misma excusa todo el día.
     *
     * @return excusa del día (misma para toda la jornada)
     */
    public Excuse generateDaily() {
        long seed = LocalDate.now().toEpochDay();
        random.setSeed(seed);
        Excuse excuse = generateRandom();
        excuse.setSeed(seed);
        return excuseRepository.save(excuse);
    }

    // Métodos privados helper
    /**
     * Obtiene un fragmento aleatorio de un tipo específico.
     * @param type tipo de fragmento (CONTEXTO, CAUSA, etc.)
     * @return fragmento aleatorio del tipo especificado
     */
    private Fragment getRandomFragment(FragmentType type) {
        List<Fragment> fragments = fragmentRepository.findByType(type);
        if (fragments.isEmpty()) {
            throw new IllegalStateException("No hay fragmentos de tipo " + type);
        }
        return fragments.get(random.nextInt(fragments.size()));
    }

    /**
     * Obtiene un meme aleatorio de la base de datos.
     * @return meme aleatorio
     */
    private Meme getRandomMeme() {
        List<Meme> memes = memeRepository.findAll();
        if (memes.isEmpty()) {
            return null;
        }
        return memes.get(random.nextInt(memes.size()));
    }

    /**
     * Obtiene una ley aleatoria de la base de datos.
     * @return ley aleatoria
     */
    private Law getRandomLaw() {
        List<Law> laws = lawRepository.findAll();
        if (laws.isEmpty()) {
            return null;
        }
        return laws.get(random.nextInt(laws.size()));
    }
}
```

### 8. Services CRUD Básicos

Implementar patrones simples para Fragment, Meme, Law:

```java
@Service
public class FragmentService {
    private final FragmentRepository repository;

    public FragmentService(FragmentRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene un fragmento por su ID.
     * @param id identificador del fragmento
     * @return Optional con el fragmento si existe
     */
    public Optional<Fragment> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Obtiene todos los fragmentos de la base de datos.
     * @return lista de todos los fragmentos
     */
    public List<Fragment> findAll() {
        return repository.findAll();
    }

    /**
     * Crea un nuevo fragmento y lo persiste.
     * Asigna createdAt si no viene seteado.
     *
     * @param fragment entidad a crear
     * @return fragmento creado con ID asignado
     */
    public Fragment create(Fragment fragment) {
        if (fragment.getCreatedAt() == null) {
            fragment.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(fragment);
    }

    /**
     * Crea un fragmento a partir de un DTO.
     * Delega la conversión al mapper.
     *
     * @param dto datos del fragmento
     * @return fragmento creado
     */
    public Fragment createFromDTO(FragmentRequestDTO dto) {
        Fragment fragment = FragmentMapper.toEntity(dto);
        return create(fragment);
    }

    /**
     * Actualiza un fragmento existente.
     * Solo modifica campos no-null del DTO.
     *
     * @param id identificador del fragmento
     * @param dto datos a actualizar
     * @return fragmento actualizado
     * @throws EntityNotFoundException si no existe el fragmento
     */
    public Fragment updateFromDTO(Long id, FragmentRequestDTO dto) {
        return findById(id)
                .map(existing -> {
                    if (dto.getText() != null) {
                        existing.setText(dto.getText());
                    }
                    if (dto.getType() != null) {
                        existing.setType(dto.getType());
                    }
                    if (dto.getRole() != null) {
                        existing.setRole(dto.getRole());
                    }
                    return repository.save(existing);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                    "Fragment con ID " + id + " no encontrado"));
    }

    /**
     * Elimina un fragmento por su ID.
     * @param id identificador del fragmento a eliminar
     * @throws EntityNotFoundException si no existe
     */
    public void delete(Long id) {
        Fragment fragment = findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Fragment con ID " + id + " no encontrado"));
        repository.delete(fragment);
    }
}
```

### 9. Anotaciones y Configuración

- `@Service`: Marca la clase como componente de servicio
- `@Transactional`: Para operaciones que requieren transacción
- Constructor público para inyección de dependencias
- Métodos públicos para operaciones externas
- Métodos privados para lógica interna helper

### 10. Validaciones y Excepciones

Crear excepción personalizada:

```java
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
```

### 11. Ejemplo Completo Integrado

```java
@Service
public class FragmentService {
    private final FragmentRepository fragmentRepository;

    public FragmentService(FragmentRepository fragmentRepository) {
        this.fragmentRepository = fragmentRepository;
    }

    // Métodos CRUD con documentación completa
    // Cada método tiene JavaDoc explicando:
    // - Qué hace
    // - Parámetros (con tipos)
    // - Retorno
    // - Excepciones posibles
    // - Comportamiento especial si aplica
}
```

## Services a Generar

1. **FragmentService** - CRUD de fragmentos
   - findById, findAll, create, createFromDTO, updateFromDTO, delete
   - Sin lógica especial, solo CRUD básico

2. **MemeService** - CRUD de memes
   - Mismo patrón que FragmentService
   - Sin lógica especial

3. **LawService** - CRUD de leyes
   - Mismo patrón que FragmentService
   - Opcionalmente filtrar por categoría

4. **ExcuseService** - **SERVICIO CORE** del proyecto
   - generateRandom() - Excusa aleatoria simple
   - generateWithMeme() - Excusa + meme
   - generateWithLaw() - Excusa + ley
   - generateUltraShark() - Excusa + meme + ley
   - generateByRole(String role) - Excusa personalizada por rol
   - generateDaily() - Excusa reproducible por día
   - findById, findAll - Para historial
   - createFromDTO - Para crear excusas personalizadas
   - Métodos privados helpers: getRandomFragment, getRandomMeme, getRandomLaw

## Convenciones de Código

- **Java 21**: Usar features modernas (var, records si aplica)
- **JavaDoc completo**: Todos los métodos públicos
- **Métodos pequeños**: Una responsabilidad por método
- **Sin duplicación**: DRY principle
- **Nombres descriptivos**: `generateWithMeme` no `gen2`
- **Transacciones**: Usar `@Transactional` en métodos que escriben
- **Logging**: Opcional pero útil en métodos importantes (`SLF4J` con `@Slf4j`)

## Criterios de Éxito

✅ Todos los services compilan sin errores  
✅ Cada service implementa patrón dual (método normal + `*FromDTO`)  
✅ Métodos `*FromDTO` resuelven relaciones usando repositorios  
✅ Inyección de dependencias por constructor  
✅ JavaDoc completo en todos los métodos públicos  
✅ Excepciones personalizadas para casos de error  
✅ Valores por defecto en service layer (no en constructor)  
✅ Métodos privados helpers para reutilizar lógica  
✅ ExcuseService con toda la lógica de generación  
✅ Clean code y nombres auto-documentables