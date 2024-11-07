package com.example.bicycleinsurance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PremiumRequest {

    @NotEmpty(message = "Bicycles list cannot be empty")
    @Valid
    private List<BicycleDto> bicycles;
}
