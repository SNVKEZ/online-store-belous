package org.ssu.belous.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssu.belous.dto.CreateOrderDto;
import org.ssu.belous.model.Order;
import org.ssu.belous.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create_order")
    public ResponseEntity<Order> create(@RequestBody CreateOrderDto createOrderDto) {
        Order order = orderService.createOrder(createOrderDto.product(), createOrderDto.quantity());
        return ResponseEntity.ok(order);
    }
}