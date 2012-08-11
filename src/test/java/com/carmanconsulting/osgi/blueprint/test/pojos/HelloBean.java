package com.carmanconsulting.osgi.blueprint.test.pojos;

public class HelloBean implements Hello
{
//----------------------------------------------------------------------------------------------------------------------
// Hello Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String sayHello(String name)
    {
        return "Hello, " + name + "!";
    }
}
