package com.bfilho.kmediatorcore.mediator

class DefaultMediator(
    private val handlers: List<Any>,
    private val interceptors: List<PipelineInterceptor>,
    private val brokers: List<MessageBroker>
) : Mediator {

    override suspend fun <TResponse : Any> send(command: Command<TResponse>): TResponse {
        val handler = handlers.filterIsInstance<CommandHandler<Command<TResponse>, TResponse>>()
            .firstOrNull() ?: throw IllegalStateException("No handler found for command of type ${command::class}")

        return executePipeline(command) { handler.handle(command) }
    }

    override suspend fun <TResponse : Any> query(query: Query<TResponse>): TResponse {
        val handler = handlers.filterIsInstance<QueryHandler<Query<TResponse>, TResponse>>()
            .firstOrNull() ?: throw IllegalStateException("No handler found for query of type ${query::class}")

        return executePipeline(query) { handler.handle(query) }
    }

    override suspend fun publish(event: Event) {
        executePipeline(event) {
            if (brokers.isNotEmpty()) {
                brokers.forEach { broker -> broker.publish(event) }
            }
        }
    }

    private suspend fun <TRequest : Any, TResponse : Any> executePipeline(
        request: TRequest,
        finalHandler: suspend () -> TResponse
    ): TResponse {
        var next: suspend (TRequest) -> TResponse = { finalHandler() }

        interceptors.reversed().forEach { interceptor ->
            val current = next
            next = { req -> interceptor.intercept(req, current) }
        }

        return next(request)
    }
}
