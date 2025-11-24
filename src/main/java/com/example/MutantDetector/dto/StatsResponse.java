package com.example.MutantDetector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Estadísticas de verificaciones de ADN realizadas")
@Data
@Builder
public class StatsResponse {
    @Schema(description = "Cantidad de ADN mutante verificado", example = "40")
    @JsonProperty("count_mutant_dna")
    private long countMutantDna;
    
    @Schema(description = "Cantidad de ADN humano verificado", example = "100")
    @JsonProperty("count_human_dna")
    private long countHumanDna;
    
    @Schema(description = "Ratio de mutantes sobre humanos", example = "0.4")
    private double ratio;
}
