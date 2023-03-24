package io.cloudflight.testresources.springboot.elasticsearch

import co.elastic.clients.elasticsearch.ElasticsearchClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTest(
    @Autowired private val elasticsearchClient: ElasticsearchClient
) {

    @Test
    fun elasticSearchIsAlive() {
        val clusterName = elasticsearchClient.info().clusterName()
        assertThat(clusterName).isNotNull()
    }
}