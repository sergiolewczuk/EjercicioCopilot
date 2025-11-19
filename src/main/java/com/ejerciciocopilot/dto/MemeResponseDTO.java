package com.ejerciciocopilot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un meme.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemeResponseDTO {

    private Long id;
    private String author;
    private String quote;
    private LocalDateTime createdAt;
}
