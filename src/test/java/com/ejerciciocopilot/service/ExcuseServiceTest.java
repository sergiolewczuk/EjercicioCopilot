package com.ejerciciocopilot.service;

import com.ejerciciocopilot.model.*;
import com.ejerciciocopilot.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ExcuseService.
 * Cubre generación de excusas aleatorias, reproducibilidad con seed, y gestión de roles.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ExcuseService - Tests Unitarios")
class ExcuseServiceTest {

    @Mock
    private ExcuseRepository excuseRepository;

    @Mock
    private FragmentRepository fragmentRepository;

    @Mock
    private MemeRepository memeRepository;

    @Mock
    private LawRepository lawRepository;

    @InjectMocks
    private ExcuseService excuseService;

    private Fragment contextFragment;
    private Fragment causeFragment;
    private Fragment consequenceFragment;
    private Fragment recommendationFragment;
    private Meme testMeme;
    private Law testLaw;

    @BeforeEach
    void setUp() {
        // Fragmentos de prueba basados en dev_axioms.json
        contextFragment = Fragment.builder()
                .id(1L)
                .type(FragmentType.CONTEXTO)
                .text("Si funciona, no lo toques. Si no funciona, tampoco, porque seguro lo rompés más.")
                .role(Role.DEV)
                .createdAt(LocalDateTime.now())
                .build();

        causeFragment = Fragment.builder()
                .id(2L)
                .type(FragmentType.CAUSA)
                .text("El código que escribiste hace seis meses lo escribió un desconocido con tus accesos.")
                .role(Role.DEV)
                .createdAt(LocalDateTime.now())
                .build();

        consequenceFragment = Fragment.builder()
                .id(3L)
                .type(FragmentType.CONSECUENCIA)
                .text("Tuvimos que hacer rollback de emergencia.")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        recommendationFragment = Fragment.builder()
                .id(4L)
                .type(FragmentType.RECOMENDACION)
                .text("Automatizar revisiones de código y documentación.")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        // Meme basado en argento-memes.json
        testMeme = Meme.builder()
                .id(1L)
                .author("Tano Pasman")
                .quote("¿CÓMO QUE FALLÓ EL PIPELINE?")
                .createdAt(LocalDateTime.now())
                .build();

        // Ley basada en murphy.json
        testLaw = Law.builder()
                .id(1L)
                .name("Ley de Murphy")
                .description("Si algo puede salir mal, saldrá mal durante la demo.")
                .category("Murphy")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("generateRandom() debe generar una excusa simple con 4 fragmentos")
    void testGenerateRandomCreatesSimpleExcuse() {
        // Arrange
        when(fragmentRepository.findByType(FragmentType.CONTEXTO))
                .thenReturn(List.of(contextFragment));
        when(fragmentRepository.findByType(FragmentType.CAUSA))
                .thenReturn(List.of(causeFragment));
        when(fragmentRepository.findByType(FragmentType.CONSECUENCIA))
                .thenReturn(List.of(consequenceFragment));
        when(fragmentRepository.findByType(FragmentType.RECOMENDACION))
                .thenReturn(List.of(recommendationFragment));

        Excuse savedExcuse = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .type(ExcuseType.SIMPLE)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.save(any(Excuse.class))).thenReturn(savedExcuse);

        // Act
        Excuse result = excuseService.generateRandom();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContext()).isEqualTo(contextFragment);
        assertThat(result.getCause()).isEqualTo(causeFragment);
        assertThat(result.getConsequence()).isEqualTo(consequenceFragment);
        assertThat(result.getRecommendation()).isEqualTo(recommendationFragment);
        assertThat(result.getType()).isEqualTo(ExcuseType.SIMPLE);
        assertThat(result.getSeed()).isNotNull();
        verify(excuseRepository, times(1)).save(any(Excuse.class));
    }

    @Test
    @DisplayName("generateRandom() lanza IllegalStateException si no hay fragmentos CONTEXTO")
    void testGenerateRandomThrowsExceptionWhenNoContextFragments() {
        // Arrange
        when(fragmentRepository.findByType(FragmentType.CONTEXTO))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThatThrownBy(() -> excuseService.generateRandom())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No hay fragmentos de tipo CONTEXTO");

        verify(excuseRepository, never()).save(any());
    }

    @Test
    @DisplayName("generateWithMeme() añade un meme a la excusa y establece tipo CON_MEME")
    void testGenerateWithMemeAddsMemeToDexcuse() {
        // Arrange
        setupFragmentMocks();

        Excuse simpleExcuse = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .type(ExcuseType.SIMPLE)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        Excuse excuseWithMeme = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .meme(testMeme)
                .type(ExcuseType.CON_MEME)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.save(any(Excuse.class)))
                .thenReturn(simpleExcuse)
                .thenReturn(excuseWithMeme);
        when(memeRepository.findAll()).thenReturn(List.of(testMeme));

        // Act
        Excuse result = excuseService.generateWithMeme();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getMeme()).isEqualTo(testMeme);
        assertThat(result.getType()).isEqualTo(ExcuseType.CON_MEME);
        verify(memeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("generateWithMeme() funciona sin memes disponibles (meme = null)")
    void testGenerateWithMemeWorksWithoutMemes() {
        // Arrange
        setupFragmentMocks();

        Excuse excuseWithoutMeme = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .meme(null)
                .type(ExcuseType.CON_MEME)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.save(any(Excuse.class)))
                .thenReturn(Excuse.builder()
                        .context(contextFragment)
                        .build())
                .thenReturn(excuseWithoutMeme);
        when(memeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Excuse result = excuseService.generateWithMeme();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo(ExcuseType.CON_MEME);
    }

    @Test
    @DisplayName("generateWithLaw() añade una ley a la excusa y establece tipo CON_LEY")
    void testGenerateWithLawAddsLawToExcuse() {
        // Arrange
        setupFragmentMocks();

        Excuse excuseWithLaw = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .law(testLaw)
                .type(ExcuseType.CON_LEY)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.save(any(Excuse.class)))
                .thenReturn(Excuse.builder()
                        .context(contextFragment)
                        .build())
                .thenReturn(excuseWithLaw);
        when(lawRepository.findAll()).thenReturn(List.of(testLaw));

        // Act
        Excuse result = excuseService.generateWithLaw();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getLaw()).isEqualTo(testLaw);
        assertThat(result.getType()).isEqualTo(ExcuseType.CON_LEY);
        verify(lawRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("generateUltraShark() combina fragmentos, meme y ley")
    void testGenerateUltraSharkCombinesAllElements() {
        // Arrange
        setupFragmentMocks();

        Excuse ultraSharkExcuse = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .meme(testMeme)
                .law(testLaw)
                .type(ExcuseType.ULTRA_SHARK)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.save(any(Excuse.class)))
                .thenReturn(Excuse.builder()
                        .context(contextFragment)
                        .build())
                .thenReturn(ultraSharkExcuse);
        when(memeRepository.findAll()).thenReturn(List.of(testMeme));
        when(lawRepository.findAll()).thenReturn(List.of(testLaw));

        // Act
        Excuse result = excuseService.generateUltraShark();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getMeme()).isEqualTo(testMeme);
        assertThat(result.getLaw()).isEqualTo(testLaw);
        assertThat(result.getType()).isEqualTo(ExcuseType.ULTRA_SHARK);
    }

    @Test
    @DisplayName("generateDaily() genera excusas reproducibles usando seed basado en fecha")
    void testGenerateDailyIsReproducible() {
        // Arrange
        setupFragmentMocks();

        long dailySeed = LocalDate.now().toEpochDay();

        Excuse dailyExcuse = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .type(ExcuseType.SIMPLE)
                .seed(dailySeed)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.save(any(Excuse.class))).thenReturn(dailyExcuse);

        // Act
        Excuse result1 = excuseService.generateDaily();
        Excuse result2 = excuseService.generateDaily();

        // Assert
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result1.getSeed()).isEqualTo(result2.getSeed());
        verify(excuseRepository, times(2)).save(any(Excuse.class));
    }

    @Test
    @DisplayName("generateByRole() genera excusas específicas para DEV")
    void testGenerateByRoleDev() {
        // Arrange
        Fragment devContext = Fragment.builder()
                .id(1L)
                .type(FragmentType.CONTEXTO)
                .text("Durante el desarrollo local")
                .role(Role.DEV)
                .createdAt(LocalDateTime.now())
                .build();

        when(fragmentRepository.findByTypeAndRole(FragmentType.CONTEXTO, Role.DEV))
                .thenReturn(List.of(devContext));
        when(fragmentRepository.findByTypeAndRole(FragmentType.CAUSA, Role.DEV))
                .thenReturn(List.of(causeFragment));
        when(fragmentRepository.findByTypeAndRole(FragmentType.CONSECUENCIA, Role.DEV))
                .thenReturn(List.of(consequenceFragment));
        when(fragmentRepository.findByTypeAndRole(FragmentType.RECOMENDACION, Role.DEV))
                .thenReturn(List.of(recommendationFragment));

        Excuse devExcuse = Excuse.builder()
                .id(1L)
                .context(devContext)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .role(Role.DEV)
                .type(ExcuseType.SIMPLE)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.save(any(Excuse.class))).thenReturn(devExcuse);

        // Act
        Excuse result = excuseService.generateByRole("DEV");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo(Role.DEV);
        assertThat(result.getContext()).isEqualTo(devContext);
    }

    @Test
    @DisplayName("generateByRole() lanza IllegalArgumentException con rol inválido")
    void testGenerateByRoleThrowsExceptionWithInvalidRole() {
        // Act & Assert
        assertThatThrownBy(() -> excuseService.generateByRole("INVALID_ROLE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rol inválido");
    }

    @Test
    @DisplayName("generateByRole() cae de vuelta a fragmentos generales si no hay específicos del rol")
    void testGenerateByRoleFallsBackToGeneralFragments() {
        // Arrange
        when(fragmentRepository.findByTypeAndRole(FragmentType.CONTEXTO, Role.QA))
                .thenReturn(Collections.emptyList());
        when(fragmentRepository.findByTypeAndRole(FragmentType.CAUSA, Role.QA))
                .thenReturn(Collections.emptyList());
        when(fragmentRepository.findByTypeAndRole(FragmentType.CONSECUENCIA, Role.QA))
                .thenReturn(Collections.emptyList());
        when(fragmentRepository.findByTypeAndRole(FragmentType.RECOMENDACION, Role.QA))
                .thenReturn(Collections.emptyList());

        // Fallback a fragmentos generales
        when(fragmentRepository.findByType(FragmentType.CONTEXTO))
                .thenReturn(List.of(contextFragment));
        when(fragmentRepository.findByType(FragmentType.CAUSA))
                .thenReturn(List.of(causeFragment));
        when(fragmentRepository.findByType(FragmentType.CONSECUENCIA))
                .thenReturn(List.of(consequenceFragment));
        when(fragmentRepository.findByType(FragmentType.RECOMENDACION))
                .thenReturn(List.of(recommendationFragment));

        Excuse qaExcuse = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .cause(causeFragment)
                .consequence(consequenceFragment)
                .recommendation(recommendationFragment)
                .role(Role.QA)
                .type(ExcuseType.SIMPLE)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.save(any(Excuse.class))).thenReturn(qaExcuse);

        // Act
        Excuse result = excuseService.generateByRole("QA");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo(Role.QA);
    }

    @Test
    @DisplayName("findById() retorna Optional con la excusa si existe")
    void testFindByIdReturnsExcuseWhenExists() {
        // Arrange
        Excuse excuse = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .type(ExcuseType.SIMPLE)
                .seed(12345L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.findById(1L)).thenReturn(Optional.of(excuse));

        // Act
        Optional<Excuse> result = excuseService.findById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(excuse);
        verify(excuseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById() retorna Optional vacío si no existe")
    void testFindByIdReturnsEmptyWhenNotExists() {
        // Arrange
        when(excuseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Excuse> result = excuseService.findById(999L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll() retorna lista de todas las excusas")
    void testFindAllReturnsAllExcuses() {
        // Arrange
        Excuse excuse1 = Excuse.builder()
                .id(1L)
                .context(contextFragment)
                .type(ExcuseType.SIMPLE)
                .seed(111L)
                .createdAt(LocalDateTime.now())
                .build();

        Excuse excuse2 = Excuse.builder()
                .id(2L)
                .context(contextFragment)
                .type(ExcuseType.CON_MEME)
                .seed(222L)
                .createdAt(LocalDateTime.now())
                .build();

        when(excuseRepository.findAll()).thenReturn(Arrays.asList(excuse1, excuse2));

        // Act
        List<Excuse> result = excuseService.findAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(excuse1, excuse2);
        verify(excuseRepository, times(1)).findAll();
    }

    /**
     * Configura mocks de fragmentos para tests que necesiten todos los tipos.
     */
    private void setupFragmentMocks() {
        when(fragmentRepository.findByType(FragmentType.CONTEXTO))
                .thenReturn(List.of(contextFragment));
        when(fragmentRepository.findByType(FragmentType.CAUSA))
                .thenReturn(List.of(causeFragment));
        when(fragmentRepository.findByType(FragmentType.CONSECUENCIA))
                .thenReturn(List.of(consequenceFragment));
        when(fragmentRepository.findByType(FragmentType.RECOMENDACION))
                .thenReturn(List.of(recommendationFragment));
    }
}
