package com.bfilho.kmediatorcore.interceptors

import com.bfilho.kmediatorcore.mediator.PipelineInterceptor

class MetricsInterceptor : PipelineInterceptor {

    override suspend fun <TRequest : Any, TResponse : Any> intercept(
        request: TRequest,
        next: suspend (TRequest) -> TResponse
    ): TResponse {
        println("[Metrics] Begin")
        val response = next(request)
        println("[Metrics] End")
        return response
    }
}
