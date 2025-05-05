package org.example.services;

import com.google.firebase.messaging.*;
import org.example.model.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseNotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(FirebaseNotificationService.class);

    public void sendNotificationToToken(PushNotificationRequest request) {
        try {
            Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build())
                .putAllData(request.getData())
                .setToken(request.getToken())
                .build();

            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            logger.info("Successfully sent message to token {}: {}", request.getToken(), response);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Failed to send push notification to token {}", request.getToken(), e);
            Thread.currentThread().interrupt();
        }
    }

    public void sendNotificationToTopic(PushNotificationRequest request) {
        try {
            Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build())
                .putAllData(request.getData())
                .setTopic(request.getTopic())
                .build();

            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            logger.info("Successfully sent message to topic {}: {}", request.getTopic(), response);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Failed to send push notification to topic {}", request.getTopic(), e);
            Thread.currentThread().interrupt();
        }
    }

    public void subscribeToTopic(List<String> tokens, String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                .subscribeToTopicAsync(tokens, topic).get();
            
            logger.info("Successfully subscribed {} tokens to topic {}, failures: {}", 
                tokens.size(), topic, response.getFailureCount());
            
            if (response.getFailureCount() > 0) {
                for (int i = 0; i < response.getFailureCount(); i++) {
                    logger.error("Failed to subscribe: {}", response.getErrors().get(i).getReason());
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Failed to subscribe tokens to topic {}", topic, e);
            Thread.currentThread().interrupt();
        }
    }

    public void unsubscribeFromTopic(List<String> tokens, String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                .unsubscribeFromTopicAsync(tokens, topic).get();
            
            logger.info("Successfully unsubscribed {} tokens from topic {}, failures: {}", 
                tokens.size(), topic, response.getFailureCount());
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Failed to unsubscribe tokens from topic {}", topic, e);
            Thread.currentThread().interrupt();
        }
    }
    
    public BatchResponse sendNotificationToMultipleTokens(List<String> tokens, PushNotificationRequest request) {
        try {
            List<Message> messages = new ArrayList<>();
            
            for (String token : tokens) {
                messages.add(Message.builder()
                    .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                    .putAllData(request.getData())
                    .setToken(token)
                    .build());
            }
            
            BatchResponse batchResponse = FirebaseMessaging.getInstance().sendEach(messages);
            logger.info("Successfully sent {} messages, failures: {}", 
                batchResponse.getSuccessCount(), batchResponse.getFailureCount());
            
            return batchResponse;
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to send batch messages", e);
            return null;
        }
    }
} 