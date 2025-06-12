package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.OrderDto;
import org.example.model.ApiResponse;
import org.example.model.OrderStatus;
import org.example.services.OrderNotificationService;
import org.example.services.OrderServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {
    
    private final OrderServices orderServices;
    private final OrderNotificationService orderNotificationService;
    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Get all orders",
        description = "Retrieves all orders in the system",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
        return ResponseEntity.ok(orderServices.getAllOrders());
    }
    
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
        summary = "Get order by ID",
        description = "Retrieves an order by its ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @Parameter(description = "Order ID") 
            @PathVariable Integer orderId) {
        return ResponseEntity.ok(orderServices.getOrderById(orderId));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
        summary = "Get orders by user",
        description = "Retrieves all orders placed by a specific user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByUser(
            @Parameter(description = "User ID") 
            @PathVariable Long userId) {
        return ResponseEntity.ok(orderServices.getOrdersByUser(userId));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
        summary = "Create a new order",
        description = "Creates a new order in the system",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto) {
        ApiResponse<OrderDto> response = orderServices.createOrder(orderDto);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
        summary = "Update an order",
        description = "Updates an existing order",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(
            @Parameter(description = "Order ID") 
            @PathVariable Integer orderId,
            @Valid @RequestBody OrderDto orderDto) {
        ApiResponse<OrderDto> response = orderServices.updateOrder(orderId, orderDto);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Update order status",
        description = "Updates the status of an order",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @Parameter(description = "Order ID") 
            @PathVariable Integer orderId,
            @RequestParam OrderStatus status) {
        ApiResponse<Void> response = orderServices.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Delete an order",
        description = "Deletes an order from the system",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @Parameter(description = "Order ID") 
            @PathVariable Integer orderId) {
        ApiResponse<Void> response = orderServices.deleteOrder(orderId);
        return ResponseEntity.ok(response);
    }
    
    // Notification endpoints
    
    @PostMapping("/{orderId}/notify/status")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Send order status notification",
        description = "Sends a push notification about the order status",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> sendOrderStatusNotification(
            @Parameter(description = "Order ID") 
            @PathVariable Integer orderId) {
        
        orderServices.findOrder(orderId).ifPresent(orderNotificationService::sendOrderStatusNotification);
        
        return ResponseEntity.ok(ApiResponse.success(
                "Notification sent for order ID: " + orderId,
                "Order status notification sent successfully"));
    }
    
    @PostMapping("/{orderId}/notify/payment")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Send payment status notification",
        description = "Sends a push notification about the payment status",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> sendPaymentNotification(
            @Parameter(description = "Order ID") 
            @PathVariable Integer orderId) {
        
        orderServices.findOrder(orderId).ifPresent(orderNotificationService::sendPaymentStatusNotification);
        
        return ResponseEntity.ok(ApiResponse.success(
                "Payment notification sent for order ID: " + orderId,
                "Payment notification sent successfully"));
    }
    
    @PostMapping("/{orderId}/notify/shipping")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Send shipping notification",
        description = "Sends a push notification that the order has shipped",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> sendShippingNotification(
            @Parameter(description = "Order ID") 
            @PathVariable Integer orderId) {
        
        orderServices.findOrder(orderId).ifPresent(orderNotificationService::sendShippingNotification);
        
        return ResponseEntity.ok(ApiResponse.success(
                "Shipping notification sent for order ID: " + orderId,
                "Shipping notification sent successfully"));
    }
    
    @PostMapping("/{orderId}/notify/custom")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Send custom order notification",
        description = "Sends a custom push notification about an order",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> sendCustomOrderNotification(
            @Parameter(description = "Order ID") 
            @PathVariable Integer orderId,
            @RequestParam String title,
            @RequestParam String body,
            @RequestBody(required = false) Map<String, String> data) {
        
        orderServices.findOrder(orderId).ifPresent(order -> 
            orderNotificationService.sendRichOrderNotification(order, title, body, data != null ? data : Map.of()));
        
        return ResponseEntity.ok(ApiResponse.success(
                "Custom notification sent for order ID: " + orderId,
                "Custom order notification sent successfully"));
    }
} 