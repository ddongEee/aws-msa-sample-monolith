package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.order.repository.OrderRepositoryCustom;
import com.aws.peach.domain.order.vo.OrderNumber;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class OrderAuroraRepositoryImpl implements OrderRepositoryCustom {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public OrderNumber nextOrderNo() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
        String number = String.format("%s-%d", FORMATTER.format(now), randomNo);
        return new OrderNumber(number);
    }
}
