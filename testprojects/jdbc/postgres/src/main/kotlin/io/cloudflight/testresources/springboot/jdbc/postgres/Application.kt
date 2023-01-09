package io.cloudflight.testresources.springboot.jdbc.postgres

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.JpaRepository

@SpringBootApplication
class Application

@Entity
class Person(var name: String) {

    @Id
    @GeneratedValue
    var id: Long = 0
}

interface PersonRepository : JpaRepository<Person, Long>