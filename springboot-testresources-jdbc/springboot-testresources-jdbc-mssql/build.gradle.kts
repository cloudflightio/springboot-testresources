description = "Spring Boot TestResourceProvider for Microsoft SQL Server"

dependencies {
    implementation(project(":springboot-testresources-jdbc"))
    implementation("org.testcontainers:mssqlserver")
}
