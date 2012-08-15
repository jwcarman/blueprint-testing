package com.carmanconsulting.osgi.blueprint.test.environment;

import de.kalpatec.pojosr.framework.felix.framework.util.MapToDictionary;
import org.osgi.framework.BundleContext;

import java.util.Map;
import java.util.TreeMap;

public class ServiceSpec<I, T extends I>
{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<I> serviceInterface;
    private final T serviceObject;
    private final Map<String, String> properties = new TreeMap<String, String>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ServiceSpec(Class<I> serviceInterface, T serviceObject)
    {
        this.serviceInterface = serviceInterface;
        this.serviceObject = serviceObject;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public ServiceSpec<I, T> property(String key, String value)
    {
        properties.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public void register(BundleContext bundleContext)
    {
        bundleContext.registerService(serviceInterface, serviceObject, new MapToDictionary(properties));
    }
}
