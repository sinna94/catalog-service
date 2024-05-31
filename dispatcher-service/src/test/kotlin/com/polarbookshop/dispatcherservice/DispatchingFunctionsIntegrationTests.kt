package com.polarbookshop.dispatcherservice

import org.junit.jupiter.api.Test
import org.springframework.cloud.function.context.FunctionCatalog
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest
import org.springframework.messaging.support.GenericMessage
import org.springframework.test.context.TestConstructor
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.function.Function

@FunctionalSpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DispatchingFunctionsIntegrationTests(
    private val catalog: FunctionCatalog
) {

//    @Test
//    fun packAndLabelOrder() {
//
//        val packAndLabel =
//            catalog.lookup<Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>>>(Function::class.java, "pack|label")
//        val orderId = 121L
//
//        val result = packAndLabel.apply(OrderAcceptedMessage(orderId))
//        StepVerifier.create(result)
//            .expectNextMatches { dispatchedOrder ->
//                dispatchedOrder.equals(OrderDispatchedMessage(orderId))
//            }.verifyComplete()
//    }
}