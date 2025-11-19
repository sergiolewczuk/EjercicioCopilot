package com.ejerciciocopilot.mapper;

import com.ejerciciocopilot.dto.LawRequestDTO;
import com.ejerciciocopilot.dto.LawResponseDTO;
import com.ejerciciocopilot.model.Law;

public final class LawMapper {
    private LawMapper() {}

    public static Law toEntity(LawRequestDTO dto) {
        if (dto == null) return null;
        return Law.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .build();
    }

    public static LawResponseDTO toResponse(Law law) {
        if (law == null) return null;
        return LawResponseDTO.builder()
                .id(law.getId())
                .name(law.getName())
                .description(law.getDescription())
                .category(law.getCategory())
                .createdAt(law.getCreatedAt())
                .updatedAt(law.getUpdatedAt())
                .build();
    }
}
