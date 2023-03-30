# Spring Boot TestResources

[![License](https://img.shields.io/badge/License-Apache_2.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/io.cloudflight.testresources.springboot/springboot-testresources-client.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.cloudflight.testresources.springboot/springboot-testresources-client)

This project is a POC to combine [Micronaut's Test Resources](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/) project with Spring Boot.
The project is still in very early stage and subject to bigger changes.

# Concept

[TestContainers](https://www.testcontainers.org/) is a great framework for testing your application code against infrastructure
components like databases, message brokers and so forth. If you are developing [Spring Boot](https://spring.io/projects/spring-boot)
applications, the [PlayTika TestContainers library](https://github.com/PlaytikaOSS/testcontainers-spring-boot/) is a helpful assistent
to connect your Spring Application context with the TestContainers.

Micronaut released a sub-project called [Test Resources](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/) which 
also comes with TestContainers under the hood but it provides a [test resources server](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/#architecture-server)
which takes care of provisioning your containers during the build. This server can run standalone and 
can therefore survive independent builds and keep your containers alive. This in turn can significantly
reduce your build times.

This library combines those two approaches by implementing instances of [TestResourcesResolver](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/#implementing-test-resources), mapping
to the default properties of Spring Boot, i.e. `spring.datasource.url`. A special `PropertySource` fetches the properties
from the resource server.

That way, together with the build plugins from Micronaut for Gradle and Maven, you can **easily test 
your Spring Boot applications using Micronaut Test Resources and TestContainers** and benefit
from keeping your containers alive during builds.

# How it works

This library consists of two parts: 

* a thin client layer to be packaged with your Spring Boot application (for test cases) which configures your ApplicationContext
* plugins for the test resources server for the various modules (MariaDB, MinIO) which configure the containers based on Spring Boot properties

Follow the following steps, to get it running:

First thing is to add the [Gradle Plugin](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/#test-resources) to your build. If you use Maven,
please consult the [official documentation](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/examples/test-resources.html).

Go to your `build.gradle.kts`: 

````kotlin
plugins {
    id("io.micronaut.test-resources") version "3.7.7"
}
````

In multi-module projects ensure that the plugin is applied on every module (especially on the one which is holding your Spring Boot application tests).

Then, add this to your `gradle.properties`:

````properties
micronautVersion=3.8.7
````

Next thing is to go to the module which contains your Spring Boot application 
and your `@SpringBootTest` and add the module `springboot-testresources-client` to the test-scope in your `build.gradle.kts`:

````kotlin
dependencies {
    testRuntimeOnly("io.cloudflight.testresources.springboot:springboot-testresources-client:0.0.2")
}
````

We strongly recommend to use `testRuntimeOnly` instead of `testImplementation` in order to avoid having Micronaut
on your implementation classpath.

Then, as a last step, you need to add one or more of our test modules to the `testResourcesImplementation` scope, which 
has been created by the Micronaut Test Resources plugin.

Suppose you need a container for MariaDB, then add the following line:

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-jdbc-mariadb:0.0.2")
}
````

That's it. You can now run your `@SpringBootTest`. 

## Full example

To give you an even better overview, here is a full example of a minimalistic Spring Boot application with Spring Data + MariaDB.

`build.gradle.kts`

````kotlin
plugins {
    id("io.cloudflight.autoconfigure-gradle") version "0.9.4"
    id("io.micronaut.test-resources") version "3.7.7"
}

version = "0.1"
group = "io.cloudflight"

repositories {
    mavenCentral()
}

autoConfigure {
    java {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.1")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.1")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.0.6")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testRuntimeOnly("io.cloudflight.testresources.springboot:springboot-testresources-client:0.0.2")
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-jdbc-mariadb:0.0.2")
}
````

Then, create a `src/main/kotlin/io/cloudflight/Application.kt`

````kotlin
package io.cloudflight

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository

@SpringBootApplication
class Application

fun main(vararg args: String) {
    runApplication<Application>(args)
}

@Entity
class Person(var name: String) {

    @Id
    @GeneratedValue
    var id: Long = 0
}

interface PersonRepository : JpaRepository<Person, Long>
````

Configure the application to create a database on startup with Hibernate Auto-DDL in `src/main/resources/application.yaml`:

````yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
````

And finally, create a `src/test/kotlin/io/cloudflight/ApplicationTest.kt`:

````kotlin
package io.cloudflight

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTest(
    @Autowired private val repository: PersonRepository
) {

    @Test
    fun enterData() {
        repository.save(Person(name = "Klaus"))
    }
}
````

If you now run the test, the following happens:

1. The Micronaut Test Resources starts the test resources server which can handle requests to configure a MariaDB docker container
2. The `ApplicationTest` picks up the [TestResourcesEnvironmentPostProcessor](https://github.com/cloudflightio/springboot-testresources/blob/master/springboot-testresources-client/src/main/java/io/cloudflight/testresources/springboot/client/TestResourcesEnvironmentPostProcessor.java) which uses the [TestResourcesPropertySource](https://github.com/cloudflightio/springboot-testresources/blob/master/springboot-testresources-client/src/main/java/io/cloudflight/testresources/springboot/client/TestResourcesPropertySource.java) to automatically configure properties like `spring.datasource.url` or `spring.datasource.username` based on the settings from the docker container.
3. The [MariaDbTestResourcesProvider](https://github.com/cloudflightio/springboot-testresources/blob/master/springboot-testresources-jdbc-mariadb/src/main/java/io/cloudflight/testresources/springboot/MariaDbTestResourcesProvider.java) starts the `MariaDBContainer` from the TestContainers project
4. The `ApplicationTest` connects its `PersonRepository` to exactly that database and can insert data to the DB.

As you see, we did not have to configure properties like `spring.datasource.url` manually, that was all provided automatically.


# Requirements

Spring Boot Test Resources is compatible with Spring Boot 3.x but also with 2.7. 
It requires a JDK 17 (we could also easily publish it for JDK 11 or below, but we want you to push towards the latest LTS)

Additionally, you need a plugin to start the test resources server. We have tested our library with the Micronaut Test Resources [Gradle Plugin](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/#test-resources), but it should also work fine for [Maven](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/examples/test-resources.html).


# Modules

We currently only provide some few modules, as this library is still in a PoC phase, but the number of modules will grow over time.

## Common configuration

For each module you can override the default image which is being pulled by adding 

````yaml
test-resources:
  containers:
    mariadb:
      image-name: mariadb:10.3
````

You can also always override any of the provided properties by adding any of those properties below `test-resources.containers.mariadb`. For example,
if you want to use another `username` than the default one, you can add:

````yaml
test-resources:
  containers:
    mariadb:
      image-name: mariadb:10.3
      username: myusername
````

This will create a MariaDB container based on `mariadb:10.3` with the default username `myusername`.

## MariaDB

* **Module-ID**: mariadb
* **Default-Image**: mariadb

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-jdbc-mariadb:0.2.1")
}
````

* **Provided properties**:
  * `spring.datasource.url`
  * `spring.datasource.username`
  * `spring.datasource.password`
  * `spring.datasource.driver-class-name`

## Microsoft SQL Server

### MSSQL specific:
Make sure to add the following property to your ```application-test.yml``` to accept the MSSQL Server License Agreement:
```yml
test-resources:
  containers:
    mssql:
      accept-license: true
```

* **Module-ID**: mssql
* **Default-Image**: mcr.microsoft.com/mssql/server:2019-CU16-GDR1-ubuntu-20.04

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-jdbc-mssql:0.2.1")
}
````

* **Provided properties**:
  * `spring.datasource.url`
  * `spring.datasource.username`
  * `spring.datasource.password`
  * `spring.datasource.driver-class-name`

## Postgres

* **Module-ID**: postgres
* **Default-Image**: postgres

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-jdbc-postgres:0.2.1")
}
````

* **Provided properties**:
  * `spring.datasource.url`
  * `spring.datasource.username`
  * `spring.datasource.password`
  * `spring.datasource.driver-class-name`


## MinIO

* **Module-ID**: minio
* **Default-Image**: minio/minio

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-minio:0.2.1")
}
````

* **Provided properties**:
    * `minio.url`
    * `minio.access-key`
    * `minio.secret-key`
    * `minio.region`

## RabbitMQ

* **Module-ID**: rabbitmq
* **Default-Image**: rabbitmq

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-rabbitmq:0.2.1")
}
````

* **Provided properties**:
  * `spring.rabbitmq.host`
  * `spring.rabbitmq.port`
  * `spring.rabbitmq.username`
  * `spring.rabbitmq.password`

## Redis

* **Module-ID**: redis
* **Default-Image**: redis

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-redis:0.2.1")
}
````

* **Provided properties**:
  * `spring.data.redis.url`

## Azurite

* **Module-ID**: azurite
* **Default-Image**: mcr.microsoft.com/azure-storage/azurite

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-azurite:0.2.1")
}
````

* **Provided properties**:
  * `spring.cloud.azure.storage.blob.account-name`
  * `spring.cloud.azure.storage.blob.account-key`
  * `spring.cloud.azure.storage.blob.endpoint`

## Mailhog

* **Module-ID**: mailhog
* **Default-Image**: mailhog/mailhog

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-mailhog:0.2.1")
}
````

* **Provided properties**:
  * `spring.mail.host`
  * `spring.mail.port`
  * `test-resources.mailhog.api-url`

## Elasticsearch

* **Module-ID**: elasticsearch
* **Default-Image**: docker.elastic.co/elasticsearch/elasticsearch

````kotlin
dependencies {
    testResourcesImplementation ("io.cloudflight.testresources.springboot:springboot-testresources-elasticsearch:0.2.1")
}
````

* **Provided properties**:
  * `spring.elasticsearch.uris`
  * `spring.elasticsearch.password`