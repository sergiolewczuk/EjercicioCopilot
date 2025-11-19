package com.ejerciciocopilot.controller;

import com.ejerciciocopilot.dto.MemeRequestDTO;
import com.ejerciciocopilot.dto.MemeResponseDTO;
import com.ejerciciocopilot.mapper.MemeMapper;
import com.ejerciciocopilot.model.Meme;
import com.ejerciciocopilot.service.MemeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gestionar memes tech argentinos.
 * Soporta CRUD completo de memes.
 */
@RestController
@RequestMapping("/api/memes")
public class MemeController {

    private final MemeService memeService;

    /**
     * Constructor con inyección de dependencias.
     */
    public MemeController(MemeService memeService) {
        this.memeService = memeService;
    }

    /**
     * Obtiene todos los memes.
     *
     * @return lista de memes como ResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<MemeResponseDTO>> listAll() {
        List<Meme> memes = memeService.findAll();
        
        List<MemeResponseDTO> dtos = memes.stream()
                .map(MemeMapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene un meme por ID.
     *
     * @param id identificador del meme
     * @return meme encontrado o 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemeResponseDTO> getById(@PathVariable Long id) {
        return memeService.findById(id)
                .map(meme -> ResponseEntity.ok(MemeMapper.toResponse(meme)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo meme.
     *
     * @param dto datos del meme a crear (validado)
     * @return meme creado con código 201
     */
    @PostMapping
    public ResponseEntity<MemeResponseDTO> create(@Valid @RequestBody MemeRequestDTO dto) {
        Meme created = memeService.createFromDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MemeMapper.toResponse(created));
    }

    /**
     * Actualiza un meme existente.
     *
     * @param id  identificador del meme
     * @param dto datos actualizados
     * @return meme actualizado o 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<MemeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MemeRequestDTO dto) {
        return memeService.findById(id)
                .map(existing -> {
                    Meme updated = memeService.updateFromDTO(id, dto);
                    return ResponseEntity.ok(MemeMapper.toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un meme.
     *
     * @param id identificador del meme
     * @return respuesta 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return memeService.findById(id)
                .map(meme -> {
                    memeService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
