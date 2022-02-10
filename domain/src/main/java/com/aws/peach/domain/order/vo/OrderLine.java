package com.aws.peach.domain.order.vo;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(schema = "samplemonolith", name = "order_lines")
public class OrderLine {
    private static final String PRODUCT_NAME_AND_QUANTITY_FORMAT = " %s(%s)";

    @Id
    @Column(name = "orderLineId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private OrderNumber orderNumber;

    @Embedded
    private OrderProduct orderProduct;

    @Column(name = "quantity")
    private int quantity;

    public int calculateAmounts() {
        return this.orderProduct.getPrice() * quantity;
    }

    public String getProductNameAndQuantity() {
        return String.format(PRODUCT_NAME_AND_QUANTITY_FORMAT, orderProduct.getProductName(), this.quantity);
    }
}
