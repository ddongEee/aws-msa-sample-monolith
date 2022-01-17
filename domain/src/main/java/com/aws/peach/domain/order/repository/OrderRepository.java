package com.aws.peach.domain.order.repository;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.OrderNumber;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends OrderRepositoryCustom {
    Order save(final Order order);
    List<Order> save(List<Order> orders);
    Optional<Order> findById(Long id);
    List<Order> findByOrderDate(final LocalDate targetDate);
    List<Order> findByOrderNumberIn(final List<OrderNumber> orderNumbers);
}
