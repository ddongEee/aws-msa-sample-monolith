package com.aws.peach.domain.order.repository;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.OrderNo;

public interface OrderRepository {
    public OrderNo nextOrderNo();
    public Order save(final Order order);
}
