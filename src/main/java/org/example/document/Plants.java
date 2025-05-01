package org.example.document;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.model.PlantStage;

@Entity
@Table(name = "Plants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Long plantId;

    @Column(name = "plant_name", nullable = false, length = 50)
    @NotBlank(message = "Plant name required")
    private String plantName;

    @Column(name = "type", length = 50)
    private String type;

    @Enumerated(EnumType.STRING) // Store ENUM as a string in the database
    @Column(name = "plant_stage", nullable = false)
    @NotBlank(message = "Growth stage required")
    private PlantStage plantStage;

    @Column(name = "growth_time")
    @Positive(message = "The time required for growth must be a positive number")
    private Integer growthTime;

    @Column(name = "optimal_conditions", columnDefinition = "TEXT")
    @NotEmpty
    private String optimalConditions;

    @Column(name = "common_diseases", columnDefinition = "TEXT")
    @NotEmpty
    private String commonDiseases;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)
    private Users user; // Relationship with the Users table


}