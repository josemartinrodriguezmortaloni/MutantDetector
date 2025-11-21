package com.example.MutantDetector.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.MutantDetector.dto.DnaRequest;
import com.example.MutantDetector.dto.StatsResponse;
import com.example.MutantDetector.service.MutantService;
import com.example.MutantDetector.service.StatsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para la detección de mutantes basada en el análisis de ADN")
public class MutantController {
    private final MutantService mutantService;
    private final StatsService statsService;

    @Operation(summary = "Detectar si un humano es mutante", description = "Analiza una secuencia de ADN para determinar si pertenece a un mutante. Retorna 200 OK si es mutante, 403 Forbidden si es humano.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es Mutante", content = @Content),
            @ApiResponse(responseCode = "403", description = "Es Humano (No Mutante)", content = @Content),
            @ApiResponse(responseCode = "400", description = "ADN Inválido (No cuadrado, caracteres erróneos, null)", content = @Content)
    })
    @PostMapping("/mutant")
    public ResponseEntity<Void> detectMutant(@Valid @RequestBody DnaRequest dnaRequest){
        boolean isMutant = mutantService.analyzeDna(dnaRequest.getDna());
        if(isMutant){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Obtener estadísticas", description = "Retorna estadísticas de las verificaciones de ADN realizadas: conteo de mutantes, humanos y ratio.")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatsResponse.class)))
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
    
}
