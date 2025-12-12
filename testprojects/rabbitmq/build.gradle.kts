plugins {
    id("io.micronaut.test-resources")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.awaitility:awaitility")

    testRuntimeOnly(project(":springboot-testresources-client"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testResourcesImplementation(project(":springboot-testresources-rabbitmq"))
}