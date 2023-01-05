package io.cloudflight.testresources.springboot.client.micronaut;

/**
 * This class has been partly copied from {@link io.micronaut.testresources.client.ConfigFinder}
 * (required, because the original class is package-private)
 */
class ConfigFinder {
    public static final String SYSTEM_PROP_PREFIX = "micronaut.test.resources";

    private ConfigFinder() {
    }

    static String systemPropertyNameOf(String propertyName) {
        return SYSTEM_PROP_PREFIX + "." + propertyName;
    }
}