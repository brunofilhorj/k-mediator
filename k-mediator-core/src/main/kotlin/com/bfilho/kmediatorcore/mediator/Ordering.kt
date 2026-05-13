package com.bfilho.kmediatorcore.mediator

import kotlin.reflect.KClass

fun orderInterceptors(interceptors: List<PipelineInterceptor>): List<PipelineInterceptor> {
    val graph = mutableMapOf<KClass<out PipelineInterceptor>, MutableSet<KClass<out PipelineInterceptor>>>()
    val all = interceptors.map { it::class }.toSet()

    interceptors.forEach { interceptor ->
        interceptor.before.forEach { before ->
            graph.getOrPut(interceptor::class) { mutableSetOf() }.add(before)
        }
        interceptor.after.forEach { after ->
            graph.getOrPut(after) { mutableSetOf() }.add(interceptor::class)
        }
    }

    val inDegree = mutableMapOf<KClass<out PipelineInterceptor>, Int>()
    all.forEach { inDegree[it] = 0 }
    graph.values.flatten().forEach { inDegree[it] = (inDegree[it] ?: 0) + 1 }

    val queue = ArrayDeque<KClass<out PipelineInterceptor>>()
    inDegree.filter { it.value == 0 }.forEach { queue.add(it.key) }

    val sortedClasses = mutableListOf<KClass<out PipelineInterceptor>>()
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        sortedClasses.add(current)

        graph[current]?.forEach { neighbor ->
            inDegree[neighbor] = inDegree[neighbor]!! - 1
            if (inDegree[neighbor] == 0) queue.add(neighbor)
        }
    }

    if (sortedClasses.size != all.size) error("Found ${sortedClasses.size} classes")

    return sortedClasses.map { clazz -> interceptors.first { it::class == clazz } }
}
