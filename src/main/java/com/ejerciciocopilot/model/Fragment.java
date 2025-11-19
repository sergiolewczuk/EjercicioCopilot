package com.ejerciciocopilot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un fragmento de excusa tech.
 * Los fragmentos son componentes reutilizables que se combinan para formar excusas.
 */
@Entity
@Table(name = "fragments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fragment {

    /**
     * Identificador único del fragmento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tipo de fragmento (CONTEXTO, CAUSA, CONSECUENCIA, RECOMENDACION).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FragmentType type;

    /**
     * Contenido textual del fragmento.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    /**
     * Rol específico para el que aplica este fragmento (opcional).
     * Si es null, el fragmento aplica para todos los roles.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    /**
     * Timestamp de creación del fragmento.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp de la última modificación.
     */
    @Column(nullable = true)
    private LocalDateTime updatedAt;
}
