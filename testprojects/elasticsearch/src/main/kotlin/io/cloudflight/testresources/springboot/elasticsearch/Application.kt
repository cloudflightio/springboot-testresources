package io.cloudflight.testresources.springboot.elasticsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository


@SpringBootApplication
class Application

@Document(indexName = "test_index")
class TestDocument( @Id var key: String? = null, var title: String? = null) {
}

@Repository
interface TestDocumentRepository : ElasticsearchRepository<TestDocument?, String?> {
    fun findByTitle(title: String?): List<TestDocument?>?
}