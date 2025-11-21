package com.example.MutantDetector.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidDnaSequenceValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDnaSequence {
    String message() default "Invalid DNA sequence";

    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};
    
}
