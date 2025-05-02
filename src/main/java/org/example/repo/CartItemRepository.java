package org.example.repo;

import org.example.document.Cart;
import org.example.document.CartItems;
import org.example.document.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Integer> {
    
    List<CartItems> findByCart(Cart cart);
    
    Optional<CartItems> findByCartAndProduct(Cart cart, Products product);
    
    boolean existsByCartAndProduct(Cart cart, Products product);
    
    void deleteByCartAndProduct(Cart cart, Products product);
    
    void deleteByCart(Cart cart);
} 