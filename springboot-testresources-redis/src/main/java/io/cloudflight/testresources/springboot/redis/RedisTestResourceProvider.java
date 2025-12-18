package io.cloudflight.testresources.springboot.redis;

import io.micronaut.testresources.testcontainers.AbstractTestContainersProvider;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

/**
 * Test container provider for Redis.
 */
public class RedisTestResourceProvider extends AbstractTestContainersProvider<GenericContainer<?>> {

    /**
     * Redis URI.
     */
    public static final String REDIS_URI = "spring.data.redis.url";
    /**
     * Default image name.
     */
    public static final String DEFAULT_IMAGE = "redis";
    /**
     * Simple name of the test resource.
     */
    public static final String SIMPLE_NAME = "redis";
    /**
     * Redis port.
     */
    public static final int REDIS_PORT = 6379;

    private static final Set<String> SUPPORTED_PROPERTIES;

    static {
        SUPPORTED_PROPERTIES = Set.of(REDIS_URI);
    }

    @Override
    public List<String> getResolvableProperties(Map<String, Collection<String>> propertyEntries, Map<String, Object> testResourcesConfig) {
        return Collections.singletonList(REDIS_URI);
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
        container.withExposedPorts(REDIS_PORT);
        return container;
    }

    @Override
    protected Optional<String> resolveProperty(String propertyName, GenericContainer<?> container) {
        if (REDIS_URI.equals(propertyName)) {
            return Optional.of("redis://" + container.getHost() + ":" + container.getMappedPort(REDIS_PORT));
        }
        return Optional.empty();
    }

    @Override
    protected boolean shouldAnswer(String propertyName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return SUPPORTED_PROPERTIES.contains(propertyName);
    }
}