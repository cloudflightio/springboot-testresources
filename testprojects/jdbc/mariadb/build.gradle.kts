plugins {
    id("io.micronaut.test-resources")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    testRuntimeOnly(project(":springboot-testresources-client"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testResourcesImplementation(project(":springboot-testresources-jdbc:springboot-testresources-jdbc-mariadb"))
}