package io.cloudflight.testresources.springboot.elasticsearch;

import io.micronaut.testresources.testcontainers.AbstractTestContainersProvider;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Test container provider for ElasticSearch.
 */
public class ElasticSearchTestResourceProvider extends AbstractTestContainersProvider<ElasticsearchContainer> {

    private static final String SIMPLE_NAME = "elasticsearch";
    private static final String DEFAULT_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch";
    private static final String DEFAULT_TAG = "8.4.3";
    private static final String PREFIX = "spring.elasticsearch";
    private static final String URIS = "uris";
    private static final String PASSWORD = "password";
    private static final String ENV_PASSWORD = "ELASTIC_PASSWORD";
    private static final List<String> SUPPORTED_LIST = Collections.unmodifiableList(
            Arrays.asList(URIS, PASSWORD)
    );

    @Override
    protected String getSimpleName() {
        return SIMPLE_NAME;
    }

    @Override
    protected String getDefaultImageName() {
        return DEFAULT_IMAGE;
    }

    @Override
    protected ElasticsearchContainer createContainer(DockerImageName imageName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        if ("latest".equals(imageName.getVersionPart())) {
            // ElasticSearch does't provide a latest tag, so we use a hardcoded version
            imageName = imageName.withTag(DEFAULT_TAG);
        }
        ElasticsearchContainer container = new ElasticsearchContainer(imageName);
        container.withEnv("xpack.security.enabled", "false");
        return container;
    }

    @Override
    protected Optional<String> resolveProperty(String propertyName, ElasticsearchContainer container) {
        String value = switch (configurationPropertyFrom(propertyName)) {
            case URIS -> container.getHttpHostAddress();
            case PASSWORD -> container.getEnvMap().get(ENV_PASSWORD);
            default -> null;
        };
        return Optional.ofNullable(value);
    }

    private String configurationPropertyFrom(String expression) {
        String[] propertyParts = expression.split("\\.");
        return propertyParts[propertyParts.length - 1];
    }

    @Override
    public boolean shouldAnswer(String propertyName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return propertyName.startsWith(PREFIX);
    }

    @Override
    public List<String> getResolvableProperties(Map<String, Collection<String>> propertyEntries, Map<String, Object> testResourcesConfig) {
        return SUPPORTED_LIST.stream().map(p -> PREFIX + "." + p).collect(Collectors.toList());
    }

}
