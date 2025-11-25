package com.example.MutantDetector.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.example.MutantDetector.dto.DnaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK cuando es mutante")
    void testDetectMutant_ReturnsOk() throws Exception {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden cuando es humano")
    void testDetectMutant_ReturnsForbidden() throws Exception {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request cuando el ADN es inválido")
    void testDetectMutant_ReturnsBadRequest_InvalidDna() throws Exception {
        String[] dna = {"ATGC", "CAG"}; // Invalid size
        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /stats debe retornar estadísticas")
    void testGetStats_ReturnsStats() throws Exception {
        // First, add some data
        String[] mutantDna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String[] humanDna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DnaRequest(mutantDna))));
        
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DnaRequest(humanDna))));

        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").exists())
                .andExpect(jsonPath("$.count_human_dna").exists())
                .andExpect(jsonPath("$.ratio").exists());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 para caracteres inválidos")
    void testDetectMutant_ReturnsBadRequest_InvalidCharacters() throws Exception {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTX", "TCACTG"};
        DnaRequest request = new DnaRequest(dna);
        
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 para matriz no cuadrada")
    void testDetectMutant_ReturnsBadRequest_NonSquare() throws Exception {
        String[] dna = {"ATGC", "CAG"};
        DnaRequest request = new DnaRequest(dna);
        
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Integración completa: POST /mutant debe guardar resultado en BD y actualizar stats")
    @Sql(statements = "DELETE FROM dna_records", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testIntegration_MutantSavesToDatabase() throws Exception {
        String[] mutantDna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRequest request = new DnaRequest(mutantDna);
        
        // Primera llamada - debe guardar
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        // Verificar que se guardó en BD consultando stats
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(1))
                .andExpect(jsonPath("$.count_human_dna").value(0));
        
        // Segunda llamada con mismo ADN - debe usar cache (no duplicar)
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        // Verificar que NO se duplicó
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(1))  // Sigue siendo 1
                .andExpect(jsonPath("$.count_human_dna").value(0));
    }

    @Test
    @DisplayName("Integración: Deduplicación por hash y cálculo correcto de estadísticas")
    void testIntegration_DeduplicationAndStats() throws Exception {
        String[] mutantDna1 = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String[] mutantDna2 = {"AAAA", "CCCC", "TTTT", "GGGG"};
        String[] humanDna1 = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        String[] humanDna2 = {"ATGC", "CAGT", "TTAT", "AGAC"};
        
        // Enviar múltiples ADNs
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DnaRequest(mutantDna1))));
        
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DnaRequest(mutantDna2))));
        
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DnaRequest(humanDna1))));
        
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DnaRequest(humanDna2))));
        
        // Intentar duplicar el primer mutante
        mockMvc.perform(post("/api/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DnaRequest(mutantDna1))));
        
        // Verificar estadísticas finales
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(2))  // Solo 2 mutantes únicos
                .andExpect(jsonPath("$.count_human_dna").value(2))   // 2 humanos únicos
                .andExpect(jsonPath("$.ratio").value(1.0));           // Ratio = 2/2 = 1.0
    }
}
