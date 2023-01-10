package io.cloudflight.testresources.springboot.rabbitmq

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@SpringBootApplication
class Application

@Configuration
class AmqpConfiguration {
    @Bean
    fun myQueue(): Queue {
        return Queue("myQueue", false)
    }
}

@Service
class Receiver {

    val messages  = mutableListOf<String>()
    @RabbitListener(queues = ["myQueue"])
    fun listen(message: String) {
        messages.add(message)
    }
}
