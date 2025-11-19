package com.ejerciciocopilot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuseSummaryDTO {
    private Long id;
    private String type;
    private String role;
    private Long seed;
}
