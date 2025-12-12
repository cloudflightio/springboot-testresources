package io.cloudflight.testresources.springboot.jdbc.mariadb;

import io.cloudflight.testresources.springboot.jdbc.mssql.MssqlServerTestResourcesProvider;
import jdk.jfr.Enabled;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnOs;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.condition.OS.LINUX;

class MssqlServerTestResourcesProviderTest {

    final private MssqlServerTestResourcesProvider provider = new MssqlServerTestResourcesProvider();

    @Test
    @Disabled("does not run on silicon mac")
    void test() {
        List<String> properties = provider.getResolvableProperties(Collections.emptyMap(), Collections.emptyMap());
        assertFalse(properties.isEmpty());
    }

    @Test
    @Disabled("does not run on silicon mac")
    void shouldAnswer() {
        assertTrue(provider.shouldAnswer("spring.datasource.url", Collections.emptyMap(), Collections.emptyMap()));
    }
}
