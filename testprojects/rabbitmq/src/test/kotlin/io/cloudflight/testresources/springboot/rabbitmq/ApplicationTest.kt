package io.cloudflight.testresources.springboot.rabbitmq

import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration.ofSeconds

@SpringBootTest
class ApplicationTest(
    @Autowired private val rabbitTemplate: RabbitTemplate,
    @Autowired private val receiver: Receiver
) {

    @Test
    fun `send message via RabbitMQ`() {
        rabbitTemplate.convertAndSend("myQueue", "Hello, world!");
        await().atMost(ofSeconds(5)).until {
            receiver.messages.size == 1 && receiver.messages.first() == "Hello, world!"
        }
    }
}