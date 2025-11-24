package com.example.MutantDetector.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MutantDetector {
    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_SEQUENCE_LIMIT = 1;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');
    
    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length == 0) {
            log.warn("ADN nulo o vacío recibido");
            return false;
        }

        int n = dna.length;
        char[][] matrix = new char[n][n];

        for(int i = 0; i < n; i++){
            String row = dna[i];
            if (row == null || row.length() != n){
                log.warn("Fila {} inválida: logitud incorrecta o nula", i);
                return false;
            }
            matrix[i] = row.toCharArray();

            for (char c : matrix[i]){
                if (!isValidDnaCharacter(c)) {
                    log.warn("Caracter inválido encontrado: {}", c);
                    return false;
                }
            }
        }
        int sequenceCount = 0;

        for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++){
                if (sequenceCount > MUTANT_SEQUENCE_LIMIT) {
                    log.debug("Mutante detectado (Early termination)");
                    return true;
                }
                // Horizontal
                if (col <= n - SEQUENCE_LENGTH){
                    if (checkHorizontal(matrix, row, col)){
                        sequenceCount ++;
                    }
                }
                // Vertical
                if (row <= n - SEQUENCE_LENGTH){
                    if (checkVertical(matrix, row, col)){
                        sequenceCount ++;
                    }
                }
                // Diagonal Descendente
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH){
                    if (checkDiagonalDescending(matrix, row, col)){
                        sequenceCount ++;
                    }
                }
                // Diagonal Ascendente
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH){
                    if (checkDiagonalAscending(matrix, row, col)){
                        sequenceCount ++;
                    }
                }
            }
        }
        boolean isMutant = sequenceCount > MUTANT_SEQUENCE_LIMIT;
        log.info("Análisis completado. Secuencias encontradas: {}. Es mutante: {}", sequenceCount, isMutant);
        return isMutant;
    }
    private boolean isValidDnaCharacter(char c){
        return VALID_BASES.contains(c);
    }
    
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
               matrix[row][col + 2] == base &&
               matrix[row][col + 3] == base;
    }
    private boolean checkVertical(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
               matrix[row + 2][col] == base &&
               matrix[row + 3][col] == base;
    }
    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
               matrix[row + 2][col + 2] == base &&
               matrix[row + 3][col + 3] == base;
    }
    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
               matrix[row - 2][col + 2] == base &&
               matrix[row - 3][col + 3] == base;
    }
}
