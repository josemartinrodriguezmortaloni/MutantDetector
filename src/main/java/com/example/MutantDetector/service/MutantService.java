package com.example.MutantDetector.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.MutantDetector.exception.DnaHashCalculationException;
import com.example.MutantDetector.model.DnaRecord;
import com.example.MutantDetector.repository.DnaRecordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MutantService {
    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    @Transactional
    public boolean analyzeDna(String[] dna) {
        String dnaHash = calculateDnaHash(dna);

        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);
        if(existingRecord.isPresent()){
            log.info("ADN ya analizado encontrado en DB. Hash: {}", dnaHash);
            return existingRecord.get().isMutant();
        }
        boolean isMutant = mutantDetector.isMutant(dna);

        DnaRecord newRecord = Objects.requireNonNull(
            DnaRecord.builder().dnaHash(dnaHash).isMutant(isMutant).build(),
            "Error construyendo DnaRecord"
        );
        DnaRecord savedRecord = dnaRecordRepository.save(newRecord);
        
        log.info("Nuevo análisis guardado. Hash: {}, Es mutante: {}", savedRecord.getDnaHash(), savedRecord.isMutant());
        return isMutant;
    }
    private String calculateDnaHash(String[] dna){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            StringBuilder sb = new StringBuilder();
            for (String s : dna) {
                sb.append(s);
            }
            byte[] encodedHash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }catch (NoSuchAlgorithmException e){
            log.error("Error calculando hash SHA-256", e);
            throw new DnaHashCalculationException("Error interno calculando hash de ADN", e);
        }
    }
}
