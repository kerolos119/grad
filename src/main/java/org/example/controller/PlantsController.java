package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PageResult;
import org.example.dto.PlantDto;
import org.example.services.PlantsServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
@RequiredArgsConstructor
public class PlantsController {
    private final PlantsServices plantsServices;

//    @Autowired
//    PlantsServices services;
    @PostMapping("/plant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlantDto> create(@RequestBody PlantDto plantDto){
        return new ResponseEntity<>(plantsServices.create(plantDto), HttpStatus.CREATED);
    }

    @GetMapping("/plants/{plantId}")
    public ResponseEntity<List<PlantDto>> getAllPlantsByUser(@PathVariable Long plantId ){
        List<PlantDto> plantDtoList = plantsServices.getAllPlantsByUser(plantId);
        return ResponseEntity.ok(plantDtoList);
    }

    @PutMapping("/{plantId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlantDto> update(@PathVariable Long plantId , @RequestBody PlantDto plantDto){
       PlantDto update= plantsServices.update(plantId,plantDto);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{plantId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete (@PathVariable Long plantId){
        plantsServices.delete(plantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/plantName/{plantName}")
    public ResponseEntity<PlantDto> findByPlantName (@PathVariable String plantName){
        PlantDto findByPlantName = plantsServices.findByPlantName(plantName);
        return  ResponseEntity.ok(findByPlantName);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<PlantDto> findByType(@PathVariable String type){
        PlantDto findByType = plantsServices.findByType(type);
        return ResponseEntity.ok(findByType);
    }

    @GetMapping("/plantStage/{plantStage}")
    public ResponseEntity<PlantDto> findByPlantStage(@PathVariable String plantStage){
        PlantDto findByPlantStage= plantsServices.findByPlantStage(plantStage);
        return ResponseEntity.ok(findByPlantStage);
    }
    // في الـ Controller
    @GetMapping("/{plantId}")
    public ResponseEntity<PlantDto> getPlant(@PathVariable Long plantId) {
        PlantDto plant = plantsServices.getPlantById(plantId);
        return ResponseEntity.ok(plant);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResult<PlantDto>> search (
            @RequestParam(required = false) String plantName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC")Sort.Direction direction){
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,sortBy));
        PageResult<PlantDto> result = plantsServices.search(plantName,type ,userId,pageable);
        return ResponseEntity.ok(result);
    }
}
