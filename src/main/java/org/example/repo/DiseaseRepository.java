package org.example.repo;

import org.example.document.Disease;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease,Long> {

    Page<Disease> findAll(Specification<Disease> spec, Pageable pageable);

    void deleteById(Long id);
}
