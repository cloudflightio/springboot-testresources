package io.cloudflight.testresources.springboot.jdbc.mssql;

import io.cloudflight.testresources.springboot.jdbc.AbstractJdbcTestResourceProvider;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.LicenseAcceptance;

import java.util.Map;

public class MssqlServerTestResourcesProvider extends AbstractJdbcTestResourceProvider<MSSQLServerContainer<?>> {

    private static final String DEFAULT_IMAGE = "mcr.microsoft.com/mssql/server:2019-CU16-GDR1-ubuntu-20.04";
    private static final String SIMPLE_NAME = "mssql";


    @Override
    protected String getSimpleName() {
        return SIMPLE_NAME;
    }

    @Override
    protected String getDefaultImageName() {
        return DEFAULT_IMAGE;
    }

    @Override
    protected MSSQLServerContainer<?> createContainer(DockerImageName imageName, Map<String, Object> requestedProperties, Map<String, Object> testResourcesConfiguration) {
        return createMSSQLContainer(imageName, getSimpleName(), testResourcesConfiguration);
    }

    public static MSSQLServerContainer<?> createMSSQLContainer(DockerImageName imageName, String simpleName, Map<String, Object> testResourcesConfiguration) {
        MSSQLServerContainer<?> container = new MSSQLServerContainer<>(imageName);
        String licenseKey = "containers." + simpleName + ".accept-license";
        Boolean acceptLicense = (Boolean) testResourcesConfiguration.get(licenseKey);
        if (Boolean.TRUE.equals(acceptLicense)) {
            container.acceptLicense();
        } else {
            try {
                LicenseAcceptance.assertLicenseAccepted(imageName.toString());
            } catch (IllegalStateException ex) {
                throw new IllegalStateException("You must set the property 'test-resources." + licenseKey + "' to true in order to use a Microsoft SQL Server test container", ex);
            }
        }
        return container;
    }

}
