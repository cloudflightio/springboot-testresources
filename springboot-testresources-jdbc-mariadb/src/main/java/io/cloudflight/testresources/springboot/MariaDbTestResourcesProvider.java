package io.cloudflight.testresources.springboot;

import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class MariaDbTestResourcesProvider extends AbstractJdbcTestResourceProvider<MariaDBContainer<?>> {

    public static final String DEFAULT_IMAGE = "mariadb";
    public static final String SIMPLE_NAME = "mariadb";


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
