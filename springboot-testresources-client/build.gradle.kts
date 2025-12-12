description = "Client library for Spring Boot integration tests to connect to the TestResources Server"

dependencies {
    implementation(libs.micronaut.testresources.client)
    implementation(libs.micronaut.http.client)

    compileOnly(libs.spring.boot)
}