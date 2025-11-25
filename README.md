# 🧬 Mutant Detector

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Coverage](https://img.shields.io/badge/Coverage-%3E80%25-success)

## 📋 Descripción del Proyecto

Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Men. Este proyecto implementa una API REST para detectar si un humano es mutante basándose en su secuencia de ADN.

El programa cumple con los 3 niveles del desafío:

1. **Algoritmo de detección**: Lógica eficiente para identificar secuencias de ADN.
2. **API REST**: Servicio hosteado para recibir detecciones.
3. **Base de Datos y Estadísticas**: Persistencia de verificaciones y exposición de estadísticas.

---

## 🚀 Características Técnicas

### Algoritmo Optimizado

El detector de mutantes ha sido diseñado con eficiencia en mente, implementando:

- **Early Termination**: Detiene la búsqueda en cuanto encuentra más de una secuencia.
- **Single Pass**: Recorre la matriz de forma eficiente.
- **Complejidad Espacial O(1)**: Sin uso excesivo de memoria auxiliar.

### Arquitectura Hexagonal / N-Capas

El proyecto sigue una arquitectura limpia dividida en capas:

- **Controller**: Manejo de peticiones HTTP.
- **Service**: Lógica de negocio y orquestación.
- **Repository**: Acceso a datos.
- **Model/Entity**: Entidades de persistencia.
- **DTO**: Objetos de transferencia de datos.
- **Config**: Configuraciones transversales (Swagger, etc).

### Tecnologías Utilizadas

- **Java 17+**
- **Spring Boot 3**
- **H2 Database** (Base de datos en memoria para persistencia)
- **Gradle** (Gestor de dependencias)
- **Lombok** (Reducción de boilerplate)
- **JUnit 5 & Mockito** (Testing)
- **JaCoCo** (Reportes de cobertura)
- **Swagger / OpenAPI** (Documentación de API)

---

## 🛠️ Instrucciones de Ejecución

### Prerrequisitos

- JDK 17 o superior
- Gradle (o usar el wrapper incluido `./gradlew`)

### Comandos Principales

**1. Compilar el proyecto:**

```bash
./gradlew build
```

**2. Ejecutar la aplicación:**

```bash
./gradlew bootRun
```

La aplicación iniciará en `http://localhost:8081`.

**3. Ejecutar Tests:**

```bash
./gradlew test
```

**4. Generar Reporte de Cobertura (JaCoCo):**

```bash
./gradlew jacocoTestReport
```

El reporte estará disponible en: `build/reports/jacoco/test/html/index.html`

---

## 🌐 API Reference

Una vez iniciada la aplicación, puedes consultar la documentación interactiva y probar el endpoint de detección directamente en Swagger UI:

👉 **[http://localhost:8081/swagger-ui/index.html#/Mutant%20Detector/detectMutant](http://localhost:8081/swagger-ui/index.html#/Mutant%20Detector/detectMutant)**

O también por:

👉 **[https://mutantdetector-ghb4.onrender.com/swagger-ui/index.html#/Mutant%20Detector/detectMutant](https://mutantdetector-ghb4.onrender.com/swagger-ui/index.html#/Mutant%20Detector/detectMutant)**

### Endpoints Principales

#### 1. Detectar Mutante

`POST /mutant`

Envía una secuencia de ADN para verificar si pertenece a un mutante.

**Body:**

```json
{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    ]
}
```

**Respuestas:**

- `200 OK`: Es un mutante.
- `403 Forbidden`: Es un humano.
- `400 Bad Request`: Datos de entrada inválidos (matriz no cuadrada, caracteres erróneos).

#### 2. Obtener Estadísticas

`GET /stats`

Devuelve estadísticas de las verificaciones de ADN realizadas.

**Respuesta:**

```json
{
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
}
```

---

## 🧪 Testing y Calidad

El proyecto cuenta con una suite de pruebas exhaustiva que cubre:

- Tests Unitarios para el algoritmo (`MutantDetector`).
- Tests de Integración para los controladores (`MutantController`).
- Validaciones de casos borde (matrices vacías, nulas, caracteres inválidos).

El objetivo de cobertura de código es **superior al 80%**.

---

## ☁️ Despliegue

La API está preparada para ser desplegada en servicios cloud como Render.
URL de producción: `[URL Render](https://mutantdetector-ghb4.onrender.com)`

---
*Desarrollado por José Martín Rodriguez Mortaloni.*
