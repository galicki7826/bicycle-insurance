package com.example.bicycleinsurance.mapper;

import com.example.bicycleinsurance.dto.BicycleDto;
import com.example.bicycleinsurance.model.Bicycle;
import com.example.bicycleinsurance.model.RiskType;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {RiskType.class})
public interface BicycleMapper {

    @Mapping(target = "risks", expression = "java(mapRisks(dto.getRisks()))")
    Bicycle toModel(BicycleDto dto);

    @Mapping(target = "risks", expression = "java(mapRiskTypes(model.getRisks()))")
    BicycleDto toDto(Bicycle model);

    default List<RiskType> mapRisks(List<String> risks) {
        return risks.stream()
                .map(RiskType::valueOf)
                .collect(Collectors.toList());
    }

    default List<String> mapRiskTypes(List<RiskType> riskTypes) {
        return riskTypes.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
