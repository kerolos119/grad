package org.example.mapper;

import org.example.document.Products;
import org.example.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends AbstractMapper<ProductDto, Products> {
    public ProductMapper() {
        super(ProductDto.class,Products.class);
    }

    @Override
    public Products updateToEntity(ProductDto dto, Products entity) {
        entity.setProductName(dto.getProductName());
        entity.setPrice(dto.getPrice());
        entity.setProductCategory(dto.getCategory());
        entity.setSPhone(dto.getSPhone());
        entity.setSAddress(dto.getSAddress());
        return entity;
    }
}
