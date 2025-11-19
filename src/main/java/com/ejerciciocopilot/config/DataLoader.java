package com.ejerciciocopilot.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ejerciciocopilot.model.Fragment;
import com.ejerciciocopilot.model.FragmentType;
import com.ejerciciocopilot.model.Law;
import com.ejerciciocopilot.model.Meme;
import com.ejerciciocopilot.repository.FragmentRepository;
import com.ejerciciocopilot.repository.LawRepository;
import com.ejerciciocopilot.repository.MemeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Precarga opcional de datos leyendo archivos JSON en directorio docs/json.
 * Solo se ejecuta fuera del perfil test.
 */
@Slf4j
@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final FragmentRepository fragmentRepository;
    private final MemeRepository memeRepository;
    private final LawRepository lawRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public DataLoader(FragmentRepository fragmentRepository,
                      MemeRepository memeRepository,
                      LawRepository lawRepository) {
        this.fragmentRepository = fragmentRepository;
        this.memeRepository = memeRepository;
        this.lawRepository = lawRepository;
    }

    @Override
    public void run(String... args) {
        try {
            loadFragments("docs/json/dev_axioms.json", FragmentType.CONTEXTO);
            loadMemes("docs/json/memes_argentinos.json");
            loadLaws("docs/json/murphy.json", "Murphy");
        } catch (Exception e) {
            log.warn("Fallo precarga: {}", e.getMessage());
        }
    }

    private void loadFragments(String path, FragmentType defaultType) throws Exception {
        if (!fragmentRepository.findAll().isEmpty()) return;
        File file = new File(path);
        if (!file.exists()) { log.info("No encontrado {}", path); return; }
        List<Map<String, Object>> rows = mapper.readValue(Files.readAllBytes(file.toPath()), new TypeReference<>() {});
        rows.forEach(r -> fragmentRepository.save(Fragment.builder()
                .type(defaultType)
                .text(String.valueOf(r.getOrDefault("text", r.getOrDefault("axiom", ""))).trim())
                .createdAt(LocalDateTime.now())
                .build()));
        log.info("Cargados {} fragmentos", rows.size());
    }

    private void loadMemes(String path) throws Exception {
        if (!memeRepository.findAll().isEmpty()) return;
        File file = new File(path);
        if (!file.exists()) { log.info("No encontrado {}", path); return; }
        List<Map<String, Object>> rows = mapper.readValue(Files.readAllBytes(file.toPath()), new TypeReference<>() {});
        rows.forEach(r -> memeRepository.save(Meme.builder()
                .author(String.valueOf(r.getOrDefault("author", "Anon")))
                .quote(String.valueOf(r.getOrDefault("quote", r.getOrDefault("text", ""))).trim())
                .createdAt(LocalDateTime.now())
                .build()));
        log.info("Cargados {} memes", rows.size());
    }

    private void loadLaws(String path, String category) throws Exception {
        if (!lawRepository.findAll().isEmpty()) return;
        File file = new File(path);
        if (!file.exists()) { log.info("No encontrado {}", path); return; }
        List<Map<String, Object>> rows = mapper.readValue(Files.readAllBytes(file.toPath()), new TypeReference<>() {});
        rows.forEach(r -> lawRepository.save(Law.builder()
                .name(String.valueOf(r.getOrDefault("name", category + " Law")))
                .description(String.valueOf(r.getOrDefault("description", r.getOrDefault("text", ""))))
                .category(category)
                .createdAt(LocalDateTime.now())
                .build()));
        log.info("Cargadas {} leyes", rows.size());
    }
}
