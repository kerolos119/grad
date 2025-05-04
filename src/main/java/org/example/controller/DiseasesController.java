package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.DiseaseDto;
import org.example.dto.PageResult;
import org.example.dto.PageUtils;
import org.example.model.ApiResponse;
import org.example.services.DiseasesServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/diseases")
@RequiredArgsConstructor
public class DiseasesController {

    private final DiseasesServices services;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DiseaseDto> create
            (@Valid @RequestBody DiseaseDto dto){
        DiseaseDto diseases = services.create(dto);
        return ApiResponse.created(diseases);
    }


    @GetMapping("/search")
    public ApiResponse<PageResult<DiseaseDto>> search(
            @RequestParam(required = false) String dName,
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "10") int size,
            @RequestParam(required = false,defaultValue = "name,asc") String[] sort) {

        Sort sorting = Sort.by(parseSortOrders(sort));
        Pageable pageable = PageRequest.of(page,size,sorting);

        Page<DiseaseDto> pageData = services.search(dName,pageable);

        PageResult<DiseaseDto> result = PageUtils.toPageResult(pageData);

        return ApiResponse.success(result,"search result");
    }

    private Sort.Order[] parseSortOrders(String[] sort) {
        return Arrays.stream(sort)
                .map(s->s.split(","))
                .map(parts->new Sort.Order(
                        Sort.Direction.fromString(parts[1]),
                        parts[0]))
                .toArray(Sort.Order[]::new);

    }
}
