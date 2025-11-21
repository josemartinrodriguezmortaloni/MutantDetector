package com.example.MutantDetector.dto;

import java.io.Serializable;

import com.example.MutantDetector.validation.ValidDnaSequence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest implements Serializable {
    @ValidDnaSequence(message = "Secuencia de ADN inválida. Debe ser una matriz NxN no vacía y contener solo caracteres 'A', 'T', 'C', 'G'.")
    private String[] dna;    
}
