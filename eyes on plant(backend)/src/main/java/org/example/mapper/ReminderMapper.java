package org.example.mapper;

import org.example.document.Reminder;
import org.example.dto.ReminderDto;
import org.springframework.stereotype.Component;

@Component
public class ReminderMapper extends AbstractMapper<ReminderDto,Reminder> {
    public ReminderMapper(){
        super(ReminderDto.class,Reminder.class);
    }

    @Override
    public Reminder updateToEntity(ReminderDto dto, Reminder entity) {
        entity.setRemiderType(dto.getReminderType());
        entity.setNextReminderDate(dto.getNextReminderDate());
        entity.setFrequency(dto.getFrequency());
        return entity;
    }
}
