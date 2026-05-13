package com.bfilho.kmediatorkafka

import com.bfilho.kmediatorcore.mediator.Event
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class KafkaBrokerTest {

    data class TestEvent(val id: Int) : Event

    @Test
    fun `should publish event`() = runBlocking {
        val broker = KafkaBroker.default("test-topic")
        val event = TestEvent(123)

        broker.publish(event)
    }
}
