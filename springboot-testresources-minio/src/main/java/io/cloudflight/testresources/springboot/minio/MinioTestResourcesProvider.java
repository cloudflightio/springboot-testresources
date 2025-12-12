package io.cloudflight.testresources.springboot.minio;

import io.micronaut.testresources.testcontainers.AbstractTestContainersProvider;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

/**
 * Test container provider for Minio.
 */
public class MinioTestResourcesProvider extends AbstractTestContainersProvider<GenericContainer<?>> {

    private static final String MINIO_URL = "minio.url";
    private static final String MINIO_ACCESS_KEY = "minio.access-key";
    private static final String MINIO_SECRET_KEY = "minio.secret-key";
    private static final String MINIO_REGION = "minio.region";
    private static final String DEFAULT_IMAGE = "minio/minio";
    private static final String SIMPLE_NAME = "minio";
    private static final int MINIO_PORT = 9000;
    private static final int MINIO_CONSOLE_PORT = 9001;

    private static final Set<String> SUPPORTED_PROPERTIES;

    static {
        SUPPORTED_PROPERTIES = Set.of(MINIO_URL, MINIO_ACCESS_KEY, MINIO_SECRET_KEY, MINIO_REGION);
    }

    @Override
    public List<String> getResolvableProperties(Map<String, Collection<String>> propertyEntries, Map<String, Object> testResourcesConfig) {
        return SUPPORTED_PROPERTIES.stream().toList();
    }

    @Override
    protected String getSimpleName() {
        return SIMPLE_NAME;
    }

    @Override
    protected String getDefaultImageName() {
        return DEFAULT_IMAGE;
    }

    @Override
    protected GenericContainer<?> createContainer(DockerImageName imageName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        GenericContainer<?> container = new GenericContainer<>(imageName);
        container.withExposedPorts(MINIO_PORT, MINIO_CONSOLE_PORT);
        container.withCommand("server", "/data", "--console-address", ":" + MINIO_CONSOLE_PORT);
        container.withEnv("MINIO_ROOT_USER", "minio");
        container.withEnv("MINIO_ROOT_PASSWORD", "minio123");
        container.withEnv("MINIO_SITE_REGION", "eu-central-1");
        return container;
    }

    @Override
    protected Optional<String> resolveProperty(String propertyName, GenericContainer<?> container) {

        if (MINIO_URL.equals(propertyName)) {
            return Optional.of("http://" + container.getHost() + ":" + container.getMappedPort(MINIO_PORT));
        }
        if (MINIO_ACCESS_KEY.equals(propertyName)) {
            return Optional.of("minio");
        }
        if (MINIO_SECRET_KEY.equals(propertyName)) {
            return Optional.of("minio123");
        }
        if (MINIO_REGION.equals(propertyName)) {
            return Optional.of("eu-central-1");
        }
        return Optional.empty();
    }

    @Override
    protected boolean shouldAnswer(String propertyName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return SUPPORTED_PROPERTIES.contains(propertyName);
    }
}