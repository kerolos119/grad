package org.example.repo;

import jakarta.validation.constraints.NotBlank;

import org.example.document.Products;
import org.example.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {

    Optional<Products> findByProductName(String productName);

    Page<Products> findAll(Specification<Products> spec, Pageable pageable);

    Optional<Products> findByProductId(Long productId); // تغيير اسم الدالة
//    List<Product> findByCategory(ProductCategory category);
}
