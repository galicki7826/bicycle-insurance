package com.example.bicycleinsurance.service;

import com.example.bicycleinsurance.dto.BicycleDto;
import com.example.bicycleinsurance.dto.PremiumResponse;
import com.example.bicycleinsurance.exception.CustomValidationException;
import com.example.bicycleinsurance.exception.ScriptExecutionException;
import com.example.bicycleinsurance.mapper.BicycleMapper;
import com.example.bicycleinsurance.model.Bicycle;
import com.example.bicycleinsurance.model.CoverageType;
import com.example.bicycleinsurance.model.RiskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InsuranceServiceTest {

    @Mock
    private BicycleMapper bicycleMapper;

    @Mock
    private GroovyScriptEngine groovyScriptEngine;

    @InjectMocks
    private InsuranceService insuranceService;

    private BicycleDto validBicycleDto;
    private Bicycle validBicycle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validBicycleDto = BicycleDto.builder()
                .make("Pearl")
                .model("Gravel SL EVO")
                .manufactureYear(2015)
                .sumInsured(BigDecimal.valueOf(1000.0))
                .coverage(CoverageType.EXTRA)
                .risks(List.of("THEFT", "DAMAGE", "THIRD_PARTY_DAMAGE"))
                .build();

        validBicycle = Bicycle.builder()
                .make(validBicycleDto.getMake())
                .model(validBicycleDto.getModel())
                .manufactureYear(validBicycleDto.getManufactureYear())
                .sumInsured(validBicycleDto.getSumInsured())
                .coverage(validBicycleDto.getCoverage())
                .risks(List.of(RiskType.THEFT, RiskType.DAMAGE, RiskType.THIRD_PARTY_DAMAGE))
                .build();

        when(bicycleMapper.toModel(validBicycleDto)).thenReturn(validBicycle);
    }

    @Test
    void calculatePremium_ShouldReturnCorrectPremium_WhenValidInput() throws Exception {
        when(groovyScriptEngine.executeScript(anyString(), anyMap())).thenReturn(BigDecimal.valueOf(30.00));

        PremiumResponse response = insuranceService.calculatePremium(List.of(validBicycleDto));

        assertNotNull(response);
        assertEquals(1, response.getObjects().size());
        assertEquals(BigDecimal.valueOf(90.0).setScale(2, RoundingMode.HALF_UP), response.getPremium().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void calculatePremium_ShouldThrowValidationException_WhenBicycleTooOld() {
        BicycleDto anotherBicycleDto = BicycleDto.builder()
                .make("Sensa")
                .model("V2")
                .manufactureYear(2000)
                .sumInsured(BigDecimal.valueOf(500.0))
                .coverage(CoverageType.STANDARD)
                .risks(List.of("DAMAGE"))
                .build();

        Bicycle anotherBicycle = Bicycle.builder()
                .make("Sensa")
                .model("V2")
                .manufactureYear(2000)
                .sumInsured(BigDecimal.valueOf(500.0))
                .coverage(CoverageType.STANDARD)
                .risks(List.of(RiskType.DAMAGE))
                .build();
        when(bicycleMapper.toModel(anotherBicycleDto)).thenReturn(anotherBicycle);

        CustomValidationException exception = assertThrows(CustomValidationException.class, () ->
                insuranceService.calculatePremium(List.of(anotherBicycleDto))
        );

        assertEquals("Bicycle must be newer than 10 years", exception.getMessage());
    }

    @Test
    void calculatePremium_ShouldHandleZeroSumInsured() throws Exception {
        validBicycleDto.setSumInsured(BigDecimal.ZERO);

        when(groovyScriptEngine.executeScript(anyString(), anyMap())).thenReturn(BigDecimal.ZERO);
        PremiumResponse response = insuranceService.calculatePremium(List.of(validBicycleDto));

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), response.getPremium().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void calculatePremium_ShouldThrowScriptExecutionException_WhenScriptFails() throws Exception {
        when(groovyScriptEngine.executeScript(anyString(), anyMap())).thenThrow(new ScriptExecutionException("Script error"));

        ScriptExecutionException exception = assertThrows(ScriptExecutionException.class, () ->
                insuranceService.calculatePremium(List.of(validBicycleDto))
        );

        assertEquals("Script error", exception.getMessage());
    }

    @Test
    void calculatePremium_ShouldReturnCorrectPremium_ForSingleRisk() throws Exception {
        BicycleDto anotherBicycleDto = BicycleDto.builder()
                .make("Sensa")
                .model("V2")
                .manufactureYear(2020)
                .sumInsured(BigDecimal.valueOf(500.0))
                .coverage(CoverageType.STANDARD)
                .risks(List.of("THEFT"))
                .build();

        Bicycle anotherBicycle = Bicycle.builder()
                .make("Sensa")
                .model("V2")
                .manufactureYear(2020)
                .sumInsured(BigDecimal.valueOf(500.0))
                .coverage(CoverageType.STANDARD)
                .risks(List.of(RiskType.THEFT))
                .build();

        when(bicycleMapper.toModel(anotherBicycleDto)).thenReturn(anotherBicycle);
        when(groovyScriptEngine.executeScript(eq("sumInsured/THEFT"), anyMap()))
                .thenReturn(BigDecimal.valueOf(1000.00));
        when(groovyScriptEngine.executeScript(eq("premiums/THEFT"), anyMap()))
                .thenReturn(BigDecimal.valueOf(25.00));


        PremiumResponse response = insuranceService.calculatePremium(List.of(anotherBicycleDto));

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(25.00).setScale(2, RoundingMode.HALF_UP), response.getPremium().setScale(2, RoundingMode.HALF_UP));

        verify(groovyScriptEngine, times(1)).executeScript(eq("sumInsured/THEFT"), anyMap());
        verify(groovyScriptEngine, times(1)).executeScript(eq("premiums/THEFT"), anyMap());
    }

    @Test
    void calculatePremium_ShouldHandleMultipleBicycles() throws Exception {
        BicycleDto firstBicycleDto = BicycleDto.builder()
                .make("Giant")
                .model("Defy Advanced")
                .manufactureYear(2019)
                .sumInsured(BigDecimal.valueOf(800.0))
                .coverage(CoverageType.EXTRA)
                .risks(List.of("THEFT", "DAMAGE"))
                .build();

        Bicycle firstBicycle = Bicycle.builder()
                .make("Giant")
                .model("Defy Advanced")
                .manufactureYear(2019)
                .sumInsured(BigDecimal.valueOf(800.0))
                .coverage(CoverageType.EXTRA)
                .risks(List.of(RiskType.THEFT, RiskType.DAMAGE))
                .build();

        BicycleDto secondBicycleDto = BicycleDto.builder()
                .make("Cannondale")
                .model("Synapse Carbon")
                .manufactureYear(2021)
                .sumInsured(BigDecimal.valueOf(600.0))
                .coverage(CoverageType.STANDARD)
                .risks(List.of("DAMAGE"))
                .build();

        Bicycle secondBicycle = Bicycle.builder()
                .make("Cannondale")
                .model("Synapse Carbon")
                .manufactureYear(2021)
                .sumInsured(BigDecimal.valueOf(600.0))
                .coverage(CoverageType.STANDARD)
                .risks(List.of(RiskType.DAMAGE))
                .build();

        when(bicycleMapper.toModel(firstBicycleDto)).thenReturn(firstBicycle);
        when(bicycleMapper.toModel(secondBicycleDto)).thenReturn(secondBicycle);

        when(groovyScriptEngine.executeScript(eq("sumInsured/THEFT"), anyMap())).thenReturn(BigDecimal.valueOf(800.00));
        when(groovyScriptEngine.executeScript(eq("premiums/THEFT"), anyMap())).thenReturn(BigDecimal.valueOf(24.00));
        when(groovyScriptEngine.executeScript(eq("sumInsured/DAMAGE"), argThat(map -> map.containsValue(firstBicycle)))).thenReturn(BigDecimal.valueOf(800.00));
        when(groovyScriptEngine.executeScript(eq("premiums/DAMAGE"), argThat(map -> map.containsValue(firstBicycle)))).thenReturn(BigDecimal.valueOf(16.00));

        when(groovyScriptEngine.executeScript(eq("sumInsured/DAMAGE"), argThat(map -> map.containsValue(secondBicycle)))).thenReturn(BigDecimal.valueOf(600.00));
        when(groovyScriptEngine.executeScript(eq("premiums/DAMAGE"), argThat(map -> map.containsValue(secondBicycle)))).thenReturn(BigDecimal.valueOf(12.00));

        PremiumResponse response = insuranceService.calculatePremium(List.of(firstBicycleDto, secondBicycleDto));

        assertNotNull(response);
        assertEquals(2, response.getObjects().size());
        assertEquals(BigDecimal.valueOf(52.00).setScale(2, RoundingMode.HALF_UP), response.getPremium().setScale(2, RoundingMode.HALF_UP));

        verify(groovyScriptEngine, times(1)).executeScript(eq("sumInsured/THEFT"), anyMap());
        verify(groovyScriptEngine, times(1)).executeScript(eq("premiums/THEFT"), anyMap());
        verify(groovyScriptEngine, times(1)).executeScript(eq("sumInsured/DAMAGE"), argThat(map -> map.containsValue(firstBicycle)));
        verify(groovyScriptEngine, times(1)).executeScript(eq("premiums/DAMAGE"), argThat(map -> map.containsValue(firstBicycle)));
        verify(groovyScriptEngine, times(1)).executeScript(eq("sumInsured/DAMAGE"), argThat(map -> map.containsValue(secondBicycle)));
        verify(groovyScriptEngine, times(1)).executeScript(eq("premiums/DAMAGE"), argThat(map -> map.containsValue(secondBicycle)));
    }



}
