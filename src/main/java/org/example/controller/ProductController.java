package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.PageResult;
import org.example.dto.ProductDto;
import org.example.services.ProductServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServices services;

    @PostMapping("/product")
    public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductDto dto){
        ProductDto result= services.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId){
        services.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable Long productId , @RequestBody ProductDto dto){
        ProductDto update= services.update(productId,dto);
        return ResponseEntity.ok(update);
    }

    @GetMapping("/productName/{productName}")
    public ResponseEntity<ProductDto> findByName(@PathVariable String productName){
        ProductDto productDto=services.findByName(productName);
        return ResponseEntity.ok(productDto);
    }

//    @GetMapping("/category/{category}")
//    public ResponseEntity<ProductDto> findByCategory(@PathVariable String category){
//        ProductDto dto = (ProductDto) services.findByCategory(category);
//        return ResponseEntity.ok(dto);
//    }

    @GetMapping("/search")
    public PageResult<ProductDto> search(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String sellerAddress,
            @RequestParam(required = false) String sellerPhone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int siza,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page,siza,Sort.by(direction,sortBy));
        PageResult<ProductDto> result = services.search(productName,category,price,sellerAddress,sellerPhone,pageable);
        return result;
    }
}