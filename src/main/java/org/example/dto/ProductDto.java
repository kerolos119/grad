package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.document.Users;
import org.example.model.ProductCategory;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    @NotBlank(message ="Product name is required" )
    private String productName;

    @NotBlank(message = "Category required")
    private ProductCategory category;

    @DecimalMin(value = "0.0", inclusive = false,message = "Price must be greater than zero")
    @NotBlank(message = "Price required")
    private BigDecimal price;

    @NotBlank(message = "Seller address required")
    private String sAddress;

    @NotBlank(message = "Seller's phone number is required")
    @Pattern(regexp = "^[0-9+]+$",message = "Invalid phone number")
    private String sPhone;

    private Users user;


}
