package com.aws.peach.application;

import com.aws.peach.domain.inventory.service.InventoryService;
import com.aws.peach.domain.member.Member;
import com.aws.peach.domain.member.MemberRepository;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.*;
import com.aws.peach.domain.product.Products;
import com.aws.peach.domain.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.aws.peach.domain.inventory.service.InventoryService.CheckOrderProduct;

@Component
public class OrderViewService {

    private final OrderRepository  orderRepository;

    public OrderViewService(final OrderRepository orderRepository) {
        this.orderRepository  = orderRepository;
    }

    public Order getOrder(final String orderId) {
        return this.orderRepository.findById(orderId);
    }

}
