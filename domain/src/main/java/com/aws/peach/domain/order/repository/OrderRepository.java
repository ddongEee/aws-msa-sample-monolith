package com.aws.peach.domain.order.repository;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.OrderNo;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {

    public OrderNo nextOrderNo();

    public Order save(final Order order);

    public Order findById(String orderId);

    List<Order> findByOrderDate(LocalDate targetDate);
}
