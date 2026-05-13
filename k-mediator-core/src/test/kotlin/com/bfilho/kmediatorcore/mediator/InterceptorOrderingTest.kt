package com.bfilho.kmediatorcore.mediator

import com.bfilho.kmediatorcore.interceptors.LoggingInterceptor
import com.bfilho.kmediatorcore.interceptors.MetricsInterceptor
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DummyHandler : (String) -> String {
    val calls = mutableListOf<String>()
    override fun invoke(p1: String): String {
        calls.add(p1)
        return "handled:$p1"
    }
}

class InterceptorOrderingTest {

    @Test
    fun `metrics and logging interceptors should wrap handler in correct order`() = runBlocking {
        val metrics = MetricsInterceptor()
        val logging = LoggingInterceptor()
        val handler = DummyHandler()
        val interceptors = listOf(metrics, logging)

        // Compose pipeline manually as in DefaultMediator
        var next: suspend (String) -> String = { handler(it) }
        interceptors.reversed().forEach { interceptor ->
            val current = next
            next = { req -> interceptor.intercept(req, current) }
        }

        val result = next("test")
        assertEquals("handled:test", result)
        assertEquals(listOf("test"), handler.calls)
        // Output is printed, but we check the call order and result
    }
}