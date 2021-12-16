package com.aws.peach.domain.inventory.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@EqualsAndHashCode
public class Inventory {
    private String productId;
    private LocalDate date;
    private int count;
}
