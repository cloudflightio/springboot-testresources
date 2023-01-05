description = "Client library for Spring Boot integration tests to connect to the TestResources Server"

dependencies {
    implementation(libs.micronaut.testresources.client)

    compileOnly(libs.spring.boot)
}