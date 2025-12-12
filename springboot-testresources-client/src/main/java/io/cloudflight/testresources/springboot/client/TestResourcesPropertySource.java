package io.cloudflight.testresources.springboot.client;

import io.micronaut.testresources.client.TestResourcesClient;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * Property source that exposes the test resources configuration as properties.
 */
public class TestResourcesPropertySource extends PropertySource<TestResourcesClient> {

    private static final String NAME = "testresources";
    private static final String TEST_RESOURCES_PROPERTY_PREFIX = "test-resources.";
    private final ConfigurableEnvironment environment;
    private static Map<String, Object> testResourcesConfiguration;

    /**
     * Constructor.
     * @param client .
     * @param environment .
     */
    public TestResourcesPropertySource(TestResourcesClient client, ConfigurableEnvironment environment) {
        super(NAME, client);
        this.environment = environment;
    }

    @Override
    public Object getProperty(String name) {
        if (source.getResolvableProperties().contains(name)) {
            return source.resolve(name, emptyMap(), getTestResourcesConfiguration(environment)).orElse(null);
        } else {
            return null;
        }
    }

    private synchronized static Map<String, Object> getTestResourcesConfiguration(Environment env) {
        if (testResourcesConfiguration == null) {
            testResourcesConfiguration = new HashMap<>();
            if (env instanceof ConfigurableEnvironment) {
                for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
                    if (propertySource instanceof EnumerablePropertySource) {
                        for (String key : ((EnumerablePropertySource<?>) propertySource).getPropertyNames()) {
                            if (key.startsWith(TEST_RESOURCES_PROPERTY_PREFIX)) {
                                testResourcesConfiguration.put(key.substring(TEST_RESOURCES_PROPERTY_PREFIX.length()), propertySource.getProperty(key));
                            }
                        }
                    }
                }
            }
        }
        return testResourcesConfiguration;
    }
}
