package com.ejerciciocopilot.repository;

import com.ejerciciocopilot.model.Meme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Meme.
 * Define operaciones CRUD y queries personalizadas de persistencia.
 */
@Repository
public interface MemeRepository extends JpaRepository<Meme, Long> {

    /**
     * Obtiene todos los memes de un autor específico.
     *
     * @param author autor del meme
     * @return lista de memes del autor especificado
     */
    List<Meme> findByAuthor(String author);
}
