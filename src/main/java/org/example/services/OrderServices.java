package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.document.Orders;
import org.example.document.Users;
import org.example.dto.OrderDto;
import org.example.mapper.OrderMapper;
import org.example.model.ApiResponse;
import org.example.model.OrderStatus;
import org.example.repo.OrderRepository;
import org.example.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServices {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    
    public ApiResponse<List<OrderDto>> getAllOrders() {
        List<Orders> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        
        return ApiResponse.success(orderDtos, "Orders retrieved successfully");
    }
    
    public ApiResponse<OrderDto> getOrderById(Integer orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        
        OrderDto orderDto = orderMapper.toDto(order);
        
        return ApiResponse.success(orderDto, "Order retrieved successfully");
    }
    
    public ApiResponse<List<OrderDto>> getOrdersByUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        List<Orders> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        
        return ApiResponse.success(orderDtos, "User orders retrieved successfully");
    }
    
    @Transactional
    public ApiResponse<OrderDto> createOrder(OrderDto orderDto) {
        Orders order = orderMapper.toEntity(orderDto);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        
        Orders savedOrder = orderRepository.save(order);
        
        return ApiResponse.created(orderMapper.toDto(savedOrder), "Order created successfully");
    }
    
    @Transactional
    public ApiResponse<OrderDto> updateOrder(Integer orderId, OrderDto orderDto) {
        Orders existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        
        Orders updatedOrder = orderMapper.updateToEntity(orderDto, existingOrder);
        Orders savedOrder = orderRepository.save(updatedOrder);
        
        return ApiResponse.success(orderMapper.toDto(savedOrder), "Order updated successfully");
    }
    
    @Transactional
    public ApiResponse<Void> updateOrderStatus(Integer orderId, OrderStatus status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        
        order.setStatus(status);
        orderRepository.save(order);
        
        return ApiResponse.success(null, "Order status updated to " + status);
    }
    
    @Transactional
    public ApiResponse<Void> deleteOrder(Integer orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }
        
        orderRepository.deleteById(orderId);
        
        return ApiResponse.noContent("Order deleted successfully");
    }
} 