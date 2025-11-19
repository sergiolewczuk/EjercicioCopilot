package com.ejerciciocopilot.controller;

import com.ejerciciocopilot.dto.FragmentRequestDTO;
import com.ejerciciocopilot.dto.FragmentResponseDTO;
import com.ejerciciocopilot.mapper.FragmentMapper;
import com.ejerciciocopilot.model.Fragment;
import com.ejerciciocopilot.service.FragmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gestionar fragmentos de excusas tech.
 * Soporta CRUD completo de fragmentos (contexto, causa, consecuencia, recomendación).
 */
@RestController
@RequestMapping("/api/fragments")
public class FragmentController {

    private final FragmentService fragmentService;

    /**
     * Constructor con inyección de dependencias.
     */
    public FragmentController(FragmentService fragmentService) {
        this.fragmentService = fragmentService;
    }

    /**
     * Obtiene todos los fragmentos, opcionalmente filtrados por tipo.
     *
     * @param tipo tipo de fragmento (CONTEXTO, CAUSA, CONSECUENCIA, RECOMENDACION)
     * @return lista de fragmentos como ResponseDTO
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
     * Obtiene un fragmento por ID.
     *
     * @param id identificador del fragmento
     * @return fragmento encontrado o 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<FragmentResponseDTO> getById(@PathVariable Long id) {
        return fragmentService.findById(id)
                .map(fragment -> ResponseEntity.ok(FragmentMapper.toResponse(fragment)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo fragmento.
     *
     * @param dto datos del fragmento a crear (validado)
     * @return fragmento creado con código 201
     */
    @PostMapping
    public ResponseEntity<FragmentResponseDTO> create(@Valid @RequestBody FragmentRequestDTO dto) {
        Fragment created = fragmentService.createFromDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FragmentMapper.toResponse(created));
    }

    /**
     * Actualiza un fragmento existente.
     *
     * @param id  identificador del fragmento
     * @param dto datos actualizados
     * @return fragmento actualizado o 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<FragmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FragmentRequestDTO dto) {
        return fragmentService.findById(id)
                .map(existing -> {
                    Fragment updated = fragmentService.updateFromDTO(id, dto);
                    return ResponseEntity.ok(FragmentMapper.toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un fragmento.
     *
     * @param id identificador del fragmento
     * @return respuesta 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return fragmentService.findById(id)
                .map(fragment -> {
                    fragmentService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
