package com.ejerciciocopilot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FragmentRequestDTO {
    @NotBlank(message = "El tipo es obligatorio")
    private String type; // CONTEXTO, CAUSA, CONSECUENCIA, RECOMENDACION

    @NotBlank(message = "El texto es obligatorio")
    @Size(min = 5, max = 500, message = "El texto debe tener entre 5 y 500 caracteres")
    private String text;

    private String role; // Rol opcional
}
