package com.example.bicycleinsurance.integration;

import com.example.bicycleinsurance.controller.InsuranceController;
import com.example.bicycleinsurance.dto.PremiumRequest;
import com.example.bicycleinsurance.dto.PremiumResponse;
import com.example.bicycleinsurance.dto.RiskResponse;
import com.example.bicycleinsurance.exception.CustomValidationException;
import com.example.bicycleinsurance.model.CoverageType;
import com.example.bicycleinsurance.service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InsuranceController.class)
class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceService insuranceService;

    /**
     * Handler wyjątków dla testów, który zwraca konkretną implementację Map.
     */
    @RestControllerAdvice
    static class TestGlobalExceptionHandler {
        @ExceptionHandler(CustomValidationException.class)
        public ResponseEntity<Map<String, String>> handleCustomValidationException(CustomValidationException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Test
    void testCalculatePremium_SuccessfulResponse() throws Exception {
        PremiumResponse.Attributes attributes = PremiumResponse.Attributes.builder()
                .MAKE("Pearl")
                .MODEL("Gravel SL EVO")
                .MANUFACTURE_YEAR("2015")
                .build();

        RiskResponse riskResponse = RiskResponse.builder()
                .riskType("THEFT")
                .sumInsured(BigDecimal.valueOf(1000))
                .premium(BigDecimal.valueOf(30))
                .build();

        PremiumResponse.ObjectPremium objectPremium = PremiumResponse.ObjectPremium.builder()
                .attributes(attributes)
                .coverageType(CoverageType.EXTRA)
                .sumInsured(BigDecimal.valueOf(1000))
                .premium(BigDecimal.valueOf(48.95))
                .risks(Collections.singletonList(riskResponse))
                .build();

        PremiumResponse premiumResponse = PremiumResponse.builder()
                .objects(Collections.singletonList(objectPremium))
                .premium(BigDecimal.valueOf(48.95))
                .build();

        Mockito.when(insuranceService.calculatePremium(anyList())).thenReturn(premiumResponse);

        String requestBody = """
            {
              "bicycles": [
                {
                  "make": "Pearl",
                  "model": "Gravel SL EVO",
                  "coverage": "EXTRA",
                  "manufactureYear": 2015,
                  "sumInsured": 1000,
                  "risks": ["THEFT"]
                }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.premium").value(48.95))
                .andExpect(jsonPath("$.objects[0].premium").value(48.95))
                .andExpect(jsonPath("$.objects[0].attributes.make").value("Pearl"))
                .andExpect(jsonPath("$.objects[0].attributes.model").value("Gravel SL EVO"))
                .andExpect(jsonPath("$.objects[0].attributes.manufacture_YEAR").value("2015"))
                .andExpect(jsonPath("$.objects[0].risks[0].riskType").value("THEFT"));
    }


    @Test
    void testCalculatePremium_InvalidManufactureYear() throws Exception {
        Mockito.doThrow(new CustomValidationException("Manufacture year cannot be before 1900"))
                .when(insuranceService).calculatePremium(anyList());

        String requestBody = """
            {
              "bicycles": [
                {
                  "make": "Pearl",
                  "model": "Gravel SL EVO",
                  "coverage": "EXTRA",
                  "manufactureYear": 1800,
                  "sumInsured": 1000,
                  "risks": ["THEFT"]
                }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Manufacture year cannot be before 1900"));
    }

    @Test
    void testCalculatePremium_MissingMandatoryFields() throws Exception {
        Mockito.doThrow(new CustomValidationException("Make is mandatory"))
                .when(insuranceService).calculatePremium(anyList());

        String requestBody = """
            {
              "bicycles": [
                {
                  "model": "Gravel SL EVO",
                  "coverage": "EXTRA",
                  "manufactureYear": 2015,
                  "sumInsured": 1000
                }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Make is mandatory"));
    }

    @Test
    void testCalculatePremium_SumInsuredExceedsLimit() throws Exception {
        Mockito.doThrow(new CustomValidationException("Sum insured must be less than 10,000"))
                .when(insuranceService).calculatePremium(anyList());

        String requestBody = """
            {
              "bicycles": [
                {
                  "make": "Pearl",
                  "model": "Gravel SL EVO",
                  "coverage": "EXTRA",
                  "manufactureYear": 2015,
                  "sumInsured": 15000,
                  "risks": ["THEFT"]
                }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sum insured must be less than 10,000"));
    }

    @Test
    void testCalculatePremium_InvalidRiskType() throws Exception {
        Mockito.doThrow(new CustomValidationException("Invalid risk types provided"))
                .when(insuranceService).calculatePremium(anyList());

        String requestBody = """
            {
              "bicycles": [
                {
                  "make": "Pearl",
                  "model": "Gravel SL EVO",
                  "coverage": "EXTRA",
                  "manufactureYear": 2015,
                  "sumInsured": 1000,
                  "risks": ["INVALID_RISK"]
                }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid risk types provided"));
    }

    @Test
    void testCalculatePremium_EmptyBicyclesList() throws Exception {
        Mockito.doThrow(new CustomValidationException("Bicycles list cannot be empty"))
                .when(insuranceService).calculatePremium(anyList());

        String requestBody = """
            {
              "bicycles": []
            }
            """;

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bicycles list cannot be empty"));
    }

    @Test
    void testCalculatePremium_AgeLimitExceeded() throws Exception {
        Mockito.doThrow(new CustomValidationException("Bicycle must be newer than 10 years"))
                .when(insuranceService).calculatePremium(anyList());

        String requestBody = """
            {
              "bicycles": [
                {
                  "make": "Pearl",
                  "model": "Gravel SL EVO",
                  "coverage": "EXTRA",
                  "manufactureYear": 2000,
                  "sumInsured": 1000,
                  "risks": ["THEFT"]
                }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bicycle must be newer than 10 years"));
    }
}
