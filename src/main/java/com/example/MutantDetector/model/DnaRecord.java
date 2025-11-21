package com.example.MutantDetector.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="dna_records", indexes = {
    @Index(name = "idx_dna_hash", columnList = "dnaHash", unique = true),
    @Index(name = "idx_is_mutant", columnList = "isMutant")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DnaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String dnaHash;

    private boolean isMutant;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
}
