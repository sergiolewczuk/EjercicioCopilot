package com.ejerciciocopilot.service;

import com.ejerciciocopilot.dto.FragmentRequestDTO;
import com.ejerciciocopilot.exception.EntityNotFoundException;
import com.ejerciciocopilot.mapper.FragmentMapper;
import com.ejerciciocopilot.model.Fragment;
import com.ejerciciocopilot.repository.FragmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar fragmentos de excusas tech.
 * Implementa la lógica de negocio para operaciones CRUD de fragmentos.
 */
@Service
@Transactional
public class FragmentService {

    private final FragmentRepository fragmentRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param fragmentRepository repositorio de fragmentos
     */
    public FragmentService(FragmentRepository fragmentRepository) {
        this.fragmentRepository = fragmentRepository;
    }

    /**
     * Obtiene un fragmento por su identificador.
     *
     * @param id identificador del fragmento
     * @return Optional con el fragmento si existe
     */
    public Optional<Fragment> findById(Long id) {
        return fragmentRepository.findById(id);
    }

    /**
     * Obtiene todos los fragmentos almacenados en la base de datos.
     *
     * @return lista de todos los fragmentos
     */
    public List<Fragment> findAll() {
        return fragmentRepository.findAll();
    }

    /**
     * Crea un nuevo fragmento y lo persiste en base de datos.
     * Asigna automáticamente el timestamp de creación si no viene seteado.
     *
     * @param fragment entidad fragmento a crear (no nulo)
     * @return fragmento creado con ID asignado
     */
    public Fragment create(Fragment fragment) {
        if (fragment.getCreatedAt() == null) {
            fragment.setCreatedAt(LocalDateTime.now());
        }
        return fragmentRepository.save(fragment);
    }

    /**
     * Crea un fragmento a partir de un DTO recibido desde el controller.
     * Convierte el DTO a entidad usando el mapper y delega al método create().
     *
     * @param dto datos del fragmento (validado por controller)
     * @return fragmento creado y persistido
     */
    public Fragment createFromDTO(FragmentRequestDTO dto) {
        Fragment fragment = FragmentMapper.toEntity(dto);
        return create(fragment);
    }

    /**
     * Actualiza un fragmento existente basándose en un DTO.
     * Solo modifica campos no-null del DTO para permitir actualizaciones parciales.
     *
     * @param id  identificador del fragmento a actualizar
     * @param dto datos a actualizar (solo campos no-null se aplican)
     * @return fragmento actualizado
     * @throws EntityNotFoundException si el fragmento no existe
     */
    public Fragment updateFromDTO(Long id, FragmentRequestDTO dto) {
        return fragmentRepository.findById(id)
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
                    existing.setUpdatedAt(LocalDateTime.now());
                    return fragmentRepository.save(existing);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fragment con ID " + id + " no encontrado"));
    }

    /**
     * Elimina un fragmento por su identificador.
     *
     * @param id identificador del fragmento a eliminar
     * @throws EntityNotFoundException si el fragmento no existe
     */
    public void delete(Long id) {
        Fragment fragment = fragmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fragment con ID " + id + " no encontrado"));
        fragmentRepository.delete(fragment);
    }
}
