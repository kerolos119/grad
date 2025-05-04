package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.PageResult;
import org.example.dto.PlantDto;
import org.example.model.ApiResponse;
import org.example.services.PlantsServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plants")
@RequiredArgsConstructor
@Tag(name = "Plants", description = "Plant management APIs")
public class PlantsController {
    private final PlantsServices plantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create a new plant",
        description = "Creates a new plant with the provided information",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Plant created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Invalid input")
    })
    public ResponseEntity<ApiResponse<PlantDto>> createPlant(@Valid @RequestBody PlantDto plantDto) {
        PlantDto result = plantService.create(plantDto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(result, "Plant created successfully"));
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get all plants for a user",
        description = "Returns all plants owned by the specified user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<PlantDto>>> getAllPlantsByUser(
            @Parameter(description = "User ID") 
            @PathVariable Long userId) {
        List<PlantDto> plants = plantService.getAllPlantsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(plants, "Plants retrieved successfully"));
    }

    @GetMapping("/{plantId}")
    @Operation(
        summary = "Get plant by ID",
        description = "Returns the plant with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<PlantDto>> getPlantById(
            @Parameter(description = "Plant ID") 
            @PathVariable Long plantId) {
        PlantDto plant = plantService.getPlantById(plantId);
        return ResponseEntity.ok(ApiResponse.success(plant, "Plant retrieved successfully"));
    }

    @PutMapping("/{plantId}")
    @Operation(
        summary = "Update a plant",
        description = "Updates the plant with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<PlantDto>> updatePlant(
            @Parameter(description = "Plant ID") 
            @PathVariable Long plantId,
            @Valid @RequestBody PlantDto plantDto) {
        PlantDto updated = plantService.update(plantId, plantDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Plant updated successfully"));
    }

    @DeleteMapping("/{plantId}")
    @Operation(
        summary = "Delete a plant",
        description = "Deletes the plant with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deletePlant(
            @Parameter(description = "Plant ID") 
            @PathVariable Long plantId) {
        plantService.delete(plantId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.noContent("Plant deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search plants",
        description = "Search for plants with optional filters and pagination",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<PageResult<PlantDto>>> searchPlants(
            @Parameter(description = "Filter by plant name") 
            @RequestParam(required = false) String plantName,
            
            @Parameter(description = "Filter by plant type") 
            @RequestParam(required = false) String type,
            
            @Parameter(description = "Filter by user ID") 
            @RequestParam(required = false) String userId,
            
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(required = false,defaultValue = "10") int size,
            
            @Parameter(description = "Sort by field") 
            @RequestParam(required = false,defaultValue = "plantName") String sortBy,
            
            @Parameter(description = "Sort direction") 
            @RequestParam(required = false,defaultValue = "ASC") Sort.Direction direction) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PageResult<PlantDto> result = plantService.search(plantName, type, userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(result, "Search results"));
    }

    @GetMapping("/name/{plantName}")
    @Operation(
        summary = "Find plant by name",
        description = "Returns the plant with the specified name",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<PlantDto>> findPlantByName(
            @Parameter(description = "Plant name") 
            @PathVariable String plantName) {
        PlantDto plant = plantService.findByPlantName(plantName);
        return ResponseEntity.ok(ApiResponse.success(plant, "Plant found"));
    }

    @GetMapping("/type/{type}")
    @Operation(
        summary = "Find plant by type",
        description = "Returns the plant with the specified type",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<PlantDto>> findPlantByType(
            @Parameter(description = "Plant type") 
            @PathVariable String type) {
        PlantDto plant = plantService.findByType(type);
        return ResponseEntity.ok(ApiResponse.success(plant, "Plant found"));
    }

    @GetMapping("/stage/{plantStage}")
    @Operation(
        summary = "Find plant by growth stage",
        description = "Returns the plant with the specified growth stage",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<PlantDto>> findPlantByStage(
            @Parameter(description = "Plant growth stage") 
            @PathVariable String plantStage) {
        PlantDto plant = plantService.findByPlantStage(plantStage);
        return ResponseEntity.ok(ApiResponse.success(plant, "Plant found"));
    }
}
