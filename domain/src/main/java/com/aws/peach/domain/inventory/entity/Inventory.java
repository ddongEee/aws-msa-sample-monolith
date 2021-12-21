package com.aws.peach.domain.inventory.entity;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Inventory {
    private String productId;
    private LocalDate date;
    private int count;
}
