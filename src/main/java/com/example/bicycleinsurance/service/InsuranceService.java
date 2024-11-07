package com.example.bicycleinsurance.service;

import com.example.bicycleinsurance.dto.BicycleDto;
import com.example.bicycleinsurance.dto.PremiumResponse;
import com.example.bicycleinsurance.dto.RiskResponse;
import com.example.bicycleinsurance.exception.CustomValidationException;
import com.example.bicycleinsurance.mapper.BicycleMapper;
import com.example.bicycleinsurance.model.Bicycle;
import com.example.bicycleinsurance.model.RiskType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for calculating insurance premiums for bicycles.
 * This service processes a list of BicycleDto objects, converts them to Bicycle models,
 * and calculates premiums and risks using Groovy scripts for each risk type.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceService {

    private final BicycleMapper bicycleMapper;
    private final GroovyScriptEngine groovyScriptEngine;

    /**
     * Calculates the total insurance premium for a list of bicycles.
     *
     * @param bicycleDtos List of BicycleDto objects representing the bicycles to calculate premiums for.
     * @return A PremiumResponse object containing the total premium and a breakdown of premiums for each bicycle.
     * @throws CustomValidationException if any of the bicycles are older than 10 years.
     */
    public PremiumResponse calculatePremium(List<BicycleDto> bicycleDtos) {
        List<Bicycle> bicycles = bicycleDtos.stream()
                .map(bicycleMapper::toModel)
                .collect(Collectors.toList());

        log.info("Starting premium calculation for {} bicycles", bicycles.size());

        List<PremiumResponse.ObjectPremium> objectPremiums = new ArrayList<>();
        BigDecimal totalPremium = BigDecimal.ZERO;

        for (Bicycle bicycle : bicycles) {
            log.debug("Calculating premium for bicycle: {}", bicycle);

            int currentYear = Year.now().getValue();
            int bicycleAge = currentYear - bicycle.getManufactureYear();

            if (bicycleAge > 10) {
                throw new CustomValidationException("Bicycle must be newer than 10 years");
            }

            BigDecimal objectPremium = BigDecimal.ZERO;
            List<RiskResponse> riskResponses = new ArrayList<>();

            for (RiskType riskType : bicycle.getRisks()) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("bicycle", bicycle);
                variables.put("riskType", riskType.name());
                variables.put("bicycleAge", bicycleAge);
                variables.put("sumInsured", bicycle.getSumInsured());
                variables.put("make", bicycle.getMake());
                variables.put("model", bicycle.getModel());
                variables.put("riskCount", bicycle.getRisks().size());

                String sumInsuredScriptName = "sumInsured/" + riskType.name();
                BigDecimal riskSumInsured = new BigDecimal(groovyScriptEngine.executeScript(sumInsuredScriptName, variables).toString());
                variables.put("riskSumInsured", riskSumInsured);

                String premiumScriptName = "premiums/" + riskType.name();
                BigDecimal riskPremium = new BigDecimal(groovyScriptEngine.executeScript(premiumScriptName, variables).toString());

                objectPremium = objectPremium.add(riskPremium);

                riskResponses.add(RiskResponse.builder()
                        .riskType(riskType.name())
                        .sumInsured(riskSumInsured.setScale(2, RoundingMode.HALF_UP))
                        .premium(riskPremium.setScale(2, RoundingMode.HALF_UP))
                        .build());
            }

            totalPremium = totalPremium.add(objectPremium);

            PremiumResponse.Attributes attributes = PremiumResponse.Attributes.builder()
                    .MAKE(bicycle.getMake())
                    .MODEL(bicycle.getModel())
                    .MANUFACTURE_YEAR(String.valueOf(bicycle.getManufactureYear()))
                    .build();

            PremiumResponse.ObjectPremium objectPremiumResponse = PremiumResponse.ObjectPremium.builder()
                    .attributes(attributes)
                    .coverageType(bicycle.getCoverage())
                    .sumInsured(bicycle.getSumInsured())
                    .premium(objectPremium.setScale(2, RoundingMode.HALF_UP))
                    .risks(riskResponses)
                    .build();

            objectPremiums.add(objectPremiumResponse);
        }

        return PremiumResponse.builder()
                .objects(objectPremiums)
                .premium(totalPremium.setScale(2, RoundingMode.HALF_UP))
                .build();
    }

}