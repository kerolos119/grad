package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.OrderDto;
import org.example.model.ApiResponse;
import org.example.model.OrderStatus;
import org.example.services.OrderServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderServices orderServices;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
        return ResponseEntity.ok(orderServices.getAllOrders());
    }
    
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderServices.getOrderById(orderId));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderServices.getOrdersByUser(userId));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto) {
        ApiResponse<OrderDto> response = orderServices.createOrder(orderDto);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(
            @PathVariable Integer orderId,
            @Valid @RequestBody OrderDto orderDto) {
        ApiResponse<OrderDto> response = orderServices.updateOrder(orderId, orderDto);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam OrderStatus status) {
        ApiResponse<Void> response = orderServices.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Integer orderId) {
        ApiResponse<Void> response = orderServices.deleteOrder(orderId);
        return ResponseEntity.ok(response);
    }
} 