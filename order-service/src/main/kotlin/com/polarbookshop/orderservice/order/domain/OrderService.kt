package com.polarbookshop.orderservice.order.domain

import com.polarbookshop.orderservice.book.Book
import com.polarbookshop.orderservice.book.BookClient
import com.polarbookshop.orderservice.order.event.OrderAcceptedMessage
import com.polarbookshop.orderservice.order.event.OrderDispatchedMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val bookClient: BookClient,
    private val streamBridge: StreamBridge
) {

    fun getAllOrders(): Flux<Order> {
        return orderRepository.findAll()
    }

    @Transactional
    fun submitOrder(isbn: String, quantity: Int): Mono<Order> {
        return bookClient.getBookByIsbn(isbn)
            .map { book -> buildAcceptedOrder(book, quantity) }
            .defaultIfEmpty(
                buildRejectedOrder(isbn, quantity)
            )
            .flatMap(orderRepository::save)
            .doOnNext(::publishOrderAcceptedEvent)
    }

    fun consumeOrderDispatchedEvent(flux: Flux<OrderDispatchedMessage>): Flux<Order> {
        return flux
            .flatMap { message -> orderRepository.findById(message.orderId) }
            .map(this::buildDispatchedOrder)
            .flatMap(orderRepository::save)
    }

    private fun buildDispatchedOrder(existingOrder: Order): Order {
        return Order(
            id = existingOrder.id,
            bookIsbn = existingOrder.bookIsbn,
            bookName = existingOrder.bookName,
            bookPrice = existingOrder.bookPrice,
            quantity = existingOrder.quantity,
            status = OrderStatus.DISPATCHED,
            createdDate = existingOrder.createdDate,
            lastModifiedDate = existingOrder.lastModifiedDate,
            version = existingOrder.version
        )
    }

    private fun publishOrderAcceptedEvent(order: Order){
        if(order.status != OrderStatus.ACCEPTED){
            return
        }

        OrderAcceptedMessage(order.id).let { message ->
            log.info("Sending order accepted event with id: {}", order.id)
            val result = streamBridge.send("acceptOrder-out-0", message)
            log.info("Result of sending data for order with id {}: {}", order.id, result)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderService::class.java)

        fun buildAcceptedOrder(book: Book, quantity: Int): Order {
            return Order(
                bookIsbn = book.isbn,
                quantity = quantity,
                status = OrderStatus.ACCEPTED,
                bookName = book.title,
                bookPrice = book.price,
            )
        }

        fun buildRejectedOrder(isbn: String, quantity: Int): Order {
            return Order(
                bookIsbn = isbn,
                quantity = quantity,
                status = OrderStatus.REJECTED
            )
        }
    }
}
