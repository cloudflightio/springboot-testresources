plugins {
    id("io.micronaut.test-resources")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    runtimeOnly("org.postgresql:postgresql")

    testRuntimeOnly(project(":springboot-testresources-client"))
    testResourcesImplementation(project(":springboot-testresources-jdbc:springboot-testresources-jdbc-postgres"))
}