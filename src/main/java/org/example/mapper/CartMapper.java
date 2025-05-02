package org.example.mapper;

import org.example.document.Cart;
import org.example.document.Users;
import org.example.dto.CartDto;
// Import seems unused but is required for CartDto.cartItems field
import org.example.dto.CartItemDto;
import org.example.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class CartMapper extends AbstractMapper<CartDto, Cart> {
    
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    
    // This field exists only to silence the "unused import" warning
    @SuppressWarnings("unused")
    private Class<?> unusedButNeeded = CartItemDto.class;
    
    @Autowired
    public CartMapper(UserRepository userRepository, CartItemMapper cartItemMapper) {
        super(CartDto.class, Cart.class);
        this.userRepository = userRepository;
        this.cartItemMapper = cartItemMapper;
    }
    
    @Override
    public CartDto toDto(Cart entity) {
        if (entity == null) {
            return null;
        }
        
        CartDto dto = CartDto.builder()
                .id(entity.getCartId())
                .userId(entity.getUser() != null ? entity.getUser().getUserId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        
        if (entity.getCartItems() != null && !entity.getCartItems().isEmpty()) {
            dto.setCartItems(entity.getCartItems().stream()
                    .map(cartItemMapper::toDto)
                    .collect(Collectors.toList()));
            
            dto.setTotalItems(entity.getCartItems().size());
            
            BigDecimal total = entity.getCartItems().stream()
                    .map(item -> {
                        BigDecimal unitPrice = item.getProduct().getPrice();
                        return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            dto.setTotalPrice(total);
        } else {
            dto.setTotalItems(0);
            dto.setTotalPrice(BigDecimal.ZERO);
        }
        
        return dto;
    }
    
    @Override
    public Cart toEntity(CartDto dto) {
        if (dto == null) {
            return null;
        }
        
        Users user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId()).orElse(null);
        }
        
        return Cart.builder()
                .cartId(dto.getId())
                .user(user)
                .build();
    }
    
    @Override
    public Cart updateToEntity(CartDto dto, Cart entity) {
        // Cart entity usually doesn't have many updatable fields
        // since it's mainly a container for cart items
        
        if (dto.getUserId() != null) {
            Users user = userRepository.findById(dto.getUserId()).orElse(entity.getUser());
            entity.setUser(user);
        }
        
        return entity;
    }
} 