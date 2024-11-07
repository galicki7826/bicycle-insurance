package com.example.bicycleinsurance.validation;

import com.example.bicycleinsurance.model.RiskType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

public class RiskTypeValidator implements ConstraintValidator<ValidRiskTypes, List<String>> {

    private static final Set<String> VALID_RISK_TYPES = Set.of(
            RiskType.THEFT.name(),
            RiskType.DAMAGE.name(),
            RiskType.THIRD_PARTY_DAMAGE.name()
    );

    @Override
    public boolean isValid(List<String> risks, ConstraintValidatorContext context) {
        return risks != null && !risks.isEmpty() && VALID_RISK_TYPES.containsAll(risks);
    }
}
