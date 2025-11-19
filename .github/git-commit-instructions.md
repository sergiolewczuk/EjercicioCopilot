# Guía de Commits - EjercicioCopilot

> Convenciones de commits para mantener un historial limpio, trazable y profesional

## 📋 Resumen Ejecutivo

Este proyecto sigue la especificación **Conventional Commits** para crear un historial de commits explícito y fácil de rastrear. Cada commit debe tener una estructura clara que permita:

- ✅ Generar **CHANGELOG** automáticamente
- ✅ Determinar **versionamiento semántico** (SemVer)
- ✅ Facilitar **code reviews** y auditorías
- ✅ Mejorar la **legibilidad** del historial

---

## 🏗️ Estructura de un Commit

### Formato Básico

```
<tipo>[escopo opcional]: <descripción>

[cuerpo opcional]

[pie de página opcional]
```

### Ejemplo Completo

```
feat(ExcuseService): implementar generación de excusas diarias

- Añadir método generateDaily() con seed basado en fecha
- Garantizar reproducibilidad: misma excusa todo el día
- Usar LocalDate.now().toEpochDay() como semilla
- Incluir documentación JavaDoc completa

Closes #42
Relacionado-Con: #38, #41
```

---

## 📝 Tipos de Commits

### **feat** - Nueva Característica
Introduce una nueva funcionalidad o capacidad en el proyecto.

**Ejemplo:**
```
feat(FragmentController): añadir filtro por tipo de fragmento

Implementar parámetro tipo=CONTEXTO en GET /api/fragments
```

**Cuándo usar:** Cuando se agrega código nuevo que añade valor al usuario final.

---

### **fix** - Corrección de Errores
Soluciona un bug o comportamiento inesperado en código existente.

**Ejemplo:**
```
fix(ExcuseService): corregir NPE cuando no hay fragmentos

Validar lista vacía antes de acceder a elementos
```

**Cuándo usar:** Cuando se arregla un defecto o comportamiento incorrecto.

---

### **docs** - Documentación
Cambios que afecten solo a documentación (README, comentarios, etc.).

**Ejemplo:**
```
docs: actualizar instrucciones de instalación

Agregar pasos para H2 Console y Swagger UI
```

**Cuándo usar:** Cambios en README, guías, comentarios o documentación.

---

### **style** - Estilo de Código
Cambios que no afecten la lógica (formato, espacios, punto y coma).

**Ejemplo:**
```
style(ExcuseService): formatear código según Google Java Style

Aplicar indentación y organización de imports
```

**Cuándo usar:** Cambios de formato, reorganización de imports, ajustes visuales.

---

### **refactor** - Refactorización
Cambios internos sin afectar funcionalidad externa (mejora de código).

**Ejemplo:**
```
refactor(ExcuseService): extraer lógica de selección a método privado

Crear getRandomFragmentWithSeed() para mejorar legibilidad
```

**Cuándo usar:** Mejora interna de código, extracción de métodos, eliminación de duplicación.

---

### **perf** - Optimización de Rendimiento
Mejoras en velocidad o uso de recursos.

**Ejemplo:**
```
perf(FragmentRepository): implementar caché de fragmentos

Reducir queries N+1 con cache en memoria
```

**Cuándo usar:** Optimizaciones que mejoren rendimiento o recursos.

---

### **test** - Pruebas
Adición o modificación de tests unitarios e integración.

**Ejemplo:**
```
test(ExcuseServiceTest): agregar cobertura de generateDaily()

Incluir 3 casos: reproducibilidad, valores null, diferentes fechas
```

**Cuándo usar:** Tests nuevos, corrección de tests, mejora de cobertura.

---

### **build** - Sistema de Build
Cambios en dependencias, Maven, compilación.

**Ejemplo:**
```
build(pom.xml): actualizar Spring Boot a 3.2.1

Agregar dependencia de lombok 1.18.30
```

**Cuándo usar:** Cambios en pom.xml, Maven, dependencias.

---

### **ci** - Integración Continua
Cambios en pipelines CI/CD, GitHub Actions, etc.

**Ejemplo:**
```
ci(github-actions): agregar workflow de tests automáticos

Ejecutar tests en push y pull requests
```

**Cuándo usar:** Cambios en workflows, configuración de CI/CD.

---

### **chore** - Tareas Operacionales
Cambios que no afectan código (setup, config, mantenimiento).

**Ejemplo:**
```
chore: crear .gitignore y carpeta de configuración

Excluir archivos de IDE y Maven target
```

**Cuándo usar:** Tareas administrativas, setup, configuración general.

---

## 🎯 Escopo (Scope)

El escopo es **opcional** pero **recomendado**. Especifica qué parte del código se modifica.

### Ejemplos de Escopos en EjercicioCopilot

```
feat(ExcuseController): nuevo endpoint /random
feat(ExcuseService): método generateByRole()
feat(FragmentRepository): custom query findByTypeAndRole()
fix(ExcuseMapper): manejo de relaciones lazy
test(ExcuseIntegrationTest): datos de JSONs
docs(README): instrucciones de instalación
```

**Escopos Válidos:**
- **Controllers:** ExcuseController, FragmentController, MemeController, LawController
- **Services:** ExcuseService, FragmentService, MemeService, LawService
- **Repositories:** ExcuseRepository, FragmentRepository, MemeRepository, LawRepository
- **Models:** Excuse, Fragment, Meme, Law, ExcuseType, FragmentType, Role
- **DTOs:** ExcuseRequestDTO, ExcuseResponseDTO, FragmentDTO, etc.
- **Mappers:** ExcuseMapper, FragmentMapper, MemeMapper, LawMapper
- **Tests:** ExcuseServiceTest, ExcuseControllerTest, etc.
- **Config:** application.properties, Spring Configuration
- **Docs:** README, CONTRIBUTING, Architecture

---

## 📖 Descripción (Description)

### Reglas

1. **Imperativo, presente:** "agregar" no "agregado" o "agregué"
2. **Minúscula:** comenzar en minúscula (a menos que sea nombre propio)
3. **Sin punto final:** no terminar con "."
4. **Máximo 50 caracteres** (para que el log sea legible)
5. **Específico y claro:** describe QUÉ cambió

### ✅ Buenas Descripciones

```
feat(ExcuseService): implementar generación ULTRA_SHARK
feat(Fragment): validar texto no vacío
fix(ExcuseController): retornar 404 cuando ID no existe
test(ExcuseServiceTest): agregar 5 nuevos casos
refactor(LawService): extraer validación a método
```

### ❌ Malas Descripciones

```
feat(ExcuseService): cosa nueva
fix: arreglar bug
test: tests
Update stuff
WIP
```

---

## 📄 Cuerpo (Body)

El cuerpo es **opcional** pero **recomendado para commits complejos**. Proporciona contexto adicional.

### Reglas

1. Línea en blanco entre descripción y cuerpo
2. Máximo **72 caracteres por línea**
3. Explicar **qué** y **por qué**, no **cómo** (el código muestra cómo)
4. Usar viñetas (bullets) para múltiples cambios
5. Mantener formato limpio y legible

### Ejemplo de Cuerpo

```
feat(ExcuseService): implementar generación de excusas diarias

- Agregar método generateDaily() que usa fecha como seed
- Garantizar reproducibilidad: mismo seed = misma excusa
- Usar LocalDate.now().toEpochDay() como semilla determinística
- Incluir logging de seed generado para debugging
- Documentar en JavaDoc los pasos y casos especiales

El propósito es que usuarios obtengan la misma excusa durante
todo el día (mismo seed). Esto permite "excusa del día" consistente
en toda la aplicación sin almacenar estado adicional.
```

### Cuándo Agregar Cuerpo

- ✅ Cambios complejos o no triviales
- ✅ Decisiones arquitectónicas importantes
- ✅ Cuando el título no es suficiente para entender
- ❌ Cambios simples (typo, formato)
- ❌ Commits que son autoevidentes

---

## 🏷️ Pie de Página (Footer)

Los pies de página son **opcionales** y comunican información técnica importante.

### Tokens Comunes

#### **Closes / Fixes** - Relacionado con Issues
```
Closes #42
Fixes #123, #124
Resolves: #567
```

#### **Relacionado-Con** - Referencias conexas
```
Relacionado-Con: #38, #41
Depende-De: #200
Bloqueado-Por: #180
```

#### **Breaking Change** - Cambios que rompen compatibilidad
```
BREAKING CHANGE: cambiar tipo ExcuseType enum

ExcuseType ahora es enum en lugar de String.
Los clientes deben actualizar código que use strings directos.
```

#### **Reviewed-By** - Revisor del código
```
Reviewed-By: @sergiolewczuk
```

### Ejemplo Completo

```
feat(ExcuseService): implementar generateByRole()

Agregar soporte para generación de excusas específicas por rol DEV,
QA, DEVOPS, PM, ARCHITECT, DEVREL. El servicio selecciona fragmentos
que coincidan con el rol, con fallback a fragmentos generales.

Closes #42
Relacionado-Con: #38
Reviewed-By: @sergiolewczuk
```

---

## 📋 Checklist de Commits

Antes de hacer commit, verificar:

- [ ] **Tipo correcto:** feat, fix, docs, style, refactor, perf, test, build, ci, chore
- [ ] **Escopo válido:** uno de los principales componentes del proyecto
- [ ] **Descripción clara:** máximo 50 caracteres, imperativa, presente
- [ ] **Sin punto final** en la descripción
- [ ] **Cuerpo explicativo** si es cambio complejo (72 caracteres/línea)
- [ ] **Cambios relacionados** incluidos (no mezclar múltiples features)
- [ ] **Tests pasando:** `mvn clean test` antes de commit
- [ ] **Código formateado:** Google Java Style o Auto-formatter
- [ ] **JavaDoc actualizado** en métodos públicos
- [ ] **Referencias a issues** en pie de página (Closes #XXX)

---

## 🔍 Ejemplos por Escenario

### Agregar Nueva Funcionalidad

```
feat(ExcuseController): agregar endpoint /role/{role}

Implementar GET /api/excuses/role/{role} para generar excusas
específicas de un rol (DEV, QA, DEVOPS, PM, ARCHITECT, DEVREL).

- Validar rol antes de procesamiento
- Usar ExcuseService.generateByRole(role)
- Retornar 400 si rol es inválido
- Documentar en Swagger/OpenAPI

Closes #45
```

### Corregir Bug

```
fix(ExcuseService): evitar NPE en generateWithMeme()

Si no hay memes en la BD, la lista estaba vacía y causaba
IndexOutOfBoundsException al acceder sin validar.

Cambios:
- Validar que lista no esté vacía antes de acceder
- Lanzar IllegalStateException si lista está vacía
- Documentar en JavaDoc que se lanza excepción

Fixes #67
```

### Agregar Tests

```
test(ExcuseServiceTest): aumentar cobertura a 85%

Agregar 8 nuevos casos de prueba:
- generateDaily() reproducibilidad
- generateDaily() diferentes fechas
- generateByRole() con roles inválidos
- generateUltraShark() sin memes disponibles
- generateUltraShark() sin leyes disponibles
- findById() cuando existe
- findById() cuando no existe
- findAll() lista vacía

Cobertura antes: 72%, después: 85%
```

### Refactorizar Código

```
refactor(ExcuseService): extraer selección de fragmentos

Mover lógica repetida de selección aleatoria a método privado:
- getRandomFragment(FragmentType)
- getRandomFragmentByRole(FragmentType, Role)

Beneficios:
- Reducir duplicación
- Mejorar testabilidad
- Facilitar futuros cambios en estrategia de selección

Cambio interno, sin impacto en API pública.
```

### Actualizar Dependencias

```
build(pom.xml): actualizar Spring Boot a 3.2.1

- spring-boot-starter-web: 3.2.0 → 3.2.1
- spring-boot-starter-data-jpa: 3.2.0 → 3.2.1
- lombok: 1.18.28 → 1.18.30

Cambios de compatibilidad: ninguno
Cambios necesarios en código: ninguno
```

### Documentación

```
docs(README): agregar instrucciones de H2 Console

Documentar cómo acceder a H2 Console en desarrollo:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Usuario: sa
- Contraseña: (vacía)

Incluir screenshot del console
```

---

## 🚀 Mejores Prácticas

### ✅ Recomendaciones

1. **Commits Atómicos**
   - Un commit = una característica/fix lógico
   - No mezclar features diferentes
   - No mezclar refactorización con nuevas features

2. **Frecuencia**
   - Hacer commits regularmente (cada 1-2 horas máximo)
   - Evitar commits gigantes al final del día
   - Commits pequeños = histórico más limpio

3. **Pruebas Antes de Commit**
   ```bash
   # Ejecutar tests antes de commit
   mvn clean test
   
   # Verificar formato de código
   mvn spotless:check
   
   # Build completo
   mvn clean package
   ```

4. **Revisar Cambios**
   ```bash
   # Ver cambios antes de staging
   git diff
   
   # Ver cambios en stage
   git diff --staged
   
   # Hacer commit solo de archivos específicos
   git add ruta/archivo.java
   git commit -m "mensaje"
   ```

5. **Evitar Commits Malos**
   - ❌ "WIP", "Fix", "TODO"
   - ❌ Mezcla de features no relacionadas
   - ❌ Cambios sin tests
   - ❌ Code que no compila
   - ❌ Caracteres especiales en descripción

### ❌ Anti-Patrones

```
# ❌ Demasiado genérico
commit: cambios

# ❌ Demasiado específico (nivel detalle bajo)
fix: cambiar variable i por index

# ❌ Mezclar múltiples concerns
feat: agregar ExcuseController y FragmentService y tests y docs

# ❌ Sin contexto
fix(ExcuseService): cosa

# ❌ Referencias rotas
feat: closes #999 (si el issue no existe)
```

---

## 🔧 Configuración de Git Local

### Configurar Tu Identidad

```bash
git config user.name "Tu Nombre"
git config user.email "tu.email@example.com"

# Global (para todos los repos)
git config --global user.name "Tu Nombre"
git config --global user.email "tu.email@example.com"
```

### Template de Commit (Opcional)

Crear archivo `.git/hooks/prepare-commit-msg` para recordar formato:

```bash
#!/bin/bash
echo "
# ────────────────────────────────────────────────────────────
# Commit Format: <type>[scope]: <description>
# ────────────────────────────────────────────────────────────
# Types: feat, fix, docs, style, refactor, perf, test, build, ci, chore
# Scopes: ExcuseService, FragmentController, ExcuseRepository, etc.
# Description: Máximo 50 caracteres, imperativo, presente
# ────────────────────────────────────────────────────────────
# Cuerpo (72 caracteres/línea, explica qué y por qué)
# ────────────────────────────────────────────────────────────
# Footer: Closes #123, Relacionado-Con: #456, BREAKING CHANGE:
# ────────────────────────────────────────────────────────────
" >> \"$1\"
```

Hacer ejecutable:
```bash
chmod +x .git/hooks/prepare-commit-msg
```

---

## 📊 Versionamiento Semántico (SemVer)

Los commits Conventional permiten **versionamiento automático**:

- **MAJOR** (1.0.0 → 2.0.0): Breaking changes
- **MINOR** (1.0.0 → 1.1.0): feat nuevas
- **PATCH** (1.0.0 → 1.0.1): fixes

Ejemplo de release notes auto-generado:

```markdown
## v1.2.0 (2024-01-20)

### Features
- feat(ExcuseService): implementar generación diaria
- feat(ExcuseController): agregar endpoint /role/{role}

### Bug Fixes
- fix(ExcuseService): evitar NPE sin memes

### Refactoring
- refactor(ExcuseService): extraer selección de fragmentos

### Documentation
- docs(README): agregar instrucciones H2 Console
```

---

## 🤖 Herramientas Recomendadas

### Commitizen (CLI Interactivo)

```bash
npm install -g commitizen
commitizen init cz-conventional-changelog --save --save-exact

# En el proyecto
cz commit
```

Pregunta interactivamente por tipo, escopo, descripción, etc.

### Conventional Commits Lint

```bash
npm install --save-dev @commitlint/config-conventional @commitlint/cli

# Crear .commitlintrc.json
echo "{ extends: ['@commitlint/config-conventional'] }" > .commitlintrc.json
```

---

## 📚 Referencias

- [Conventional Commits Spec](https://www.conventionalcommits.org/)
- [Git Commit Best Practices](https://www.git-scm.com/)
- [Angular Commit Guidelines](https://github.com/angular/angular/blob/master/CONTRIBUTING.md#commit)
- [Semantic Versioning](https://semver.org/)

---

## ❓ Preguntas Frecuentes

**P: ¿Puedo hacer rebase de commits antes de hacer push?**
A: Sí, es recomendable limpiar commits locales antes de push. Nunca rebase commits que ya estén en `origin/main`.

**P: ¿Qué pasa con commits anteriores que no siguen este formato?**
A: Los commits históricos pueden dejarse como están. Aplica esto a commits nuevos.

**P: ¿Es obligatorio agregar cuerpo?**
A: No es obligatorio para cambios simples, pero se recomienda para cambios complejos.

**P: ¿Puedo hacer amend a un commit después de hacer push?**
A: No se recomienda si otros ya hicieron pull. Usa `git push --force-with-lease` solo si sabes qué haces.

**P: ¿Cómo reporto un breaking change?**
A: Usar `BREAKING CHANGE:` en el pie de página o agregar `!` después del scope:

```
feat(ExcuseType)!: cambiar de String a Enum

BREAKING CHANGE: ExcuseType ahora es enum, no String
```

---

**Última actualización:** Noviembre 2024  
**Proyecto:** EjercicioCopilot - Generador de Excusas Tech  
**Mantenedor:** Equipo de Desarrollo
