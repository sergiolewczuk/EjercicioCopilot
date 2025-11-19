package com.ejerciciocopilot.service;

import com.ejerciciocopilot.dto.ExcuseRequestDTO;
import com.ejerciciocopilot.exception.EntityNotFoundException;
import com.ejerciciocopilot.mapper.ExcuseMapper;
import com.ejerciciocopilot.model.*;
import com.ejerciciocopilot.repository.ExcuseRepository;
import com.ejerciciocopilot.repository.FragmentRepository;
import com.ejerciciocopilot.repository.LawRepository;
import com.ejerciciocopilot.repository.MemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Servicio core del proyecto para generar y gestionar excusas tech.
 * Este es el servicio más importante, implementa toda la lógica de generación
 * de excusas combinando fragmentos, memes y leyes de forma creativa y reproducible.
 */
@Service
@Transactional
public class ExcuseService {

    private final ExcuseRepository excuseRepository;
    private final FragmentRepository fragmentRepository;
    private final MemeRepository memeRepository;
    private final LawRepository lawRepository;
    private final Random random;

    /**
     * Constructor con inyección de todas las dependencias necesarias.
     *
     * @param excuseRepository  repositorio de excusas
     * @param fragmentRepository repositorio de fragmentos
     * @param memeRepository    repositorio de memes
     * @param lawRepository     repositorio de leyes
     */
    public ExcuseService(ExcuseRepository excuseRepository,
                        FragmentRepository fragmentRepository,
                        MemeRepository memeRepository,
                        LawRepository lawRepository) {
        this.excuseRepository = excuseRepository;
        this.fragmentRepository = fragmentRepository;
        this.memeRepository = memeRepository;
        this.lawRepository = lawRepository;
        this.random = new Random();
    }

    /**
     * Obtiene una excusa por su identificador.
     *
     * @param id identificador de la excusa
     * @return Optional con la excusa si existe
     */
    public Optional<Excuse> findById(Long id) {
        return excuseRepository.findById(id);
    }

    /**
     * Obtiene todas las excusas generadas (historial completo).
     *
     * @return lista de todas las excusas
     */
    public List<Excuse> findAll() {
        return excuseRepository.findAll();
    }

    /**
     * Genera una excusa tech aleatoria simple.
     * Selecciona 4 fragmentos aleatorios (contexto, causa, consecuencia, recomendación)
     * y los combina en una excusa tipo SIMPLE.
     * Esta es la base para las otras generaciones más complejas.
     *
     * @return excusa generada aleatoria
     * @throws IllegalStateException si no hay fragmentos suficientes de algún tipo
     */
    public Excuse generateRandom() {
        Excuse excuse = new Excuse();
        excuse.setContext(getRandomFragment(FragmentType.CONTEXTO));
        excuse.setCause(getRandomFragment(FragmentType.CAUSA));
        excuse.setConsequence(getRandomFragment(FragmentType.CONSECUENCIA));
        excuse.setRecommendation(getRandomFragment(FragmentType.RECOMENDACION));
        excuse.setType(ExcuseType.SIMPLE);
        excuse.setSeed(System.nanoTime());
        excuse.setCreatedAt(LocalDateTime.now());
        return excuseRepository.save(excuse);
    }

    /**
     * Genera una excusa con un meme tech argentino incluido.
     * Combina una excusa simple con un meme aleatorio para hacerla más entretenida.
     * La excusa resultante es tipo CON_MEME.
     *
     * @return excusa con meme
     * @throws IllegalStateException si no hay fragmentos suficientes o memes disponibles
     */
    public Excuse generateWithMeme() {
        Excuse excuse = generateRandom();
        Meme meme = getRandomMeme();
        if (meme != null) {
            excuse.setMeme(meme);
        }
        excuse.setType(ExcuseType.CON_MEME);
        return excuseRepository.save(excuse);
    }

    /**
     * Genera una excusa justificada con una ley o axioma del desarrollo.
     * Similar a generateRandom() pero añade una ley del desarrollo como justificación.
     * La excusa resultante es tipo CON_LEY.
     *
     * @return excusa con ley
     * @throws IllegalStateException si no hay fragmentos suficientes o leyes disponibles
     */
    public Excuse generateWithLaw() {
        Excuse excuse = generateRandom();
        Law law = getRandomLaw();
        if (law != null) {
            excuse.setLaw(law);
        }
        excuse.setType(ExcuseType.CON_LEY);
        return excuseRepository.save(excuse);
    }

    /**
     * Genera una excusa ULTRA_SHARK con TODO incluido.
     * Combina fragmentos + meme + ley en una excusa completa y super entretenida.
     * Esta es la versión más completa (nivel Megalodon).
     *
     * @return excusa ULTRA_SHARK con fragmentos, meme y ley
     * @throws IllegalStateException si no hay elementos suficientes
     */
    public Excuse generateUltraShark() {
        Excuse excuse = generateRandom();
        
        Meme meme = getRandomMeme();
        if (meme != null) {
            excuse.setMeme(meme);
        }
        
        Law law = getRandomLaw();
        if (law != null) {
            excuse.setLaw(law);
        }
        
        excuse.setType(ExcuseType.ULTRA_SHARK);
        return excuseRepository.save(excuse);
    }

    /**
     * Genera una excusa específica para un rol del desarrollador.
     * Intenta seleccionar fragmentos que apliquen específicamente a ese rol
     * (DEV, QA, DEVOPS, PM, ARCHITECT, DEVREL).
     * Si no hay fragmentos específicos del rol, usa fragmentos generales.
     *
     * @param role rol del desarrollador (en mayúsculas o minúsculas)
     * @return excusa personalizada para el rol especificado
     * @throws IllegalArgumentException si el rol no es válido
     * @throws IllegalStateException si no hay fragmentos suficientes
     */
    public Excuse generateByRole(String role) {
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + role + 
                    ". Roles válidos: DEV, QA, DEVOPS, PM, ARCHITECT, DEVREL");
        }

        Excuse excuse = new Excuse();
        excuse.setRole(roleEnum);

        // Intentar obtener fragmentos específicos del rol
        excuse.setContext(getRandomFragmentByRole(FragmentType.CONTEXTO, roleEnum));
        excuse.setCause(getRandomFragmentByRole(FragmentType.CAUSA, roleEnum));
        excuse.setConsequence(getRandomFragmentByRole(FragmentType.CONSECUENCIA, roleEnum));
        excuse.setRecommendation(getRandomFragmentByRole(FragmentType.RECOMENDACION, roleEnum));

        excuse.setType(ExcuseType.SIMPLE);
        excuse.setSeed(System.nanoTime());
        excuse.setCreatedAt(LocalDateTime.now());

        return excuseRepository.save(excuse);
    }

    /**
     * Genera la excusa del día de manera reproducible.
     * Usa la fecha actual como seed para garantizar que
     * todos los usuarios obtengan la misma excusa durante el mismo día.
     * Perfecto para una excusa diaria compartida.
     *
     * @return excusa del día (determinista según la fecha)
     */
    public Excuse generateDaily() {
        long seed = LocalDate.now().toEpochDay();
        Random dailyRandom = new Random(seed);

        Excuse excuse = new Excuse();
        excuse.setContext(getRandomFragmentWithSeed(FragmentType.CONTEXTO, dailyRandom));
        excuse.setCause(getRandomFragmentWithSeed(FragmentType.CAUSA, dailyRandom));
        excuse.setConsequence(getRandomFragmentWithSeed(FragmentType.CONSECUENCIA, dailyRandom));
        excuse.setRecommendation(getRandomFragmentWithSeed(FragmentType.RECOMENDACION, dailyRandom));
        excuse.setType(ExcuseType.SIMPLE);
        excuse.setSeed(seed);
        excuse.setCreatedAt(LocalDateTime.now());

        return excuseRepository.save(excuse);
    }

    /**
     * Crea una excusa personalizada a partir de un DTO.
     * Convierte el DTO a entidad resolviendo las relaciones por ID.
     *
     * @param dto datos de la excusa
     * @return excusa creada y persistida
     */
    public Excuse createFromDTO(ExcuseRequestDTO dto) {
        Excuse excuse = ExcuseMapper.toEntity(dto);

        // Resolver relaciones por ID
        if (dto.getContextId() != null) {
            fragmentRepository.findById(dto.getContextId())
                    .ifPresent(excuse::setContext);
        }
        if (dto.getCauseId() != null) {
            fragmentRepository.findById(dto.getCauseId())
                    .ifPresent(excuse::setCause);
        }
        if (dto.getConsequenceId() != null) {
            fragmentRepository.findById(dto.getConsequenceId())
                    .ifPresent(excuse::setConsequence);
        }
        if (dto.getRecommendationId() != null) {
            fragmentRepository.findById(dto.getRecommendationId())
                    .ifPresent(excuse::setRecommendation);
        }
        if (dto.getMemeId() != null) {
            memeRepository.findById(dto.getMemeId())
                    .ifPresent(excuse::setMeme);
        }
        if (dto.getLawId() != null) {
            lawRepository.findById(dto.getLawId())
                    .ifPresent(excuse::setLaw);
        }

        if (excuse.getType() == null) {
            excuse.setType(ExcuseType.SIMPLE);
        }
        if (excuse.getSeed() == null) {
            excuse.setSeed(System.nanoTime());
        }
        excuse.setCreatedAt(LocalDateTime.now());

        return excuseRepository.save(excuse);
    }

    /**
     * Obtiene un fragmento aleatorio de un tipo específico.
     * Método privado helper que lanza excepción si no hay fragmentos.
     *
     * @param type tipo de fragmento (CONTEXTO, CAUSA, etc.)
     * @return fragmento aleatorio del tipo especificado
     * @throws IllegalStateException si no hay fragmentos disponibles
     */
    private Fragment getRandomFragment(FragmentType type) {
        List<Fragment> fragments = fragmentRepository.findByType(type);
        if (fragments.isEmpty()) {
            throw new IllegalStateException(
                    "No hay fragmentos de tipo " + type + " disponibles en la base de datos");
        }
        return fragments.get(random.nextInt(fragments.size()));
    }

    /**
     * Obtiene un fragmento aleatorio de un tipo específico para un rol.
     * Si no hay fragmentos específicos del rol, cae de vuelta a fragmentos generales.
     * Método privado helper.
     *
     * @param type tipo de fragmento
     * @param role rol del desarrollador
     * @return fragmento del tipo y rol, o general si no hay específico del rol
     */
    private Fragment getRandomFragmentByRole(FragmentType type, Role role) {
        List<Fragment> roleFragments = fragmentRepository.findByTypeAndRole(type, role);
        
        if (!roleFragments.isEmpty()) {
            return roleFragments.get(random.nextInt(roleFragments.size()));
        }
        
        // Si no hay fragmentos específicos del rol, usar fragmentos generales
        return getRandomFragment(type);
    }

    /**
     * Obtiene un fragmento aleatorio usando un Random con seed específico.
     * Útil para generación reproducible (ej: excusa del día).
     * Método privado helper.
     *
     * @param type tipo de fragmento
     * @param randomWithSeed instancia Random con seed predeterminado
     * @return fragmento aleatorio
     */
    private Fragment getRandomFragmentWithSeed(FragmentType type, Random randomWithSeed) {
        List<Fragment> fragments = fragmentRepository.findByType(type);
        if (fragments.isEmpty()) {
            throw new IllegalStateException(
                    "No hay fragmentos de tipo " + type + " disponibles en la base de datos");
        }
        return fragments.get(randomWithSeed.nextInt(fragments.size()));
    }

    /**
     * Obtiene un meme aleatorio de la base de datos.
     * Retorna null si no hay memes disponibles.
     * Método privado helper.
     *
     * @return meme aleatorio o null si no hay memes
     */
    private Meme getRandomMeme() {
        List<Meme> memes = memeRepository.findAll();
        if (memes.isEmpty()) {
            return null;
        }
        return memes.get(random.nextInt(memes.size()));
    }

    /**
     * Obtiene una ley/axioma aleatoria de la base de datos.
     * Retorna null si no hay leyes disponibles.
     * Método privado helper.
     *
     * @return ley aleatoria o null si no hay leyes
     */
    private Law getRandomLaw() {
        List<Law> laws = lawRepository.findAll();
        if (laws.isEmpty()) {
            return null;
        }
        return laws.get(random.nextInt(laws.size()));
    }
}
