package io.cloudflight.testresources.springboot;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class MinioTestResourcesProviderTest {

    private final MinioTestResourcesProvider provider = new MinioTestResourcesProvider();

    @Test
    void getResolvableProperties() {
        assertThat(provider.getResolvableProperties(Collections.emptyMap(), Collections.emptyMap()))
                .contains("minio.secret-key", "minio.url", "minio.region", "minio.access-key");
    }
}
