package io.cloudflight.testresources.springboot.jdbc.mariadb;

import io.cloudflight.testresources.springboot.jdbc.AbstractJdbcTestResourceProvider;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

/**
 * Test container provider for MariaDB.
 */
public class MariaDbTestResourcesProvider extends AbstractJdbcTestResourceProvider<MariaDBContainer<?>> {

    private static final String DEFAULT_IMAGE = "mariadb";
    private static final String SIMPLE_NAME = "mariadb";


    @Override
    protected String getSimpleName() {
        return SIMPLE_NAME;
    }

    @Override
    protected String getDefaultImageName() {
        return DEFAULT_IMAGE;
    }

    @Override
    protected MariaDBContainer<?> createContainer(DockerImageName imageName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return new MariaDBContainer<>(imageName);
    }

}
