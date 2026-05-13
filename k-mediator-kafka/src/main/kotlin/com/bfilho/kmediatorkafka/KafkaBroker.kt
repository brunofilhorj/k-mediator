package com.bfilho.kmediatorkafka

import com.bfilho.kmediatorcore.mediator.Event
import com.bfilho.kmediatorcore.mediator.MessageBroker
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties

class KafkaBroker(
    private val topic: String,
    private val producer: KafkaProducer<String, String>
) : MessageBroker {

    override suspend fun <TEvent : Event> publish(event: TEvent) {
        val record = ProducerRecord(topic, event::class.simpleName, event.toString())
        producer.send(record) { metadata, exception ->
            if (exception != null) {
                exception.printStackTrace()
            } else {
                println("[KafkaBroker] Event published to topic ${metadata.topic()} at offset ${metadata.offset()}")
            }
        }
    }

    companion object {
        fun default(topic: String): KafkaBroker {
            val props = Properties().apply {
                put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
                put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            }
            val producer = KafkaProducer<String, String>(props)
            return KafkaBroker(topic, producer)
        }
    }
}
