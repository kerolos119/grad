package org.example.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {
    private final RestTemplate restTemplate;
    @Value("${ai.service.url:http://127.0.0.1:5000/predict}")
    private String apiUrl;

    public AIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Process plant image from mobile scanner
     * @param imageFile Image captured by mobile device
     * @return Plant identification and care information
     */
    public String scanPlantImage(MultipartFile imageFile) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Convert image to Base64
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("image", base64Image);
            requestBody.put("source", "mobile_scanner");
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Error processing image: " + e.getMessage();
        }
    }

    /**
     * Text-based AI plant care assistant
     * @param prompt User's text query about plant care
     * @return AI response with plant care advice
     */
    public String callAI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        return response.getBody();
    }
    
    public String getPlantCareAdvice(String plantName) {
        String prompt = "Provide care tips for " + plantName;
        return callAI(prompt);
    }
}
