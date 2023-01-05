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

@Order(Ordered.LOWEST_PRECEDENCE)
public class TestResourcesEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Optional<TestResourcesClient> resourcesClient = TestResourcesClientFactory.fromSystemProperties();
        if (resourcesClient.isPresent()) {
            environment.getPropertySources().addFirst(new TestResourcesPropertySource(resourcesClient.get(), environment));
        } else {
            throw new ApplicationContextException("TestResources Service could not be found. Is the Test-Resources Build plugin for Maven or Gradle active?");
        }
    }
}
