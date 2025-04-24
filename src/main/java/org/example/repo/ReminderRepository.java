package org.example.repo;

import org.example.document.Plants;

import org.example.document.Reminders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminders,Integer> {
   Optional<Reminders> findByPlant(Plants plant);
    Optional<Reminders> findById(Integer reminderId);

    Page<Reminders> findAll(Specification<Reminders> spec, Pageable pageable);
}
