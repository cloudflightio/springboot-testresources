plugins {
    id("io.micronaut.test-resources")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")

    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly(project(":springboot-testresources-client"))
    testResourcesImplementation(project(":springboot-testresources-elasticsearch"))
}