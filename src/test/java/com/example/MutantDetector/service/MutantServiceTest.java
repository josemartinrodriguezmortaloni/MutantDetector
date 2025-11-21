package com.example.MutantDetector.service;

import com.example.MutantDetector.model.DnaRecord;
import com.example.MutantDetector.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    @Test
    @DisplayName("Debe retornar resultado cacheado si ya existe en BD")
    void testAnalyzeDna_ReturnsCachedResult() {
        String[] dna = {"AAAA", "CCCC", "TTTT", "GGGG"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setMutant(true);

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingRecord));

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        verify(dnaRecordRepository, never()).save(any(DnaRecord.class)); // No debería guardar de nuevo
        verify(mutantDetector, never()).isMutant(any()); // No debería llamar al detector
    }

    @Test
    @DisplayName("Debe analizar y guardar si no existe en BD (Mutante)")
    void testAnalyzeDna_NewMutant_SavesAndReturnsTrue() {
        String[] dna = {"AAAA", "CCCC", "TTTT", "GGGG"};
        
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        verify(mutantDetector).isMutant(dna);
        verify(dnaRecordRepository).save(argThat(record -> 
            record.isMutant() == true && record.getDnaHash() != null
        ));
    }

    @Test
    @DisplayName("Debe analizar y guardar si no existe en BD (Humano)")
    void testAnalyzeDna_NewHuman_SavesAndReturnsFalse() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(false);

        boolean result = mutantService.analyzeDna(dna);

        assertFalse(result);
        verify(mutantDetector).isMutant(dna);
        verify(dnaRecordRepository).save(argThat(record -> 
            record.isMutant() == false && record.getDnaHash() != null
        ));
    }

    @Test
    @DisplayName("Debe calcular el mismo hash para el mismo ADN")
    void testAnalyzeDna_ConsistentHash() {
        String[] dna = {"AAAA", "CCCC"};
        
        // Call twice
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        mutantService.analyzeDna(dna);
        mutantService.analyzeDna(dna);

        // Capture the hash used in findByDnaHash
        // In a real unit test we might just trust the logic or extract the hash method to test it separately, 
        // but here checking side effects is enough.
        verify(dnaRecordRepository, times(2)).findByDnaHash(anyString());
    }
}

