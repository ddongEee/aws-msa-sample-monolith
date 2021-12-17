package com.aws.peach.interfaces.api;


import com.aws.peach.application.OrderViewService;
import com.aws.peach.domain.order.statement.OrderStatementExporter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.aws.peach.application.OrderViewService.*;
import static com.aws.peach.domain.order.statement.OrderStatementExporter.*;

@Slf4j
@RestController
@RequestMapping("/farmer")
public class FarmerController {
    private final OrderViewService orderViewService;

    public FarmerController(final OrderViewService orderViewService) {
        this.orderViewService = orderViewService;
    }

    @GetMapping("/orders/today")
    public List<GroupedOrderStatementDto> todayOrders() {
        return ordersByDate(LocalDate.now());
    }

    @GetMapping("/orders/by-date/{targetDate}")
    public List<GroupedOrderStatementDto> ordersByDate(@PathVariable("targetDate") final LocalDate targetDate) {
        return orderViewService.listOrderByDate(targetDate);
    }
}
