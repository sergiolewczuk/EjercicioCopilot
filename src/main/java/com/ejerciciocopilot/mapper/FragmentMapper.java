package com.ejerciciocopilot.mapper;

import com.ejerciciocopilot.dto.FragmentRequestDTO;
import com.ejerciciocopilot.dto.FragmentResponseDTO;
import com.ejerciciocopilot.model.Fragment;
import com.ejerciciocopilot.model.FragmentType;
import com.ejerciciocopilot.model.Role;

public final class FragmentMapper {
    private FragmentMapper() {}

    public static Fragment toEntity(FragmentRequestDTO dto) {
        if (dto == null) return null;
        return Fragment.builder()
                .type(FragmentType.valueOf(dto.getType()))
                .text(dto.getText())
                .role(dto.getRole() != null ? Role.valueOf(dto.getRole()) : null)
                .build();
    }

    public static FragmentResponseDTO toResponse(Fragment fragment) {
        if (fragment == null) return null;
        return FragmentResponseDTO.builder()
                .id(fragment.getId())
                .type(fragment.getType() != null ? fragment.getType().name() : null)
                .text(fragment.getText())
                .role(fragment.getRole() != null ? fragment.getRole().name() : null)
                .createdAt(fragment.getCreatedAt())
                .updatedAt(fragment.getUpdatedAt())
                .build();
    }
}
