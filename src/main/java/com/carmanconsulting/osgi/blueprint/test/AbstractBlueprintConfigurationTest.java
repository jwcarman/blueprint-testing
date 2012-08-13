package com.carmanconsulting.osgi.blueprint.test;

import com.carmanconsulting.osgi.blueprint.test.environment.EnvironmentBundleActivator;
import com.carmanconsulting.osgi.blueprint.test.environment.EnvironmentBundleSpec;
import de.kalpatec.pojosr.framework.PojoServiceRegistryFactoryImpl;
import de.kalpatec.pojosr.framework.launch.BundleDescriptor;
import de.kalpatec.pojosr.framework.launch.ClasspathScanner;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistry;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistryFactory;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.ops4j.pax.swissbox.tinybundles.core.TinyBundle;
import org.ops4j.pax.swissbox.tinybundles.core.TinyBundles;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public abstract class AbstractBlueprintConfigurationTest
{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final long DEFAULT_TIMEOUT = 1000;

    public static final String DEFAULT_CLASSPATH_BUNDLE_FILTER = "(Bundle-SymbolicName=*)";
    public static final int DEFAULT_START_LEVEL = 50;

    private final Map<String, Integer> startLevels = new TreeMap<String, Integer>();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private BundleContext bundleContext;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    protected AbstractBlueprintConfigurationTest()
    {
        setStartLevel(getTestBundleSymbolicName(), 1);
        //setStartLevel("org.apache.aries.blueprint.core", 10);
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract URL getBlueprintDescriptor();

    protected abstract void configureEnvironment(EnvironmentBundleSpec environmentBundleSpec);

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected List<BundleDescriptor> getClasspathBundleDescriptors() throws Exception
    {
        return new ClasspathScanner().scanForBundles(getClasspathBundleFilter());
    }

    protected String getClasspathBundleFilter()
    {
        return DEFAULT_CLASSPATH_BUNDLE_FILTER;
    }

    public <T> T getOsgiService(Class<T> type)
    {
        return getOsgiService(type, null, DEFAULT_TIMEOUT);
    }

    public <T> T getOsgiService(Class<T> type, long timeout)
    {
        return getOsgiService(type, null, timeout);
    }

    public <T> T getOsgiService(Class<T> type, String filter)
    {
        return getOsgiService(type, filter, DEFAULT_TIMEOUT);
    }

    public <T> T getOsgiService(Class<T> type, String filter, long timeout)
    {
        ServiceTracker tracker;
        try
        {
            String flt;
            if (filter != null)
            {
                if (filter.startsWith("("))
                {
                    flt = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")" + filter + ")";
                }
                else
                {
                    flt = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")(" + filter + "))";
                }
            }
            else
            {
                flt = "(" + Constants.OBJECTCLASS + "=" + type.getName() + ")";
            }
            Filter osgiFilter = FrameworkUtil.createFilter(flt);
            tracker = new ServiceTracker(bundleContext, osgiFilter, null);
            tracker.open(true);
            // Note that the tracker is not closed to keep the reference
            // This is buggy, as the service reference may change i think
            Object svc = tracker.waitForService(timeout);
            if (svc == null)
            {
                Dictionary<?, ?> dic = bundleContext.getBundle().getHeaders();
                System.err.println("Test bundle headers: " + explode(dic));

                for (ServiceReference ref : asCollection(bundleContext.getAllServiceReferences(null, null)))
                {
                    System.err.println("ServiceReference: " + ref + ", bundle: " + ref.getBundle() + ", symbolicName: " + ref.getBundle().getSymbolicName());
                }

                for (ServiceReference ref : asCollection(bundleContext.getAllServiceReferences(null, flt)))
                {
                    System.err.println("Filtered ServiceReference: " + ref + ", bundle: " + ref.getBundle() + ", symbolicName: " + ref.getBundle().getSymbolicName());
                }

                throw new RuntimeException("Gave up waiting for service " + flt);
            }
            return type.cast(svc);
        }
        catch (InvalidSyntaxException e)
        {
            throw new IllegalArgumentException("Invalid filter", e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Explode the dictionary into a <code>,</code> delimited list of <code>key=value</code> pairs.
     */
    private static String explode(Dictionary<?, ?> dictionary)
    {
        Enumeration<?> keys = dictionary.keys();
        StringBuilder result = new StringBuilder();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            result.append(String.format("%s=%s", key, dictionary.get(key)));
            if (keys.hasMoreElements())
            {
                result.append(", ");
            }
        }
        return result.toString();
    }

    /**
     * Provides an iterable collection of references, even if the original array is <code>null</code>.
     */
    private static Collection<ServiceReference> asCollection(ServiceReference[] references)
    {
        return references == null ? new ArrayList<ServiceReference>(0) : Arrays.asList(references);
    }

    public <T> ServiceReference getOsgiServiceReference(Class<T> type, String filter, long timeout)
    {
        ServiceTracker tracker;
        try
        {
            String flt;
            if (filter != null)
            {
                if (filter.startsWith("("))
                {
                    flt = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")" + filter + ")";
                }
                else
                {
                    flt = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")(" + filter + "))";
                }
            }
            else
            {
                flt = "(" + Constants.OBJECTCLASS + "=" + type.getName() + ")";
            }
            Filter osgiFilter = FrameworkUtil.createFilter(flt);
            tracker = new ServiceTracker(bundleContext, osgiFilter, null);
            tracker.open(true);
            // Note that the tracker is not closed to keep the reference
            // This is buggy, as the service reference may change i think
            Object svc = tracker.waitForService(timeout);
            if (svc == null)
            {
                Dictionary<?, ?> dic = bundleContext.getBundle().getHeaders();
                System.err.println("Test bundle headers: " + explode(dic));

                for (ServiceReference ref : asCollection(bundleContext.getAllServiceReferences(null, null)))
                {
                    System.err.println("ServiceReference: " + ref + ", bundle: " + ref.getBundle() + ", symbolicName: " + ref.getBundle().getSymbolicName());
                }

                for (ServiceReference ref : asCollection(bundleContext.getAllServiceReferences(null, flt)))
                {
                    System.err.println("Filtered ServiceReference: " + ref + ", bundle: " + ref.getBundle() + ", symbolicName: " + ref.getBundle().getSymbolicName());
                }

                throw new RuntimeException("Gave up waiting for service " + flt);
            }
            return tracker.getServiceReference();
        }
        catch (InvalidSyntaxException e)
        {
            throw new IllegalArgumentException("Invalid filter", e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected String getTestBundleSymbolicName()
    {
        return getClass().getSimpleName();
    }

    protected String getTestBundleVersion()
    {
        return "1.0.0";
    }

    protected void setStartLevel(String symbolicName, int startLevel)
    {
        startLevels.put(symbolicName, startLevel);
    }

    @Before
    public void setUp() throws Exception
    {
        final File bundleDirectory = createTemporaryBundleDir();

        List<BundleDescriptor> descriptors = getClasspathBundleDescriptors();
        descriptors.add(createTestBundle(bundleDirectory));
        descriptors.add(createEnvironmentBundle(bundleDirectory));
        sort(descriptors);

        System.setProperty("org.osgi.framework.storage", bundleDirectory.getAbsolutePath());
        if (logger.isDebugEnabled())
        {
            printBundles(descriptors);
        }
        Map<String, List<BundleDescriptor>> config = new HashMap<String, List<BundleDescriptor>>();
        config.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS, descriptors);
        PojoServiceRegistry reg = new PojoServiceRegistryFactoryImpl().newPojoServiceRegistry(config);
        bundleContext = reg.getBundleContext();
    }

    @After
    public void tearDown()
    {
        try
        {
            if (bundleContext != null)
            {
                try
                {
                    bundleContext.getBundle().stop();
                }
                catch (BundleException e)
                {
                    logger.error("Unable to stop bundle.", e);
                    throw new RuntimeException("Unable to stop bundle.", e);
                }
            }
        }
        finally
        {
            System.clearProperty("org.osgi.framework.storage");
        }
    }

    private File createTemporaryBundleDir() throws IOException
    {
        File bundleDir = new File("target/bundles/" + getClass().getSimpleName() + "-" + System.currentTimeMillis());
        if (!bundleDir.mkdirs())
        {
            throw new RuntimeException("Unable to create bundle directory.");
        }
        return bundleDir;
    }

    protected BundleDescriptor createEnvironmentBundle(File bundleDirectory) throws IOException
    {
        String environmentBundleSymbolicName = getTestBundleSymbolicName() + "Environment";
        configureEnvironment(EnvironmentBundleSpec.create(environmentBundleSymbolicName));
        TinyBundle bundle = TinyBundles.newBundle();
        final Map<String, String> headers = new TreeMap<String, String>();
        headers.put("Manifest-Version", "2");
        headers.put("Bundle-ManifestVersion", "2");
        headers.put("Bundle-SymbolicName", environmentBundleSymbolicName);
        headers.put("Bundle-Version", getTestBundleVersion());
        headers.put("Bundle-Activator", EnvironmentBundleActivator.class.getName());
        return createBundleDescriptor(bundle, headers, bundleDirectory);
    }

    protected BundleDescriptor createTestBundle(File bundleDirectory) throws IOException
    {
        TinyBundle bundle = TinyBundles.newBundle();
        final String symbolicName = getTestBundleSymbolicName();
        final URL descriptorUrl = getBlueprintDescriptor();
        logger.info("Using blueprint XML file: {}", descriptorUrl.getFile());
        bundle.add("OSGI-INF/blueprint/" + symbolicName + ".xml", descriptorUrl);
        final Map<String, String> headers = new TreeMap<String, String>();
        headers.put("Manifest-Version", "2");
        headers.put("Bundle-ManifestVersion", "2");
        headers.put("Bundle-SymbolicName", symbolicName);
        headers.put("Bundle-Version", getTestBundleVersion());
        return createBundleDescriptor(bundle, headers, bundleDirectory);
    }

    private BundleDescriptor createBundleDescriptor(TinyBundle bundle, Map<String, String> headers, File bundleDirectory) throws IOException
    {
        copyHeaders(headers, bundle);
        return new BundleDescriptor(getClass().getClassLoader(), toUrl(bundleDirectory, bundle), headers);
    }

    private void copyHeaders(Map<String, String> headers, TinyBundle bundle)
    {
        for (Map.Entry<String, String> entry : headers.entrySet())
        {
            bundle.set(entry.getKey(), entry.getValue());
        }
    }

    private URL toUrl(File bundleDirectory, TinyBundle tinyBundle) throws IOException
    {
        final File file = new File(bundleDirectory, "bundle-" + UUID.randomUUID().toString() + ".jar");
        final FileOutputStream fout = new FileOutputStream(file);
        IOUtils.copy(tinyBundle.build(), fout);
        IOUtils.closeQuietly(fout);
        return new URL("jar:" + file.toURI().toString() + "!/");
    }

    private void sort(List<BundleDescriptor> bundles)
    {
        Collections.sort(bundles, new Comparator<BundleDescriptor>()
        {
            @Override
            public int compare(BundleDescriptor o1, BundleDescriptor o2)
            {
                return getStartLevel(getSymbolicName(o1)) - getStartLevel(getSymbolicName(o2));
            }
        });
    }

    private int getStartLevel(String symbolicName)
    {
        final Integer startLevel = startLevels.get(symbolicName);
        return startLevel == null ? DEFAULT_START_LEVEL : startLevel;
    }

    private String getSymbolicName(BundleDescriptor bundle)
    {
        return bundle.getHeaders().get(Constants.BUNDLE_SYMBOLICNAME);
    }

    private void printBundles(List<BundleDescriptor> descriptors)
    {
        for (int i = 0; i < descriptors.size(); i++)
        {
            BundleDescriptor desc = descriptors.get(i);
            logger.debug("Bundle #{} -> {}", i, desc);
        }
    }
}

