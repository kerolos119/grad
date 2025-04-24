package org.example.mapper;


import org.example.document.Diseases;
import org.example.dto.DiseaseDto;
import org.springframework.stereotype.Component;

@Component
public class DiseasesMapper extends AbstractMapper<DiseaseDto, Diseases> {
    public DiseasesMapper(){
        super(DiseaseDto.class,Diseases.class);
    }


    @Override
    public Diseases updateToEntity(DiseaseDto dto, Diseases entity) {
        return null;
    }
}
