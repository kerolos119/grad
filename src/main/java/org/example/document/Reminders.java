package org.example.document;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.model.ReminderType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Reminders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id")
    private Integer reminderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull(message = "User is required")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", referencedColumnName = "plant_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull(message = "Plant is required")
    private Plants plant;

    @Enumerated(EnumType.STRING)
    @Column(name = "reminder_type", nullable = false)
    @NotNull(message = "Reminder type is required")
    private ReminderType reminderType;

    @Column(name = "next_reminder_date")
    @FutureOrPresent(message = "The reminder date must be in the present or future")
    private LocalDate nextReminderDate;

    @Column(name = "frequency")
    @Positive(message = "Frequency must be positive")
    private Integer frequency;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Method to return the ID (for compatibility with services)
    public Integer getId() {
        return reminderId;
    }

    //@Enumerated(EnumType.STRING)
    //@Column(name = "status")
    //private ReminderStatus status = ReminderStatus.PENDING; // حالة التذكير (افتراضي: Pending)

    //@Column(name = "created_at", updatable = false, nullable = false)
   // private LocalDate createdAt = LocalDate.now();

    //@Column(name = "updated_at", nullable = false)
    //private LocalDate updatedAt = LocalDate.now();

    //@PreUpdate
    //protected void onUpdate() {
       // updatedAt = LocalDate.now();
    //}
}
