package com.example.MutantDetector.validation;

import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDnaSequenceValidator  implements ConstraintValidator<ValidDnaSequence, String[]>{
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');
    @Override
    public void initialize(ValidDnaSequence constraintAnnotation){}
    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if(dna == null){
            return false;
        }

        int n = dna.length;
        if( n == 0){
            return false;
        }

        for (String row : dna) {
            if(row == null) {
                return false;
            }

            if(row.length() != n) {
                return false;
            }
            for(char c : row.toCharArray()){
                if(!isValidCharacter(c)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidCharacter(char c) {
        return VALID_BASES.contains(c);
    }
    
}
