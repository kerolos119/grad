package org.example.mapper;

import org.example.document.Plants;
import org.example.dto.PlantDto;
import org.springframework.stereotype.Component;

@Component
public class PlantMapper extends AbstractMapper<PlantDto, Plants> {
    public PlantMapper() {
        super(PlantDto.class,Plants.class);
    }

    @Override
    public Plants updateToEntity(PlantDto dto, Plants entity) {
        entity.setPlantName(dto.getPlantName());
        entity.setType(dto.getType());
        entity.setCommonDiseases(dto.getCommonDiseases());
        entity.setGrowthTime(dto.getGrowthTime());
        entity.setPlantStage(dto.getPlantStage());
        entity.setOptimalConditions(dto.getOptimalConditions());
        return entity;
    }
}
