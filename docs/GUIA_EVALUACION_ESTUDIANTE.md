# 📊 Guía de Evaluación - Proyecto Mutantes MercadoLibre

## 🎯 Objetivo
Esta guía te ayudará a entender **cómo será evaluado tu proyecto** y qué aspectos debes cuidar durante el desarrollo. La evaluación es **automatizada** y contempla 5 categorías principales.

---

## 📈 Distribución de Puntos (Total: 100 puntos)

| Categoría | Puntos | % | Descripción |
|-----------|--------|---|-------------|
| 🧬 **Algoritmo de Detección** | 35 | 35% | Correctitud, rendimiento y optimizaciones |
| 🏗️ **Arquitectura y Código** | 25 | 25% | Estructura, patrones y buenas prácticas |
| 🧪 **Testing y Cobertura** | 20 | 20% | Tests unitarios, integración y cobertura |
| 🌐 **API REST** | 12 | 12% | Endpoints, documentación Swagger |
| 💾 **Persistencia** | 8 | 8% | Base de datos y estrategia de almacenamiento |

**Puntuación mínima para aprobar: 70 puntos**

---

## 🧬 Categoría 1: Algoritmo de Detección de Mutantes (35 puntos)

Esta es la categoría **MÁS IMPORTANTE** del proyecto. Se evalúa la calidad y eficiencia de tu algoritmo.

### 1.1 Correctitud Funcional (10 puntos)

**¿Qué se evalúa?**
- ✅ Tu algoritmo detecta correctamente ADN mutante (con 2+ secuencias)
- ✅ Tu algoritmo detecta correctamente ADN humano (con 0-1 secuencias)
- ✅ Valida correctamente matriz NxN
- ✅ Valida caracteres permitidos (A, T, C, G)
- ✅ Maneja casos especiales (null, empty, matrices inválidas)

**Cómo obtener los puntos:**

| Métrica | Puntos | Cómo lograrlo |
|---------|--------|---------------|
| **Tests unitarios** | 6 pts | Mínimo 15 tests en `MutantDetectorTest` (óptimo: 17+) |
| **Cobertura** | 4 pts | Cobertura de `MutantDetector` >85% (óptimo: >95%) |

**💡 Consejos:**
```java
// ✅ BIEN: Cubre todos los casos
@Test
public void testMutantWithHorizontalAndVertical() { ... }

@Test
public void testHumanWithOnlyOneSequence() { ... }

@Test
public void testInvalidMatrixNonSquare() { ... }

@Test
public void testNullDnaArray() { ... }
```

---

### 1.2 Complejidad Temporal - Rendimiento (12 puntos) ⏱️

**¡IMPORTANTE!** Esta es la sección con más peso técnico.

**¿Qué se evalúa?**
Tu algoritmo debe ser **RÁPIDO** y **EFICIENTE**. Se miden tiempos reales de ejecución.

#### Métricas de Performance:

| Tamaño Matriz | Tiempo Óptimo | Tiempo Aceptable | Puntos |
|---------------|---------------|------------------|--------|
| 6x6 (estándar) | ≤ 1 ms | ≤ 5 ms | 2.4 |
| 100x100 | ≤ 20 ms | ≤ 100 ms | 3.6 |
| 1000x1000 | ≤ 500 ms | ≤ 5000 ms | 3.6 |

#### Optimizaciones Clave (2.4 puntos adicionales):

**1. Early Termination (CRÍTICO)** 🚀
```java
// ✅ BIEN: Retorna inmediatamente al encontrar >1 secuencia
if (sequenceCount > 1) {
    return true;  // ¡No seguir buscando!
}

// ❌ MAL: Recorre toda la matriz innecesariamente
// (sin early termination)
```
**Puntos:** 2.4 | **Ahorro:** ~80% del tiempo en casos mutantes

**2. Conversión a char[][]** ⚡
```java
// ✅ BIEN: Acceso O(1) rápido
char[][] matrix = new char[n][];
for (int i = 0; i < n; i++) {
    matrix[i] = dna[i].toCharArray();
}

// ❌ MAL: String.charAt() es más lento
// dna[row].charAt(col)
```
**Puntos:** 2.0 | **Ahorro:** ~15-20% del tiempo

**3. Boundary Checking** 🎯
```java
// ✅ BIEN: Solo busca donde hay espacio
if (col <= n - SEQUENCE_LENGTH) {
    checkHorizontal(...);  // Solo si cabe la secuencia
}

// ❌ MAL: Verificar dentro del método (más lento)
```
**Puntos:** 2.0

**4. Comparaciones Directas** 💨
```java
// ✅ BIEN: Comparación directa sin loops
private boolean checkHorizontal(char[][] matrix, int row, int col) {
    final char base = matrix[row][col];
    return matrix[row][col + 1] == base &&
           matrix[row][col + 2] == base &&
           matrix[row][col + 3] == base;
}

// ❌ MAL: Loop innecesario
for (int i = 0; i < 4; i++) {
    if (matrix[row][col + i] != base) return false;
}
```
**Puntos:** 1.5

**💡 Consejo PRO:**
Tu algoritmo debe tener complejidad **O(N²) en el peor caso**, pero gracias al **early termination**, en la práctica será **~O(N)** para ADN mutante.

---

### 1.3 Complejidad Espacial (5 puntos)

**¿Qué se evalúa?**
Tu algoritmo debe usar **O(1) espacio adicional** (sin estructuras auxiliares innecesarias).

| Criterio | Puntos | Descripción |
|----------|--------|-------------|
| **Sin estructuras auxiliares** | 3 pts | No usar `ArrayList`, `HashMap`, etc. dentro de `isMutant()` |
| **Conversión eficiente** | 2 pts | Usar `toCharArray()` para acceso rápido |

```java
// ❌ MAL: O(N) espacio adicional innecesario
public boolean isMutant(String[] dna) {
    List<String> foundSequences = new ArrayList<>();  // ¡NO!
    ...
}

// ✅ BIEN: O(1) espacio
public boolean isMutant(String[] dna) {
    int sequenceCount = 0;  // Solo un contador
    char[][] matrix = ...;  // Conversión necesaria
    ...
}
```

---

### 1.4 Optimizaciones Implementadas (8 puntos)

**Checklist de optimizaciones:**

| Optimización | Puntos | ¿Implementado? |
|--------------|--------|----------------|
| ✅ **Early Termination** | 2.4 | `if (sequenceCount > 1) return true;` |
| ✅ **Single Pass** (un solo recorrido) | 2.0 | Solo 2 loops anidados (row, col) |
| ✅ **Boundary Checking** | 1.6 | Verificar límites antes de buscar |
| ✅ **Direct Comparison** | 1.2 | Sin loops adicionales en checks |
| ✅ **Validation Set O(1)** | 0.8 | `Set.of('A','T','C','G')` |

**Ejemplo de implementación óptima:**

```java
@Service
public class MutantDetector {
    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    public boolean isMutant(String[] dna) {
        if (!isValidDna(dna)) return false;

        final int n = dna.length;
        int sequenceCount = 0;

        // Conversión a char[][] (Optimización #1)
        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // Single Pass: recorrer UNA SOLA VEZ (Optimización #2)
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Boundary Checking (Optimización #3)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination!
                    }
                }

                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination!
                    }
                }

                // ... diagonales similares
            }
        }
        return false;
    }

    // Comparación directa (Optimización #4)
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
               matrix[row][col + 2] == base &&
               matrix[row][col + 3] == base;
    }
}
```

---

## 🏗️ Categoría 2: Arquitectura y Calidad de Código (25 puntos)

### 2.1 Arquitectura de 6 Capas (8 puntos)

**Estructura requerida:**

```
src/main/java/org/example/
├── controller/          (1.5 pts) - MutantController.java
├── dto/                 (1.5 pts) - DnaRequest.java, StatsResponse.java
├── service/             (1.5 pts) - MutantDetector.java, MutantService.java, StatsService.java
├── repository/          (1.0 pts) - DnaRecordRepository.java
├── entity/              (1.5 pts) - DnaRecord.java
└── config/              (1.0 pts) - SwaggerConfig.java
```

**💡 Consejo:** Mantén cada capa con su responsabilidad específica. No mezcles lógica de negocio en el controller.

---

### 2.2 Patrones de Diseño (5 puntos)

#### Dependency Injection (2.0 pts)

```java
// ✅ BIEN: Usar @RequiredArgsConstructor de Lombok
@RestController
@RequiredArgsConstructor
public class MutantController {
    private final MutantService mutantService;  // Inyección automática
    private final StatsService statsService;
}

// ❌ MAL: Constructor manual innecesario
@RestController
public class MutantController {
    private MutantService mutantService;

    @Autowired
    public MutantController(MutantService mutantService) {
        this.mutantService = mutantService;
    }
}
```

#### DTO Pattern (1.5 pts)

```java
// ✅ BIEN: DTOs separados para request/response
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {
    @NotNull
    @NotEmpty
    private String[] dna;
}

@Data
public class StatsResponse {
    private long count_mutant_dna;
    private long count_human_dna;
    private double ratio;
}
```

#### Repository Pattern (1.5 pts)

```java
// ✅ BIEN: Extends JpaRepository
@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {
    Optional<DnaRecord> findByDnaHash(String dnaHash);
    long countByIsMutant(boolean isMutant);
}
```

---

### 2.3 Uso de Lombok (3 puntos)

**Anotaciones requeridas (mínimo 3 para puntaje completo):**

| Anotación | Uso | Beneficio |
|-----------|-----|-----------|
| `@Data` | DTOs | Genera getters, setters, toString, equals, hashCode |
| `@NoArgsConstructor` | DTOs, Entities | Constructor vacío (necesario para Jackson/JPA) |
| `@AllArgsConstructor` | DTOs | Constructor con todos los campos |
| `@RequiredArgsConstructor` | Services, Controllers | DI automática de campos `final` |
| `@Getter` / `@Setter` | Entities | Control fino de acceso |

**Ejemplo óptimo:**

```java
// DTO con Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para verificar ADN")
public class DnaRequest {
    @NotNull
    @NotEmpty
    private String[] dna;
}

// Service con Lombok
@Service
@RequiredArgsConstructor
public class MutantService {
    private final MutantDetector mutantDetector;
    private final DnaRecordRepository repository;
}
```

---

### 2.4 Manejo de Excepciones (4 puntos)

#### GlobalExceptionHandler (2.0 pts)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        // Manejar errores de validación
    }

    @ExceptionHandler(DnaHashCalculationException.class)
    public ResponseEntity<ErrorResponse> handleDnaHashError(
            DnaHashCalculationException ex) {
        // Manejar error custom
    }
}
```

#### Custom Exceptions (1.0 pts)

```java
public class DnaHashCalculationException extends RuntimeException {
    public DnaHashCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

### 2.5 Validaciones (5 puntos)

#### Bean Validation en DTOs (2.0 pts)

```java
@Data
@Schema(description = "Request para verificar ADN")
public class DnaRequest {

    @NotNull(message = "DNA no puede ser null")
    @NotEmpty(message = "DNA no puede estar vacío")
    @ValidDnaSequence  // Custom validator
    private String[] dna;
}
```

#### Custom Validator (3.0 pts)

```java
// Anotación custom (1.5 pts)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDnaSequenceValidator.class)
public @interface ValidDnaSequence {
    String message() default "Secuencia de ADN inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// Implementación (1.5 pts)
public class ValidDnaSequenceValidator
        implements ConstraintValidator<ValidDnaSequence, String[]> {

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0) return false;

        int n = dna.length;
        Pattern pattern = Pattern.compile("^[ATCG]+$");

        for (String row : dna) {
            if (row == null || row.length() != n) return false;
            if (!pattern.matcher(row).matches()) return false;
        }
        return true;
    }
}
```

---

## 🧪 Categoría 3: Testing y Cobertura (20 puntos)

### 3.1 Cobertura de Código con JaCoCo (8 puntos)

**Comandos:**
```bash
./gradlew test jacocoTestReport
# Reporte en: build/reports/jacoco/test/html/index.html
```

| Métrica | Óptimo | Aceptable | Puntos |
|---------|--------|-----------|--------|
| **Cobertura Total** | ≥90% | ≥70% | 3.2 |
| **Cobertura Service Layer** | ≥96% | ≥85% | 3.2 |
| **Cobertura Controller** | ≥95% | ≥80% | 1.6 |

**💡 Nota sobre Lombok:**
- Lombok genera código extra (equals, hashCode, toString)
- Esto baja la cobertura reportada (~71%)
- **Lo importante:** Service layer debe tener >90%
- El evaluador automático considera esto

**Configuración en `build.gradle`:**
```gradle
jacoco {
    toolVersion = "0.8.11"
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        html.required = true
    }
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: [
                '**/MutantDetectorApplication.class',
                '**/config/**'
            ])
        }))
    }
}
```

---

### 3.2 Suite de Tests Completa (7 puntos)

**Requisitos:**

| Tipo de Test | Mínimo | Óptimo | Puntos | Archivos |
|--------------|--------|--------|--------|----------|
| **Tests Totales** | 25 | 35+ | 2.1 | Todos |
| **Tests Unitarios** | 20 | 28+ | 2.45 | MutantDetectorTest, MutantServiceTest, StatsServiceTest |
| **Tests Integración** | 8 | 12+ | 2.45 | MutantControllerTest |

**Ejemplo de suite completa:**

```
src/test/java/org/example/
├── service/
│   ├── MutantDetectorTest.java      (17 tests)
│   ├── MutantServiceTest.java       (5 tests)
│   └── StatsServiceTest.java        (6 tests)
└── controller/
    └── MutantControllerTest.java    (8 tests)

TOTAL: 36 tests
```

---

### 3.3 Casos de Test del Algoritmo (5 puntos)

**Checklist de casos requeridos:**

| Caso de Test | Puntos | Ejemplo |
|--------------|--------|---------|
| ✅ **Mutante - Horizontal** | 1.0 | ADN con 2+ secuencias horizontales |
| ✅ **Mutante - Diagonal** | 1.0 | ADN con secuencias diagonales |
| ✅ **Humano - Sin secuencias** | 1.0 | ADN sin ninguna secuencia de 4 |
| ✅ **Humano - 1 secuencia** | 1.0 | ADN con exactamente 1 secuencia |
| ✅ **Validación - Inválido** | 1.0 | Null, empty, non-square, caracteres inválidos |

**Ejemplos de tests:**

```java
@SpringBootTest
public class MutantDetectorTest {

    @Autowired
    private MutantDetector mutantDetector;

    // Mutante - Horizontal + Vertical (1.0 pts)
    @Test
    public void testMutantWithHorizontalAndVertical() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // Mutante - Diagonal (1.0 pts)
    @Test
    public void testMutantWithDiagonalSequence() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // Humano - Sin secuencias (1.0 pts)
    @Test
    public void testHumanWithNoSequences() {
        String[] dna = {
            "ATGC",
            "CAGT",
            "TTAT",
            "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // Humano - 1 secuencia (1.0 pts)
    @Test
    public void testHumanWithOnlyOneSequence() {
        String[] dna = {
            "AAAA",
            "CAGT",
            "TTAT",
            "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // Validación - Matriz no cuadrada (1.0 pts)
    @Test
    public void testInvalidDnaNonSquare() {
        String[] dna = {
            "ATGC",
            "CAG",
            "TTAT"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // Validación - Caracteres inválidos (1.0 pts)
    @Test
    public void testInvalidDnaCharacters() {
        String[] dna = {
            "ATGX",
            "CAGT",
            "TTAT",
            "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // Validación - Null (1.0 pts)
    @Test
    public void testNullDnaArray() {
        assertFalse(mutantDetector.isMutant(null));
    }

    // Validación - Empty (1.0 pts)
    @Test
    public void testEmptyDnaArray() {
        String[] dna = {};
        assertFalse(mutantDetector.isMutant(dna));
    }
}
```

---

## 🌐 Categoría 4: API REST y Documentación (12 puntos)

### 4.1 Endpoints Funcionales (5 puntos)

#### POST /mutant (4.0 pts)

**Casos de test requeridos:**

```java
@SpringBootTest
@AutoConfigureMockMvc
public class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mutante válido - 200 OK (1.5 pts)
    @Test
    public void testMutantEndpoint_ReturnOk() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
            }
            """;

        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dnaJson))
                .andExpect(status().isOk());
    }

    // Humano válido - 403 Forbidden (1.5 pts)
    @Test
    public void testHumanEndpoint_ReturnForbidden() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]
            }
            """;

        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dnaJson))
                .andExpect(status().isForbidden());
    }

    // Input inválido - 400 Bad Request (1.0 pts)
    @Test
    public void testInvalidDna_ReturnBadRequest() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGX","CAGT"]
            }
            """;

        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dnaJson))
                .andExpect(status().isBadRequest());
    }
}
```

#### GET /stats (1.0 pts)

```java
// Test stats endpoint (1.0 pts)
@Test
public void testStatsEndpoint_ReturnOk() throws Exception {
    mockMvc.perform(get("/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count_mutant_dna").exists())
            .andExpect(jsonPath("$.count_human_dna").exists())
            .andExpect(jsonPath("$.ratio").exists());
}
```

---

### 4.2 Documentación Swagger/OpenAPI (4 puntos)

#### Configuración Swagger (1.0 pts)

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API")
                        .version("1.0")
                        .description("API para detectar mutantes mediante análisis de ADN"));
    }
}
```

#### Anotaciones en Controller (1.0 pts)

```java
@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para detección de mutantes")
public class MutantController {

    @PostMapping("/mutant")
    @Operation(summary = "Verificar si un ADN es mutante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Es mutante"),
        @ApiResponse(responseCode = "403", description = "No es mutante"),
        @ApiResponse(responseCode = "400", description = "ADN inválido")
    })
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        // ...
    }
}
```

#### Schema en DTOs (1.0 pts)

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para verificar si un ADN es mutante")
public class DnaRequest {

    @Schema(
        description = "Secuencia de ADN representada como matriz NxN",
        example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]",
        required = true
    )
    @NotNull
    @NotEmpty
    @ValidDnaSequence
    private String[] dna;
}
```

#### application.properties (1.0 pts)

```properties
# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operationsSorter=method
```

**💡 Verificar Swagger:**
```bash
./gradlew bootRun
# Abrir: http://localhost:8080/swagger-ui.html
```

---

### 4.3 Contratos de API (3 puntos)

#### DTOs con Validación (1.5 pts)

```java
// Request
@Data
@Schema(description = "Request para verificar ADN")
public class DnaRequest {
    @NotNull(message = "DNA no puede ser null")
    @NotEmpty(message = "DNA no puede estar vacío")
    @ValidDnaSequence
    private String[] dna;
}

// Response
@Data
@AllArgsConstructor
@Schema(description = "Estadísticas de verificaciones de ADN")
public class StatsResponse {

    @Schema(description = "Cantidad de ADN mutante verificado")
    private long count_mutant_dna;

    @Schema(description = "Cantidad de ADN humano verificado")
    private long count_human_dna;

    @Schema(description = "Ratio: mutantes / humanos")
    private double ratio;
}
```

#### ResponseEntity en Controller (1.5 pts)

```java
@RestController
@RequiredArgsConstructor
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.analyzeDna(request.getDna());
        return isMutant
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
```

---

## 💾 Categoría 5: Persistencia y Base de Datos (8 puntos)

### 5.1 Estrategia de Deduplicación con Hash (4 puntos)

**¿Por qué hash?**
- Evita duplicados en BD
- Búsqueda O(1)
- Ahorra espacio

#### Cálculo de Hash (1.5 pts)

```java
@Service
@RequiredArgsConstructor
public class MutantService {

    private final DnaRecordRepository repository;
    private final MutantDetector mutantDetector;

    public boolean analyzeDna(String[] dna) {
        // Calcular hash del DNA
        String dnaHash = calculateDnaHash(dna);

        // Verificar si ya existe en BD
        Optional<DnaRecord> existing = repository.findByDnaHash(dnaHash);
        if (existing.isPresent()) {
            return existing.get().isMutant();  // Retornar resultado cacheado
        }

        // Analizar DNA
        boolean isMutant = mutantDetector.isMutant(dna);

        // Guardar resultado
        DnaRecord record = new DnaRecord();
        record.setDnaHash(dnaHash);
        record.setIsMutant(isMutant);
        record.setCreatedAt(LocalDateTime.now());
        repository.save(record);

        return isMutant;
    }

    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String dnaString = String.join("", dna);
            byte[] hashBytes = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));

            // Convertir a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error calculando hash", e);
        }
    }
}
```

#### Entidad con dnaHash (2.5 pts)

```java
@Entity
@Table(name = "dna_records")
@Getter
@Setter
@NoArgsConstructor
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false)  // ¡UNIQUE!
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
```

**Puntos:**
- Campo `dnaHash`: 1.0 pts
- Constraint `unique = true`: 1.0 pts
- Método `findByDnaHash`: 0.5 pts

---

### 5.2 Repository JPA (2 puntos)

```java
@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    Optional<DnaRecord> findByDnaHash(String dnaHash);  // Buscar por hash

    long countByIsMutant(boolean isMutant);  // Contar mutantes/humanos
}
```

**Puntos:**
- `extends JpaRepository`: 1.0 pts
- Query methods (`findByDnaHash`, `countByIsMutant`): 1.0 pts

---

### 5.3 Entidad JPA (2 puntos)

**Anotaciones requeridas:**

```java
@Entity
@Table(name = "dna_records")
@Getter
@Setter
@NoArgsConstructor
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
```

**Checklist:**
- ✅ `@Entity`: Marca como entidad JPA
- ✅ `@Table`: Define nombre de tabla
- ✅ `@Id`: Clave primaria
- ✅ `@GeneratedValue`: ID auto-generado
- ✅ `@Column`: Configuración de columnas

**Puntos:**
- 3+ anotaciones JPA: 1.0 pts
- 4 campos requeridos (id, dnaHash, isMutant, createdAt): 1.0 pts

---

## 📊 Resumen de Evaluación por Niveles

### 🌟 Nivel Excelente (90-100 puntos)

**Características:**
- ✅ Algoritmo con **TODAS** las optimizaciones (early termination, single pass, etc.)
- ✅ Tiempo de ejecución **óptimo** (1ms para 6x6, 20ms para 100x100)
- ✅ Cobertura de tests **>90%**
- ✅ 35+ tests (17+ unitarios, 8+ integración)
- ✅ Arquitectura de 6 capas **perfectamente organizada**
- ✅ Swagger **completamente documentado**
- ✅ Hash para deduplicación **implementado correctamente**
- ✅ Lombok usado en **todos** los componentes apropiados

**Estudiante que logra este nivel:**
Domina Spring Boot, entiende optimización de algoritmos, sigue buenas prácticas profesionales.

---

### 🔷 Nivel Muy Bueno (80-89 puntos)

**Características:**
- ✅ Algoritmo con **mayoría** de optimizaciones (early termination + 2 más)
- ✅ Tiempo de ejecución **bueno** (3ms para 6x6, 50ms para 100x100)
- ✅ Cobertura de tests **>85%**
- ✅ 30+ tests
- ✅ Arquitectura de 6 capas completa
- ✅ Swagger configurado con anotaciones básicas
- ✅ Hash implementado
- ⚠️ Algunas optimizaciones menores faltantes

**Estudiante que logra este nivel:**
Buen dominio técnico, implementación sólida, pequeños detalles a mejorar.

---

### 🟢 Nivel Bueno (70-79 puntos) - APROBADO

**Características:**
- ✅ Algoritmo **funcional** con optimizaciones básicas (early termination)
- ✅ Tiempo de ejecución **aceptable** (5ms para 6x6, 100ms para 100x100)
- ✅ Cobertura de tests **>80%**
- ✅ 25+ tests
- ✅ Arquitectura de 6 capas (puede faltar config)
- ⚠️ Swagger básico o incompleto
- ⚠️ Hash implementado pero puede mejorar
- ⚠️ Algunas capas sin Lombok

**Estudiante que logra este nivel:**
Cumple con los requisitos mínimos, proyecto funcional, áreas de mejora identificables.

---

### ⚠️ Nivel Suficiente (60-69 puntos) - REQUIERE MEJORAS

**Características:**
- ⚠️ Algoritmo funcional pero **lento** (>5ms para 6x6)
- ⚠️ Sin early termination o con pocas optimizaciones
- ⚠️ Cobertura **70-80%**
- ⚠️ 20-25 tests
- ⚠️ Arquitectura incompleta (faltan 1-2 capas)
- ❌ Sin Swagger o mínimo
- ⚠️ Persistencia básica sin hash

**Estudiante que logra este nivel:**
Proyecto funcional pero requiere optimización y mejoras arquitectónicas significativas.

---

### ❌ Nivel Insuficiente (<60 puntos) - NO APROBADO

**Características:**
- ❌ Algoritmo **muy lento** o con errores
- ❌ Sin optimizaciones
- ❌ Cobertura **<70%**
- ❌ <20 tests
- ❌ Arquitectura desorganizada o monolítica
- ❌ Sin documentación API
- ❌ Persistencia incorrecta o ausente

**Recomendación:** Revisar completamente el proyecto siguiendo esta guía paso a paso.

---

## 🎯 Checklist Final de Entrega

### Antes de entregar, verifica:

#### ✅ **Algoritmo (35 pts)**
- [ ] Tests de `MutantDetectorTest` pasan (17+ tests)
- [ ] Early termination implementado
- [ ] Conversión a `char[][]`
- [ ] Boundary checking
- [ ] Comparaciones directas
- [ ] Sin estructuras auxiliares innecesarias
- [ ] Cobertura de `MutantDetector` >85%

#### ✅ **Arquitectura (25 pts)**
- [ ] 6 carpetas: controller, dto, service, repository, entity, config
- [ ] `@RequiredArgsConstructor` en services/controllers
- [ ] Mínimo 2 DTOs
- [ ] Repository extends `JpaRepository`
- [ ] 3+ anotaciones Lombok
- [ ] `GlobalExceptionHandler` con `@RestControllerAdvice`
- [ ] Custom validator implementado

#### ✅ **Testing (20 pts)**
- [ ] 35+ tests totales
- [ ] Cobertura total >70% (service >90%)
- [ ] Tests de: mutante horizontal, diagonal, humano, validaciones
- [ ] Tests de integración del controller
- [ ] `./gradlew test` pasa sin errores

#### ✅ **API REST (12 pts)**
- [ ] POST /mutant retorna 200 para mutante
- [ ] POST /mutant retorna 403 para humano
- [ ] POST /mutant retorna 400 para inválido
- [ ] GET /stats retorna JSON correcto
- [ ] `SwaggerConfig` configurado
- [ ] Anotaciones `@Tag`, `@Operation`, `@ApiResponse` en controller
- [ ] `@Schema` en DTOs
- [ ] Swagger UI accesible en `/swagger-ui.html`

#### ✅ **Persistencia (8 pts)**
- [ ] Campo `dnaHash` en entity
- [ ] `unique = true` en `dnaHash`
- [ ] Método `calculateDnaHash()` con SHA-256
- [ ] `findByDnaHash()` en repository
- [ ] `countByIsMutant()` en repository
- [ ] 4 campos en entity: id, dnaHash, isMutant, createdAt

---

## 🚀 Comandos Útiles

```bash
# Compilar proyecto
./gradlew build

# Ejecutar tests
./gradlew test

# Generar reporte de cobertura
./gradlew jacocoTestReport
# Ver: build/reports/jacoco/test/html/index.html

# Ejecutar aplicación
./gradlew bootRun
# App: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html

# Limpiar y recompilar
./gradlew clean build

# Tests específicos
./gradlew test --tests MutantDetectorTest
./gradlew test --tests MutantControllerTest

# Tests + cobertura (Windows)
./gradlew test jacocoTestReport --no-daemon
```

---

## 💡 Consejos Finales

### Para obtener >90 puntos:

1. **Prioriza el algoritmo** (35% del total)
   - Implementa TODAS las optimizaciones
   - Mide tiempos reales con benchmarks
   - Asegúrate que `sequenceCount > 1` retorne inmediatamente

2. **Cobertura de tests >90%**
   - Cubre todos los casos edge
   - Tests tanto de éxito como de error
   - Verifica con JaCoCo regularmente

3. **Documenta con Swagger**
   - Facilita la evaluación automática
   - Demuestra profesionalismo
   - Permite testing visual

4. **Usa Lombok consistentemente**
   - Menos código boilerplate
   - Más legible
   - Más mantenible

5. **Hash para deduplicación**
   - Demuestra entendimiento de BD
   - Optimización importante
   - Evita duplicados eficientemente

---

## 📞 Preguntas Frecuentes

**P: ¿Cuánto tiempo tengo para ejecutar en matrices grandes?**
R: Para matriz 1000x1000, el tiempo óptimo es 500ms, aceptable hasta 5000ms.

**P: ¿Qué pasa si mi cobertura es 71% por Lombok?**
R: El evaluador considera esto. Lo importante es que tu capa service tenga >90%.

**P: ¿Es obligatorio usar Lombok?**
R: No es obligatorio, pero obtienes 3 puntos si usas mínimo 3 anotaciones.

**P: ¿Necesito desplegar en Render?**
R: No para la evaluación automática, pero es recomendado para el proyecto completo.

**P: ¿Cómo pruebo mi rendimiento?**
R: Crea tests con `System.nanoTime()` antes/después de llamar a `isMutant()`.

---

## 🎓 Conclusión

Esta guía te proporciona **todos** los criterios de evaluación detallados. Síguelos paso a paso y obtendrás una excelente calificación.

**Recuerda:**
- 70 puntos para aprobar
- 35% del total es el algoritmo (priorízalo)
- Optimizaciones son clave para alto puntaje
- Tests y cobertura demuestran calidad

**¡Éxito en tu proyecto!** 🚀