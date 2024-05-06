package com.polarbookshop.orderservice.order.domain

import com.polarbookshop.orderservice.book.Book
import com.polarbookshop.orderservice.book.BookClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val bookClient: BookClient,
) {

    fun getAllOrders(): Flux<Order> {
        return orderRepository.findAll()
    }

    fun submitOrder(isbn: String, quantity: Int): Mono<Order> {
        return bookClient.getBookByIsbn(isbn)
            .map { book -> buildAcceptedOrder(book, quantity) }
            .defaultIfEmpty(
                buildRejectedOrder(isbn, quantity)
            )
            .flatMap(orderRepository::save)
    }

    companion object {
        fun buildAcceptedOrder(book: Book, quantity: Int): Order {
            return Order(
                bookIsbn = book.isbn,
                quantity = quantity,
                status = OrderStatus.ACCEPTED,
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
