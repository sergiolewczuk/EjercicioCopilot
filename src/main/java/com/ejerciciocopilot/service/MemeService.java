package com.ejerciciocopilot.service;

import com.ejerciciocopilot.dto.MemeRequestDTO;
import com.ejerciciocopilot.exception.EntityNotFoundException;
import com.ejerciciocopilot.mapper.MemeMapper;
import com.ejerciciocopilot.model.Meme;
import com.ejerciciocopilot.repository.MemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar memes tech argentinos.
 * Implementa la lógica de negocio para operaciones CRUD de memes.
 */
@Service
@Transactional
public class MemeService {

    private final MemeRepository memeRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param memeRepository repositorio de memes
     */
    public MemeService(MemeRepository memeRepository) {
        this.memeRepository = memeRepository;
    }

    /**
     * Obtiene un meme por su identificador.
     *
     * @param id identificador del meme
     * @return Optional con el meme si existe
     */
    public Optional<Meme> findById(Long id) {
        return memeRepository.findById(id);
    }

    /**
     * Obtiene todos los memes almacenados en la base de datos.
     *
     * @return lista de todos los memes
     */
    public List<Meme> findAll() {
        return memeRepository.findAll();
    }

    /**
     * Crea un nuevo meme y lo persiste en base de datos.
     * Asigna automáticamente el timestamp de creación si no viene seteado.
     *
     * @param meme entidad meme a crear (no nulo)
     * @return meme creado con ID asignado
     */
    public Meme create(Meme meme) {
        if (meme.getCreatedAt() == null) {
            meme.setCreatedAt(LocalDateTime.now());
        }
        meme.setUpdatedAt(null); // aseguramos coherencia inicial
        return memeRepository.save(meme);
    }

    /**
     * Crea un meme a partir de un DTO recibido desde el controller.
     * Convierte el DTO a entidad usando el mapper y delega al método create().
     *
     * @param dto datos del meme (validado por controller)
     * @return meme creado y persistido
     */
    public Meme createFromDTO(MemeRequestDTO dto) {
        Meme meme = MemeMapper.toEntity(dto);
        return create(meme);
    }

    /**
     * Actualiza un meme existente basándose en un DTO.
     * Solo modifica campos no-null del DTO para permitir actualizaciones parciales.
     *
     * @param id  identificador del meme a actualizar
     * @param dto datos a actualizar (solo campos no-null se aplican)
     * @return meme actualizado
     * @throws EntityNotFoundException si el meme no existe
     */
    public Meme updateFromDTO(Long id, MemeRequestDTO dto) {
        return memeRepository.findById(id)
                .map(existing -> {
                    if (dto.getAuthor() != null) {
                        existing.setAuthor(dto.getAuthor());
                    }
                    if (dto.getQuote() != null) {
                        existing.setQuote(dto.getQuote());
                    }
                    existing.setUpdatedAt(LocalDateTime.now());
                    return memeRepository.save(existing);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Meme con ID " + id + " no encontrado"));
    }

    /**
     * Elimina un meme por su identificador.
     *
     * @param id identificador del meme a eliminar
     * @throws EntityNotFoundException si el meme no existe
     */
    public void delete(Long id) {
        Meme meme = memeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Meme con ID " + id + " no encontrado"));
        memeRepository.delete(meme);
    }
}
