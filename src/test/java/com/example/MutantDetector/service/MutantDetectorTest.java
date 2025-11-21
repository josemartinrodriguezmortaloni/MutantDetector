package com.example.MutantDetector.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontales")
    void testMutantWithHorizontalSequences() {
        String[] dna = {
            "AAAAAA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantWithVerticalSequences() {
        String[] dna = {
            "ATGCGA",
            "ATGTGC",
            "ATATGT",
            "AGAAGG",
            "ACTCTA",
            "TCACTG"
        };
        // Col 0 has AAAA starting at row 0
        // Need another sequence. Col 0 has AAAAAA. That counts as 1? 
        // The requirement is "more than one sequence".
        // Let's make sure we have 2 distinct sequences.
        // Vertical A at (0,0) -> (3,0)
        // Vertical G at (0,2) is G, G, A, A... no.
        // Let's allow overlaps or distinct? 
        // Usually distinct sequences. "AAAA" and "CCCC".
        
        String[] dna2 = {
            "ATGCGA",
            "ATGTGC",
            "ATATGT",
            "AGATGT", // A at col 0 (0-3), T at col 4 (0-3) ? No, let's check.
            "CCCCTA",
            "TCACTG"
        };
        // Let's construct a clear case.
        // Vertical A at col 0, rows 0-3.
        // Vertical G at col 5, rows 2-5.
        String[] dnaClear = {
            "A....G", // row 0
            "A....G", // row 1
            "A....G", // row 2
            "A....G", // row 3
            ".....G", // row 4
            ".....G"  // row 5
        };
        // Filling with valid chars
        String[] dnaFinal = {
            "ATGCGG",
            "ATGTGG",
            "ATATGG",
            "AGATGG",
            "CCCCTG",
            "TCACTG"
        };
        // Col 0: AAAA (rows 0-3)
        // Col 5: GGGG (rows 0-3)
        assertTrue(mutantDetector.isMutant(dnaFinal));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias diagonales")
    void testMutantWithDiagonalSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        // Diagonal descending: (0,0)A -> (1,1)A -> (2,2)A -> (3,3)A
        // Horizontal: (4,0)CCCC
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal ascendente")
    void testMutantWithDiagonalAscending() {
        String[] dna = {
            "ATGCGT", // 
            "CAGTGC", //      G(1,5)
            "TTATGT", //    G(2,4)
            "AGAAGG", //  G(3,3)
            "CCCCTG", //G(4,2) -> Sequence GGGG
            "TCACTG"
        };
        // Let's ensure we have 2 sequences.
        // 1. Diagonal Ascending GGGG from (4,2) to (1,5)
        // 2. Horizontal CCCC at (4,0)
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar mutante con solo una secuencia")
    void testHumanWithOneSequence() {
        String[] dna = {
            "AAAAAA", // 1 sequence
            "CAGTGC",
            "TTATGT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar mutante sin secuencias")
    void testHumanWithNoSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para ADN nulo")
    void testNullDna() {
        assertFalse(mutantDetector.isMutant(null));
    }

    @Test
    @DisplayName("Debe retornar false para ADN vacío")
    void testEmptyDna() {
        assertFalse(mutantDetector.isMutant(new String[]{}));
    }

    @Test
    @DisplayName("Debe retornar false para matriz NxM (no cuadrada)")
    void testNonSquareDna() {
        String[] dna = {
            "ATGC",
            "CAGT",
            "TTAT"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para caracteres inválidos")
    void testInvalidCharacters() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTX", // X is invalid
            "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz grande 10x10")
    void testLargeMatrix() {
        String[] dna = {
            "ATGCGAATGC",
            "CAGTGCAGTG",
            "TTATGTATGT",
            "AGAAGGAGAA",
            "CCCCTACCCC", // CCCC x 2
            "TCACTGTCACT",
            "ATGCGAATGC",
            "CAGTGCAGTG",
            "TTATGTATGT",
            "AGAAGGAGAA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }
    
    @Test
    @DisplayName("Debe manejar correctamente filas nulas")
    void testNullRow() {
         String[] dna = {
            "ATGC",
            null,
            "TTAT",
            "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }
}

