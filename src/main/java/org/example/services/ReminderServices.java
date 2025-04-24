package org.example.services;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.document.Plants;
import org.example.document.Reminders;
import org.example.dto.PageResult;
import org.example.dto.ReminderDto;
import org.example.exception.ReminderNotFoundException;
import org.example.mapper.ReminderMapper;
import org.example.repo.PlantRepository;
import org.example.repo.ReminderRepository;
import org.example.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReminderServices {
    private  final ReminderRepository repository;
    private final ReminderMapper mapper;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public ReminderDto create(ReminderDto dto) {
        Reminders reminder = mapper.toEntity(dto);
        Reminders result= repository.save(reminder);
        return mapper.toDto(result);
    }

    public void delete(Integer reminderId) {
        Reminders reminder = repository.findById(reminderId)
                .orElseThrow(()-> new ReminderNotFoundException("Reminder not found"));
        repository.delete(reminder);
    }

    public ReminderDto update(Integer reminderId, ReminderDto dto) {
        Reminders reminder = repository.findById(reminderId).orElseThrow(()->new ReminderNotFoundException("Reminder not found"));
        mapper.updateToEntity(dto,reminder);

        Reminders updateReminder = repository.save(reminder);
        return mapper.toDto(updateReminder);
    }

    public ReminderDto findByPlant(String plantId) {
        if (plantId == null || plantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Plant ID cannot be null or empty");
        }

        // 1. Find the Plant entity first
        Plants plant = plantRepository.findById(Integer.valueOf(plantId.trim()))
                .orElseThrow(() -> new IllegalArgumentException("Plant not found with ID: " + plantId));

        // 2. Find Reminder by Plant entity
        return repository.findByPlant(plant)
                .map(mapper::toDto)
                .orElseThrow(() -> new ReminderNotFoundException("Reminder not found for plant: " + plantId));
    }

    public PageResult<ReminderDto> search(String plant, String reminderType, Pageable pageable) {
        Specification<Reminders> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (plant != null) {
                predicates.add(cb.like(root.get("plant"), "%" + plant + "%"));
            }
            if (reminderType != null) {
                predicates.add(cb.like(root.get("reminderType"), "%" + reminderType + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Reminders> reminderPage = repository.findAll(spec, pageable);
        List<ReminderDto> reminderDtos = reminderPage.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return new PageResult<>(
                reminderDtos,
                reminderPage.getTotalElements(),
                reminderPage.getTotalPages(),
                reminderPage.getNumber() + 1,
                reminderPage.getSize()
        );
    }
}
