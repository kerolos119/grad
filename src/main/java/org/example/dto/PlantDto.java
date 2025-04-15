package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.document.Users;
import org.example.model.PlantStage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantDto {

    @NotBlank(message = "Plant name required")
    private String plantName;

    private String type;
    @NotBlank(message = "Growth stage required")
    private PlantStage plantStage;

    @Positive(message = "The time required for growth must be a positive number")
    private Integer growthTime;

    private String optimalConditions;

    private String commonDiseases;

    @NotNull(message = "User ID required")
    private Users user;

}