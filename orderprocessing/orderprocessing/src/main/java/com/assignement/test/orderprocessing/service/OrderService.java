package com.assignement.test.orderprocessing.service;
import com.assignement.test.orderprocessing.dto.OrderRequestDto;
import com.assignement.test.orderprocessing.exception.InvalidOrderStateException;
import com.assignement.test.orderprocessing.exception.OrderNotFoundException;
import com.assignement.test.orderprocessing.mapper.OrderMapper;
import com.assignement.test.orderprocessing.model.Order;
import com.assignement.test.orderprocessing.model.OrderStatus;
import com.assignement.test.orderprocessing.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {
    private final OrderRepository orderRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Transactional
    public Order createOrder(OrderRequestDto dto) {
        Order order = OrderMapper.toEntity(dto);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }


    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }


    public List<Order> listOrders(Optional<OrderStatus> status) {
        return status.map(orderRepository::findByStatus)
                .orElseGet(orderRepository::findAll);
    }


    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrder(id);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Only PENDING orders can be cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }


    @Transactional
    public void updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = getOrder(id);
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }


    // Scheduled job: every 5 minutes -> move PENDING -> PROCESSING
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void processPendingOrders() {
        List<Order> pending = orderRepository.findByStatus(OrderStatus.PENDING);
        List<Order> toUpdate = pending.stream().peek(o -> {
            o.setStatus(OrderStatus.PROCESSING);
            o.setUpdatedAt(LocalDateTime.now());
        }).collect(Collectors.toList());


        if (!toUpdate.isEmpty()) {
            orderRepository.saveAll(toUpdate);
        }
    }
}
