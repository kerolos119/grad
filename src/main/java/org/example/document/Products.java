package org.example.document;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.model.ProductCategory;

import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Table(name = "Products")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name",columnDefinition = "TEXT",length = 50,nullable = false)
    @NotBlank(message ="Product name is required" )
    @Size(max = 50,message = "Name cannot exceed 50 characters ")
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)  // Changed column name for clarity
    @NotNull(message = "Category is required")
    private ProductCategory productCategory;

    @Column(name = "price",nullable = false,precision = 10,scale = 2)
    @DecimalMin(value = "0.0", inclusive = false,message = "Price must be greater than zero")
    @NotBlank(message = "Price required")
    private BigDecimal price;

    @Column(name = "seller_address",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Seller address required")
    private String sAddress;

    @Column(name = "seller_phone",nullable = false,length = 50)
    @NotBlank(message = "Seller's phone number is required")
    @Pattern(regexp = "^[0-9+]+$",message = "Invalid phone number")
    private String sPhone;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Users user;




}
