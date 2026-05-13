package com.bfilho.kmediatorcore

import com.bfilho.kmediatorcore.interceptors.RoutingInterceptor
import com.bfilho.kmediatorcore.mediator.Command
import com.bfilho.kmediatorcore.mediator.CommandHandler
import com.bfilho.kmediatorcore.mediator.Event
import com.bfilho.kmediatorcore.mediator.MediatorBuilder
import com.bfilho.kmediatorcore.mediator.MessageBroker
import java.time.LocalDateTime

suspend fun main() {
    val routingInterceptor = RoutingInterceptor(
        routingRules = mapOf(
            CreateEvent::class to listOf(DummyBroker())
        )
    )

    val mediator = MediatorBuilder()
        .autoDiscover()
        .addInterceptor(routingInterceptor)
        .build()

    mediator.send(CreateCommand("Test Command"))
        .let { result ->
            mediator.publish(CreateEvent(result))
        }
}

data class CreateCommand(val name: String) : Command<String>
data class CreateEvent(val name: String) : Event

class CreateHandler: CommandHandler<CreateCommand, String> {
    override suspend fun handle(command: CreateCommand): String {
        println("[Handler] Executing command: ${command.name}")
        return "${command.name} created at ${LocalDateTime.now()}"
    }
}

class DummyBroker : MessageBroker {
    override suspend fun <TEvent : Event> publish(event: TEvent) {
        println("[Broker:Dummy] Publishing event ${event::class.simpleName}: $event")
    }
}

class OtherBroker : MessageBroker {
    override suspend fun <TEvent : Event> publish(event: TEvent) {
        println("[OtherBroker] Publishing event ${event::class.simpleName}: $event")
    }
}