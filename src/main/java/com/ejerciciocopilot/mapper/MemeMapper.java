package com.ejerciciocopilot.mapper;

import com.ejerciciocopilot.dto.MemeRequestDTO;
import com.ejerciciocopilot.dto.MemeResponseDTO;
import com.ejerciciocopilot.model.Meme;

/**
 * Mapper estático para convertir entre Meme entity y DTOs.
 */
public class MemeMapper {

    private MemeMapper() {}

    /**
     * Convierte MemeRequestDTO a entidad Meme.
     */
    public static Meme toEntity(MemeRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Meme.builder()
                .author(dto.getAuthor())
                .quote(dto.getQuote())
                .build();
    }

    /**
     * Convierte Meme entity a MemeResponseDTO.
     */
    public static MemeResponseDTO toResponse(Meme meme) {
        if (meme == null) {
            return null;
        }
        return MemeResponseDTO.builder()
                .id(meme.getId())
                .author(meme.getAuthor())
                .quote(meme.getQuote())
                .createdAt(meme.getCreatedAt())
                .build();
    }
}
