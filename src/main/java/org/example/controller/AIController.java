package org.example.controller;

import org.example.services.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/predict")
    public ResponseEntity<String> predict(@RequestBody Map<String, String> body) {
        String prompt = body.get("prompt");
        if (prompt == null || prompt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Prompt cannot be empty");
        }
        
        String response = aiService.callAI(prompt);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/scan")
    public ResponseEntity<String> scanPlant(@RequestParam("file") MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Image file is required");
        }
        
        String result = aiService.scanPlantImage(imageFile);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/plant-care/{plantName}")
    public ResponseEntity<String> getPlantCareAdvice(@PathVariable String plantName) {
        if (plantName == null || plantName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Plant name cannot be empty");
        }
        
        String advice = aiService.getPlantCareAdvice(plantName);
        return ResponseEntity.ok(advice);
    }
}
