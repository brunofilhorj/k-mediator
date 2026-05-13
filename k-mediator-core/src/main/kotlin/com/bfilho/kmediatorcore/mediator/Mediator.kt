package com.bfilho.kmediatorcore.mediator

interface Mediator {
    suspend fun <TResponse : Any> send(command: Command<TResponse>): TResponse
    suspend fun <TResponse : Any> query(query: Query<TResponse>): TResponse
    suspend fun publish(event: Event)
}
