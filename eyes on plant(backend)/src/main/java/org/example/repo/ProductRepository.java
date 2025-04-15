package org.example.repo;

import jakarta.validation.constraints.NotBlank;
import org.example.document.Product;
import org.example.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    Optional<Product> findByProductName(String productName);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
//    List<Product> findByCategory(ProductCategory category);
}
