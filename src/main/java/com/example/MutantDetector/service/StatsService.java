package com.example.MutantDetector.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.MutantDetector.dto.StatsResponse;
import com.example.MutantDetector.repository.DnaRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final DnaRecordRepository dnaRecordRepository;

    @Transactional(readOnly = true)
    public StatsResponse getStats(){
        long countMutantDna = dnaRecordRepository.countByIsMutant(true);
        long countHumanDna = dnaRecordRepository.countByIsMutant(false);
        double ratio = 0.0;

        if(countHumanDna > 0){
            ratio = (double) countMutantDna / countHumanDna;
        } else if (countMutantDna > 0){
            ratio = (double) countMutantDna;
        }
        return StatsResponse.builder().countMutantDna(countMutantDna).countHumanDna(countHumanDna).ratio(ratio).build();
    }

    
}
