package org.example.repo;

import org.example.document.Plants;
import org.example.document.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder,Integer> {
   Optional<Reminder> findByPlant(Plants plant);
    Optional<Reminder> findById(Integer reminderId);

    Page<Reminder> findAll(Specification<Reminder> spec, Pageable pageable);
}
