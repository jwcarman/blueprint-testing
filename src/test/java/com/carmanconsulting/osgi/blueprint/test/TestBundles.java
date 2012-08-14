package com.carmanconsulting.osgi.blueprint.test;

import com.carmanconsulting.osgi.blueprint.test.environment.EnvironmentBundleSpec;
import com.carmanconsulting.osgi.blueprint.test.pojos.Hello;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationAdmin;

import javax.transaction.TransactionManager;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class TestBundles extends AbstractBlueprintConfigurationTest
{
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configureEnvironment(EnvironmentBundleSpec environmentBundleSpec)
    {
        environmentBundleSpec.nullService(TransactionManager.class);
        environmentBundleSpec.nullService(ConfigurationAdmin.class);
    }

    @Override
    protected URL getBlueprintDescriptor()
    {
        return getClass().getResource("/simple-bundle.xml");
    }

    @Test
    public void testBar()
    {
        assertNotNull(getOsgiService(Hello.class));
    }

    @Test
    public void testFoo()
    {
        assertNotNull(getOsgiService(Hello.class));
    }
}
