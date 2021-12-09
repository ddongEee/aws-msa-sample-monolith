package com.aws.peach.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//@Builder
@Getter
@AllArgsConstructor(staticName = "of")
public class OrderProduct {
    private String productId;
    private int quantity;
}
