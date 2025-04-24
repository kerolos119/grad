package org.example.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.document.Plants;
import org.example.dto.PageResult;
import org.example.dto.PlantDto;
import org.example.exception.PlantNotFoundException;
import org.example.mapper.PlantMapper;
import org.example.model.PlantStage;
import org.example.repo.PlantRepository;
import org.example.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlantsServices {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;
    private final UserRepository userRepository;

    public PlantDto create(PlantDto plantDto) {
        Plants plants = plantMapper.toEntity(plantDto);
        Plants plant = plantRepository.save(plants);
        return plantMapper.toDto(plant);
    }

    public List<PlantDto> getAllPlantsByUser(Long userId) {
        return plantRepository.findByUser_UserId(userId)
                .stream().map(plantMapper::toDto).collect(Collectors.toList());
    }

    public PlantDto update(Long plantId, PlantDto plantDto) {
        Plants existingPlant = plantRepository.findById(Math.toIntExact(plantId))
                .orElseThrow(()-> new PlantNotFoundException(" Plant not found"));

        plantMapper.updateToEntity(plantDto,existingPlant);
        Plants updatePlant = plantRepository.save(existingPlant);
        return plantMapper.toDto(updatePlant);
    }

    public void delete(Long plantId) {
        plantRepository.deleteById(Math.toIntExact(plantId));
    }

    public PlantDto findByPlantName(String plantName) {
        if (plantName==null ||plantName.trim().isEmpty()){
            throw new IllegalArgumentException("Name cannot  be null or empty");
        }

        return Optional.ofNullable(plantRepository.findByPlantName(plantName.trim()))
                .map(plantMapper::toDto)
                .orElseThrow(()->new PlantNotFoundException("Plant not found with name" + plantName));
    }
    public PlantDto getPlantById(Long plantId) {
        return plantRepository.findById(Math.toIntExact(plantId))
                .map(plantMapper::toDto)
                .orElseThrow(() -> new PlantNotFoundException("Plant not found"));
    }

    public PlantDto findByType(String type) {
        if (type==null || type.trim().isEmpty()){
            throw new IllegalArgumentException("Type os plant cannot be null or empty");
        }
        return Optional.ofNullable(plantRepository.findByType(type.trim()))
                .map(plantMapper::toDto)
                .orElseThrow(()->new PlantNotFoundException("Plant not found with type"+type));
    }

    public PlantDto findByPlantStage(String plantStage) {
        if (plantStage==null || plantStage.trim().isEmpty()){
            throw new IllegalArgumentException("Plant stage cannot be null or empty");
        }
        return Optional.ofNullable(plantRepository.findByPlantStage(PlantStage.valueOf(plantStage.trim())))
                .map(plantMapper::toDto)
                .orElseThrow(()->new PlantNotFoundException("Plant not found with stage"+plantStage));
    }


    public PageResult<PlantDto> search(String plantName, String type, String userId, Pageable pageable) {
        Specification<Plants> spec = (root, query, cb) ->{
            List<Predicate> predicates  = new ArrayList<>();
            if (plantName != null){
                predicates.add(cb.like(root.get("plantName"),"%"+plantName+"%"));
            }
            if (type!=null){
                predicates.add(cb.like(root.get("type"),"%"+type+"%"));
            }
            if (userId!=null){
                predicates.add(cb.like(root.get("userId"),"%"+userId+"%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Plants> plantsPage=plantRepository.findAll(spec,pageable);
        List<PlantDto> plantDtos=plantsPage.getContent().stream().map(plantMapper::toDto).collect(Collectors.toList());

        return PageResult.<PlantDto>builder()
                .items(plantDtos)
                .totalElement(plantsPage.getTotalElements())
                .totalPages(plantsPage.getTotalPages())
                .currentPage(plantsPage.getNumber() + 1)
                .pageSize(plantsPage.getSize())
                .build();
    }
}
