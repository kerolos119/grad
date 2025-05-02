package org.example.mapper;

import org.example.document.Cart;
import org.example.document.CartItems;
import org.example.document.Products;
import org.example.dto.CartItemDto;
import org.example.repo.CartRepository;
import org.example.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemMapper extends AbstractMapper<CartItemDto, CartItems> {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    @Autowired
    public CartItemMapper(CartRepository cartRepository, ProductRepository productRepository) {
        super(CartItemDto.class, CartItems.class);
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public CartItemDto toDto(CartItems entity) {
        if (entity == null) {
            return null;
        }
        
        BigDecimal unitPrice = entity.getProduct() != null ? entity.getProduct().getPrice() : BigDecimal.ZERO;
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(entity.getQuantity()));
        
        return CartItemDto.builder()
                .id(entity.getItemId())
                .cartId(entity.getCart() != null ? entity.getCart().getCartId() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getProductId().intValue() : null)
                .productName(entity.getProduct() != null ? entity.getProduct().getProductName() : null)
                .productImage(entity.getProduct() != null && !entity.getProduct().getImageUrls().isEmpty() ? 
                              entity.getProduct().getImageUrls().get(0) : null)
                .productPrice(unitPrice)
                .quantity(entity.getQuantity())
                .subtotal(subtotal)
                .addedAt(entity.getAddedAt())
                .build();
    }
    
    @Override
    public CartItems toEntity(CartItemDto dto) {
        if (dto == null) {
            return null;
        }
        
        Cart cart = null;
        if (dto.getCartId() != null) {
            cart = cartRepository.findById(dto.getCartId()).orElse(null);
        }
        
        Products product = null;
        if (dto.getProductId() != null) {
            product = productRepository.findById(dto.getProductId().longValue()).orElse(null);
        }
        
        return CartItems.builder()
                .itemId(dto.getId())
                .cart(cart)
                .product(product)
                .quantity(dto.getQuantity())
                .build();
    }
    
    @Override
    public CartItems updateToEntity(CartItemDto dto, CartItems entity) {
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        
        if (dto.getProductId() != null && (entity.getProduct() == null || 
                !dto.getProductId().equals(entity.getProduct().getProductId().intValue()))) {
            Products product = productRepository.findById(dto.getProductId().longValue()).orElse(entity.getProduct());
            entity.setProduct(product);
        }
        
        if (dto.getCartId() != null && (entity.getCart() == null || 
                !dto.getCartId().equals(entity.getCart().getCartId()))) {
            Cart cart = cartRepository.findById(dto.getCartId()).orElse(entity.getCart());
            entity.setCart(cart);
        }
        
        return entity;
    }
} 