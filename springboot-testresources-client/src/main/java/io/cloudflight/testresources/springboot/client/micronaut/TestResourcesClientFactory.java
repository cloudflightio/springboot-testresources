package io.cloudflight.testresources.springboot.client.micronaut;

import io.micronaut.http.client.DefaultHttpClientConfiguration;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.testresources.client.DefaultTestResourcesClient;
import io.micronaut.testresources.client.TestResourcesClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * This class has been partly copied from {@link io.micronaut.testresources.client.TestResourcesClientFactory}
 * (required, because the original class is package-private)
 */
public class TestResourcesClientFactory {

    /**
     * Tries to create a {@link TestResourcesClient} from the system properties.
     * @return The {@link TestResourcesClient} if it could be created, otherwise {@link Optional#empty()}
     */
    public static Optional<TestResourcesClient> fromSystemProperties() {
        String serverUri = System.getProperty(ConfigFinder.systemPropertyNameOf(TestResourcesClient.SERVER_URI));
        if (serverUri != null) {
            String accessToken = System.getProperty(ConfigFinder.systemPropertyNameOf(TestResourcesClient.ACCESS_TOKEN));
            String clientTimeoutString = System.getProperty(ConfigFinder.systemPropertyNameOf(TestResourcesClient.CLIENT_READ_TIMEOUT), DEFAULT_TIMEOUT_SECONDS);
            int clientReadTimeout = Integer.parseInt(clientTimeoutString);
            HttpClientConfiguration config = new DefaultHttpClientConfiguration();
            config.setReadTimeout(Duration.of(clientReadTimeout, ChronoUnit.SECONDS));
            HttpClient client;
            try {
                client = HttpClient.create(new URL(serverUri), config);
            } catch (MalformedURLException e) {
                return Optional.empty();
            }
            return Optional.of(new DefaultTestResourcesClient(client, accessToken));
        }
        return Optional.empty();
    }

    private static final String DEFAULT_TIMEOUT_SECONDS = "60";

}
