description = "Spring Boot TestResourceProvider for Postgres"

dependencies {
    implementation(project(":springboot-testresources-jdbc"))
    implementation(libs.testcontainers.postgresql)
}