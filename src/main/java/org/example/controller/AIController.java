package org.example.controller;

import org.example.services.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }
    @PostMapping("/scan")
    public ResponseEntity<String> scanPlant(@RequestParam("file") MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Image file is required");
        }
        
        String result = aiService.scanPlantImage(imageFile);
        return ResponseEntity.ok(result);
    }
    
}
