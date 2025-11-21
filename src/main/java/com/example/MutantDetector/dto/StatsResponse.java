package com.example.MutantDetector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsResponse {
    @JsonProperty("count_mutant_dna")
    private long countMutantDna;
    @JsonProperty("count_human_dna")
    private long countHumanDna;
    
    private double ratio;
}
