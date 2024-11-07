package com.example.bicycleinsurance.dto;

import com.example.bicycleinsurance.model.CoverageType;
import com.example.bicycleinsurance.validation.ValidRiskTypes;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BicycleDto {

    @NotBlank(message = "Make is mandatory")
    private String make;

    @NotBlank(message = "Model is mandatory")
    private String model;

    @NotNull(message = "Manufacture year is mandatory")
    @Min(value = 1900, message = "Manufacture year cannot be before 1900")
    @Max(value = 2100, message = "Manufacture year cannot be after 2100")
    private Integer manufactureYear;

    @NotNull(message = "Sum insured is mandatory")
    @Positive(message = "Sum insured must be positive")
    @DecimalMax(value = "10000", message = "Sum insured must be less than 10,000")
    private BigDecimal sumInsured;

    @NotNull(message = "Coverage is mandatory")
    private CoverageType coverage;

    @NotEmpty(message = "Risks cannot be empty")
    @ValidRiskTypes
    private List<@NotBlank(message = "Risk type cannot be blank") String> risks;
}
