package com.ejerciciocopilot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FragmentResponseDTO {
    private Long id;
    private String type;
    private String text;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
