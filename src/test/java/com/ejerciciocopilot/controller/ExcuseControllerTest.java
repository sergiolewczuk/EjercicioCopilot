package com.ejerciciocopilot.controller;

import com.ejerciciocopilot.model.*;
import com.ejerciciocopilot.service.ExcuseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para ExcuseController usando MockMvc.
 */
@WebMvcTest(ExcuseController.class)
@DisplayName("ExcuseController - Tests de Integración")
class ExcuseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExcuseService excuseService;

    private Excuse testExcuse;
    private Fragment testFragment;

    @BeforeEach
    void setUp() {
        testFragment = Fragment.builder()
                .id(1L)
                .type(FragmentType.CONTEXTO)
                .text("During CI/CD pipeline execution")
                .role(Role.DEV)
                .createdAt(LocalDateTime.now())
                .build();

        testExcuse = Excuse.builder()
                .id(1L)
                .context(testFragment)
                .cause(testFragment)
                .consequence(testFragment)
                .recommendation(testFragment)
                .type(ExcuseType.SIMPLE)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("GET /api/excuses/random debe retornar 200 con excusa aleatoria")
    void testGetRandomExcuse() throws Exception {
        // Arrange
        when(excuseService.generateRandom()).thenReturn(testExcuse);

        // Act & Assert
        mockMvc.perform(get("/api/excuses/random")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("SIMPLE"));

        verify(excuseService, times(1)).generateRandom();
    }

    @Test
    @DisplayName("GET /api/excuses/daily debe retornar 200 con excusa del día")
    void testGetDailyExcuse() throws Exception {
        // Arrange
        when(excuseService.generateDaily()).thenReturn(testExcuse);

        // Act & Assert
        mockMvc.perform(get("/api/excuses/daily")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("SIMPLE"));

        verify(excuseService, times(1)).generateDaily();
    }

    @Test
    @DisplayName("GET /api/excuses/meme debe retornar 200 con excusa con meme")
    void testGetExcuseWithMeme() throws Exception {
        // Arrange
        Meme testMeme = Meme.builder()
                .id(1L)
                .author("Tano Pasman")
                .quote("¿CÓMO QUE FALLÓ EL PIPELINE?")
                .createdAt(LocalDateTime.now())
                .build();

        Excuse excuseWithMeme = Excuse.builder()
                .id(1L)
                .context(testFragment)
                .cause(testFragment)
                .consequence(testFragment)
                .recommendation(testFragment)
                .meme(testMeme)
                .type(ExcuseType.CON_MEME)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseService.generateWithMeme()).thenReturn(excuseWithMeme);

        // Act & Assert
        mockMvc.perform(get("/api/excuses/meme")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("CON_MEME"));

        verify(excuseService, times(1)).generateWithMeme();
    }

    @Test
    @DisplayName("GET /api/excuses/law debe retornar 200 con excusa con ley")
    void testGetExcuseWithLaw() throws Exception {
        // Arrange
        Law testLaw = Law.builder()
                .id(1L)
                .name("Ley de Murphy")
                .description("Si algo puede salir mal, saldrá mal durante la demo.")
                .category("Murphy")
                .createdAt(LocalDateTime.now())
                .build();

        Excuse excuseWithLaw = Excuse.builder()
                .id(1L)
                .context(testFragment)
                .cause(testFragment)
                .consequence(testFragment)
                .recommendation(testFragment)
                .law(testLaw)
                .type(ExcuseType.CON_LEY)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseService.generateWithLaw()).thenReturn(excuseWithLaw);

        // Act & Assert
        mockMvc.perform(get("/api/excuses/law")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("CON_LEY"));

        verify(excuseService, times(1)).generateWithLaw();
    }

    @Test
    @DisplayName("GET /api/excuses/ultra debe retornar 200 con excusa ULTRA_SHARK")
    void testGetUltraSharkExcuse() throws Exception {
        // Arrange
        Meme testMeme = Meme.builder()
                .id(1L)
                .author("Tano Pasman")
                .quote("¿CÓMO QUE FALLÓ EL PIPELINE?")
                .createdAt(LocalDateTime.now())
                .build();

        Law testLaw = Law.builder()
                .id(1L)
                .name("Ley de Murphy")
                .description("Si algo puede salir mal, saldrá mal durante la demo.")
                .category("Murphy")
                .createdAt(LocalDateTime.now())
                .build();

        Excuse ultraSharkExcuse = Excuse.builder()
                .id(1L)
                .context(testFragment)
                .cause(testFragment)
                .consequence(testFragment)
                .recommendation(testFragment)
                .meme(testMeme)
                .law(testLaw)
                .type(ExcuseType.ULTRA_SHARK)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseService.generateUltraShark()).thenReturn(ultraSharkExcuse);

        // Act & Assert
        mockMvc.perform(get("/api/excuses/ultra")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("ULTRA_SHARK"));

        verify(excuseService, times(1)).generateUltraShark();
    }

    @Test
    @DisplayName("GET /api/excuses/role/DEV debe retornar 200 con excusa para rol DEV")
    void testGetExcuseByRoleDev() throws Exception {
        // Arrange
        Excuse devExcuse = Excuse.builder()
                .id(1L)
                .context(testFragment)
                .cause(testFragment)
                .consequence(testFragment)
                .recommendation(testFragment)
                .role(Role.DEV)
                .type(ExcuseType.SIMPLE)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseService.generateByRole("DEV")).thenReturn(devExcuse);

        // Act & Assert
        mockMvc.perform(get("/api/excuses/role/DEV")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("DEV"));

        verify(excuseService, times(1)).generateByRole("DEV");
    }

    @Test
    @DisplayName("GET /api/excuses/role/INVALID debe retornar 400")
    void testGetExcuseByInvalidRoleReturns400() throws Exception {
        // Arrange
        when(excuseService.generateByRole(anyString()))
                .thenThrow(new IllegalArgumentException("Rol inválido"));

        // Act & Assert
        mockMvc.perform(get("/api/excuses/role/INVALID_ROLE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(excuseService, times(1)).generateByRole("INVALID_ROLE");
    }

    @Test
    @DisplayName("GET /api/excuses/{id} debe retornar 200 cuando excusa existe")
    void testGetExcuseByIdSuccess() throws Exception {
        // Arrange
        when(excuseService.findById(1L)).thenReturn(Optional.of(testExcuse));

        // Act & Assert
        mockMvc.perform(get("/api/excuses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(excuseService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/excuses/{id} debe retornar 404 cuando excusa no existe")
    void testGetExcuseByIdNotFound() throws Exception {
        // Arrange
        when(excuseService.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/excuses/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(excuseService, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET /api/excuses debe retornar 200 con lista de excusas")
    void testListAllExcuses() throws Exception {
        // Arrange
        Excuse excuse1 = testExcuse;
        Excuse excuse2 = Excuse.builder()
                .id(2L)
                .context(testFragment)
                .cause(testFragment)
                .consequence(testFragment)
                .recommendation(testFragment)
                .type(ExcuseType.CON_MEME)
                .seed(54321L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseService.findAll()).thenReturn(Arrays.asList(excuse1, excuse2));

        // Act & Assert
        mockMvc.perform(get("/api/excuses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(excuseService, times(1)).findAll();
    }
}
