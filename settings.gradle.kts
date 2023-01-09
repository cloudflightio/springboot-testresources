plugins {
    id("io.cloudflight.autoconfigure-settings") version "0.8.11"
}

rootProject.name = "springboot-testresources"

include("springboot-testresources-client")
include("springboot-testresources-minio")
include("springboot-testresources-jdbc")
include("springboot-testresources-jdbc:springboot-testresources-jdbc-mariadb")
include("springboot-testresources-jdbc:springboot-testresources-jdbc-postgres")

include("testprojects:jdbc:mariadb")
include("testprojects:jdbc:postgres")
include("testprojects:minio")

