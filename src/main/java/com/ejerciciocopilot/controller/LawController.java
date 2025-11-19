package com.ejerciciocopilot.controller;

import com.ejerciciocopilot.dto.LawRequestDTO;
import com.ejerciciocopilot.dto.LawResponseDTO;
import com.ejerciciocopilot.mapper.LawMapper;
import com.ejerciciocopilot.model.Law;
import com.ejerciciocopilot.service.LawService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gestionar leyes y axiomas del desarrollo.
 * Soporta CRUD completo de leyes (Murphy, Hofstadter, Dilbert, DevOps, etc.).
 */
@RestController
@RequestMapping("/api/laws")
public class LawController {

    private final LawService lawService;

    /**
     * Constructor con inyección de dependencias.
     */
    public LawController(LawService lawService) {
        this.lawService = lawService;
    }

    /**
     * Obtiene todas las leyes, opcionalmente filtradas por categoría.
     *
     * @param category categoría de la ley (Murphy, Hofstadter, Dilbert, DevOps, etc.)
     * @return lista de leyes como ResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<LawResponseDTO>> listAll(
            @RequestParam(required = false) String category) {
        List<Law> laws = lawService.findAll();
        
        List<LawResponseDTO> dtos = laws.stream()
                .map(LawMapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene una ley por ID.
     *
     * @param id identificador de la ley
     * @return ley encontrada o 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<LawResponseDTO> getById(@PathVariable Long id) {
        return lawService.findById(id)
                .map(law -> ResponseEntity.ok(LawMapper.toResponse(law)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva ley.
     *
     * @param dto datos de la ley a crear (validado)
     * @return ley creada con código 201
     */
    @PostMapping
    public ResponseEntity<LawResponseDTO> create(@Valid @RequestBody LawRequestDTO dto) {
        Law created = lawService.createFromDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LawMapper.toResponse(created));
    }

    /**
     * Actualiza una ley existente.
     *
     * @param id  identificador de la ley
     * @param dto datos actualizados
     * @return ley actualizada o 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<LawResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LawRequestDTO dto) {
        return lawService.findById(id)
                .map(existing -> {
                    Law updated = lawService.updateFromDTO(id, dto);
                    return ResponseEntity.ok(LawMapper.toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una ley.
     *
     * @param id identificador de la ley
     * @return respuesta 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return lawService.findById(id)
                .map(law -> {
                    lawService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
