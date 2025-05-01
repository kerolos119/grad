package org.example.document;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.CareGuide;

@Entity
@Table(name = "KeyAwareness" )
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeyAwareness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key_id")
    private Integer keyId;

    @Column(name = "interesting_story",columnDefinition = "TEXT")
    private String interestingStory ;

    @Enumerated(EnumType.STRING)
    @Column(name = "care_guide")
    @NotBlank(message = "Care Guide Required" )
    private CareGuide careGuide;

    @Column(name = "plant_description" , columnDefinition = "TEXT")
    private String plantDescription;


}
