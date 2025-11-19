package com.ejerciciocopilot.service;

import com.ejerciciocopilot.dto.FragmentRequestDTO;
import com.ejerciciocopilot.model.Fragment;
import com.ejerciciocopilot.model.FragmentType;
import com.ejerciciocopilot.model.Role;
import com.ejerciciocopilot.repository.FragmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para FragmentService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FragmentService - Tests Unitarios")
class FragmentServiceTest {

    @Mock
    private FragmentRepository fragmentRepository;

    @InjectMocks
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
    @DisplayName("findById() retorna Optional con fragmento cuando existe")
    void testFindByIdReturnsFragmentWhenExists() {
        // Arrange
        when(fragmentRepository.findById(1L)).thenReturn(Optional.of(testFragment));

        // Act
        Optional<Fragment> result = fragmentService.findById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testFragment);
        verify(fragmentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById() retorna Optional vacío cuando no existe")
    void testFindByIdReturnsEmptyWhenNotExists() {
        // Arrange
        when(fragmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Fragment> result = fragmentService.findById(999L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll() retorna lista de todos los fragmentos")
    void testFindAllReturnsAllFragments() {
        // Arrange
        Fragment fragment2 = Fragment.builder()
                .id(2L)
                .type(FragmentType.CAUSA)
                .text("El token de CI/CD venció")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        when(fragmentRepository.findAll()).thenReturn(Arrays.asList(testFragment, fragment2));

        // Act
        java.util.List<Fragment> result = fragmentService.findAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testFragment, fragment2);
    }

    @Test
    @DisplayName("create() persiste un fragmento")
    void testCreatePersistsFragment() {
        // Arrange
        when(fragmentRepository.save(any(Fragment.class))).thenReturn(testFragment);

        // Act
        Fragment result = fragmentService.create(testFragment);

        // Assert
        assertThat(result).isEqualTo(testFragment);
        verify(fragmentRepository, times(1)).save(testFragment);
    }

    @Test
    @DisplayName("delete() elimina un fragmento por ID")
    void testDeleteRemovesFragment() {
        // Arrange
        when(fragmentRepository.findById(1L)).thenReturn(Optional.of(testFragment));
        doNothing().when(fragmentRepository).delete(testFragment);

        // Act
        fragmentService.delete(1L);

        // Assert
        verify(fragmentRepository, times(1)).findById(1L);
        verify(fragmentRepository, times(1)).delete(testFragment);
    }
}
