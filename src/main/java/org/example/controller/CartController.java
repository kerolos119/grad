package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.CartDto;
import org.example.dto.CartItemDto;
import org.example.model.ApiResponse;
import org.example.services.CartServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartServices cartServices;
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartDto>> getCartByUser(@PathVariable Long userId) {
        ApiResponse<CartDto> response = cartServices.getCartByUser(userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/user/{userId}/items")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartDto>> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemDto cartItemDto) {
        ApiResponse<CartDto> response = cartServices.addItemToCart(userId, cartItemDto);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/user/{userId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartDto>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Integer itemId,
            @Valid @RequestBody CartItemDto cartItemDto) {
        ApiResponse<CartDto> response = cartServices.updateCartItem(userId, itemId, cartItemDto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/user/{userId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartDto>> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Integer itemId) {
        ApiResponse<CartDto> response = cartServices.removeCartItem(userId, itemId);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/user/{userId}/clear")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        ApiResponse<Void> response = cartServices.clearCart(userId);
        return ResponseEntity.ok(response);
    }
} 