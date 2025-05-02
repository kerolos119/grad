package org.example.mapper;

import org.example.document.Orders;
import org.example.document.Users;
import org.example.dto.OrderDto;
import org.example.dto.OrderItemDto;
import org.example.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper extends AbstractMapper<OrderDto, Orders> {
    
    private final UserRepository userRepository;
    private final OrderItemMapper orderItemMapper;
    
    @SuppressWarnings("unused")
    private Class<?> unusedButNeeded = OrderItemDto.class;
    
    @Autowired
    public OrderMapper(UserRepository userRepository, OrderItemMapper orderItemMapper) {
        super(OrderDto.class, Orders.class);
        this.userRepository = userRepository;
        this.orderItemMapper = orderItemMapper;
    }
    
    @Override
    public OrderDto toDto(Orders entity) {
        if (entity == null) {
            return null;
        }
        
        OrderDto dto = OrderDto.builder()
                .id(entity.getOrderId())
                .userId(entity.getUser() != null ? entity.getUser().getUserId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .orderDate(entity.getOrderDate())
                .totalAmount(entity.getTotalAmount())
                .shippingAddress(entity.getShippingAddress())
                .billingAddress(entity.getBillingAddress())
                .status(entity.getStatus())
                .paymentMethod(entity.getPaymentMethod())
                .paymentStatus(entity.getPaymentStatus())
                .trackingNumber(entity.getTrackingNumber())
                .build();
        
        if (entity.getOrderItems() != null) {
            dto.setOrderItems(entity.getOrderItems().stream()
                    .map(orderItemMapper::toDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    @Override
    public Orders toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        
        Users user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId()).orElse(null);
        }
        
        Orders entity = Orders.builder()
                .orderId(dto.getId())
                .user(user)
                .orderDate(dto.getOrderDate())
                .totalAmount(dto.getTotalAmount())
                .shippingAddress(dto.getShippingAddress())
                .billingAddress(dto.getBillingAddress())
                .status(dto.getStatus())
                .paymentMethod(dto.getPaymentMethod())
                .paymentStatus(dto.getPaymentStatus())
                .trackingNumber(dto.getTrackingNumber())
                .build();
        
        return entity;
    }
    
    @Override
    public Orders updateToEntity(OrderDto dto, Orders entity) {
        if (dto.getOrderDate() != null) {
            entity.setOrderDate(dto.getOrderDate());
        }
        
        if (dto.getTotalAmount() != null) {
            entity.setTotalAmount(dto.getTotalAmount());
        }
        
        if (dto.getShippingAddress() != null) {
            entity.setShippingAddress(dto.getShippingAddress());
        }
        
        if (dto.getBillingAddress() != null) {
            entity.setBillingAddress(dto.getBillingAddress());
        }
        
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        
        if (dto.getPaymentMethod() != null) {
            entity.setPaymentMethod(dto.getPaymentMethod());
        }
        
        if (dto.getPaymentStatus() != null) {
            entity.setPaymentStatus(dto.getPaymentStatus());
        }
        
        if (dto.getTrackingNumber() != null) {
            entity.setTrackingNumber(dto.getTrackingNumber());
        }
        
        if (dto.getUserId() != null) {
            Users user = userRepository.findById(dto.getUserId()).orElse(entity.getUser());
            entity.setUser(user);
        }
        
        return entity;
    }
} 