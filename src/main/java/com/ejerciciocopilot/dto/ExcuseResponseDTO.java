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
public class ExcuseResponseDTO {
    private Long id;
    private FragmentResponseDTO context;
    private FragmentResponseDTO cause;
    private FragmentResponseDTO consequence;
    private FragmentResponseDTO recommendation;
    private MemeResponseDTO meme;
    private LawResponseDTO law;
    private String type;
    private String role;
    private Long seed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
