package com.assignement.test.orderprocessing.repository;

import com.assignement.test.orderprocessing.model.Order;
import com.assignement.test.orderprocessing.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
}

