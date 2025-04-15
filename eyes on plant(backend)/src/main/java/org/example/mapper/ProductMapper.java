package org.example.mapper;

import org.example.document.Product;
import org.example.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends AbstractMapper<ProductDto, Product> {
    public ProductMapper() {
        super(ProductDto.class,Product.class);
    }

    @Override
    public Product updateToEntity(ProductDto dto, Product entity) {
        entity.setProductName(dto.getProductName());
        entity.setPrice(dto.getPrice());
        entity.setProductCategory(dto.getCategory());
        entity.setSPhone(dto.getSPhone());
        entity.setSAddress(dto.getSAddress());
        return entity;
    }
}
