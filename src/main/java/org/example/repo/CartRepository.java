package org.example.repo;

import org.example.document.Cart;
import org.example.document.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    
    Optional<Cart> findByUser(Users user);
    
    boolean existsByUser(Users user);
    
    void deleteByUser(Users user);
} 