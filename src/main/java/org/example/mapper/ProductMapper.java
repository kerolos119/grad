package org.example.mapper;

import org.example.document.Products;
import org.example.document.Users;
import org.example.dto.ProductDto;
import org.example.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends AbstractMapper<ProductDto, Products> {
    
    private final UserRepository userRepository;
    
    @Autowired
    public ProductMapper(UserRepository userRepository) {
        super(ProductDto.class, Products.class);
        this.userRepository = userRepository;
    }
    
    @Override
    public ProductDto toDto(Products entity) {
        if (entity == null) {
            return null;
        }
        
        return ProductDto.builder()
                .id(entity.getProductId())
                .productName(entity.getProductName())
                .category(entity.getProductCategory())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .stockQuantity(entity.getStockQuantity())
                .imageUrls(entity.getImageUrls())
                .isOnSale(entity.getIsOnSale())
                .discountPrice(entity.getDiscountPrice())
                .careInstructions(entity.getCareInstructions())
                .wateringFrequency(entity.getWateringFrequency())
                .sunlightRequirements(entity.getSunlightRequirements())
                .plantHeight(entity.getPlantHeight())
                .plantType(entity.getPlantType())
                .isIndoor(entity.getIsIndoor())
                .sellerAddress(entity.getSellerAddress())
                .sellerPhone(entity.getSellerPhone())
                .sellerId(entity.getSeller() != null ? entity.getSeller().getUserId() : null)
                .sellerName(entity.getSeller() != null ? entity.getSeller().getUsername() : null)
                .build();
    }
    
    @Override
    public Products toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        
        Users seller = null;
        if (dto.getSellerId() != null) {
            seller = userRepository.findById(dto.getSellerId()).orElse(null);
        }
        
        return Products.builder()
                .productId(dto.getId())
                .productName(dto.getProductName())
                .productCategory(dto.getCategory())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .stockQuantity(dto.getStockQuantity())
                .imageUrls(dto.getImageUrls())
                .isOnSale(dto.getIsOnSale())
                .discountPrice(dto.getDiscountPrice())
                .careInstructions(dto.getCareInstructions())
                .wateringFrequency(dto.getWateringFrequency())
                .sunlightRequirements(dto.getSunlightRequirements())
                .plantHeight(dto.getPlantHeight())
                .plantType(dto.getPlantType())
                .isIndoor(dto.getIsIndoor())
                .sellerAddress(dto.getSellerAddress())
                .sellerPhone(dto.getSellerPhone())
                .seller(seller)
                .build();
    }

    @Override
    public Products updateToEntity(ProductDto dto, Products entity) {
        if (dto == null) {
            return entity;
        }
        
        if (dto.getProductName() != null) {
            entity.setProductName(dto.getProductName());
        }
        
        if (dto.getCategory() != null) {
            entity.setProductCategory(dto.getCategory());
        }
        
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        
        if (dto.getStockQuantity() != null) {
            entity.setStockQuantity(dto.getStockQuantity());
        }
        
        if (dto.getImageUrls() != null) {
            entity.setImageUrls(dto.getImageUrls());
        }
        
        if (dto.getIsOnSale() != null) {
            entity.setIsOnSale(dto.getIsOnSale());
        }
        
        if (dto.getDiscountPrice() != null) {
            entity.setDiscountPrice(dto.getDiscountPrice());
        }
        
        if (dto.getCareInstructions() != null) {
            entity.setCareInstructions(dto.getCareInstructions());
        }
        
        if (dto.getWateringFrequency() != null) {
            entity.setWateringFrequency(dto.getWateringFrequency());
        }
        
        if (dto.getSunlightRequirements() != null) {
            entity.setSunlightRequirements(dto.getSunlightRequirements());
        }
        
        if (dto.getPlantHeight() != null) {
            entity.setPlantHeight(dto.getPlantHeight());
        }
        
        if (dto.getPlantType() != null) {
            entity.setPlantType(dto.getPlantType());
        }
        
        if (dto.getIsIndoor() != null) {
            entity.setIsIndoor(dto.getIsIndoor());
        }
        
        if (dto.getSellerAddress() != null) {
            entity.setSellerAddress(dto.getSellerAddress());
        }
        
        if (dto.getSellerPhone() != null) {
            entity.setSellerPhone(dto.getSellerPhone());
        }
        
        if (dto.getSellerId() != null) {
            Users seller = userRepository.findById(dto.getSellerId()).orElse(entity.getSeller());
            entity.setSeller(seller);
        }
        
        return entity;
    }
}
