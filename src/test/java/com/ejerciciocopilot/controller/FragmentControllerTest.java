package com.ejerciciocopilot.controller;

import com.ejerciciocopilot.model.Fragment;
import com.ejerciciocopilot.model.FragmentType;
import com.ejerciciocopilot.model.Role;
import com.ejerciciocopilot.service.FragmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para FragmentController.
 */
@WebMvcTest(FragmentController.class)
@DisplayName("FragmentController - Tests de Integración")
class FragmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FragmentService fragmentService;

    private Fragment testFragment;

    @BeforeEach
    void setUp() {
        testFragment = Fragment.builder()
                .id(1L)
                .type(FragmentType.CONTEXTO)
                .text("Durante el despliegue del pipeline")
                .role(Role.DEV)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("GET /api/fragments/{id} debe retornar 200 cuando fragmento existe")
    void testGetFragmentByIdSuccess() throws Exception {
        // Arrange
        when(fragmentService.findById(1L)).thenReturn(Optional.of(testFragment));

        // Act & Assert
        mockMvc.perform(get("/api/fragments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("CONTEXTO"));

        verify(fragmentService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/fragments/{id} debe retornar 404 cuando fragmento no existe")
    void testGetFragmentByIdNotFound() throws Exception {
        // Arrange
        when(fragmentService.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/fragments/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(fragmentService, times(1)).findById(999L);
    }

    @Test
    @DisplayName("DELETE /api/fragments/{id} debe retornar 204 No Content")
    void testDeleteFragmentSuccess() throws Exception {
        // Arrange
        when(fragmentService.findById(1L)).thenReturn(Optional.of(testFragment));
        doNothing().when(fragmentService).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/fragments/1"))
                .andExpect(status().isNoContent());

        verify(fragmentService, times(1)).findById(1L);
        verify(fragmentService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/fragments/{id} debe retornar 404 si no existe")
    void testDeleteFragmentNotFound() throws Exception {
        // Arrange
        when(fragmentService.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/fragments/999"))
                .andExpect(status().isNotFound());

        verify(fragmentService, never()).delete(any());
    }
}
