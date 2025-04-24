package org.example.mapper;

import org.example.document.Disease;
import org.example.dto.DiseaseDto;
import org.springframework.stereotype.Component;

@Component
public class DiseasesMapper extends AbstractMapper<DiseaseDto, Disease> {
    public DiseasesMapper(){
        super(DiseaseDto.class,Disease.class);
    }


    @Override
    public Disease updateToEntity(DiseaseDto dto, Disease entity) {
        return null;
    }
}
