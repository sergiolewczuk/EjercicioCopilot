package com.ejerciciocopilot.repository;

import com.ejerciciocopilot.model.Excuse;
import com.ejerciciocopilot.model.ExcuseType;
import com.ejerciciocopilot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Excuse.
 * Define operaciones CRUD y queries personalizadas de persistencia.
 */
@Repository
public interface ExcuseRepository extends JpaRepository<Excuse, Long> {

    /**
     * Obtiene todas las excusas de un tipo específico.
     *
     * @param type tipo de excusa
     * @return lista de excusas del tipo especificado
     */
    List<Excuse> findByType(ExcuseType type);

    /**
     * Obtiene todas las excusas generadas para un rol específico.
     *
     * @param role rol del desarrollador
     * @return lista de excusas para ese rol
     */
    List<Excuse> findByRole(Role role);

    /**
     * Obtiene el número de excusas generadas.
     *
     * @return cantidad total de excusas
     */
    long count();
}
