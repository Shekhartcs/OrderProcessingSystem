package com.assignement.test.orderprocessing.controller;
import com.assignement.test.orderprocessing.dto.OrderRequestDto;
import com.assignement.test.orderprocessing.dto.OrderResponseDto;
import com.assignement.test.orderprocessing.mapper.OrderMapper;
import com.assignement.test.orderprocessing.model.Order;
import com.assignement.test.orderprocessing.model.OrderStatus;
import com.assignement.test.orderprocessing.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(OrderMapper.toDto(order));
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(OrderMapper.toDto(order));
    }


    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> listOrders(@RequestParam(required = false) OrderStatus status) {
        List<Order> orders = orderService.listOrders(Optional.ofNullable(status));
        List<OrderResponseDto> dtos = orders.stream().map(OrderMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long id) {
        Order ord = orderService.cancelOrder(id);
        return ResponseEntity.ok(OrderMapper.toDto(ord));
    }

}
