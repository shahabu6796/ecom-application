package com.app.ecom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ecom.dto.OrderResponse;
import com.app.ecom.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class OrderController
{
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("X-USER-ID") String userId)
    {
        return orderService.createOrder(userId).map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.CREATED)).orElseGet(() -> ResponseEntity.badRequest().build());

    }
}
