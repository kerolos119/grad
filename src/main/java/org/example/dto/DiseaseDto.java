package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseDto {
    @NotBlank(message = "Disease name is required")
    private String dName;

    private String affectedParts;

    private String recommendedAction;

    private String symptoms;

    private String treatment;
}
