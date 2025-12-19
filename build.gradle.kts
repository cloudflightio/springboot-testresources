plugins {
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    signing
    id("io.micronaut.test-resources") version "3.7.7" apply (false)
}

description = "Test-Resources for Spring Boot"
group = "io.cloudflight.testresources.springboot"

autoConfigure {
    java {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendorName.set("Cloudflight")
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
    configurations {
        all {
            exclude(group = "junit")
        }
    }

    if (!this.path.startsWith(":testprojects")) {
        plugins.apply(MavenPublishPlugin::class)
        plugins.apply(SigningPlugin::class)
    }

    afterEvaluate {
        if (this.subprojects.isEmpty()) {
            dependencies {
                "testImplementation"(libs.jupiter.api)
                "testRuntimeOnly"(libs.jupiter.engine)
                "testImplementation"(libs.assertj)
            }
        }

        if (!this.path.startsWith(":testprojects")) {

            if (!name.endsWith("-client")) {
                dependencies {
                    "implementation"(libs.micronaut.testresources.testcontainers)
                    "compileOnly"(libs.micronaut.core)
                    "implementation"(libs.testcontainers.junit4.mock)
                }
            }

            configure<PublishingExtension> {
                publications {
                    create<MavenPublication>("maven") {
                        from(components["java"])
                        versionMapping {
                            // Ensure resolved versions are written to generated POMs
                            allVariants {
                                fromResolutionResult()
                            }
                        }
                        pom {
                            name.set(project.name)
                            description.set(project.description)
                            url.set("https://github.com/cloudflightio/springboot-testresources")
                            licenses {
                                license {
                                    name.set("The Apache License, Version 2.0")
                                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                }
                            }
                            inceptionYear.set("2023")
                            organization {
                                name.set("Cloudflight")
                                url.set("https://cloudflight.io")
                            }
                            developers {
                                developer {
                                    id.set("cloudflight")
                                    name.set("Cloudflight Team")
                                    email.set("opensource@cloudflight.io")
                                }
                            }
                            scm {
                                connection.set("scm:ggit@github.com:cloudflightio/springboot-testresources.git")
                                developerConnection.set("scm:git@github.com:cloudflightio/springboot-testresources.git")
                                url.set("https://github.com/cloudflightio/springboot-testresources")
                            }
                        }
                    }
                }
            }

            configure<SigningExtension> {
                setRequired {
                    System.getenv("PGP_SECRET") != null
                }
                useInMemoryPgpKeys(System.getenv("PGP_SECRET"), System.getenv("PGP_PASSPHRASE"))
                sign(publishing.publications.getByName("maven"))
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(System.getenv("MAVEN_USERNAME"))
            password.set(System.getenv("MAVEN_PASSWORD"))
        }
    }
}


