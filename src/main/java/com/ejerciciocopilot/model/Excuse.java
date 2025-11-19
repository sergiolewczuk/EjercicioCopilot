package com.ejerciciocopilot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una excusa tech generada.
 * Una excusa es la combinación de fragmentos, opcionalmente con meme y/o ley.
 */
@Entity
@Table(name = "excuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Excuse {

    /**
     * Identificador único de la excusa.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fragmento de contexto (situación).
     * Relación lazy con la tabla fragments.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "context_id")
    private Fragment context;

    /**
     * Fragmento de causa (razón técnica).
     * Relación lazy con la tabla fragments.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cause_id")
    private Fragment cause;

    /**
     * Fragmento de consecuencia (resultado).
     * Relación lazy con la tabla fragments.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consequence_id")
    private Fragment consequence;

    /**
     * Fragmento de recomendación (solución).
     * Relación lazy con la tabla fragments.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id")
    private Fragment recommendation;

    /**
     * Meme opcional incluido en la excusa.
     * Relación lazy con la tabla memes.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meme_id")
    private Meme meme;

    /**
     * Ley/axioma opcional incluido en la excusa.
     * Relación lazy con la tabla laws.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "law_id")
    private Law law;

    /**
     * Tipo de excusa (SIMPLE, CON_MEME, CON_LEY, ULTRA_SHARK).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExcuseType type;

    /**
     * Rol específico para el que se generó la excusa (opcional).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    /**
     * Seed utilizado para generar la excusa.
     * Permite reproducibilidad de excusas (misma seed = misma excusa).
     */
    @Column(nullable = false)
    private Long seed;

    /**
     * Timestamp de creación de la excusa.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp de la última modificación.
     */
    @Column(nullable = true)
    private LocalDateTime updatedAt;
}
