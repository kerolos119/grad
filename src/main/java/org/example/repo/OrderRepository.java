package org.example.repo;

import org.example.document.Orders;
import org.example.document.Users;
import org.example.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    
    List<Orders> findByUser(Users user);
    
    List<Orders> findByUserOrderByOrderDateDesc(Users user);
    
    List<Orders> findByStatus(OrderStatus status);
    
    List<Orders> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Orders> findByUserAndStatus(Users user, OrderStatus status);
    
    Long countByUser(Users user);
    
    Long countByStatus(OrderStatus status);
} 