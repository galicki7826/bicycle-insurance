package com.example.bicycleinsurance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bicycle {
    private String make;
    private String model;
    private Integer manufactureYear;
    private BigDecimal sumInsured;
    private CoverageType coverage;
    private List<RiskType> risks;
}
