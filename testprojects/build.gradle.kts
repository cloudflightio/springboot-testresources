subprojects {
    afterEvaluate {
        if (this.subprojects.isEmpty()) {
            dependencies {
                "implementation"(platform(libs.spring.bootbom))
            }
        }
    }
}