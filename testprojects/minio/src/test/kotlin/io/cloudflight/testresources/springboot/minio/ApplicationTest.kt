package io.cloudflight.testresources.springboot.minio

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTest(
    @Autowired private val storageService: StorageService
) {

    @Test
    fun minioIsAlive() {
        assertThat(storageService.url).isNotBlank
    }
}