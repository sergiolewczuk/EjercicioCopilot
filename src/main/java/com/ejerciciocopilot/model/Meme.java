package com.ejerciciocopilot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un meme tech argentino.
 * Los memes se pueden combinar con excusas para hacerlas más entretenidas.
 */
@Entity
@Table(name = "memes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meme {

    /**
     * Identificador único del meme.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Autor o persona famosa del meme.
     * Ejemplo: "Tano Pasman", "Milei", "La Nación"
     */
    @Column(nullable = false)
    private String author;

    /**
     * Texto o cita del meme.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String quote;

    /**
     * Timestamp de creación del meme.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp de la última modificación.
     */
    @Column(nullable = true)
    private LocalDateTime updatedAt;
}
