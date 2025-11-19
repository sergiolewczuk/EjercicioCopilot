package com.ejerciciocopilot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuseRequestDTO {
    @NotNull(message = "contextId es obligatorio")
    private Long contextId;
    @NotNull(message = "causeId es obligatorio")
    private Long causeId;
    @NotNull(message = "consequenceId es obligatorio")
    private Long consequenceId;
    @NotNull(message = "recommendationId es obligatorio")
    private Long recommendationId;
    private Long memeId;
    private Long lawId;
    private String type; // SIMPLE, CON_MEME, CON_LEY, ULTRA_SHARK
    private String role; // DEV, QA, DEVOPS, PM, ARCHITECT, DEVREL
}
