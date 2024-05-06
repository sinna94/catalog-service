package com.polarbookshop.orderservice.web

import com.polarbookshop.orderservice.order.domain.Order
import com.polarbookshop.orderservice.order.domain.OrderService
import com.polarbookshop.orderservice.order.web.OrderController
import com.polarbookshop.orderservice.order.web.OrderRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest(OrderController::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderControllerWebFluxTests(
    private val webClient: WebTestClient,
    @MockBean private val orderService: OrderService,
) {

    @Test
    fun whenBookNotAvailableThenRejectOrder() {
        val orderRequest = OrderRequest("1234567890", 3)
        val rejectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn, orderRequest.quantity)
        BDDMockito.given(orderService.submitOrder(orderRequest.isbn, orderRequest.quantity))
            .willReturn(Mono.just(rejectedOrder))

        webClient
            .post()
            .uri("/orders")
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order::class.java).value { actualOrder ->
                assertThat(actualOrder).isNotNull()
                assertThat(actualOrder.status).isEqualTo(rejectedOrder.status)
            }
    }
}