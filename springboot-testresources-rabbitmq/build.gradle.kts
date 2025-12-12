description = "Spring Boot TestResourceProvider for RabbitMQ"

dependencies {
    implementation(libs.testcontainers.rabbitmq)
    testImplementation(libs.micronaut.core)
}
