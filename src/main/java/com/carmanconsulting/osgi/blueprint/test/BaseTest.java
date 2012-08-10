package com.carmanconsulting.osgi.blueprint.test;

import org.junit.After;
import org.junit.Before;
import org.ops4j.pax.swissbox.tinybundles.core.TinyBundle;
import org.ops4j.pax.swissbox.tinybundles.core.TinyBundles;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest
{
    private BundleContext bundleContext;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Before
    public void setUp() throws Exception
    {
        String symbolicName = getClass().getSimpleName();

        TinyBundle testBundle = Helper.createTestBundle(symbolicName, "1.0.0.SNAPSHOT", getBlueprintDescriptor());

        this.bundleContext = Helper.createBundleContext(getBundleFilter(), new TinyBundle[]{testBundle});
        log.debug("Waiting for BlueprintContainer to be published with symbolicName: {}", symbolicName);
        getOsgiService(BlueprintContainer.class, "(osgi.blueprint.container.symbolicname=" + symbolicName + ")");

    }

    @After
    public void tearDown() throws Exception
    {
        Helper.disposeBundleContext(bundleContext);
    }

    /**
     * Return the system bundle context
     *
     * @return
     */
    protected BundleContext getBundleContext()
    {
        return bundleContext;
    }

    /**
     * Gets the bundle descriptor from the classpath.
     * <p/>
     * Return the location(s) of the bundle descriptors from the classpath.
     * Separate multiple locations by comma, or return a single location.
     * <p/>
     * For example override this method and return <tt>OSGI-INF/blueprint/camel-context.xml</tt>
     *
     * @return the location of the bundle descriptor file.
     */
    protected String getBlueprintDescriptor()
    {
        return null;
    }

    /**
     * Gets filter expression of bundle descriptors.
     * Modify this method if you wish to change default behavior.
     *
     * @return filter expression for OSGi bundles.
     */
    protected String getBundleFilter()
    {
        return Helper.BUNDLE_FILTER;
    }

    /**
     * Gets test bundle version.
     * Modify this method if you wish to change default behavior.
     *
     * @return test bundle version
     */
    protected String getBundleVersion()
    {
        return Helper.BUNDLE_VERSION;
    }

    protected <T> T getOsgiService(Class<T> type)
    {
        return Helper.getOsgiService(bundleContext, type);
    }

    protected <T> T getOsgiService(Class<T> type, long timeout)
    {
        return Helper.getOsgiService(bundleContext, type, timeout);
    }

    protected <T> T getOsgiService(Class<T> type, String filter)
    {
        return Helper.getOsgiService(bundleContext, type, filter);
    }

    protected <T> T getOsgiService(Class<T> type, String filter, long timeout)
    {
        return Helper.getOsgiService(bundleContext, type, filter, timeout);
    }
}
