-- ============================================================================
-- EjercicioCopilot - PostgreSQL Initialization Script
-- ============================================================================
--
-- Script para inicializar la base de datos PostgreSQL.
-- Se ejecuta automáticamente al crear el contenedor de PostgreSQL.
--
-- Ubicación: /docker-entrypoint-initdb.d/01-init.sql
-- (Debe ser nombrado con patrón numérico para que se ejecute en orden)
--

-- ============================================================================
-- CREAR EXTENSIONES
-- ============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================================================
-- CREAR SCHEMAS
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS public;

-- ============================================================================
-- CREAR TABLAS
-- ============================================================================

-- Tabla Fragment (Fragmentos de excusas)
CREATE TABLE IF NOT EXISTS fragments (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    text VARCHAR(500) NOT NULL,
    role VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fragment_type CHECK (type IN ('CONTEXTO', 'CAUSA', 'CONSECUENCIA', 'RECOMENDACION'))
);

-- Tabla Meme
CREATE TABLE IF NOT EXISTS memes (
    id SERIAL PRIMARY KEY,
    author VARCHAR(100) NOT NULL,
    quote VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Law (Leyes y axiomas)
CREATE TABLE IF NOT EXISTS laws (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_law_category CHECK (category IN ('Murphy', 'Hofstadter', 'Dilbert', 'DevOps', 'DevAxiom'))
);

-- Tabla Excuse (Excusas generadas)
CREATE TABLE IF NOT EXISTS excuses (
    id SERIAL PRIMARY KEY,
    context_id BIGINT REFERENCES fragments(id) ON DELETE SET NULL,
    cause_id BIGINT REFERENCES fragments(id) ON DELETE SET NULL,
    consequence_id BIGINT REFERENCES fragments(id) ON DELETE SET NULL,
    recommendation_id BIGINT REFERENCES fragments(id) ON DELETE SET NULL,
    meme_id BIGINT REFERENCES memes(id) ON DELETE SET NULL,
    law_id BIGINT REFERENCES laws(id) ON DELETE SET NULL,
    type VARCHAR(50) NOT NULL,
    role VARCHAR(50),
    seed BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_excuse_type CHECK (type IN ('SIMPLE', 'CON_MEME', 'CON_LEY', 'ULTRA_SHARK'))
);

-- ============================================================================
-- CREAR ÍNDICES
-- ============================================================================

CREATE INDEX IF NOT EXISTS idx_fragments_type ON fragments(type);
CREATE INDEX IF NOT EXISTS idx_fragments_role ON fragments(role);
CREATE INDEX IF NOT EXISTS idx_memes_author ON memes(author);
CREATE INDEX IF NOT EXISTS idx_laws_category ON laws(category);
CREATE INDEX IF NOT EXISTS idx_excuses_type ON excuses(type);
CREATE INDEX IF NOT EXISTS idx_excuses_created_at ON excuses(created_at DESC);

-- ============================================================================
-- INSERTAR DATOS INICIALES (OPCIONAL)
-- ============================================================================

-- Insertar fragmentos de ejemplo
INSERT INTO fragments (type, text, role) VALUES
('CONTEXTO', 'Durante el despliegue a producción', 'DEV'),
('CONTEXTO', 'En la reunión con el cliente', 'PM'),
('CAUSA', 'El servidor se quedó sin memoria', NULL),
('CAUSA', 'El token de API expiró', 'DEVOPS'),
('CONSECUENCIA', 'Tuvimos que hacer rollback urgente', NULL),
('CONSECUENCIA', 'La aplicación estuvo 2 horas caída', NULL),
('RECOMENDACION', 'Implementar monitoring más detallado', 'DEVOPS'),
('RECOMENDACION', 'Hacer load testing antes de deployar', 'QA');

-- Insertar memes de ejemplo
INSERT INTO memes (author, quote) VALUES
('Tano Pasman', '¿CÓMO QUE FALLÓ EL PIPELINE?'),
('Anónimo', 'Funciona en mi máquina'),
('Senior Dev', 'Eso no debería ser posible');

-- Insertar leyes de ejemplo
INSERT INTO laws (name, description, category) VALUES
('Ley de Murphy', 'Si algo puede salir mal, saldrá mal.', 'Murphy'),
('Murphy Corolario 1', 'Si algo puede salir mal, saldrá mal en la peor forma posible.', 'Murphy'),
('Ley de Hofstadter', 'Una tarea siempre toma más tiempo de lo que esperas.', 'Hofstadter'),
('Principio de Dilbert', 'Los proyectos nunca usan sus mejores recursos.', 'Dilbert');

-- ============================================================================
-- CREAR USUARIO CON PERMISOS (OPCIONAL)
-- ============================================================================

-- Nota: El usuario ya debería estar creado por docker-compose
-- pero esto asegura que tenga los permisos correctos

-- GRANT CONNECT ON DATABASE ejerciciocopilot TO appuser;
-- GRANT USAGE ON SCHEMA public TO appuser;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO appuser;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO appuser;

-- ============================================================================
-- COMENTARIOS Y NOTAS
-- ============================================================================

/*
 * Este script se ejecuta automáticamente cuando se crea el contenedor PostgreSQL.
 * Usado solo si PostgreSQL está habilitado en docker-compose.yml
 * 
 * Para usar PostgreSQL en lugar de H2:
 * 1. Descomentar el servicio 'postgres' en docker-compose.yml
 * 2. Cambiar SPRING_DATASOURCE_URL en variables de entorno
 * 3. Cambiar SPRING_JPA_DATABASE_PLATFORM a PostgreSQL
 * 4. Reiniciar con: docker-compose down -v && docker-compose up -d
 * 
 * Conexión manual:
 *   psql -h localhost -U appuser -d ejerciciocopilot
 *   Password: changeme (o la que esté en .env)
 */
