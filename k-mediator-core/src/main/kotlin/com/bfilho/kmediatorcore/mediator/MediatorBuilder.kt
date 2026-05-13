package com.bfilho.kmediatorcore.mediator

import io.github.classgraph.ClassGraph
import kotlin.reflect.full.createInstance

class MediatorBuilder {
    private val handlers = mutableListOf<Any>()
    private val interceptors = mutableListOf<PipelineInterceptor>()
    private var brokers = mutableListOf<MessageBroker>()

    fun autoDiscover(): MediatorBuilder {
        val scanResult = ClassGraph().enableAllInfo().scan()

        scanResult.allClasses.forEach { classInfo ->
            if (classInfo.isInterface || classInfo.isAbstract) return@forEach
            val clazz = classInfo.loadClass()

            when {
                CommandHandler::class.java.isAssignableFrom(clazz) ||
                QueryHandler::class.java.isAssignableFrom(clazz) ||
                EventHandler::class.java.isAssignableFrom(clazz) -> {
                    handlers.add(clazz.kotlin.createInstance())
                }
                PipelineInterceptor::class.java.isAssignableFrom(clazz) -> {
                    interceptors.add(clazz.kotlin.createInstance() as PipelineInterceptor)
                }
                MessageBroker::class.java.isAssignableFrom(clazz) -> {
                    brokers.add(clazz.kotlin.createInstance() as MessageBroker)
                }
            }
        }

        return this
    }

    fun addInterceptor(interceptor: PipelineInterceptor): MediatorBuilder {
        interceptors.add(interceptor)
        return this
    }

    fun addHandler(handler: Any): MediatorBuilder {
        handlers.add(handler)
        return this
    }

    fun addBroker(broker: MessageBroker): MediatorBuilder {
        brokers.add(broker)
        return this
    }

    fun build(): Mediator {
        val orderedInterceptors = orderInterceptors(interceptors)
        return DefaultMediator(
            handlers,
            orderedInterceptors,
            brokers
        )
    }
}
