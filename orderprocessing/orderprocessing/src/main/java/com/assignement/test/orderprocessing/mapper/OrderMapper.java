package com.assignement.test.orderprocessing.mapper;
import com.assignement.test.orderprocessing.dto.OrderItemDto;
import com.assignement.test.orderprocessing.dto.OrderRequestDto;
import com.assignement.test.orderprocessing.dto.OrderResponseDto;
import com.assignement.test.orderprocessing.model.Order;
import com.assignement.test.orderprocessing.model.OrderItem;
import com.assignement.test.orderprocessing.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
public class OrderMapper {
    public static Order toEntity(OrderRequestDto dto) {
        Order order = Order.builder()
                .customerId(dto.getCustomerId())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        List<OrderItem> items = dto.getItems().stream().map(i -> {
            OrderItem oi = OrderItem.builder()
                    .productName(i.getProductName())
                    .quantity(i.getQuantity())
                    .price(i.getPrice())
                    .build();
            oi.setOrder(order);
            return oi;
        }).collect(Collectors.toList());


        order.setItems(items);
        return order;
    }


    public static OrderResponseDto toDto(Order order) {
        List<OrderItemDto> items = order.getItems().stream().map(i -> OrderItemDto.builder()
                .id(i.getId())
                .productName(i.getProductName())
                .quantity(i.getQuantity())
                .price(i.getPrice())
                .build()).collect(Collectors.toList());


        return OrderResponseDto.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items)
                .build();
    }
}
