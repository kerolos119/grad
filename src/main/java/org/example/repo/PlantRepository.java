package org.example.repo;

import org.example.document.Plants;
import org.example.model.PlantStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plants,Integer> {
    @NonNull
    Optional<Plants> findById(@NonNull Integer plantId);

    Plants findByPlantName(String plantName);

    Plants findByType(String type);

    Plants findByPlantStage (PlantStage plantStage);

    Page<Plants> findAll(Specification<Plants> spec, Pageable pageable);


    List<Plants> findByUser_UserId(Long userId); // البحث حسب user_id

}
