package com.ejerciciocopilot.controller;

import com.ejerciciocopilot.dto.ExcuseRequestDTO;
import com.ejerciciocopilot.dto.ExcuseResponseDTO;
import com.ejerciciocopilot.mapper.ExcuseMapper;
import com.ejerciciocopilot.model.Excuse;
import com.ejerciciocopilot.service.ExcuseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gestionar y generar excusas tech.
 * Soporta generación de excusas simples, con memes, con leyes y modo ULTRA_SHARK.
 */
@RestController
@RequestMapping("/api/excuses")
public class ExcuseController {

    private final ExcuseService excuseService;

    /**
     * Constructor con inyección de dependencias.
     */
    public ExcuseController(ExcuseService excuseService) {
        this.excuseService = excuseService;
    }

    /**
     * Obtiene una excusa aleatoria.
     *
     * @return excusa aleatoria como ResponseDTO
     */
    @GetMapping("/random")
    public ResponseEntity<ExcuseResponseDTO> getRandom() {
        Excuse excuse = excuseService.generateRandom();
        return ResponseEntity.ok(ExcuseMapper.toResponse(excuse));
    }

    /**
     * Obtiene una excusa aleatoria para un rol específico.
     *
     * @param role rol del desarrollador (DEV, QA, DEVOPS, PM, ARCHITECT, DEVREL)
     * @return excusa personalizada para el rol o 400 si el rol es inválido
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<ExcuseResponseDTO> getByRole(@PathVariable String role) {
        try {
            Excuse excuse = excuseService.generateByRole(role);
            return ResponseEntity.ok(ExcuseMapper.toResponse(excuse));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene la excusa del día (reproducible basada en fecha).
     *
     * @return excusa del día como ResponseDTO
     */
    @GetMapping("/daily")
    public ResponseEntity<ExcuseResponseDTO> getDaily() {
        Excuse excuse = excuseService.generateDaily();
        return ResponseEntity.ok(ExcuseMapper.toResponse(excuse));
    }

    /**
     * Obtiene una excusa con meme incluido.
     *
     * @return excusa con meme como ResponseDTO
     */
    @GetMapping("/meme")
    public ResponseEntity<ExcuseResponseDTO> getMeme() {
        Excuse excuse = excuseService.generateWithMeme();
        return ResponseEntity.ok(ExcuseMapper.toResponse(excuse));
    }

    /**
     * Obtiene una excusa justificada con una ley.
     *
     * @return excusa con ley como ResponseDTO
     */
    @GetMapping("/law")
    public ResponseEntity<ExcuseResponseDTO> getLaw() {
        Excuse excuse = excuseService.generateWithLaw();
        return ResponseEntity.ok(ExcuseMapper.toResponse(excuse));
    }

    /**
     * Obtiene una excusa ULTRA_SHARK (meme + ley + fragmentos).
     *
     * @return excusa completa ULTRA_SHARK como ResponseDTO
     */
    @GetMapping("/ultra")
    public ResponseEntity<ExcuseResponseDTO> getUltra() {
        Excuse excuse = excuseService.generateUltraShark();
        return ResponseEntity.ok(ExcuseMapper.toResponse(excuse));
    }

    /**
     * Obtiene una excusa por ID.
     *
     * @param id identificador de la excusa
     * @return excusa encontrada o 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExcuseResponseDTO> getById(@PathVariable Long id) {
        return excuseService.findById(id)
                .map(excuse -> ResponseEntity.ok(ExcuseMapper.toResponse(excuse)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una excusa personalizada.
     *
     * @param dto datos de la excusa a crear (validado)
     * @return excusa creada con código 201
     */
    @PostMapping
    public ResponseEntity<ExcuseResponseDTO> create(@Valid @RequestBody ExcuseRequestDTO dto) {
        Excuse created = excuseService.createFromDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ExcuseMapper.toResponse(created));
    }

    /**
     * Obtiene el historial de excusas generadas.
     *
     * @return lista de excusas como ResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<ExcuseResponseDTO>> listAll() {
        List<Excuse> excuses = excuseService.findAll();
        
        List<ExcuseResponseDTO> dtos = excuses.stream()
                .map(ExcuseMapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
}
