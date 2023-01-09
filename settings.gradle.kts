plugins {
    id("io.cloudflight.autoconfigure-settings") version "0.8.11"
}

rootProject.name = "springboot-testresources"

include("springboot-testresources-client")
include("springboot-testresources-minio")
include("springboot-testresources-jdbc-mariadb")

include("testprojects:jdbc:mariadb")
include("testprojects:minio")
