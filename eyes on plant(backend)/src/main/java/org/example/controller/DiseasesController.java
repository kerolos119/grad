package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.DiseasesDto;
import org.example.dto.PageResult;
import org.example.services.DiseasesServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diseases")
@RequiredArgsConstructor
@ControllerAdvice
public class DiseasesController {

    private final DiseasesServices services;

    @PostMapping
    public ResponseEntity<DiseasesDto> create( @Valid @RequestBody DiseasesDto dto){
        DiseasesDto result= services.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        services.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public PageResult<DiseasesDto> search(
            @RequestParam(required = false) String dName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dName") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction){
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,sortBy));
        PageResult<DiseasesDto> result = services.search(dName,pageable);
        return result;
    }
}
