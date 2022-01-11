package com.aws.peach.domain.order.repository;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.OrderNumber;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends OrderRepositoryCustom {
    Order save(final Order order);
    List<Order> save(List<Order> orders);
    Order findById(String orderId);
    List<Order> findByOrderDate(final LocalDate targetDate);
    List<Order> findByOrderNumberIn(final List<OrderNumber> orderNumbers);
}
