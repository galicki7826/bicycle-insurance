package com.example.bicycleinsurance.dto;

import com.example.bicycleinsurance.model.CoverageType;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumResponse {

    private List<ObjectPremium> objects;
    private BigDecimal premium;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectPremium {
        private CoverageType coverageType;
        private BigDecimal sumInsured;
        private BigDecimal premium;
        private List<RiskResponse> risks;
        private Attributes attributes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attributes {
        private String MAKE;
        private String MODEL;
        private String MANUFACTURE_YEAR;
    }
}
