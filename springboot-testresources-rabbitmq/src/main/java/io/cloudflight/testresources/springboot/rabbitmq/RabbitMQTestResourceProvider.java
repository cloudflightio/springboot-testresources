package io.cloudflight.testresources.springboot.rabbitmq;

import io.micronaut.testresources.testcontainers.AbstractTestContainersProvider;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

public class RabbitMQTestResourceProvider extends AbstractTestContainersProvider<RabbitMQContainer> {

    private static final String RABBITMQ_HOST = "spring.rabbitmq.host";
    private static final String RABBITMQ_PORT = "spring.rabbitmq.port";
    private static final String RABBITMQ_USERNAME = "spring.rabbitmq.username";
    private static final String RABBITMQ_PASSWORD = "spring.rabbitmq.password";
    private static final String DEFAULT_IMAGE = "rabbitmq";
    private static final String SIMPLE_NAME = "rabbitmq";

    private static final List<String> SUPPORTED_KEYS = List.of(RABBITMQ_HOST, RABBITMQ_PORT, RABBITMQ_USERNAME, RABBITMQ_PASSWORD);
    private static final Set<String> SUPPORTED_KEYSET = Set.copyOf(SUPPORTED_KEYS);

    @Override
    public List<String> getResolvableProperties(Map<String, Collection<String>> propertyEntries, Map<String, Object> testResourcesConfig) {
        return SUPPORTED_KEYS;
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
    protected RabbitMQContainer createContainer(DockerImageName imageName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return new RabbitMQContainer(imageName);
    }

    @Override
    protected Optional<String> resolveProperty(String propertyName, RabbitMQContainer container) {
        switch (propertyName) {
            case RABBITMQ_HOST -> {
                return Optional.of(container.getHost());
            }
            case RABBITMQ_PORT -> {
                return Optional.of(container.getAmqpPort().toString());
            }
            case RABBITMQ_USERNAME -> {
                return Optional.of(container.getAdminUsername());
            }
            case RABBITMQ_PASSWORD -> {
                return Optional.of(container.getAdminPassword());
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    @Override
    protected boolean shouldAnswer(String propertyName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return SUPPORTED_KEYSET.contains(propertyName);
    }
}