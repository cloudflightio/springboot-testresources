package io.cloudflight.testresources.springboot.jdbc.mariadb;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MariaDbTestResourcesProviderTest {

    final private MariaDbTestResourcesProvider provider = new MariaDbTestResourcesProvider();

    @Test
    void test() {
        List<String> properties = provider.getResolvableProperties(Collections.emptyMap(), Collections.emptyMap());
        assertFalse(properties.isEmpty());
    }

    @Test
    void shouldAnswer() {
        assertTrue(provider.shouldAnswer("spring.datasource.url", Collections.emptyMap(), Collections.emptyMap()));
    }
}
