package org.example.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.model.ReminderType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderDto {
    private Integer id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String userName;

    @NotNull(message = "Plant ID is required")
    private Integer plantId;
    
    private String plantName;

    @NotNull(message = "Reminder type is required")
    private ReminderType reminderType;

    @FutureOrPresent(message = "The reminder date must be in the present or future")
    private LocalDate nextReminderDate;

    @Positive(message = "Frequency must be a positive number")
    private Integer frequency;
}