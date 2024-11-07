package com.example.bicycleinsurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskResponse {
    private String riskType;
    private BigDecimal sumInsured;
    private BigDecimal premium;
}
