package org.example.repo;

import org.example.document.Products;
import org.example.document.Users;
import org.example.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    Optional<Products> findByProductName(String productName);
    
    Optional<Products> findByProductId(Long productId);
    
    Page<Products> findAll(Specification<Products> spec, Pageable pageable);
    
    List<Products> findByProductCategory(ProductCategory category);
    
    List<Products> findBySeller(Users seller);
    
    @Query("SELECT p FROM Products p WHERE p.isOnSale = true")
    List<Products> findAllOnSale(Pageable pageable);
    
    @Query("SELECT p FROM Products p WHERE p.isIndoor = :isIndoor")
    List<Products> findByIndoorStatus(Boolean isIndoor, Pageable pageable);
    
    List<Products> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("SELECT p FROM Products p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.plantType) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Products> searchByKeyword(String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Products p WHERE p.stockQuantity > 0")
    List<Products> findInStock(Pageable pageable);
}
