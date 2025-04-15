package org.example.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.document.Product;
import org.example.dto.PageResult;
import org.example.dto.ProductDto;
import org.example.exception.ProductNotFoundException;
import org.example.mapper.ProductMapper;
import org.example.model.ProductCategory;
import org.example.repo.PlantRepository;
import org.example.repo.ProductRepository;
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
public class ProductServices {
    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;


    public ProductDto create(ProductDto dto) {
        Product product = mapper.toEntity(dto);
        Product result = repository.save(product);
        return mapper.toDto(result);
    }

    public void delete(Integer productId) {
        Product product = repository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException(" Product Not Found "));
        repository.delete(product);
    }

    public ProductDto update(Integer productId, ProductDto dto) {
        Product product = repository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException(" Product Not Found"));
        mapper.updateToEntity(dto,product);
        Product result = repository.save(product);
        return mapper.toDto(result);
    }

    public ProductDto findByName(String productName) {
        // Corrected validation logic
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        return repository.findByProductName(productName.trim())
                .map(mapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + productName));
    }

    // Fixed findByCategory method

//    public List<ProductDto> findByCategory(String category) {
//        // Validate input
//        if (category == null || category.trim().isEmpty()) {
//            throw new IllegalArgumentException("Category cannot be null or empty");
//        }
//
//        // Convert to enum safely
//        ProductCategory productCategory;
//        try {
//            productCategory = ProductCategory.valueOf(category.trim().toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new InvalidCategoryException("Invalid product category: " + category);
//        }
//
//        // Get products from repository
//        List<Product> products = repository.findByCategory(productCategory);
//
//        // Check for empty results
//        if (products.isEmpty()) {
//            throw new ProductNotFoundException("No products found with category: " + category);
//        }
//
//        // Correct mapping using lambda expression
//        return products.stream()
//                .map(product -> mapper.toDto(product))  // Fixed this line
//                .collect(Collectors.toList());
//    }

    // Fixed search method
    public PageResult<ProductDto> search(String productName, String category, String price,
                                         String sellerAddress, String sellerPhone, Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productName != null && !productName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("productName")), "%" + productName.toLowerCase() + "%"));
            }

            if (category != null && !category.isBlank()) {
                try {
                    ProductCategory categoryEnum = ProductCategory.valueOf(category.toUpperCase());
                    predicates.add(cb.equal(root.get("category"), categoryEnum));
                } catch (IllegalArgumentException e) {
                    throw new InvalidCategoryException("Invalid category filter: " + category);
                }
            }

            if (price != null && !price.isBlank()) {
                predicates.add(cb.like(root.get("price"), "%" + price + "%"));
            }

            if (sellerAddress != null && !sellerAddress.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("sellerAddress")), "%" + sellerAddress.toLowerCase() + "%"));
            }

            if (sellerPhone != null && !sellerPhone.isBlank()) {
                predicates.add(cb.like(root.get("sellerPhone"), "%" + sellerPhone + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> productPage = repository.findAll(spec, pageable);

        List<ProductDto> productDtos = productPage.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return new PageResult<>(
                productDtos,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getNumber() + 1,
                productPage.getSize()
        );
    }

    // Add this custom exception class
    public static class InvalidCategoryException extends RuntimeException {
        public InvalidCategoryException(String message) {
            super(message);
        }
    }

    }

