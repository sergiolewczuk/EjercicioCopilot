package com.ejerciciocopilot.repository;

import com.ejerciciocopilot.model.Law;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Law.
 * Define operaciones CRUD y queries personalizadas de persistencia.
 */
@Repository
public interface LawRepository extends JpaRepository<Law, Long> {

    /**
     * Obtiene todas las leyes de una categoría específica.
     *
     * @param category categoría de la ley
     * @return lista de leyes de esa categoría
     */
    List<Law> findByCategory(String category);

    /**
     * Busca una ley por su nombre exacto.
     *
     * @param name nombre de la ley
     * @return ley encontrada o null
     */
    Law findByName(String name);
}
