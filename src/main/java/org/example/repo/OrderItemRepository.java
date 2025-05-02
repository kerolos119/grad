package org.example.repo;

import org.example.document.OrderItems;
import org.example.document.Orders;
import org.example.document.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Integer> {
    
    List<OrderItems> findByOrder(Orders order);
    
    List<OrderItems> findByProduct(Products product);
    
    void deleteByOrder(Orders order);
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItems oi WHERE oi.product = ?1")
    Integer getTotalQuantitySold(Products product);
    
    @Query("SELECT oi.product.productId, SUM(oi.quantity) as totalSold FROM OrderItems oi GROUP BY oi.product.productId ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();
} 