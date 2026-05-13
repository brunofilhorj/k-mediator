package com.bfilho.kmediatorcore.mediator

interface CommandHandler<TCommand : Command<TResponse>, TResponse: Any>:
    Command<TResponse> {
    suspend fun handle(command: TCommand): TResponse
}

interface QueryHandler<TQuery : Query<TResponse>, TResponse: Any>:
    Query<TResponse> {
    suspend fun handle(query: TQuery): TResponse
}

interface EventHandler<TEvent : Event> {
    suspend fun handle(event: TEvent)
}
