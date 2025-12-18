package io.cloudflight.testresources.springboot.azurite;

import io.micronaut.testresources.testcontainers.AbstractTestContainersProvider;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

/**
 * Test container provider for Azure Blob Storage Emulator.
 */
public class AzuriteTestResourceProvider extends AbstractTestContainersProvider<GenericContainer<?>> {

    private static final String PREFIX = "spring.cloud.azure.storage.blob.";
    private static final String ACCOUNT_NAME_PROPERTY = PREFIX + "account-name";
    private static final String ACCOUNT_KEY_PROPERTY = PREFIX + "account-key";
    private static final String ACCOUNT_ENDPOINT_PROPERTY = PREFIX + "endpoint";
    private static final String DEFAULT_IMAGE = "mcr.microsoft.com/azure-storage/azurite";
    private static final String SIMPLE_NAME = "azurite";

    /**
     * can't be changed, see <a href="https://github.com/Azure/Azurite#default-storage-account">https://github.com/Azure/Azurite#default-storage-account</a>
     */
    private static final String ACCOUNT_NAME = "devstoreaccount1";

    /**
     * can't be changed, see <a href="https://github.com/Azure/Azurite#default-storage-account">https://github.com/Azure/Azurite#default-storage-account</a>
     */
    private static final String ACCOUNT_KEY = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";

    private static final int AZURITE_PORT = 10000;

    private static final Set<String> SUPPORTED_PROPERTIES;

    static {
        SUPPORTED_PROPERTIES = Set.of(ACCOUNT_NAME_PROPERTY, ACCOUNT_KEY_PROPERTY, ACCOUNT_ENDPOINT_PROPERTY);
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
        container.withExposedPorts(AZURITE_PORT);
        return container;
    }

    @Override
    protected Optional<String> resolveProperty(String propertyName, GenericContainer<?> container) {
        String value = switch (propertyName) {
            case ACCOUNT_ENDPOINT_PROPERTY -> "http://" + container.getHost() + ":" + container.getMappedPort(AZURITE_PORT) + "/" + ACCOUNT_NAME;
            case ACCOUNT_NAME_PROPERTY -> ACCOUNT_NAME;
            case ACCOUNT_KEY_PROPERTY -> ACCOUNT_KEY;
            default -> null;
        };
        return Optional.ofNullable(value);
    }

    @Override
    protected boolean shouldAnswer(String propertyName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return SUPPORTED_PROPERTIES.contains(propertyName);
    }
}