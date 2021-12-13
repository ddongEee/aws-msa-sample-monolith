package com.aws.peach.domain.product.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Product {
    private String id;
    private String name;
    private int price;
}
