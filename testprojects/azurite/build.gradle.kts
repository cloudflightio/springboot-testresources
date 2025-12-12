plugins {
    id("io.micronaut.test-resources")
}

dependencies {
    implementation(libs.spring.cloud.azure.starter.storage.blob)
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly(project(":springboot-testresources-client"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testResourcesImplementation(project(":springboot-testresources-azurite"))
}