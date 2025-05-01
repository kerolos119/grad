package org.example.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.document.Plants;

import org.example.document.Users;
import org.example.model.RemiderType;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderDto {
    @NotNull(message = "User ID required")
    private Users user;

    @NotNull(message = "Plant ID required")
    private Plants plant;

    @NotNull(message = "Reminder type required")
    private RemiderType reminderType;

    @FutureOrPresent(message = "The reminder date must be in the present or future.")
    private LocalDate nextReminderDate;

    @Positive(message = "Frequency must be a positive number")
    private Integer frequency;
}