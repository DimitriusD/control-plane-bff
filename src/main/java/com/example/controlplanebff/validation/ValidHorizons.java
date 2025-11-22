package com.example.controlplanebff.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidHorizonsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHorizons {
    String message() default "enabledHorizons must be subset of [5m,15m,1h,1d]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}



