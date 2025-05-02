@RestController
@RequestMapping("/api/v1/diseases")
public class DiseasesController {
    private final DiseasesServices services;
    
    public DiseasesController(DiseasesServices services) {
        this.services = services;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DiseaseDto> create(@Valid @RequestBody DiseaseDto dto) {
        DiseaseDto diseases = services.create(dto);
        return ApiResponse.created(diseases);
    }
} 