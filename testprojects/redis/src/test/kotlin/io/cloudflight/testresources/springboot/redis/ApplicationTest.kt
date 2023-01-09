package io.cloudflight.testresources.springboot.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest
class ApplicationTest(
    @Autowired private val redisTemplate: RedisTemplate<Any, Any>
) {

    @Test
    fun redisIsAlive() {
        redisTemplate.opsForValue().set("test", "me")
        assertThat(redisTemplate.opsForValue().get("test")).isEqualTo("me")
    }
}