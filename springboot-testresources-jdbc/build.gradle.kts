description = "Base library for TestProvider libraries based on JDBC"

dependencies {
    implementation(libs.testcontainers.jdbc)
    testImplementation(libs.micronaut.core)
}