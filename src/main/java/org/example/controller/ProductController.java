package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PageResult;
import org.example.dto.ProductDto;
import org.example.model.ApiResponse;
import org.example.services.ProductServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description = "Plant e-commerce product management APIs")
public class ProductController {
    private final ProductServices productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create a new product",
        description = "Creates a new plant product listing",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Product created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Invalid input")
    })
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody ProductDto dto) {
        log.info("Creating new product: {}", dto.getProductName());
        ProductDto result = productService.create(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(result, "Product created successfully"));
    }

    @GetMapping("/{productId}")
    @Operation(
        summary = "Get product by ID",
        description = "Returns the product with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(
            @Parameter(description = "Product ID") 
            @PathVariable Long productId) {
        log.info("Getting product with ID: {}", productId);
        ProductDto product = productService.findById(productId);
        return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
    }

    @PutMapping("/{productId}")
    @Operation(
        summary = "Update a product",
        description = "Updates the product with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @Parameter(description = "Product ID") 
            @PathVariable Long productId,
            @Valid @RequestBody ProductDto dto) {
        log.info("Updating product with ID: {}", productId);
        ProductDto updated = productService.update(productId, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product updated successfully"));
    }

    @DeleteMapping("/{productId}")
    @Operation(
        summary = "Delete a product",
        description = "Deletes the product with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID") 
            @PathVariable Long productId) {
        log.info("Deleting product with ID: {}", productId);
        productService.delete(productId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.noContent("Product deleted successfully"));
    }

    @GetMapping("/name/{productName}")
    @Operation(
        summary = "Find product by name",
        description = "Returns the product with the specified name",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<ProductDto>> findProductByName(
            @Parameter(description = "Product name") 
            @PathVariable String productName) {
        log.info("Finding product by name: {}", productName);
        ProductDto product = productService.findByName(productName);
        return ResponseEntity.ok(ApiResponse.success(product, "Product found"));
    }

    @GetMapping("/category/{category}")
    @Operation(
        summary = "Find products by category",
        description = "Returns products in the specified category",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<ProductDto>>> findProductsByCategory(
            @Parameter(description = "Product category") 
            @PathVariable String category) {
        log.info("Finding products by category: {}", category);
        List<ProductDto> products = productService.findByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
    }

    @GetMapping("/seller/{sellerId}")
    @Operation(
        summary = "Find products by seller",
        description = "Returns products from the specified seller",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<ProductDto>>> findProductsBySeller(
            @Parameter(description = "Seller ID") 
            @PathVariable Long sellerId) {
        log.info("Finding products by seller ID: {}", sellerId);
        List<ProductDto> products = productService.findBySeller(sellerId);
        return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
    }

    @GetMapping("/sale")
    @Operation(
        summary = "Find products on sale",
        description = "Returns products currently on sale",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<ProductDto>>> findProductsOnSale(
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Finding products on sale");
        Pageable pageable = PageRequest.of(page, size);
        List<ProductDto> products = productService.findOnSaleProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Products on sale retrieved successfully"));
    }

    @GetMapping("/indoor")
    @Operation(
        summary = "Find indoor/outdoor plants",
        description = "Returns indoor or outdoor plants based on parameter",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<ProductDto>>> findByIndoorStatus(
            @Parameter(description = "Indoor status (true=indoor, false=outdoor)") 
            @RequestParam Boolean isIndoor,
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Finding products by indoor status: {}", isIndoor);
        Pageable pageable = PageRequest.of(page, size);
        List<ProductDto> products = productService.findByIndoorStatus(isIndoor, pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Plants retrieved successfully"));
    }

    @GetMapping("/price-range")
    @Operation(
        summary = "Find products by price range",
        description = "Returns products within the specified price range",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<ProductDto>>> findByPriceRange(
            @Parameter(description = "Minimum price") 
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price") 
            @RequestParam BigDecimal maxPrice,
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(required = false,defaultValue = "10") int size) {
        log.info("Finding products in price range: {} to {}", minPrice, maxPrice);
        Pageable pageable = PageRequest.of(page, size);
        List<ProductDto> products = productService.findByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
    }

    @GetMapping("/keyword")
    @Operation(
        summary = "Search products by keyword",
        description = "Returns products matching the keyword in name, description, or plant type",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<ProductDto>>> searchByKeyword(
            @Parameter(description = "Search keyword") 
            @RequestParam String keyword,
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(required = false,defaultValue = "10") int size) {
        log.info("Searching products with keyword: {}", keyword);
        Pageable pageable = PageRequest.of(page, size);
        List<ProductDto> products = productService.searchByKeyword(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
    }

    @GetMapping("/in-stock")
    @Operation(
        summary = "Find in-stock products",
        description = "Returns products that are currently in stock",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<ProductDto>>> findInStockProducts(
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(required = false,defaultValue = "10") int size) {
        log.info("Finding in-stock products");
        Pageable pageable = PageRequest.of(page, size);
        List<ProductDto> products = productService.findInStockProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "In-stock products retrieved successfully"));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Advanced product search",
        description = "Search for products with optional filters and pagination",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<PageResult<ProductDto>>> searchProducts(
            @Parameter(description = "Filter by product name") 
            @RequestParam(required = false) String productName,
            
            @Parameter(description = "Filter by category") 
            @RequestParam(required = false) String category,
            
            @Parameter(description = "Filter by price") 
            @RequestParam(required = false) String price,
            
            @Parameter(description = "Filter by seller address") 
            @RequestParam(required = false) String sellerAddress,
            
            @Parameter(description = "Filter by seller phone") 
            @RequestParam(required = false) String sellerPhone,
            
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(required = false,defaultValue = "10") int size,
            
            @Parameter(description = "Sort by field") 
            @RequestParam(required = false,defaultValue = "productName") String sortBy,
            
            @Parameter(description = "Sort direction") 
            @RequestParam(required = false,defaultValue = "ASC") Sort.Direction direction) {
        
        log.info("Searching products with filters");
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PageResult<ProductDto> result = productService.search(productName, category, price, sellerAddress, sellerPhone, pageable);
        return ResponseEntity.ok(ApiResponse.success(result, "Search results"));
    }
}