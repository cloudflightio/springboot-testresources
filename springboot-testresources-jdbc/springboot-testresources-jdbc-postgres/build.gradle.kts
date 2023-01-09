description = "Spring Boot TestResourceProvider for Postgres"

dependencies {
    implementation(project(":springboot-testresources-jdbc"))
    implementation("org.testcontainers:postgresql")
}