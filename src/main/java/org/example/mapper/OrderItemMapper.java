package org.example.mapper;

import org.example.document.OrderItems;
import org.example.document.Orders;
import org.example.document.Products;
import org.example.dto.OrderItemDto;
import org.example.repo.OrderRepository;
import org.example.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper extends AbstractMapper<OrderItemDto, OrderItems> {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    @Autowired
    public OrderItemMapper(OrderRepository orderRepository, ProductRepository productRepository) {
        super(OrderItemDto.class, OrderItems.class);
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public OrderItemDto toDto(OrderItems entity) {
        if (entity == null) {
            return null;
        }
        
        return OrderItemDto.builder()
                .id(entity.getItemId())
                .orderId(entity.getOrder() != null ? entity.getOrder().getOrderId() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getProductId().intValue() : null)
                .productName(entity.getProduct() != null ? entity.getProduct().getProductName() : null)
                .quantity(entity.getQuantity())
                .unitPrice(entity.getPricePerUnit())
                .subtotal(entity.getTotalPrice())
                .build();
    }
    
    @Override
    public OrderItems toEntity(OrderItemDto dto) {
        if (dto == null) {
            return null;
        }
        
        Orders order = null;
        if (dto.getOrderId() != null) {
            order = orderRepository.findById(dto.getOrderId()).orElse(null);
        }
        
        Products product = null;
        if (dto.getProductId() != null) {
            product = productRepository.findById(dto.getProductId().longValue()).orElse(null);
        }
        
        return OrderItems.builder()
                .itemId(dto.getId())
                .order(order)
                .product(product)
                .quantity(dto.getQuantity())
                .pricePerUnit(dto.getUnitPrice())
                .totalPrice(dto.getSubtotal())
                .build();
    }
    
    @Override
    public OrderItems updateToEntity(OrderItemDto dto, OrderItems entity) {
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        
        if (dto.getUnitPrice() != null) {
            entity.setPricePerUnit(dto.getUnitPrice());
        }
        
        if (dto.getSubtotal() != null) {
            entity.setTotalPrice(dto.getSubtotal());
        }
        
        if (dto.getProductId() != null && (entity.getProduct() == null || !dto.getProductId().equals(entity.getProduct().getProductId().intValue()))) {
            Products product = productRepository.findById(dto.getProductId().longValue()).orElse(entity.getProduct());
            entity.setProduct(product);
        }
        
        if (dto.getOrderId() != null && (entity.getOrder() == null || !dto.getOrderId().equals(entity.getOrder().getOrderId()))) {
            Orders order = orderRepository.findById(dto.getOrderId()).orElse(entity.getOrder());
            entity.setOrder(order);
        }
        
        return entity;
    }
}
    
 