package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.document.Cart;
import org.example.document.CartItems;
import org.example.document.Products;
import org.example.document.Users;
import org.example.dto.CartDto;
import org.example.dto.CartItemDto;
import org.example.mapper.CartMapper;
import org.example.model.ApiResponse;
import org.example.repo.CartItemRepository;
import org.example.repo.CartRepository;
import org.example.repo.ProductRepository;
import org.example.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServices {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    
    public ApiResponse<CartDto> getCartByUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        return ApiResponse.success(cartMapper.toDto(cart), "Cart retrieved successfully");
    }
    
    private Cart createNewCart(Users user) {
        Cart newCart = Cart.builder()
                .user(user)
                .build();
        
        return cartRepository.save(newCart);
    }
    
    @Transactional
    public ApiResponse<CartDto> addItemToCart(Long userId, CartItemDto cartItemDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        Products product = productRepository.findById(cartItemDto.getProductId().longValue())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + cartItemDto.getProductId()));
        
        // Check if product is in stock
        if (product.getStockQuantity() < cartItemDto.getQuantity()) {
            return ApiResponse.badRequest("Not enough stock available. Available: " + product.getStockQuantity());
        }
        
        // Get or create user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        // Check if the product is already in cart
        Optional<CartItems> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Update existing item quantity
            CartItems cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            // Add new item to cart
            CartItems newItem = CartItems.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemDto.getQuantity())
                    .build();
            
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }
        
        Cart updatedCart = cartRepository.save(cart);
        return ApiResponse.success(cartMapper.toDto(updatedCart), "Item added to cart successfully");
    }
    
    @Transactional
    public ApiResponse<CartDto> updateCartItem(Long userId, Integer itemId, CartItemDto cartItemDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user with ID: " + userId));
        
        CartItems cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: " + itemId));
        
        // Ensure the item belongs to the user's cart
        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            return ApiResponse.badRequest("This item does not belong to your cart");
        }
        
        // Check stock if quantity is being updated
        if (cartItemDto.getQuantity() != null && cartItemDto.getQuantity() > 0) {
            Products product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItemDto.getQuantity()) {
                return ApiResponse.badRequest("Not enough stock available. Available: " + product.getStockQuantity());
            }
            
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItemRepository.save(cartItem);
        }
        
        return ApiResponse.success(cartMapper.toDto(cart), "Cart item updated successfully");
    }
    
    @Transactional
    public ApiResponse<CartDto> removeCartItem(Long userId, Integer itemId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user with ID: " + userId));
        
        CartItems cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: " + itemId));
        
        // Ensure the item belongs to the user's cart
        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            return ApiResponse.badRequest("This item does not belong to your cart");
        }
        
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        
        Cart updatedCart = cartRepository.save(cart);
        return ApiResponse.success(cartMapper.toDto(updatedCart), "Item removed from cart successfully");
    }
    
    @Transactional
    public ApiResponse<Void> clearCart(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user with ID: " + userId));
        
        cartItemRepository.deleteByCart(cart);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        
        return ApiResponse.noContent("Cart cleared successfully");
    }
} 