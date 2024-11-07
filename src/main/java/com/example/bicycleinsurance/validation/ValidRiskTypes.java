package com.example.bicycleinsurance.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = RiskTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRiskTypes {
    String message() default "Invalid risk types provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
