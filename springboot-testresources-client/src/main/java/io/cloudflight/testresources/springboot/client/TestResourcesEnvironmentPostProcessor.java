package io.cloudflight.testresources.springboot.client;

import io.cloudflight.testresources.springboot.client.micronaut.TestResourcesClientFactory;
import io.micronaut.testresources.client.TestResourcesClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Optional;

/**
 * The TestResourcesEnvironmentPostProcessor class is a Spring EnvironmentPostProcessor
 * that integrates with the test resources service and modifies the application's
 * environment configuration accordingly.
 *
 * This processor attempts to instantiate a TestResourcesClient from system
 * properties using the {@link TestResourcesClientFactory#fromSystemProperties()}
 * method. If successful, it adds a custom {@link TestResourcesPropertySource}
 * to the environment's property sources to resolve properties from the test
 * resources service.
 *
 * If the TestResourcesClient cannot be created, an ApplicationContextException
 * is thrown indicating that the TestResources service is unavailable, and the
 * Test-Resources Build plugin for Maven or Gradle may not be active.
 *
 * The processor is set to execute with the lowest precedence to ensure it interacts
 * with an already configured environment.
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class TestResourcesEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Optional<TestResourcesClient> resourcesClient = TestResourcesClientFactory.fromSystemProperties();
        if (resourcesClient.isPresent()) {
            environment.getPropertySources().addFirst(new TestResourcesPropertySource(resourcesClient.get(), environment));
        } else {
            // If the Test Resources service is not available, do not fail the context startup.
            // Simply proceed without registering the property source. This allows tests/projects
            // which don't rely on the Test Resources service to run normally, and avoids
            // hard failures when the build plugin isn't active for a given task.
        }
    }
}
