package org.example.controller;

import org.example.model.PushNotificationRequest;
import org.example.services.FirebaseNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.model.ApiResponse;
import org.example.model.ReminderType;
import org.example.services.ReminderNotificationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notifications", description = "Notification management APIs")
public class NotificationController {

    @Autowired
    private FirebaseNotificationService notificationService;

    @Autowired
    private ReminderNotificationService reminderNotificationService;

    @PostMapping("/token")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> sendNotificationToToken(@RequestBody PushNotificationRequest request) {
        notificationService.sendNotificationToToken(request);
        return new ResponseEntity<>("Notification sent to token: " + request.getToken(), HttpStatus.OK);
    }

    @PostMapping("/topic")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> sendNotificationToTopic(@RequestBody PushNotificationRequest request) {
        notificationService.sendNotificationToTopic(request);
        return new ResponseEntity<>("Notification sent to topic: " + request.getTopic(), HttpStatus.OK);
    }
    
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToTopic(
            @RequestParam List<String> tokens,
            @RequestParam String topic) {
        notificationService.subscribeToTopic(tokens, topic);
        return new ResponseEntity<>("Tokens subscribed to topic: " + topic, HttpStatus.OK);
    }
    
    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeFromTopic(
            @RequestParam List<String> tokens,
            @RequestParam String topic) {
        notificationService.unsubscribeFromTopic(tokens, topic);
        return new ResponseEntity<>("Tokens unsubscribed from topic: " + topic, HttpStatus.OK);
    }
    
    @PostMapping("/tokens")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> sendNotificationToMultipleTokens(
            @RequestParam List<String> tokens,
            @RequestBody PushNotificationRequest request) {
        notificationService.sendNotificationToMultipleTokens(tokens, request);
        return new ResponseEntity<>("Notifications sent to multiple tokens", HttpStatus.OK);
    }
    
    @PostMapping("/reminder")
    @Operation(
        summary = "Send a plant care reminder",
        description = "Sends a plant care reminder notification to a user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> sendReminderNotification(
            @Parameter(description = "User ID") @RequestParam Integer userId,
            @Parameter(description = "Plant ID") @RequestParam Integer plantId,
            @Parameter(description = "Reminder type") @RequestParam ReminderType reminderType) {
        
        reminderNotificationService.sendTestReminderNotification(userId, plantId, reminderType);
        
        return ResponseEntity.ok(ApiResponse.success(
                "Notification sent",
                "Plant care reminder notification sent successfully"));
    }

    @PostMapping("/send-plant-reminder")
    @Operation(
        summary = "Send a plant reminder notification",
        description = "Sends a notification for plant care directly to a device token",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> sendPlantReminder(
            @RequestParam String token, 
            @RequestParam String plantName, 
            @RequestParam String reminderType) {
        
        PushNotificationRequest request = PushNotificationRequest.builder()
            .token(token)
            .title("Plant Care Reminder")
            .body("Time to " + reminderType.toLowerCase() + " your " + plantName)
            .data(Map.of(
                "plantName", plantName,
                "reminderType", reminderType,
                "timestamp", String.valueOf(System.currentTimeMillis())
            ))
            .build();
        
        notificationService.sendNotificationToToken(request);
        return ResponseEntity.ok(ApiResponse.success(
                "Plant reminder sent to token: " + token,
                "Plant care reminder notification sent successfully"));
    }
} 