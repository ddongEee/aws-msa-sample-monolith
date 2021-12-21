package com.aws.peach.interfaces.api;


import com.aws.peach.application.order.OrderViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.aws.peach.application.order.OrderViewService.GroupedOrderStatementDto;

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
    public List<GroupedOrderStatementDto> ordersByDate(@PathVariable("targetDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate targetDate) {
        return orderViewService.listOrderByDate(targetDate);
    }
}
