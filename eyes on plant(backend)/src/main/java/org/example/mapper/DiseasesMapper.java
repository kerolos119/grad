package org.example.mapper;

import org.example.document.Diseases;
import org.example.dto.DiseasesDto;
import org.springframework.stereotype.Component;

@Component
public class DiseasesMapper extends AbstractMapper<DiseasesDto, Diseases> {
    public DiseasesMapper(){
        super(DiseasesDto.class,Diseases.class);
    }


    @Override
    public Diseases updateToEntity(DiseasesDto dto, Diseases entity) {
        return null;
    }
}
