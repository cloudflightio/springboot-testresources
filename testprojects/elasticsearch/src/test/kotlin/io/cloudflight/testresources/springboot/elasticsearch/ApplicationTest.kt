package io.cloudflight.testresources.springboot.elasticsearch

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTest(
    @Value("\${spring.elasticsearch.uris}") private val uris: String,
    @Autowired private val testDocumentRepository: TestDocumentRepository
) {

    @Test
    fun urisPropertyIsBound() {
        assertThat(uris).matches("^localhost:[1-9][0-9]{3,4}\$")
    }

    @Test
    fun springDataShouldWork() {
        val key = "test::1"
        val title = "myvalue"
        assertThat(testDocumentRepository).isNotNull()
        assertThat(testDocumentRepository.findById(key).isPresent()).isFalse()
        val testDocument = TestDocument(key, title)
        testDocumentRepository.save(testDocument)
        val foundDocument = testDocumentRepository.findById(key).get()
        assertThat(foundDocument.key).isEqualTo(key)
        assertThat(foundDocument.title).isEqualTo(title)
    }
}