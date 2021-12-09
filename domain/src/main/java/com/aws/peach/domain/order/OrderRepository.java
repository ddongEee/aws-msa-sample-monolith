package com.aws.peach.domain.order;

public interface OrderRepository {
    Order save(final Order order);
}
