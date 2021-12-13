package com.aws.peach.domain.order.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderProduct {
    private String productId;
    private String productName;
    private int price;
}
/*
 주문 상품 Context 는 Product 의 Context 와 다르다.
 주문 상품은 주문하는 시점의 Product 이다. 즉, 상품  관리 에서 상품의 가격은 상품의 가격과 다르다
 */