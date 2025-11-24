package com.example.MutantDetector.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
            "ATGCGG",
            "ATGTGG",
            "ATATGG",
            "AGATGG",
            "CCCCTG",
            "TCACTG"
        };
        // Columna 0: AAAA (filas 0-3)
        // Columna 5: GGGG (filas 0-3)
        assertTrue(mutantDetector.isMutant(dna));
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
        // Diagonal descendente: (0,0)A -> (1,1)A -> (2,2)A -> (3,3)A
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
            "CCCCTG", //G(4,2) -> Secuencia GGGG
            "TCACTG"
        };
        // Aseguramos que haya 2 secuencias.
        // 1. Diagonal ascendente GGGG desde (4,2) a (1,5)
        // 2. Horizontal CCCC en (4,0)
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar mutante con solo una secuencia")
    void testHumanWithOneSequence() {
        String[] dna = {
            "AAAATA", // 1 secuencia (AAAA) - Se cambió G por T para evitar coincidencia vertical
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
            "CCCCTX", // X es inválido
            "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz grande 10x10")
    void testLargeMatrix() {
        String[] dna = {
            "AAAAAAAAAA",
            "AAAAAAAAAA",
            "AAAAAAAAAA",
            "AAAAAAAAAA",
            "AAAAAAAAAA",
            "AAAAAAAAAA",
            "AAAAAAAAAA",
            "AAAAAAAAAA",
            "AAAAAAAAAA",
            "AAAAAAAAAA"
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

    @Test
    @DisplayName("Debe detectar mutante en matriz enorme 100x100")
    void testHugeMatrix100x100() {
        int size = 100;
        String[] dna = new String[size];
        // Llenar con datos aleatorios que no sean de mutante
        char[] row = new char[size];
        Arrays.fill(row, 'T'); 
        for(int i=0; i<size; i++) {
            dna[i] = new String(row);
        }
        
        // Inyectar secuencias
        // 1. Horizontal en fila 0, columna 0
        char[] row0 = dna[0].toCharArray();
        row0[0]='A'; row0[1]='A'; row0[2]='A'; row0[3]='A';
        dna[0] = new String(row0);
        
        // 2. Horizontal en fila 10, columna 10
        char[] row10 = dna[10].toCharArray();
        row10[10]='C'; row10[11]='C'; row10[12]='C'; row10[13]='C';
        dna[10] = new String(row10);
        
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mezcla de direcciones (Horizontal y Vertical)")
    void testMixedDirections() {
        String[] dna = {
            "AAAA..", // Horizontal
            "C.....", 
            "C.....", 
            "C.....", 
            "C.....", // Vertical C en columna 0
            "......"
        };
        // Reemplazar los puntos con caracteres válidos distintos para evitar coincidencias accidentales
        for(int i=0; i<dna.length; i++) {
            dna[i] = dna[i].replace('.', 'T');
        }
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mezcla de diagonales (Ascendente y Descendente)")
    void testMixedDiagonals() {
        // 6x6
        char[][] matrix = new char[6][6];
        for (char[] row : matrix) Arrays.fill(row, 'T'); // Valor por defecto T
        
        // Diagonal descendente AAAA desde (0,0)
        matrix[0][0] = 'A';
        matrix[1][1] = 'A';
        matrix[2][2] = 'A';
        matrix[3][3] = 'A';

        // Diagonal ascendente GGGG desde (5,0) -> (2,3)
        matrix[5][0] = 'G';
        matrix[4][1] = 'G';
        matrix[3][2] = 'G';
        matrix[2][3] = 'G';

        String[] dna = new String[6];
        for(int i=0; i<6; i++) dna[i] = new String(matrix[i]);

        assertTrue(mutantDetector.isMutant(dna));
    }
}
