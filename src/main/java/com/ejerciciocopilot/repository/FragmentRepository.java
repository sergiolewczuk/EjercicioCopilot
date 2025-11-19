package com.ejerciciocopilot.repository;

import com.ejerciciocopilot.model.Fragment;
import com.ejerciciocopilot.model.FragmentType;
import com.ejerciciocopilot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Fragment.
 * Define operaciones CRUD y queries personalizadas de persistencia.
 */
@Repository
public interface FragmentRepository extends JpaRepository<Fragment, Long> {

    /**
     * Obtiene todos los fragmentos de un tipo específico.
     *
     * @param type tipo de fragmento a buscar
     * @return lista de fragmentos del tipo especificado
     */
    List<Fragment> findByType(FragmentType type);

    /**
     * Obtiene todos los fragmentos de un tipo específico para un rol.
     *
     * @param type tipo de fragmento
     * @param role rol del desarrollador
     * @return lista de fragmentos del tipo y rol especificados
     */
    List<Fragment> findByTypeAndRole(FragmentType type, Role role);

    /**
     * Obtiene todos los fragmentos para un rol específico.
     *
     * @param role rol del desarrollador
     * @return lista de fragmentos para ese rol
     */
    List<Fragment> findByRole(Role role);
}
