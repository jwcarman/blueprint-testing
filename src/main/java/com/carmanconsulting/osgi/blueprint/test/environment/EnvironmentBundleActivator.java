package com.carmanconsulting.osgi.blueprint.test.environment;

import com.carmanconsulting.osgi.blueprint.test.environment.EnvironmentBundleSpec;
import com.carmanconsulting.osgi.blueprint.test.environment.ServiceSpec;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class EnvironmentBundleActivator implements BundleActivator
{
//----------------------------------------------------------------------------------------------------------------------
// BundleActivator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void start(BundleContext bundleContext) throws Exception
    {
        EnvironmentBundleSpec bundleSpec = EnvironmentBundleSpec.get(bundleContext.getBundle().getSymbolicName());
        bundleSpec.registerServices(bundleContext);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception
    {
        // Do nothing...
    }
}
