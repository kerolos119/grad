package org.example.mapper;

import org.example.document.Plants;
import org.example.document.Reminders;
import org.example.document.Users;
import org.example.dto.ReminderDto;
import org.example.repo.PlantRepository;
import org.example.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReminderMapper extends AbstractMapper<ReminderDto, Reminders> {
    
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    
    @Autowired
    public ReminderMapper(UserRepository userRepository, PlantRepository plantRepository) {
        super(ReminderDto.class, Reminders.class);
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    @Override
    public ReminderDto toDto(Reminders entity) {
        if (entity == null) {
            return null;
        }
        
        return ReminderDto.builder()
                .id(entity.getReminderId())
                .userId(entity.getUser() != null ? entity.getUser().getUserId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .plantId(entity.getPlant() != null ? entity.getPlant().getPlantId() : null)
                .plantName(entity.getPlant() != null ? entity.getPlant().getPlantName() : null)
                .reminderType(entity.getReminderType())
                .nextReminderDate(entity.getNextReminderDate())
                .frequency(entity.getFrequency())
                .build();
    }
    
    @Override
    public Reminders toEntity(ReminderDto dto) {
        if (dto == null) {
            return null;
        }
        
        Users user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId()).orElse(null);
        }
        
        Plants plant = null;
        if (dto.getPlantId() != null) {
            plant = plantRepository.findById(dto.getPlantId()).orElse(null);
        }
        
        return Reminders.builder()
                .reminderId(dto.getId())
                .user(user)
                .plant(plant)
                .reminderType(dto.getReminderType())
                .nextReminderDate(dto.getNextReminderDate())
                .frequency(dto.getFrequency())
                .build();
    }

    @Override
    public Reminders updateToEntity(ReminderDto dto, Reminders entity) {
        if (dto.getReminderType() != null) {
            entity.setReminderType(dto.getReminderType());
        }
        
        if (dto.getNextReminderDate() != null) {
            entity.setNextReminderDate(dto.getNextReminderDate());
        }
        
        if (dto.getFrequency() != null) {
            entity.setFrequency(dto.getFrequency());
        }
        
        if (dto.getUserId() != null) {
            Users user = userRepository.findById(dto.getUserId()).orElse(entity.getUser());
            entity.setUser(user);
        }
        
        if (dto.getPlantId() != null) {
            Plants plant = plantRepository.findById(dto.getPlantId()).orElse(entity.getPlant());
            entity.setPlant(plant);
        }
        
        return entity;
    }
}
