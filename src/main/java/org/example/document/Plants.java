package org.example.document;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.model.PlantStage;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "plants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Integer plantId;

    @Column(name = "plant_name", nullable = false, length = 100)
    @NotBlank(message = "Plant name is required")
    @Size(max = 100, message = "Plant name cannot exceed 100 characters")
    private String plantName;

    @Column(name = "scientific_name", length = 100)
    @Size(max = 100, message = "Scientific name cannot exceed 100 characters")
    private String scientificName;

    @Column(name = "type", length = 50)
    @Size(max = 50, message = "Plant type cannot exceed 50 characters")
    private String type;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "plant_stage")
    private PlantStage plantStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Users user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}