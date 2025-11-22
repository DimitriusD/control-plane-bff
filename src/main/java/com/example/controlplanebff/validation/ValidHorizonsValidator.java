package com.example.controlplanebff.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

public class ValidHorizonsValidator implements ConstraintValidator<ValidHorizons, List<String>> {

    private static final Set<String> ALLOWED_HORIZONS = Set.of("5m", "15m", "1h", "1d");

    @Override
    public void initialize(ValidHorizons constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(List<String> horizons, ConstraintValidatorContext context) {
        if (horizons == null || horizons.isEmpty()) {
            return true; // Let @Size handle empty validation
        }
        return horizons.stream().allMatch(ALLOWED_HORIZONS::contains);
    }
}



