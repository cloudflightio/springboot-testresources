package io.cloudflight.testresources.springboot.jdbc.postgres;

import io.cloudflight.testresources.springboot.jdbc.AbstractJdbcTestResourceProvider;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

/**
 * Test container provider for PostgreSQL.
 */
public class PostgresTestResourcesProvider extends AbstractJdbcTestResourceProvider<PostgreSQLContainer<?>> {

    private static final String DEFAULT_IMAGE = "postgres";
    private static final String SIMPLE_NAME = "postgres";

    @Override
    protected String getSimpleName() {
        return SIMPLE_NAME;
    }

    @Override
    protected String getDefaultImageName() {
        return DEFAULT_IMAGE;
    }

    @Override
    protected PostgreSQLContainer<?> createContainer(DockerImageName imageName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return new PostgreSQLContainer<>(imageName);
    }

}
