package io.cloudflight.testresources.springboot.jdbc.mariadb

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTest(
    @Autowired private val repository: PersonRepository
) {

    @Test
    fun enterData() {
        repository.save(Person(name = "Person1"))
    }
}