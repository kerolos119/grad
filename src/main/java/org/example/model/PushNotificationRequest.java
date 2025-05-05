package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationRequest {
    private String title;
    private String body;
    private String topic;
    private String token;
    private Map<String, String> data;
    
    // For sending to specific devices
    public boolean isTargetSpecificDevice() {
        return token != null && !token.isEmpty();
    }
    
    // For topic messaging
    public boolean isTargetTopic() {
        return topic != null && !topic.isEmpty();
    }
} 