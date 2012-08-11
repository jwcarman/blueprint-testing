package com.carmanconsulting.osgi.blueprint.test;

import com.carmanconsulting.osgi.blueprint.test.environment.EnvironmentBundleSpec;
import com.carmanconsulting.osgi.blueprint.test.environment.ServiceSpec;
import com.carmanconsulting.osgi.blueprint.test.pojos.Hello;
import org.junit.Test;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.io.Serializable;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class TestBundles extends BaseTest
{
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configureEnvironment(EnvironmentBundleSpec environmentBundleSpec)
    {
        environmentBundleSpec.nullService(TransactionManager.class);
    }

    @Override
    protected String getBlueprintDescriptor()
    {
        return "simple-bundle.xml";
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
