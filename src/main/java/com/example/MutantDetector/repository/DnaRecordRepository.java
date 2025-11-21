package com.example.MutantDetector.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MutantDetector.model.DnaRecord;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long>{
    Optional<DnaRecord> findByDnaHash(String dnaHash);
    long countByIsMutant(boolean isMutant);
   
}
