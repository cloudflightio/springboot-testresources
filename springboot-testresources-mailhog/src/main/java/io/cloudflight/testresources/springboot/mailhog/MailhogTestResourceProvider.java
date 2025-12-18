package io.cloudflight.testresources.springboot.mailhog;

import io.micronaut.testresources.testcontainers.AbstractTestContainersProvider;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

/**
 * Test container provider for Mailhog.
 */
public class MailhogTestResourceProvider extends AbstractTestContainersProvider<GenericContainer<?>> {
    /**
     * Host name
     */
    public static final String MAILHOG_HOST = "spring.mail.host";
    /**
     *  SMTP port
     */
    public static final String MAILHOG_SMTP_PORT = "spring.mail.port";
    /**
     * API URL
     */
    public static final String MAILHOG_API_URL = TEST_RESOURCES_PROPERTY + ".mailhog.api-url";
    /**
     * Simple name of the test resource.
     */
    public static final String SIMPLE_NAME = "mailhog";
    /**
     * Default Mailhog image.
     */
    public static final String DEFAULT_IMAGE = "mailhog/mailhog";

    /**
     * Mailhog SMTP port.
     */
    public static final int SMTP_PORT = 1025;
    /**
     * Mailhog API port.
     */
    public static final int API_PORT = 8025;

    private static final Set<String> SUPPORTED_PROPERTIES = Set.of(
            MAILHOG_HOST,
            MAILHOG_API_URL,
            MAILHOG_SMTP_PORT
    );

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
        var readyCheck = new HttpWaitStrategy()
                .forPort(API_PORT)
                .forPath("/api/v2/messages");

        return new GenericContainer<>(imageName)
                .withExposedPorts(SMTP_PORT, API_PORT)
                .waitingFor(readyCheck)
                .withEnv("MH_SMTP_BIND_ADDR", "0.0.0.0:%d".formatted(SMTP_PORT))
                .withEnv("MH_API_BIND_ADDR", "0.0.0.0:%d".formatted(API_PORT));
    }

    @Override
    protected Optional<String> resolveProperty(String propertyName, GenericContainer<?> container) {
        if (MAILHOG_HOST.equals(propertyName)) {
            return Optional.of(container.getHost());
        }
        if (MAILHOG_SMTP_PORT.equals(propertyName)) {
            return Optional.of(container.getMappedPort(SMTP_PORT).toString());
        }
        if (MAILHOG_API_URL.equals(propertyName)) {
            return Optional.of("http://%s:%s".formatted(container.getHost(), container.getMappedPort(API_PORT).toString()));
        }

        return Optional.empty();
    }

    @Override
    protected boolean shouldAnswer(String propertyName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return SUPPORTED_PROPERTIES.contains(propertyName);
    }
}
