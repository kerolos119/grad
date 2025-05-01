package org.example.repo;



import org.example.document.Diseases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

@Repository
public interface DiseaseRepository extends JpaRepository<Diseases,Long> {

    Page<Diseases> findAll(Specification<Diseases> spec, Pageable pageable);

    void deleteById(@NonNull Long id);
}
