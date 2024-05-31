package com.polarbookshop.dispatcherservice

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Import
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.context.TestConstructor

@SpringBootTest
@Import(TestChannelBinderConfiguration::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class FunctionsStreamIntegrationTests(
    private val input: InputDestination,
    private val output: OutputDestination,
    private val objectMapper: ObjectMapper,
) {

    @Test
    fun whenOrderAcceptedThenDispatched() {
        val orderId = 121L
        val orderAcceptedMessage = OrderAcceptedMessage(orderId)
        val inputMessage = MessageBuilder.withPayload(orderAcceptedMessage).build()
        val orderDispatchedMessage = OrderDispatchedMessage(orderId)
        val expectedOutputMessage = MessageBuilder.withPayload(orderDispatchedMessage).build()

        input.send(inputMessage)
        assertThat(objectMapper.readValue(output.receive().payload, orderDispatchedMessage.javaClass))
            .isEqualTo(expectedOutputMessage.payload)
    }
}