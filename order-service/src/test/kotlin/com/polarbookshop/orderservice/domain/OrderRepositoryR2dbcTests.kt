package com.polarbookshop.orderservice.domain

import com.polarbookshop.orderservice.config.DataConfig
import com.polarbookshop.orderservice.order.domain.OrderRepository
import com.polarbookshop.orderservice.order.domain.OrderService
import com.polarbookshop.orderservice.order.domain.OrderStatus
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier

@DataR2dbcTest
@Import(DataConfig::class)
@Testcontainers
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderRepositoryR2dbcTests(
    private val orderRepository: OrderRepository
) {

    companion object {
        @Container
        val postgresql = PostgreSQLContainer("postgres:latest")

        @DynamicPropertySource
        fun postgresqlProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url", OrderRepositoryR2dbcTests::r2dbcUrl)
            registry.add("spring.r2dbc.username", postgresql::getUsername)
            registry.add("spring.r2dbc.password", postgresql::getPassword)
            registry.add("spring.flyway.url", postgresql::getJdbcUrl)
        }

        private fun r2dbcUrl() =
            "r2dbc:postgresql://${postgresql.host}:${postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)}/${postgresql.databaseName}"
    }

    @Test
    fun createRejectedOrder(){
        val rejectedOrder = OrderService.buildRejectedOrder("1234567890", 3)
        StepVerifier
            .create(orderRepository.save(rejectedOrder))
            .expectNextMatches { order -> order.status == OrderStatus.REJECTED }
            .verifyComplete()
    }
}