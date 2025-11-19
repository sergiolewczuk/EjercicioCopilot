package com.ejerciciocopilot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una ley, axioma o principio del desarrollo.
 * Las leyes se utilizan para justificar y explicar las excusas tech.
 */
@Entity
@Table(name = "laws")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Law {

    /**
     * Identificador único de la ley.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la ley o axioma.
     * Ejemplo: "Ley de Murphy", "Axioma de Hofstadter", "Principio DRY"
     */
    @Column(nullable = false)
    private String name;

    /**
     * Descripción detallada de la ley.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Categoría de la ley.
     * Ejemplo: "Murphy", "Hofstadter", "Dilbert", "DevOps", "Axiomas"
     */
    @Column(nullable = false)
    private String category;

    /**
     * Timestamp de creación de la ley.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp de la última modificación.
     */
    @Column(nullable = true)
    private LocalDateTime updatedAt;
}
