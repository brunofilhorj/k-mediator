package com.bfilho.kmediatorcore.mediator

import kotlin.reflect.KClass

interface PipelineInterceptor {
    val before: Set<KClass<out PipelineInterceptor>> get() = emptySet()
    val after: Set<KClass<out PipelineInterceptor>> get() = emptySet()

    suspend fun <TRequest : Any, TResponse : Any> intercept(
        request: TRequest,
        next: suspend (TRequest) -> TResponse
    ): TResponse
}
