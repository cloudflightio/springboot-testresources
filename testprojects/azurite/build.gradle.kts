plugins {
    id("io.micronaut.test-resources")
}

dependencies {
    implementation("com.azure.spring:spring-cloud-azure-starter-storage-blob:5.0.0")



    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly(project(":springboot-testresources-client"))
    testResourcesImplementation(project(":springboot-testresources-azurite"))
}