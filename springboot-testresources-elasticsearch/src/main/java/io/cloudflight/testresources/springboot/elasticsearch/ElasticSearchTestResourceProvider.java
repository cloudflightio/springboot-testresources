package io.cloudflight.testresources.springboot.elasticsearch;

import io.micronaut.testresources.testcontainers.AbstractTestContainersProvider;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

public class ElasticSearchTestResourceProvider extends AbstractTestContainersProvider<ElasticsearchContainer> {

    public static final String ELASTICSEARCH_HOSTS = "elasticsearch.http-hosts";
    public static final String SIMPLE_NAME = "elasticsearch";
    public static final String DEFAULT_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch";

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
        ElasticsearchContainer container = new ElasticsearchContainer(imageName);
        container.withEnv("xpack.security.enabled", "false");
        return container;
    }

    @Override
    protected Optional<String> resolveProperty(String propertyName, ElasticsearchContainer container) {
        if (ELASTICSEARCH_HOSTS.equals(propertyName)) {
            return Optional.of("http://" + container.getHttpHostAddress());
        }
        return Optional.empty();
    }

    @Override
    protected boolean shouldAnswer(String propertyName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return ELASTICSEARCH_HOSTS.equals(propertyName);
    }

    @Override
    public List<String> getResolvableProperties(Map<String, Collection<String>> propertyEntries, Map<String, Object> testResourcesConfig) {
        return Collections.singletonList(ELASTICSEARCH_HOSTS);
    }
}
