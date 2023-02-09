package io.cloudflight.testresources.springboot.mailhog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.KotlinSerializationStringHttpMessageConverter
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.web.client.RestTemplate

@SpringBootTest
class ApplicationTest(
    @Autowired private val mailSender: MailSender,
    @Value("\${test-resources.mailhog.api-url}") private val mailhogApiUrl: String,
) {
    @Serializable
    data class MailhogResponse(val count: Int)

    val restTemplate = RestTemplate(listOf(object : KotlinSerializationStringHttpMessageConverter<Json>(Json {
        ignoreUnknownKeys = true
    }, MediaType("text", "json")) {}))

    @Test
    fun isShouldBeAbleToSendAnEmailViaSmtp() {
        restTemplate.getForEntity("$mailhogApiUrl/api/v2/messages", MailhogResponse::class.java).apply {
            assertThat(body).isNotNull
            assertThat(body!!.count).isEqualTo(0)
        }

        mailSender.send(SimpleMailMessage().apply {
            from = "me@example.com"
            setTo("you@example.com")
            subject = "One test email"
            text = "Content here"
        })

        restTemplate.getForEntity("$mailhogApiUrl/api/v2/messages", MailhogResponse::class.java).apply {
            assertThat(body).isNotNull
            assertThat(body!!.count).isEqualTo(1)
        }
    }
}
