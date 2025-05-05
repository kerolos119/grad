package org.example.services;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.document.Plants;
import org.example.document.Reminders;
import org.example.document.Users;
import org.example.model.PushNotificationRequest;
import org.example.model.ReminderType;
import org.example.repo.ReminderRepository;
import org.example.repo.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderNotificationService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final FirebaseNotificationService firebaseNotificationService;

    /**
     * Send a plant care reminder notification to a user's device
     */
    public void sendPlantCareReminder(Users user, Plants plant, ReminderType reminderType) {
        if (user.getDeviceToken() == null || user.getDeviceToken().isEmpty()) {
            log.warn("Cannot send notification to user {} - no device token registered", user.getUserId());
            return;
        }

        String actionText = getActionTextForReminderType(reminderType);
        String title = "Plant Care Reminder";
        String body = "Time to " + actionText + " your " + plant.getPlantName();
        
        Map<String, String> data = new HashMap<>();
        data.put("plantId", plant.getPlantId().toString());
        data.put("plantName", plant.getPlantName());
        data.put("reminderType", reminderType.toString());
        data.put("actionText", actionText);
        data.put("userId", user.getUserId().toString());
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));

        PushNotificationRequest request = PushNotificationRequest.builder()
            .token(user.getDeviceToken())
            .title(title)
            .body(body)
            .data(data)
            .build();
        
        log.info("Sending plant care reminder notification to user {}: {}", user.getUserId(), body);
        firebaseNotificationService.sendNotificationToToken(request);
    }

    /**
     * Send plant care reminder for a specific reminder
     */
    public void sendReminderNotification(Reminders reminder) {
        Users user = reminder.getUser();
        Plants plant = reminder.getPlant();
        sendPlantCareReminder(user, plant, reminder.getReminderType());
    }

    /**
     * Schedule to check and send due reminders every day at 8:00 AM
     */
    @Scheduled(cron = "0 0 8 * * ?")
    @Async
    public void scheduledReminderCheck() {
        log.info("Running scheduled reminder check...");
        sendDueReminders();
    }

    /**
     * Send notifications for all reminders due today
     */
    public void sendDueReminders() {
        LocalDate today = LocalDate.now();
        log.info("Checking for reminders due on {}", today);

        // Build a specification to find all reminders with nextReminderDate = today
        List<Reminders> dueReminders = reminderRepository.findAll()
            .stream()
            .filter(reminder -> today.equals(reminder.getNextReminderDate()))
            .toList();

        log.info("Found {} reminders due today", dueReminders.size());
        
        // Send notifications for each due reminder
        for (Reminders reminder : dueReminders) {
            try {
                sendReminderNotification(reminder);
                
                // Update next reminder date based on frequency
                if (reminder.getFrequency() != null && reminder.getFrequency() > 0) {
                    reminder.setNextReminderDate(today.plusDays(reminder.getFrequency()));
                    reminderRepository.save(reminder);
                    log.info("Updated next reminder date for reminder {} to {}", 
                             reminder.getReminderId(), reminder.getNextReminderDate());
                }
            } catch (Exception e) {
                log.error("Failed to process reminder {}", reminder.getReminderId(), e);
            }
        }
    }

    /**
     * Send a test reminder notification to a specific user
     */
    public void sendTestReminderNotification(Integer userId, Integer plantId, ReminderType reminderType) {
        Users user = userRepository.findById(userId.longValue())
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // If no device token, log warning and return
        if (user.getDeviceToken() == null || user.getDeviceToken().isEmpty()) {
            log.warn("Cannot send test notification - no device token registered for user {}", userId);
            return;
        }

        // Find the reminder for this user and plant
        reminderRepository.findAll().stream()
            .filter(r -> r.getUser().getUserId().equals(userId.longValue()) && 
                       r.getPlant().getPlantId().equals(plantId) &&
                       r.getReminderType().equals(reminderType))
            .findFirst()
            .ifPresentOrElse(
                this::sendReminderNotification,
                () -> log.warn("No reminder found for user {}, plant {}, and type {}", userId, plantId, reminderType)
            );
    }
    
    /**
     * Send a rich notification with custom Android and iOS configuration
     */
    public void sendRichReminderNotification(Users user, Plants plant, ReminderType reminderType) {
        if (user.getDeviceToken() == null || user.getDeviceToken().isEmpty()) {
            log.warn("Cannot send notification to user {} - no device token registered", user.getUserId());
            return;
        }

        String actionText = getActionTextForReminderType(reminderType);
        
        try {
            // Create Android config
            AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                    .setTitle("Plant Care Reminder")
                    .setBody("Time to " + actionText + " your " + plant.getPlantName())
                    .setIcon("ic_plant_notification")
                    .setColor("#4CAF50")
                    .setClickAction("PLANT_CARE_ACTIVITY")
                    .build())
                .build();

            // Create APNS (iOS) config
            ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder()
                    .setCategory("PLANT_CARE_CATEGORY")
                    .setSound("default")
                    .setContentAvailable(true)
                    .build())
                .build();

            // Data payload
            Map<String, String> data = new HashMap<>();
            data.put("plantId", plant.getPlantId().toString());
            data.put("plantName", plant.getPlantName());
            data.put("reminderType", reminderType.toString());
            data.put("actionText", actionText);
            data.put("userId", user.getUserId().toString());
            data.put("timestamp", String.valueOf(System.currentTimeMillis()));

            // Build and send message
            Message message = Message.builder()
                .setToken(user.getDeviceToken())
                .setNotification(Notification.builder()
                    .setTitle("Plant Care Reminder")
                    .setBody("Time to " + actionText + " your " + plant.getPlantName())
                    .build())
                .putAllData(data)
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .build();

            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            log.info("Successfully sent rich notification to user {}: {}", user.getUserId(), response);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to send rich notification to user {}", user.getUserId(), e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get action text for the reminder type
     */
    private String getActionTextForReminderType(ReminderType reminderType) {
        return switch (reminderType) {
            case WATERING -> "water";
            case FERTILIZING -> "fertilize";
            case REPOTTING -> "repot";
            case PRUNING -> "prune";
            case PEST_CHECK -> "check for pests on";
        };
    }
} 