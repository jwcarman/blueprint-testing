package com.carmanconsulting.osgi.blueprint.test;

import com.carmanconsulting.osgi.blueprint.test.pojos.Hello;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

public class TestBundles extends BaseTest
{
    @Test
    public void testWhatever()
    {
        assertNotNull(getOsgiService(Hello.class));
    }

    @Override
    protected String getBlueprintDescriptor()
    {
        return "simple-bundle.xml";
    }
}
