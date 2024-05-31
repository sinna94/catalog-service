package com.polarbookshop.dispatcherservice

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Function

@Configuration
class DispatchingFunctions {
    companion object {
        private val log = LoggerFactory.getLogger(DispatchingFunctions::class.java)
    }

    @Bean
    fun pack(): Function<OrderAcceptedMessage, Long> {
        return Function{ orderAcceptedMessage: OrderAcceptedMessage ->
            log.info("The order with id {} is being packed.", orderAcceptedMessage.orderId)
            orderAcceptedMessage.orderId
        }
    }

    @Bean
    fun label(): Function<Flux<Long>, Flux<OrderDispatchedMessage>> {
        return Function{ orderFlux: Flux<Long> ->
            orderFlux.map { orderId ->
                log.info("The order with id {} is labeled.", orderId)
                OrderDispatchedMessage(orderId)
            }
        }
    }
}
