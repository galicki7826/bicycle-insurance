package com.example.bicycleinsurance.controller;

import com.example.bicycleinsurance.dto.BicycleDto;
import com.example.bicycleinsurance.dto.PremiumRequest;
import com.example.bicycleinsurance.dto.PremiumResponse;
import com.example.bicycleinsurance.service.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @Operation(summary = "Calculate premiums for bicycle insurance policies",
            description = "Given a list of bicycles, calculates the total premium associated with each bicycle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated premiums and risks",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PremiumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error occurred",
                    content = @Content)
    })
    @PostMapping("/calculate")
    @ResponseStatus(HttpStatus.OK)
    public PremiumResponse calculatePremium(
            @RequestBody @Valid PremiumRequest request) {
        List<BicycleDto> bicycles = request.getBicycles();
        return insuranceService.calculatePremium(bicycles);
    }
}
