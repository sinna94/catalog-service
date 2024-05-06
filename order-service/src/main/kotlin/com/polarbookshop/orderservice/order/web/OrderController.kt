package com.polarbookshop.orderservice.order.web

import com.polarbookshop.orderservice.order.domain.Order
import com.polarbookshop.orderservice.order.domain.OrderService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("orders")
class OrderController(
    private val orderService: OrderService,
) {

    @GetMapping
    fun getAllOrders() = orderService.getAllOrders()

    @PostMapping
    fun submitOrder(
        @RequestBody @Valid orderRequest: OrderRequest,
    ): Mono<Order> {
        return orderService.submitOrder(orderRequest.isbn, orderRequest.quantity)
    }
}