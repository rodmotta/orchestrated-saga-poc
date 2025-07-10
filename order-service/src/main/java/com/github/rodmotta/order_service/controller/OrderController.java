package com.github.rodmotta.order_service.controller;

import com.github.rodmotta.order_service.entity.Order;
import com.github.rodmotta.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createOrder() {
        orderService.create();
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Order> getOrders() {
        return orderService.getOrders();
    }
}
