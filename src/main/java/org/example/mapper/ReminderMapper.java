package org.example.mapper;

import org.example.document.Reminders;
import org.example.dto.ReminderDto;
import org.springframework.stereotype.Component;

@Component
public  class ReminderMapper extends AbstractMapper<ReminderDto, Reminders> {
    public ReminderMapper(){
        super(ReminderDto.class,Reminders.class);
    }

    @Override
    public Reminders updateToEntity(ReminderDto dto, Reminders entity) {
        entity.setRemiderType(dto.getReminderType());
        entity.setNextReminderDate(dto.getNextReminderDate());
        entity.setFrequency(dto.getFrequency());
        return entity;
    }

}
