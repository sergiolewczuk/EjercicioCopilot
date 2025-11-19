package com.ejerciciocopilot.mapper;

import com.ejerciciocopilot.dto.ExcuseRequestDTO;
import com.ejerciciocopilot.dto.ExcuseResponseDTO;
import com.ejerciciocopilot.dto.ExcuseSummaryDTO;
import com.ejerciciocopilot.model.Excuse;
import com.ejerciciocopilot.model.ExcuseType;
import com.ejerciciocopilot.model.Role;

public final class ExcuseMapper {
    private ExcuseMapper() {}

    public static Excuse toEntity(ExcuseRequestDTO dto) {
        if (dto == null) return null;
        return Excuse.builder()
                .type(dto.getType() != null ? ExcuseType.valueOf(dto.getType()) : ExcuseType.SIMPLE)
                .role(dto.getRole() != null ? Role.valueOf(dto.getRole()) : null)
                .build();
    }

    public static ExcuseResponseDTO toResponse(Excuse excuse) {
        if (excuse == null) return null;
        return ExcuseResponseDTO.builder()
                .id(excuse.getId())
                .context(FragmentMapper.toResponse(excuse.getContext()))
                .cause(FragmentMapper.toResponse(excuse.getCause()))
                .consequence(FragmentMapper.toResponse(excuse.getConsequence()))
                .recommendation(FragmentMapper.toResponse(excuse.getRecommendation()))
                .meme(MemeMapper.toResponse(excuse.getMeme()))
                .law(LawMapper.toResponse(excuse.getLaw()))
                .type(excuse.getType() != null ? excuse.getType().name() : null)
                .role(excuse.getRole() != null ? excuse.getRole().name() : null)
                .seed(excuse.getSeed())
                .createdAt(excuse.getCreatedAt())
                .updatedAt(excuse.getUpdatedAt())
                .build();
    }

    public static ExcuseSummaryDTO toSummary(Excuse excuse) {
        if (excuse == null) return null;
        return ExcuseSummaryDTO.builder()
                .id(excuse.getId())
                .type(excuse.getType() != null ? excuse.getType().name() : null)
                .role(excuse.getRole() != null ? excuse.getRole().name() : null)
                .seed(excuse.getSeed())
                .build();
    }
}
