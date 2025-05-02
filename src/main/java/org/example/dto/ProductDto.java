package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.ProductCategory;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
    
    private List<String> imageUrls;
    
    private Boolean isOnSale;
    
    private BigDecimal discountPrice;
    
    private String careInstructions;
    
    private String wateringFrequency;
    
    private String sunlightRequirements;
    
    private String plantHeight;
    
    private String plantType;
    
    private Boolean isIndoor;
    
    @NotBlank(message = "Seller address is required")
    private String sellerAddress;

    @NotBlank(message = "Seller's phone number is required")
    @Pattern(regexp = "^[0-9+]+$", message = "Invalid phone number")
    private String sellerPhone;

    private Long sellerId;
    
    private String sellerName;
}
