plugins {
    id("io.micronaut.test-resources")
    kotlin("plugin.serialization") version "1.7.21"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-autoconfigure")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-mail")
    testImplementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    testRuntimeOnly(project(":springboot-testresources-client"))
    testResourcesImplementation(project(":springboot-testresources-mailhog"))

}
