package com.aws.peach.domain.order;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.OrderLine;
import com.aws.peach.domain.order.vo.OrderProduct;
import com.aws.peach.domain.order.vo.OrderState;
import com.aws.peach.domain.order.vo.ShippingInformation;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter // for producer(jsonSerialize). can be replace with @JsonProperty("field_name") on each field
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) // for consumer(jsonDeserialize)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for producer
public class OrderStateChangeMessage {
    private String orderNumber;
    private String ordererId;
    private String ordererName;
    private List<OrderLineDto> orderLines;
    private String status;
    private String orderDate;
    private ShippingInformationDto shippingInformation;

    public static OrderStateChangeMessage paidCompleted(final Order order) {
        List<OrderLineDto> orderLines = getOrderLineDtos(order.getOrderLines());
        ShippingInformationDto shipping = getShippingInformationDto(order.getShippingInformation());
        return builder()
                .orderNumber(order.getOrderNo())
                .ordererId(order.getOrdererId())
                .ordererName(order.getOrdererName())
                .orderLines(orderLines)
                .status(OrderState.PAID.name())
                .orderDate(order.getOrderDate().format(DateTimeFormatter.ISO_DATE))
                .shippingInformation(shipping)
                .build();
    }

    private static ShippingInformationDto getShippingInformationDto(ShippingInformation shipping) {
        if (shipping == null) {
            return new ShippingInformationDto();
        }
        return ShippingInformationDto.of(shipping);
    }

    private static List<OrderLineDto> getOrderLineDtos(List<OrderLine> orderLines) {
        if (orderLines == null) {
            return Collections.emptyList();
        }
        return orderLines.stream()
                .map(OrderLineDto::of)
                .collect(Collectors.toList());
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OrderProductDto {
        private String productId;
        private String productName;
        private int price;

        private static OrderProductDto of(OrderProduct op) {
            return new OrderProductDto(op.getProductId(), op.getProductName(), op.getPrice());
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OrderLineDto {
        private OrderProductDto orderProduct;
        private int quantity;

        private static OrderLineDto of(OrderLine ol) {
            OrderProductDto productDto = OrderProductDto.of(ol.getOrderProduct());
            return new OrderLineDto(productDto, ol.getQuantity());
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ShippingInformationDto {
        private String city;
        private String telephoneNumber;
        private String receiver;
        private String address1;
        private String address2;

        private static ShippingInformationDto of(ShippingInformation s) {
            return ShippingInformationDto.builder()
                    .receiver(s.getReceiver())
                    .telephoneNumber(s.getTelephoneNumber())
                    .city(s.getCity())
                    .address1(s.getAddress1())
                    .address2(s.getAddress2())
                    .build();
        }
    }
}
