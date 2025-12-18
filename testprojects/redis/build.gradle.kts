plugins {
    id("io.micronaut.test-resources")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly(project(":springboot-testresources-client"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testResourcesImplementation(project(":springboot-testresources-redis"))
}