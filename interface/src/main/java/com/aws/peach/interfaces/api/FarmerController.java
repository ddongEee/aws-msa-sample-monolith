package com.aws.peach.interfaces.api;


import com.aws.peach.application.order.OrderViewService;
import com.aws.peach.application.order.PayOrderService;
import com.aws.peach.application.order.OrderViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

import static com.aws.peach.application.order.OrderViewService.GroupedOrderStatementDto;

@Slf4j
@RestController
@RequestMapping("/farmer")
public class FarmerController {
    private final OrderViewService orderViewService;
    private final PayOrderService payOrderService;

    public FarmerController(final OrderViewService orderViewService,
                            final PayOrderService payOrderService) {
        this.orderViewService = orderViewService;
        this.payOrderService = payOrderService;
    }

    @GetMapping("/orders/today")
    public List<GroupedOrderStatementDto> listTodayUnpaidOrders() {
        return listUnpaidOrdersByDate(LocalDate.now());
    }

    @GetMapping("/orders/by-date/{targetDate}")
    public List<GroupedOrderStatementDto> listUnpaidOrdersByDate(@PathVariable("targetDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate targetDate) {
        return orderViewService.listUnpaidOrderByDate(targetDate);
    }

    @PostMapping("/orders/paid/{orderNumbers}")
    public List<String> updateToPaid(@PathVariable List<String> orderNumbers) {
        return payOrderService.paid(orderNumbers);
    }
}
