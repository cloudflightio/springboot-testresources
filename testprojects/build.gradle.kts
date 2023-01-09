subprojects {
    if (this.subprojects.isEmpty()) {
        dependencies {
            "implementation"(platform("org.springframework.boot:spring-boot-dependencies:3.0.1"))
        }
    }
}