package io.cloudflight.testresources.springboot.jdbc.mariadb;

import io.micronaut.testresources.testcontainers.AbstractTestContainersProvider;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Superclass for JDBC test containers providers.
 *
 * @param <T> the type of the container
 */
public abstract class AbstractJdbcTestResourceProvider<T extends JdbcDatabaseContainer<? extends T>> extends AbstractTestContainersProvider<T> {
    private static final String PREFIX = "spring.datasource";
    private static final String URL = "url";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRIVER = "driver-class-name";
    private static final String DB_NAME = "db-name";
    private static final String INIT_SCRIPT = "init-script-path";

    private static final List<String> SUPPORTED_LIST = Collections.unmodifiableList(
            Arrays.asList(URL, USERNAME, PASSWORD, DRIVER)
    );

    @Override
    public List<String> getResolvableProperties(Map<String, Collection<String>> propertyEntries, Map<String, Object> testResourcesConfig) {
        return SUPPORTED_LIST.stream().map(p -> PREFIX + "." + p).collect(Collectors.toList());
    }

    @Override
    public List<String> getRequiredPropertyEntries() {
        return Collections.singletonList(PREFIX);
    }

    @Override
    protected boolean shouldAnswer(String propertyName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return propertyName.startsWith(PREFIX);
    }

    @Override
    protected Optional<String> resolveProperty(String expression, T container) {
        String value = switch (datasourcePropertyFrom(expression)) {
            case URL -> container.getJdbcUrl();
            case USERNAME -> container.getUsername();
            case PASSWORD -> container.getPassword();
            case DRIVER -> container.getDriverClassName();
            default -> null;
        };
        return Optional.ofNullable(value);
    }

    @Override
    protected void configureContainer(T container, Map<String, Object> properties, Map<String, Object> testResourcesConfiguration) {
        super.configureContainer(container, properties, testResourcesConfiguration);
        ifPresent(INIT_SCRIPT, testResourcesConfiguration, container::withInitScript);
        ifPresent(USERNAME, testResourcesConfiguration, container::withUsername);
        ifPresent(PASSWORD, testResourcesConfiguration, container::withPassword);
        ifPresent(DB_NAME, testResourcesConfiguration, container::withDatabaseName);
    }

    private void ifPresent(String key, Map<String, Object> testResourcesConfiguration, Consumer<String> consumer) {
        Object value = testResourcesConfiguration.get("containers." + getSimpleName() + "." + key);
        if (value != null) {
            consumer.accept(value.toString());
        }
    }

    protected static String datasourcePropertyFrom(String expression) {
        String remainder = expression.substring(1 + expression.indexOf('.'));
        return remainder.substring(1 + remainder.indexOf("."));
    }
}