description = "Spring Boot TestResourceProvider for MinIO"

dependencies {
    // Needed for both compilation and runtime of unit tests (Minio provider extends a type that implements Ordered)
    testImplementation(libs.micronaut.core)
}
