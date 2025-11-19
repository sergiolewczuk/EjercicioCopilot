package com.ejerciciocopilot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear/actualizar un meme.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemeRequestDTO {

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 1, max = 100, message = "El autor debe tener entre 1 y 100 caracteres")
    private String author;

    @NotBlank(message = "La frase es obligatoria")
    @Size(min = 10, max = 500, message = "La frase debe tener entre 10 y 500 caracteres")
    private String quote;
}
