description = "Spring Boot TestResourceProvider for MariaDB"

dependencies {
    implementation(project(":springboot-testresources-jdbc"))
    implementation(libs.testcontainers.mariadb)
}