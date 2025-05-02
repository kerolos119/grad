package org.example.mapper;

import org.example.document.Plants;
import org.example.document.Users;
import org.example.dto.PlantDto;
import org.example.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlantMapper extends AbstractMapper<PlantDto, Plants> {
    
    private final UserRepository userRepository;
    
    @Autowired
    public PlantMapper(UserRepository userRepository) {
        super(PlantDto.class, Plants.class);
        this.userRepository = userRepository;
    }
    
    @Override
    public PlantDto toDto(Plants entity) {
        if (entity == null) {
            return null;
        }
        
        return PlantDto.builder()
                .id(entity.getPlantId())
                .plantName(entity.getPlantName())
                .scientificName(entity.getScientificName())
                .type(entity.getType())
                .description(entity.getDescription())
                .userId(entity.getUser() != null ? entity.getUser().getUserId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .build();
    }
    
    @Override
    public Plants toEntity(PlantDto dto) {
        if (dto == null) {
            return null;
        }
        
        Users user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId()).orElse(null);
        }
        
        return Plants.builder()
                .plantId(dto.getId())
                .plantName(dto.getPlantName())
                .scientificName(dto.getScientificName())
                .type(dto.getType())
                .description(dto.getDescription())
                .user(user)
                .build();
    }

    @Override
    public Plants updateToEntity(PlantDto dto, Plants entity) {
        if (dto.getPlantName() != null) {
            entity.setPlantName(dto.getPlantName());
        }
        
        if (dto.getScientificName() != null) {
            entity.setScientificName(dto.getScientificName());
        }
        
        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
        
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        
        if (dto.getUserId() != null) {
            Users user = userRepository.findById(dto.getUserId()).orElse(entity.getUser());
            entity.setUser(user);
        }
        
        return entity;
    }
}
