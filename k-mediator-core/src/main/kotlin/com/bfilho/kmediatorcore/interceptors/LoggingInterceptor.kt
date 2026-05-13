package com.bfilho.kmediatorcore.interceptors

import com.bfilho.kmediatorcore.mediator.PipelineInterceptor

class LoggingInterceptor : PipelineInterceptor {

    override val after = setOf(MetricsInterceptor::class)

    override suspend fun <TRequest : Any, TResponse : Any> intercept(
        request: TRequest,
        next: suspend (TRequest) -> TResponse
    ): TResponse {
        println("[Logging] Begin")
        val response = next(request)
        println("[Logging] End")
        return response
    }
}
