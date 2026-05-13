package com.bfilho.kmediatorcore.interceptors

import com.bfilho.kmediatorcore.mediator.Event
import com.bfilho.kmediatorcore.mediator.MessageBroker
import com.bfilho.kmediatorcore.mediator.PipelineInterceptor
import kotlin.reflect.KClass

class RoutingInterceptor(
    private val routingRules: Map<KClass<out Event>, List<MessageBroker>>
) : PipelineInterceptor {

    override suspend fun <TRequest : Any, TResponse : Any> intercept(
        request: TRequest,
        next: suspend (TRequest) -> TResponse
    ): TResponse {
        if (request is Event) {
            val event = request as Event
            val brokers = routingRules[event::class]
            if (brokers != null) {
                brokers.forEach { broker -> broker.publish(request) }
                @Suppress("UNCHECKED_CAST")
                return Unit as TResponse
            }
        }
        return next(request)
    }
}
