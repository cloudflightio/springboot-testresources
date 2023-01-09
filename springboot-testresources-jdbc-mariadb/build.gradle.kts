plugins {
    id("io.micronaut.test-resources")
}

description = "Spring Boot TestResourceProvider for MariaDB"

dependencies {
    implementation(libs.testcontainers.mariadb)
}