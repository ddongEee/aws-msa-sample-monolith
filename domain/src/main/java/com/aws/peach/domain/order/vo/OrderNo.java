package com.aws.peach.domain.order.vo;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Builder
@Getter
@EqualsAndHashCode
public class OrderNo implements Serializable {

    private String number;

    private OrderNo() {}

    public OrderNo(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

}