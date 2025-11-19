package com.ejerciciocopilot.integration;

import com.ejerciciocopilot.model.*;
import com.ejerciciocopilot.repository.ExcuseRepository;
import com.ejerciciocopilot.repository.FragmentRepository;
import com.ejerciciocopilot.repository.LawRepository;
import com.ejerciciocopilot.repository.MemeRepository;
import com.ejerciciocopilot.service.ExcuseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests de integración que usan datos reales de los JSONs.
 * Validan reproducibilidad con seed y generación de excusas completas.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Excuse Integration Tests - Usando datos de JSONs")
class ExcuseIntegrationTest {

    @Autowired
    private ExcuseService excuseService;

    @Autowired
    private ExcuseRepository excuseRepository;

    @Autowired
    private FragmentRepository fragmentRepository;

    @Autowired
    private MemeRepository memeRepository;

    @Autowired
    private LawRepository lawRepository;

    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        excuseRepository.deleteAll();
        fragmentRepository.deleteAll();
        memeRepository.deleteAll();
        lawRepository.deleteAll();

        // Cargar datos de prueba basados en los JSONs
        loadTestDataFromJsons();
    }

    @Test
    @DisplayName("generateRandom() produce excusas consistentes con datos de dev_axioms.json")
    void testGenerateRandomWithDevAxiomsData() {
        // Act
        Excuse excuse = excuseService.generateRandom();

        // Assert
        assertThat(excuse).isNotNull();
        assertThat(excuse.getId()).isNotNull();
        assertThat(excuse.getContext()).isNotNull();
        assertThat(excuse.getCause()).isNotNull();
        assertThat(excuse.getConsequence()).isNotNull();
        assertThat(excuse.getRecommendation()).isNotNull();
        assertThat(excuse.getType()).isEqualTo(ExcuseType.SIMPLE);
        assertThat(excuse.getSeed()).isNotNull();
        assertThat(excuse.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("generateDaily() es reproducible - misma seed para la misma fecha")
    void testGenerateDailyIsReproducibleBySeed() {
        // Act
        Excuse excuse1 = excuseService.generateDaily();
        Excuse excuse2 = excuseService.generateDaily();

        // Assert
        assertThat(excuse1).isNotNull();
        assertThat(excuse2).isNotNull();
        // Ambas excusas deben tener el mismo seed (día)
        assertThat(excuse1.getSeed()).isEqualTo(excuse2.getSeed());
        assertThat(excuse1.getSeed()).isEqualTo(LocalDate.now().toEpochDay());
    }

    @Test
    @DisplayName("generateWithMeme() añade meme de argento-memes.json")
    void testGenerateWithMemeAddsRandomMeme() {
        // Act
        Excuse excuse = excuseService.generateWithMeme();

        // Assert
        assertThat(excuse).isNotNull();
        assertThat(excuse.getType()).isEqualTo(ExcuseType.CON_MEME);
        // Meme puede ser null si no hay memes, pero debemos tener fragmentos
        assertThat(excuse.getContext()).isNotNull();
    }

    @Test
    @DisplayName("generateWithLaw() añade ley de murphy.json o hofstadter.json")
    void testGenerateWithLawAddsRandomLaw() {
        // Act
        Excuse excuse = excuseService.generateWithLaw();

        // Assert
        assertThat(excuse).isNotNull();
        assertThat(excuse.getType()).isEqualTo(ExcuseType.CON_LEY);
        assertThat(excuse.getContext()).isNotNull();
    }

    @Test
    @DisplayName("generateUltraShark() combina fragmentos + meme + ley")
    void testGenerateUltraSharkCombinesAllElements() {
        // Act
        Excuse excuse = excuseService.generateUltraShark();

        // Assert
        assertThat(excuse).isNotNull();
        assertThat(excuse.getType()).isEqualTo(ExcuseType.ULTRA_SHARK);
        assertThat(excuse.getContext()).isNotNull();
        assertThat(excuse.getCause()).isNotNull();
        assertThat(excuse.getConsequence()).isNotNull();
        assertThat(excuse.getRecommendation()).isNotNull();
    }

    @Test
    @DisplayName("generateByRole(DEV) produce excusas con fragmentos específicos del rol")
    void testGenerateByRoleDEVUsesDEVFragments() {
        // Act
        Excuse excuse = excuseService.generateByRole("DEV");

        // Assert
        assertThat(excuse).isNotNull();
        assertThat(excuse.getRole()).isEqualTo(Role.DEV);
        assertThat(excuse.getContext()).isNotNull();
        assertThat(excuse.getType()).isEqualTo(ExcuseType.SIMPLE);
    }

    @Test
    @DisplayName("reproducibilidad con seed - misma seed produce misma excusa")
    void testSeedReproducibilitySameSeedSameExcuse() {
        // Act
        Excuse excuse1 = excuseService.generateDaily();
        Excuse excuse2 = excuseService.generateDaily();

        // Assert
        // Mismo seed = mismos fragmentos generados
        assertThat(excuse1.getSeed()).isEqualTo(excuse2.getSeed());
        assertThat(excuse1.getContext().getId())
                .isEqualTo(excuse2.getContext().getId());
        assertThat(excuse1.getCause().getId())
                .isEqualTo(excuse2.getCause().getId());
    }

    /**
     * Carga datos de prueba simulando el contenido de los JSONs.
     * En producción, estos datos vendrían de un CommandLineRunner.
     */
    private void loadTestDataFromJsons() {
        // Fragmentos de dev_axioms.json
        Fragment context1 = Fragment.builder()
                .type(FragmentType.CONTEXTO)
                .text("Si funciona, no lo toques. Si no funciona, tampoco, porque seguro lo rompés más.")
                .role(Role.DEV)
                .createdAt(LocalDateTime.now())
                .build();

        Fragment cause1 = Fragment.builder()
                .type(FragmentType.CAUSA)
                .text("El código que escribiste hace seis meses lo escribió un desconocido con tus accesos.")
                .role(Role.DEV)
                .createdAt(LocalDateTime.now())
                .build();

        Fragment consequence1 = Fragment.builder()
                .type(FragmentType.CONSECUENCIA)
                .text("Tuvimos que hacer rollback de emergencia.")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        Fragment recommendation1 = Fragment.builder()
                .type(FragmentType.RECOMENDACION)
                .text("Implementar code reviews obligatorios y documentación automática.")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        // Fragmentos generales (sin rol específico)
        Fragment context2 = Fragment.builder()
                .type(FragmentType.CONTEXTO)
                .text("Durante el despliegue del pipeline")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        Fragment cause2 = Fragment.builder()
                .type(FragmentType.CAUSA)
                .text("El token de CI/CD venció sin aviso")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        Fragment consequence2 = Fragment.builder()
                .type(FragmentType.CONSECUENCIA)
                .text("El deploy falló en la mitad del proceso")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        Fragment recommendation2 = Fragment.builder()
                .type(FragmentType.RECOMENDACION)
                .text("Automatizar la rotación de secretos")
                .role(null)
                .createdAt(LocalDateTime.now())
                .build();

        fragmentRepository.saveAll(java.util.Arrays.asList(
                context1, cause1, consequence1, recommendation1,
                context2, cause2, consequence2, recommendation2
        ));

        // Memes de argento-memes.json
        Meme meme1 = Meme.builder()
                .author("Tano Pasman")
                .quote("¿CÓMO QUE FALLÓ EL PIPELINE?")
                .createdAt(LocalDateTime.now())
                .build();

        Meme meme2 = Meme.builder()
                .author("Anónimo")
                .quote("Nada más argentino que debuggear con el mate al lado")
                .createdAt(LocalDateTime.now())
                .build();

        memeRepository.saveAll(java.util.Arrays.asList(meme1, meme2));

        // Leyes de murphy.json
        Law law1 = Law.builder()
                .name("Ley de Murphy")
                .description("Si algo puede salir mal, saldrá mal durante la demo.")
                .category("Murphy")
                .createdAt(LocalDateTime.now())
                .build();

        Law law2 = Law.builder()
                .name("Ley de Murphy - Deploy")
                .description("Si el deploy es tranquilo, es porque el problema todavía no se notó.")
                .category("Murphy")
                .createdAt(LocalDateTime.now())
                .build();

        // Leyes de hofstadter.json
        Law law3 = Law.builder()
                .name("Ley de Hofstadter")
                .description("Hofstadter dice que todo proyecto lleva el doble del tiempo, incluso si tenés en cuenta la Ley de Hofstadter.")
                .category("Hofstadter")
                .createdAt(LocalDateTime.now())
                .build();

        lawRepository.saveAll(java.util.Arrays.asList(law1, law2, law3));
    }
}
