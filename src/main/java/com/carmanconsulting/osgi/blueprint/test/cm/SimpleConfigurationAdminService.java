package com.carmanconsulting.osgi.blueprint.test.cm;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;

public class SimpleConfigurationAdminService implements ConfigurationAdmin
{
    @Override
    public Configuration createFactoryConfiguration(String factoryPid) throws IOException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Configuration createFactoryConfiguration(String factoryPid, String location) throws IOException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Configuration getConfiguration(String pid, String location) throws IOException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Configuration getConfiguration(String pid) throws IOException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Configuration[] listConfigurations(String filter) throws IOException, InvalidSyntaxException
    {
        return new Configuration[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
