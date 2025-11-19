---
agent: agent
---

# Prompt para Generación de Controllers - EjercicioCopilot

## Contexto del Proyecto

Estamos desarrollando **EjercicioCopilot**, un generador de excusas tech con Spring Boot 3.2.0 y Java 21.
La arquitectura sigue el patrón **Hexagonal (Ports & Adapters)** con Clean Code y SOLID principles.

## Objetivo

Generar controllers REST que sigan **buenas prácticas**, **clean code** y los patrones específicos del proyecto.

## Requisitos Principales

### 1. Estructura y Ubicación
- **Paquete**: `com.ejerciciocopilot.controller`
- **Patrón de nombres**: `{Recurso}Controller` (ej: `FragmentController`, `ExcuseController`)
- **Anotación de clase**: `@RestController` + `@RequestMapping("/api/{recurso}")`

### 2. Clean Code y Buenas Prácticas
- **Métodos pequeños**: Una responsabilidad por método
- **Nombres descriptivos**: Métodos que comiencen con verbos HTTP (`get`, `create`, `update`, `delete`)
- **Documentación**: JavaDoc en métodos públicos
- **Sin lógica de negocio**: Solo coordinación entre HTTP y servicios
- **Inyección por constructor**: Preferir constructor sobre `@Autowired` en campos
- **Códigos HTTP apropiados**: 200 OK, 201 Created, 204 No Content, 400 Bad Request, 404 Not Found, 500 Internal Server Error

### 3. Manejo de Requests/Responses
- **Request**: Usar DTOs con sufijo `RequestDTO` (ej: `FragmentRequestDTO`)
- **Response**: Usar DTOs con sufijo `ResponseDTO` (ej: `FragmentResponseDTO`)
- **Retorno**: Envolver en `ResponseEntity<T>` con código HTTP apropiado
- **Validación**: `@Valid` en parámetros de entrada
- **Códigos HTTP en ResponseEntity**:
  - `201 Created` para POST exitosos
  - `200 OK` para GET y PUT
  - `204 No Content` para DELETE
  - `400 Bad Request` para datos inválidos

### 4. Parámetros y Endpoints
- **Path variables**: `@PathVariable Long id`
- **Query parameters**: `@RequestParam(required = false)` para filtros opcionales
- **Body**: `@RequestBody` para datos complejos
- **Validación en DTOs**: `@NotBlank`, `@NotNull`, `@Size`, etc. en RequestDTOs

### 5. Patrones REST
```
GET    /api/{recurso}           # Listar todos
GET    /api/{recurso}/{id}      # Obtener por ID
POST   /api/{recurso}           # Crear (respuesta 201)
PUT    /api/{recurso}/{id}      # Actualizar completo
PATCH  /api/{recurso}/{id}      # Actualizar parcial
DELETE /api/{recurso}/{id}      # Eliminar (respuesta 204)
```

### 6. Llamadas a Service Layer
- **DEBEN usar métodos `*FromDTO()`** del service para resolver relaciones por ID
- Ejemplo: `excuseService.createFromDTO(requestDTO)` en lugar de `excuseService.create(requestDTO)`
- El service resuelve las relaciones usando los repositorios

### 7. Manejo de Errores
- Usar `try-catch` para capturar `EntityNotFoundException` u otras excepciones
- Retornar `ResponseEntity.notFound().build()` para recursos no encontrados
- Retornar `ResponseEntity.badRequest()` para datos inválidos
- Propagar excepciones personalizadas si existen

### 8. Anotaciones y Documentación
- `@RestController` en clase
- `@RequestMapping("/api/...")` en clase
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` en métodos
- JavaDoc para métodos públicos (qué hace, parámetros, retorno)
- `@ApiOperation` (si usas Swagger) o comentarios descriptivos

### 9. Ejemplo de Controller Correcto

```java
package com.ejerciciocopilot.controller;

import com.ejerciciocopilot.dto.FragmentRequestDTO;
import com.ejerciciocopilot.dto.FragmentResponseDTO;
import com.ejerciciocopilot.model.Fragment;
import com.ejerciciocopilot.service.FragmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fragments")
public class FragmentController {

    private final FragmentService fragmentService;

    /**
     * Constructor con inyección de dependencias
     */
    public FragmentController(FragmentService fragmentService) {
        this.fragmentService = fragmentService;
    }

    /**
     * Obtiene todos los fragmentos, opcionalmente filtrados por tipo
     */
    @GetMapping
    public ResponseEntity<List<FragmentResponseDTO>> listAll(
            @RequestParam(required = false) String tipo) {
        List<Fragment> fragments = fragmentService.findAll();
        
        List<FragmentResponseDTO> dtos = fragments.stream()
                .map(FragmentMapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene un fragmento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FragmentResponseDTO> getById(@PathVariable Long id) {
        return fragmentService.findById(id)
                .map(fragment -> ResponseEntity.ok(FragmentMapper.toResponse(fragment)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo fragmento
     */
    @PostMapping
    public ResponseEntity<FragmentResponseDTO> create(@Valid @RequestBody FragmentRequestDTO dto) {
        Fragment created = fragmentService.createFromDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FragmentMapper.toResponse(created));
    }

    /**
     * Actualiza un fragmento existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<FragmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FragmentRequestDTO dto) {
        try {
            Fragment updated = fragmentService.updateFromDTO(id, dto);
            return ResponseEntity.ok(FragmentMapper.toResponse(updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un fragmento
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            fragmentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

## Controllers a Generar

1. **FragmentController** (`/api/fragments`)
   - Operaciones CRUD completas
   - Filtro opcional por tipo (query param)
   - Relaciones resueltas en service

2. **ExcuseController** (`/api/excuses`)
   - GET /random - Excusa aleatoria
   - GET /role/{rol} - Excusa para rol específico
   - GET /daily - Excusa del día
   - GET /meme - Excusa con meme
   - GET /law - Excusa con ley
   - GET /ultra - Excusa completa (ULTRA_SHARK)
   - POST / - Crear excusa personalizada
   - GET /{id} - Obtener por ID

3. **MemeController** (`/api/memes`)
   - CRUD completo de memes
   - GET / - Listar todos
   - POST / - Crear meme
   - GET /{id} - Obtener por ID
   - PUT /{id} - Actualizar
   - DELETE /{id} - Eliminar

4. **LawController** (`/api/laws`)
   - CRUD completo de leyes
   - GET / - Listar todos (con filtro opcional por category)
   - POST / - Crear ley
   - GET /{id} - Obtener por ID
   - PUT /{id} - Actualizar
   - DELETE /{id} - Eliminar

## Convenciones a Seguir

- **Java 21**: Usar features modernas (records donde sea apropiado, var cuando sea claro)
- **Lombok**: Los DTOs deben tener `@Data`, `@Builder`
- **Validación**: En RequestDTOs con Jakarta Validation
- **Logging**: Opcional pero recomendado (`SLF4J` con `@Slf4j`)
- **Comentarios**: Solo donde la lógica no sea evidente
- **Formato**: Usar Google Java Style Guide (indentación 4 espacios)

## Criterios de Éxito

✅ Todos los controllers compilan sin errores  
✅ Cada controller maneja sus DTOs Request y Response  
✅ Códigos HTTP correctos para cada operación  
✅ Inyección de dependencias por constructor  
✅ Sin lógica de negocio en controllers  
✅ Métodos `*FromDTO()` del service siendo usados  
✅ Validación con `@Valid` en RequestDTOs  
✅ Manejo de excepciones con ResponseEntity  
✅ JavaDoc en métodos públicos  
✅ Nombres descriptivos y clean code