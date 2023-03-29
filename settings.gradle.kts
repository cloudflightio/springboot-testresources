plugins {
    id("io.cloudflight.autoconfigure-settings") version "0.9.1"
}

rootProject.name = "springboot-testresources"

include("springboot-testresources-azurite")
include("springboot-testresources-client")
include("springboot-testresources-minio")
include("springboot-testresources-jdbc")
include("springboot-testresources-jdbc:springboot-testresources-jdbc-mariadb")
include("springboot-testresources-jdbc:springboot-testresources-jdbc-mssql")
include("springboot-testresources-jdbc:springboot-testresources-jdbc-postgres")
include("springboot-testresources-rabbitmq")
include("springboot-testresources-redis")
include("springboot-testresources-mailhog")
include("springboot-testresources-elasticsearch")

include("testprojects:jdbc:mariadb")
include("testprojects:jdbc:mssql")
include("testprojects:jdbc:postgres")
include("testprojects:azurite")
include("testprojects:minio")
include("testprojects:rabbitmq")
include("testprojects:redis")
include("testprojects:mailhog")
include("testprojects:elasticsearch")
