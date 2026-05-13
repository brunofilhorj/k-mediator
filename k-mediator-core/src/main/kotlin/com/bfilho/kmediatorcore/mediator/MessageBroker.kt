package com.bfilho.kmediatorcore.mediator

interface MessageBroker {
    suspend fun <TEvent : Event> publish(event: TEvent)
}
