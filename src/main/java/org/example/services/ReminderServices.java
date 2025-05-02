package org.example.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.document.Plants;
import org.example.document.Reminders;
import org.example.dto.PageResult;
import org.example.dto.ReminderDto;
import org.example.exception.NotFoundException;
import org.example.mapper.ReminderMapper;
import org.example.repo.PlantRepository;
import org.example.repo.ReminderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReminderServices {
    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;
    private final PlantRepository plantRepository;

    /**
     * Create a new reminder
     */
    public ReminderDto create(ReminderDto dto) {
        log.info("Creating new reminder for plant ID: {}", dto.getPlantId());
        if (dto.getPlantId() == null) {
            throw new IllegalArgumentException("Plant ID cannot be null");
        }
        
        // Validate plant exists
        plantRepository.findById(Integer.valueOf(dto.getPlantId()))
                .orElseThrow(() -> new NotFoundException("Plant not found with ID: " + dto.getPlantId()));
        
        Reminders reminder = reminderMapper.toEntity(dto);
        Reminders savedReminder = reminderRepository.save(reminder);
        log.info("Reminder created with ID: {}", savedReminder.getId());
        return reminderMapper.toDto(savedReminder);
    }

    /**
     * Find reminder by ID
     */
    public ReminderDto findById(Integer reminderId) {
        log.info("Finding reminder by ID: {}", reminderId);
        if (reminderId == null) {
            throw new IllegalArgumentException("Reminder ID cannot be null");
        }
        
        Reminders reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NotFoundException("Reminder not found with ID: " + reminderId));
        
        return reminderMapper.toDto(reminder);
    }

    /**
     * Delete a reminder by ID
     */
    public void delete(Integer reminderId) {
        log.info("Deleting reminder with ID: {}", reminderId);
        if (reminderId == null) {
            throw new IllegalArgumentException("Reminder ID cannot be null");
        }
        
        Reminders reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NotFoundException("Reminder not found with ID: " + reminderId));
        
        reminderRepository.delete(reminder);
        log.info("Reminder deleted successfully: {}", reminderId);
    }

    /**
     * Update an existing reminder
     */
    public ReminderDto update(Integer reminderId, ReminderDto dto) {
        log.info("Updating reminder with ID: {}", reminderId);
        if (reminderId == null) {
            throw new IllegalArgumentException("Reminder ID cannot be null");
        }
        
        // Validate that reminder exists
        Reminders existingReminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NotFoundException("Reminder not found with ID: " + reminderId));
        
        // Validate plant exists if changing plant ID
        if (dto.getPlantId() != null && !Integer.valueOf(dto.getPlantId()).equals(existingReminder.getPlant().getPlantId())) {
            plantRepository.findById(Integer.valueOf(dto.getPlantId()))
                    .orElseThrow(() -> new NotFoundException("Plant not found with ID: " + dto.getPlantId()));
        }
        
        reminderMapper.updateToEntity(dto, existingReminder);
        Reminders updatedReminder = reminderRepository.save(existingReminder);
        log.info("Reminder updated successfully: {}", reminderId);
        return reminderMapper.toDto(updatedReminder);
    }

    /**
     * Find reminder by plant ID
     */
    public ReminderDto findByPlant(String plantId) {
        log.info("Finding reminder for plant ID: {}", plantId);
        if (plantId == null || plantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Plant ID cannot be null or empty");
        }

        try {
            // Find the Plant entity first
            Plants plant = plantRepository.findById(Integer.valueOf(plantId.trim()))
                    .orElseThrow(() -> new NotFoundException("Plant not found with ID: " + plantId));
            
            // Find Reminder by Plant entity
            Reminders reminder = reminderRepository.findByPlant(plant)
                    .orElseThrow(() -> new NotFoundException("Reminder not found for plant ID: " + plantId));
            
            return reminderMapper.toDto(reminder);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid plant ID format: " + plantId);
        }
    }

    /**
     * Search reminders with filters
     */
    public PageResult<ReminderDto> search(String plant, String reminderType, Pageable pageable) {
        log.info("Searching reminders with filters - plant: {}, type: {}", plant, reminderType);
        
        Specification<Reminders> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (plant != null && !plant.isEmpty()) {
                predicates.add(cb.equal(root.get("plant").get("plantId"), Integer.valueOf(plant)));
            }
            
            if (reminderType != null && !reminderType.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("reminderType")), "%" + reminderType.toLowerCase() + "%"));
            }
            
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Page<Reminders> reminderPage = reminderRepository.findAll(spec, pageable);
        List<ReminderDto> reminderDtos = reminderPage.getContent().stream()
                .map(reminderMapper::toDto)
                .collect(Collectors.toList());

        log.info("Search returned {} results", reminderDtos.size());
        
        return PageResult.<ReminderDto>builder()
                .items(reminderDtos)
                .totalElement(reminderPage.getTotalElements())
                .totalPages(reminderPage.getTotalPages())
                .currentPage(reminderPage.getNumber() + 1)
                .pageSize(reminderPage.getSize())
                .build();
    }
}
