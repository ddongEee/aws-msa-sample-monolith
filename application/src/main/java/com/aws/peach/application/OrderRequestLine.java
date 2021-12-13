package com.aws.peach.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@Builder
@Getter
@AllArgsConstructor(staticName = "of")
public class OrderRequestLine {
    private String productId;
    private int quantity;

}
