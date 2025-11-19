package com.ejerciciocopilot.service;

import com.ejerciciocopilot.dto.LawRequestDTO;
import com.ejerciciocopilot.exception.EntityNotFoundException;
import com.ejerciciocopilot.mapper.LawMapper;
import com.ejerciciocopilot.model.Law;
import com.ejerciciocopilot.repository.LawRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar leyes y axiomas del desarrollo.
 * Implementa la lógica de negocio para operaciones CRUD de leyes.
 */
@Service
@Transactional
public class LawService {

    private final LawRepository lawRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param lawRepository repositorio de leyes
     */
    public LawService(LawRepository lawRepository) {
        this.lawRepository = lawRepository;
    }

    /**
     * Obtiene una ley por su identificador.
     *
     * @param id identificador de la ley
     * @return Optional con la ley si existe
     */
    public Optional<Law> findById(Long id) {
        return lawRepository.findById(id);
    }

    /**
     * Obtiene todas las leyes almacenadas en la base de datos.
     *
     * @return lista de todas las leyes
     */
    public List<Law> findAll() {
        return lawRepository.findAll();
    }

    /**
     * Obtiene todas las leyes de una categoría específica.
     *
     * @param category categoría de la ley (Murphy, Hofstadter, Dilbert, DevOps, etc.)
     * @return lista de leyes de esa categoría
     */
    public List<Law> findByCategory(String category) {
        return lawRepository.findByCategory(category);
    }

    /**
     * Crea una nueva ley y la persiste en base de datos.
     * Asigna automáticamente el timestamp de creación si no viene seteado.
     *
     * @param law entidad ley a crear (no nulo)
     * @return ley creada con ID asignado
     */
    public Law create(Law law) {
        if (law.getCreatedAt() == null) {
            law.setCreatedAt(LocalDateTime.now());
        }
        law.setUpdatedAt(null);
        return lawRepository.save(law);
    }

    /**
     * Crea una ley a partir de un DTO recibido desde el controller.
     * Convierte el DTO a entidad usando el mapper y delega al método create().
     *
     * @param dto datos de la ley (validado por controller)
     * @return ley creada y persistida
     */
    public Law createFromDTO(LawRequestDTO dto) {
        Law law = LawMapper.toEntity(dto);
        return create(law);
    }

    /**
     * Actualiza una ley existente basándose en un DTO.
     * Solo modifica campos no-null del DTO para permitir actualizaciones parciales.
     *
     * @param id  identificador de la ley a actualizar
     * @param dto datos a actualizar (solo campos no-null se aplican)
     * @return ley actualizada
     * @throws EntityNotFoundException si la ley no existe
     */
    public Law updateFromDTO(Long id, LawRequestDTO dto) {
        return lawRepository.findById(id)
                .map(existing -> {
                    if (dto.getName() != null) {
                        existing.setName(dto.getName());
                    }
                    if (dto.getDescription() != null) {
                        existing.setDescription(dto.getDescription());
                    }
                    if (dto.getCategory() != null) {
                        existing.setCategory(dto.getCategory());
                    }
                    existing.setUpdatedAt(LocalDateTime.now());
                    return lawRepository.save(existing);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Law con ID " + id + " no encontrado"));
    }

    /**
     * Elimina una ley por su identificador.
     *
     * @param id identificador de la ley a eliminar
     * @throws EntityNotFoundException si la ley no existe
     */
    public void delete(Long id) {
        Law law = lawRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Law con ID " + id + " no encontrado"));
        lawRepository.delete(law);
    }
}
