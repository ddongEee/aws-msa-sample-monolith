package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAuroraRepository extends OrderRepository, JpaRepository<Order, Long> {
}
