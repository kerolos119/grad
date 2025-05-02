package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantDto {
    private Integer id;

    @NotBlank(message = "Plant name is required")
    @Size(max = 100, message = "Plant name cannot exceed 100 characters")
    private String plantName;

    @Size(max = 100, message = "Scientific name cannot exceed 100 characters")
    private String scientificName;

    @Size(max = 50, message = "Plant type cannot exceed 50 characters")
    private String type;

    private String description;

    private Long userId;
    
    private String userName;
}