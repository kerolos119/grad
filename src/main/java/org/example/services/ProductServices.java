package org.example.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.document.Products;
import org.example.document.Users;
import org.example.dto.PageResult;
import org.example.dto.ProductDto;
import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.mapper.ProductMapper;
import org.example.model.ProductCategory;
import org.example.repo.ProductRepository;
import org.example.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServices {
    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final UserRepository userRepository;

    /**
     * Create a new product
     */
    public ProductDto create(ProductDto dto) {
        log.info("Creating new product: {}", dto.getProductName());
        
        // Validate seller exists if sellerId is provided
        if (dto.getSellerId() != null) {
            userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new NotFoundException("Seller not found with ID: " + dto.getSellerId()));
        }
        
        Products product = mapper.toEntity(dto);
        Products result = repository.save(product);
        log.info("Product created with ID: {}", result.getProductId());
        return mapper.toDto(result);
    }

    /**
     * Delete a product
     */
    public void delete(Long productId) {
        log.info("Deleting product with ID: {}", productId);
        Products product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
        repository.delete(product);
        log.info("Product deleted successfully: {}", productId);
    }

    /**
     * Update a product
     */
    public ProductDto update(Long productId, ProductDto dto) {
        log.info("Updating product with ID: {}", productId);
        Products product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
        
        // Validate seller exists if sellerId is changed
        if (dto.getSellerId() != null) {
            userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new NotFoundException("Seller not found with ID: " + dto.getSellerId()));
        }
        
        mapper.updateToEntity(dto, product);
        Products result = repository.save(product);
        log.info("Product updated successfully: {}", productId);
        return mapper.toDto(result);
    }

    /**
     * Find product by name
     */
    public ProductDto findByName(String productName) {
        log.info("Finding product by name: {}", productName);
        if (productName == null || productName.trim().isEmpty()) {
            throw new BadRequestException("Product name cannot be null or empty");
        }

        return repository.findByProductName(productName.trim())
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found with name: " + productName));
    }

    /**
     * Find product by ID
     */
    public ProductDto findById(Long productId) {
        log.info("Finding product by ID: {}", productId);
        return repository.findById(productId)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
    }

    /**
     * Find products by category
     */
    public List<ProductDto> findByCategory(String category) {
        log.info("Finding products by category: {}", category);
        if (category == null || category.trim().isEmpty()) {
            throw new BadRequestException("Category cannot be null or empty");
        }

        ProductCategory productCategory;
        try {
            productCategory = ProductCategory.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid product category: " + category);
        }

        List<Products> products = repository.findByProductCategory(productCategory);
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find products by seller
     */
    public List<ProductDto> findBySeller(Long sellerId) {
        log.info("Finding products by seller ID: {}", sellerId);
        Users seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new NotFoundException("Seller not found with ID: " + sellerId));
        
        List<Products> products = repository.findBySeller(seller);
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find products on sale
     */
    public List<ProductDto> findOnSaleProducts(Pageable pageable) {
        log.info("Finding products on sale");
        List<Products> products = repository.findAllOnSale(pageable);
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find indoor or outdoor plants
     */
    public List<ProductDto> findByIndoorStatus(Boolean isIndoor, Pageable pageable) {
        log.info("Finding products by indoor status: {}", isIndoor);
        List<Products> products = repository.findByIndoorStatus(isIndoor, pageable);
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find products by price range
     */
    public List<ProductDto> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Finding products in price range: {} to {}", minPrice, maxPrice);
        List<Products> products = repository.findByPriceBetween(minPrice, maxPrice, pageable);
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Search products by keyword
     */
    public List<ProductDto> searchByKeyword(String keyword, Pageable pageable) {
        log.info("Searching products with keyword: {}", keyword);
        List<Products> products = repository.searchByKeyword(keyword, pageable);
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find in-stock products
     */
    public List<ProductDto> findInStockProducts(Pageable pageable) {
        log.info("Finding in-stock products");
        List<Products> products = repository.findInStock(pageable);
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Advanced search with filters
     */
    public PageResult<ProductDto> search(String productName, String category, String price,
                                      String sellerAddress, String sellerPhone, Pageable pageable) {
        log.info("Searching products with filters");
        
        Specification<Products> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productName != null && !productName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("productName")), "%" + productName.toLowerCase() + "%"));
            }

            if (category != null && !category.isBlank()) {
                try {
                    ProductCategory categoryEnum = ProductCategory.valueOf(category.toUpperCase());
                    predicates.add(cb.equal(root.get("productCategory"), categoryEnum));
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid category filter: {}", category);
                }
            }

            if (price != null && !price.isBlank()) {
                try {
                    BigDecimal priceValue = new BigDecimal(price);
                    predicates.add(cb.equal(root.get("price"), priceValue));
                } catch (NumberFormatException e) {
                    log.warn("Invalid price filter: {}", price);
                }
            }

            if (sellerAddress != null && !sellerAddress.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("sellerAddress")), "%" + sellerAddress.toLowerCase() + "%"));
            }

            if (sellerPhone != null && !sellerPhone.isBlank()) {
                predicates.add(cb.like(root.get("sellerPhone"), "%" + sellerPhone + "%"));
            }

            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Products> productPage = repository.findAll(spec, pageable);
        
        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
                
        log.info("Search returned {} results", productDtos.size());

        return PageResult.<ProductDto>builder()
                .items(productDtos)
                .totalElement(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .currentPage(productPage.getNumber() + 1)
                .pageSize(productPage.getSize())
                .build();
    }
}

