package org.example.services;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.document.Orders;
import org.example.document.Users;
import org.example.model.OrderStatus;
import org.example.model.PaymentStatus;
import org.example.model.PushNotificationRequest;
import org.example.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderNotificationService {

    private final FirebaseNotificationService notificationService;
    private final OrderRepository orderRepository;
    
    /**
     * Notify user about order status change
     */
    public void sendOrderStatusNotification(Orders order) {
        Users user = order.getUser();
        if (user.getDeviceToken() == null || user.getDeviceToken().isEmpty()) {
            log.warn("Cannot send notification to user {} - no device token registered", user.getUserId());
            return;
        }
        
        String title = "Order Status Update";
        String body = getOrderStatusNotificationBody(order);
        
        Map<String, String> data = new HashMap<>();
        data.put("orderId", order.getOrderId().toString());
        data.put("orderStatus", order.getStatus().toString());
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        data.put("notificationType", "ORDER_STATUS");
        
        PushNotificationRequest request = PushNotificationRequest.builder()
            .token(user.getDeviceToken())
            .title(title)
            .body(body)
            .data(data)
            .build();
        
        notificationService.sendNotificationToToken(request);
        
        // Update notification status
        order.setStatusNotificationSent(true);
        orderRepository.save(order);
        log.info("Order status notification sent for order: {}", order.getOrderId());
    }
    
    /**
     * Notify user about payment status change
     */
    public void sendPaymentStatusNotification(Orders order) {
        Users user = order.getUser();
        if (user.getDeviceToken() == null || user.getDeviceToken().isEmpty()) {
            log.warn("Cannot send notification to user {} - no device token registered", user.getUserId());
            return;
        }
        
        String title = "Payment Update";
        String body = getPaymentStatusNotificationBody(order);
        
        Map<String, String> data = new HashMap<>();
        data.put("orderId", order.getOrderId().toString());
        data.put("paymentStatus", order.getPaymentStatus().toString());
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        data.put("notificationType", "PAYMENT_STATUS");
        
        PushNotificationRequest request = PushNotificationRequest.builder()
            .token(user.getDeviceToken())
            .title(title)
            .body(body)
            .data(data)
            .build();
        
        notificationService.sendNotificationToToken(request);
        
        // Update notification status
        order.setPaymentNotificationSent(true);
        orderRepository.save(order);
        log.info("Payment status notification sent for order: {}", order.getOrderId());
    }
    
    /**
     * Notify user that their order has shipped
     */
    public void sendShippingNotification(Orders order) {
        Users user = order.getUser();
        if (user.getDeviceToken() == null || user.getDeviceToken().isEmpty()) {
            log.warn("Cannot send notification to user {} - no device token registered", user.getUserId());
            return;
        }
        
        if (order.getStatus() != OrderStatus.SHIPPED) {
            log.warn("Attempted to send shipping notification for non-shipped order: {}", order.getOrderId());
            return;
        }
        
        String title = "Your Order Has Shipped!";
        String body = "Your order #" + order.getOrderId() + " has shipped";
        if (order.getTrackingNumber() != null && !order.getTrackingNumber().isEmpty()) {
            body += ". Tracking number: " + order.getTrackingNumber();
        }
        
        Map<String, String> data = new HashMap<>();
        data.put("orderId", order.getOrderId().toString());
        data.put("trackingNumber", order.getTrackingNumber() != null ? order.getTrackingNumber() : "");
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        data.put("notificationType", "ORDER_SHIPPED");
        
        PushNotificationRequest request = PushNotificationRequest.builder()
            .token(user.getDeviceToken())
            .title(title)
            .body(body)
            .data(data)
            .build();
        
        notificationService.sendNotificationToToken(request);
        
        // Update notification status
        order.setShippingNotificationSent(true);
        orderRepository.save(order);
        log.info("Shipping notification sent for order: {}", order.getOrderId());
    }
    
    /**
     * Send a rich notification with direct FCM implementation
     */
    public void sendRichOrderNotification(Orders order, String title, String body, Map<String, String> data) {
        Users user = order.getUser();
        if (user.getDeviceToken() == null || user.getDeviceToken().isEmpty()) {
            log.warn("Cannot send notification to user {} - no device token registered", user.getUserId());
            return;
        }
        
        try {
            Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .setImage("https://example.com/order-image.jpg") // Optional image URL
                    .build())
                .putAllData(data)
                .setToken(user.getDeviceToken())
                .build();
                
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            log.info("Successfully sent rich notification for order {}: {}", order.getOrderId(), response);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to send rich notification for order {}", order.getOrderId(), e);
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Generate notification body based on order status
     */
    private String getOrderStatusNotificationBody(Orders order) {
        return switch(order.getStatus()) {
            case PENDING -> "Your order #" + order.getOrderId() + " has been received and is pending processing.";
            case PROCESSING -> "We're preparing your order #" + order.getOrderId() + " for shipment.";
            case SHIPPED -> "Your order #" + order.getOrderId() + " has been shipped!";
            case DELIVERED -> "Your order #" + order.getOrderId() + " has been delivered!";
            case CANCELLED -> "Your order #" + order.getOrderId() + " has been cancelled.";
            default -> "Your order #" + order.getOrderId() + " status has been updated.";
        };
    }
    
    /**
     * Generate notification body based on payment status
     */
    private String getPaymentStatusNotificationBody(Orders order) {
        PaymentStatus status = order.getPaymentStatus();
        return switch(status) {
            case PENDING -> "Payment for order #" + order.getOrderId() + " is pending.";
            case PAID -> "Payment for order #" + order.getOrderId() + " has been completed. Thank you!";
            case FAILED -> "Payment for order #" + order.getOrderId() + " has failed. Please update your payment method.";
            case REFUNDED -> "Payment for order #" + order.getOrderId() + " has been refunded.";
        };
    }
} 
