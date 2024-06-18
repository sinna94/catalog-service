package com.polarbookshop.orderservice.order.event

import com.polarbookshop.orderservice.order.domain.OrderService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux

@Configuration
class OrderFunctions {

    companion object {
        private val log = LoggerFactory.getLogger(OrderFunctions::class.java)
    }

    @Bean
    fun dispatchOrder(orderService: OrderService): (Flux<OrderDispatchedMessage>) -> Unit {
        return { flux ->
            orderService.consumeOrderDispatchedEvent(flux)
                .doOnNext { order -> log.info("The order with id {} is dispatched.", order.id) }
                .subscribe()
        }
    }
}