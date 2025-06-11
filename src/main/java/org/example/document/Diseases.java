package org.example.document;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Users user;
}
