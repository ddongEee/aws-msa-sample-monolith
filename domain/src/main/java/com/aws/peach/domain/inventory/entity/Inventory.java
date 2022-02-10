package com.aws.peach.domain.inventory.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(schema = "samplemonolith", name = "inventories")
@EqualsAndHashCode(of = "inventoryId")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inventoryId")
    private Long inventoryId;

    @Column(name = "productId")
    private String productId;

    // todo : 어떤 date 인지 구체적인 변수명 필요
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "count")
    private int count;
}
