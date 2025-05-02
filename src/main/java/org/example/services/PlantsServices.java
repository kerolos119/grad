package org.example.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.document.Plants;
import org.example.dto.PageResult;
import org.example.dto.PlantDto;
import org.example.exception.NotFoundException;
import org.example.mapper.PlantMapper;
import org.example.model.PlantStage;
import org.example.repo.PlantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlantsServices {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;

    /**
     * Create a new plant
     */
    public PlantDto create(PlantDto plantDto) {
        log.info("Creating new plant: {}", plantDto.getPlantName());
        if (plantDto.getPlantName() == null || plantDto.getPlantName().trim().isEmpty()) {
            throw new IllegalArgumentException("Plant name cannot be null or empty");
        }
        
        Plants plant = plantMapper.toEntity(plantDto);
        Plants savedPlant = plantRepository.save(plant);
        log.info("Plant created with ID: {}", savedPlant.getPlantId());
        return plantMapper.toDto(savedPlant);
    }

    /**
     * Get all plants for a specific user
     */
    public List<PlantDto> getAllPlantsByUser(Long userId) {
        log.info("Fetching all plants for user ID: {}", userId);
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        List<Plants> plants = plantRepository.findByUser_UserId(userId);
        if (plants.isEmpty()) {
            log.info("No plants found for user ID: {}", userId);
        } else {
            log.info("Found {} plants for user ID: {}", plants.size(), userId);
        }
        
        return plants.stream()
                .map(plantMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing plant
     */
    public PlantDto update(Long plantId, PlantDto plantDto) {
        log.info("Updating plant with ID: {}", plantId);
        if (plantId == null) {
            throw new IllegalArgumentException("Plant ID cannot be null");
        }
        
        Plants existingPlant = plantRepository.findById(Math.toIntExact(plantId))
                .orElseThrow(() -> new NotFoundException("Plant not found with ID: " + plantId));

        // Validate plant name if provided
        if (plantDto.getPlantName() != null && plantDto.getPlantName().trim().isEmpty()) {
            throw new IllegalArgumentException("Plant name cannot be empty");
        }
        
        plantMapper.updateToEntity(plantDto, existingPlant);
        Plants updatedPlant = plantRepository.save(existingPlant);
        log.info("Plant updated successfully: {}", plantId);
        return plantMapper.toDto(updatedPlant);
    }

    /**
     * Delete a plant by ID
     */
    public void delete(Long plantId) {
        log.info("Deleting plant with ID: {}", plantId);
        if (plantId == null) {
            throw new IllegalArgumentException("Plant ID cannot be null");
        }
        
        // Check if plant exists before deleting
        if (!plantRepository.existsById(Math.toIntExact(plantId))) {
            throw new NotFoundException("Plant not found with ID: " + plantId);
        }
        
        plantRepository.deleteById(Math.toIntExact(plantId));
        log.info("Plant deleted successfully: {}", plantId);
    }

    /**
     * Find plant by name
     */
    public PlantDto findByPlantName(String plantName) {
        log.info("Finding plant by name: {}", plantName);
        if (plantName == null || plantName.trim().isEmpty()) {
            throw new IllegalArgumentException("Plant name cannot be null or empty");
        }

        Plants plant = plantRepository.findByPlantName(plantName.trim());
        if (plant == null) {
            throw new NotFoundException("Plant not found with name: " + plantName);
        }
        
        return plantMapper.toDto(plant);
    }

    /**
     * Get plant by ID
     */
    public PlantDto getPlantById(Long plantId) {
        log.info("Getting plant by ID: {}", plantId);
        if (plantId == null) {
            throw new IllegalArgumentException("Plant ID cannot be null");
        }
        
        Plants plant = plantRepository.findById(Math.toIntExact(plantId))
                .orElseThrow(() -> new NotFoundException("Plant not found with ID: " + plantId));
                
        return plantMapper.toDto(plant);
    }

    /**
     * Find plant by type
     */
    public PlantDto findByType(String type) {
        log.info("Finding plant by type: {}", type);
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Plant type cannot be null or empty");
        }
        
        Plants plant = plantRepository.findByType(type.trim());
        if (plant == null) {
            throw new NotFoundException("Plant not found with type: " + type);
        }
        
        return plantMapper.toDto(plant);
    }

    /**
     * Find plant by growth stage
     */
    public PlantDto findByPlantStage(String plantStage) {
        log.info("Finding plant by stage: {}", plantStage);
        if (plantStage == null || plantStage.trim().isEmpty()) {
            throw new IllegalArgumentException("Plant stage cannot be null or empty");
        }
        
        try {
            PlantStage stage = PlantStage.valueOf(plantStage.trim().toUpperCase());
            Plants plant = plantRepository.findByPlantStage(stage);
            
            if (plant == null) {
                throw new NotFoundException("Plant not found with stage: " + plantStage);
            }
            
            return plantMapper.toDto(plant);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid plant stage: " + plantStage);
        }
    }

    /**
     * Search plants with filters
     */
    public PageResult<PlantDto> search(String plantName, String type, String userId, Pageable pageable) {
        log.info("Searching plants with filters - name: {}, type: {}, userId: {}", plantName, type, userId);
        
        Specification<Plants> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (plantName != null && !plantName.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("plantName")), "%" + plantName.toLowerCase() + "%"));
            }
            
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("type")), "%" + type.toLowerCase() + "%"));
            }
            
            if (userId != null && !userId.isEmpty()) {
                predicates.add(cb.equal(root.get("user").get("userId"), Long.valueOf(userId)));
            }
            
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Page<Plants> plantsPage = plantRepository.findAll(spec, pageable);
        List<PlantDto> plantDtos = plantsPage.getContent().stream()
                .map(plantMapper::toDto)
                .collect(Collectors.toList());

        log.info("Search returned {} results", plantDtos.size());
        
        return PageResult.<PlantDto>builder()
                .items(plantDtos)
                .totalElement(plantsPage.getTotalElements())
                .totalPages(plantsPage.getTotalPages())
                .currentPage(plantsPage.getNumber() + 1)
                .pageSize(plantsPage.getSize())
                .build();
    }
}
