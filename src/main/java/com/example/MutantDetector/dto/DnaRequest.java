package com.example.MutantDetector.dto;

import java.io.Serializable;

import com.example.MutantDetector.validation.ValidDnaSequence;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request para verificar si un ADN es mutante", 
        example = "{\"dna\": [\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest implements Serializable {
    @Schema(description = "Secuencia de ADN representada como matriz NxN. Debe contener solo caracteres A, T, C, G", 
            example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]")
    @ValidDnaSequence(message = "Secuencia de ADN inválida. Debe ser una matriz NxN no vacía y contener solo caracteres 'A', 'T', 'C', 'G'.")
    private String[] dna;    
}
