package org.example.document;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Diseases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diseases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Long diseaseId;

    @Column(name = "disease_name",length = 50 ,nullable = false)
    @NotBlank(message = "Disease name is required")
    private String dName;

    @Column(name = "affected_parts",columnDefinition = "TEXT")
    private String affectedParts;

    @Column(name = "recommended_action",columnDefinition = "TEXT")
    private String recommendedAction;

    @Column(name = "symptoms",columnDefinition = "TEXT")
    private String symptoms;

    @Column(name = "treatment",columnDefinition = "TEXT")
    private String treatment;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)
    private Users user;
}
