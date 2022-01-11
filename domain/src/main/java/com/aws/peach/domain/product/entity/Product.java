package com.aws.peach.domain.product.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(schema = "samplemonolith", name = "products")
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @Column(name = "productId")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;
}
