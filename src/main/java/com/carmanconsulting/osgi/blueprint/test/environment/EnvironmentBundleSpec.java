package com.carmanconsulting.osgi.blueprint.test.environment;

import org.apache.commons.proxy.ProxyFactory;
import org.apache.commons.proxy.ProxyUtils;
import org.apache.commons.proxy.invoker.NullInvoker;
import org.osgi.framework.BundleContext;

import java.util.*;

public class EnvironmentBundleSpec
{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Map<String,EnvironmentBundleSpec> specMap = new TreeMap<String, EnvironmentBundleSpec>();
    private final List<ServiceSpec<?,?>> services = new LinkedList<ServiceSpec<?,?>>();
    private ProxyFactory proxyFactory = new ProxyFactory();

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static EnvironmentBundleSpec create(String bundleSymbolicName)
    {
        final EnvironmentBundleSpec spec = new EnvironmentBundleSpec();
        specMap.put(bundleSymbolicName, spec);
        return spec;
    }

    public static EnvironmentBundleSpec get(String bundleSymbolicName)
    {
        return specMap.remove(bundleSymbolicName);
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private EnvironmentBundleSpec()
    {
        
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public <I> EnvironmentBundleSpec nullService(Class<I> serviceInterface)
    {
        I proxy = (I)ProxyUtils.createNullObject(proxyFactory, new Class[] {serviceInterface});
        service(new ServiceSpec<I,I>(serviceInterface, proxy));
        return this;
    }

    public void registerServices(BundleContext bundleContext)
    {
        for (ServiceSpec<?, ?> service : services)
        {
            service.register(bundleContext);
        }
    }

    public <I,T extends I> EnvironmentBundleSpec service(ServiceSpec<I,T> serviceSpec)
    {
        services.add(serviceSpec);
        return this;
    }
}
