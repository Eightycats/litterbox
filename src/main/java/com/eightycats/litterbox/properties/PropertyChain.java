package com.eightycats.litterbox.properties;

import java.util.Properties;

/**
 * Properties implementation that can inherit values from a chain of parent Properties. If a
 * property is not set in this instance, it will check its parents for a default value.
 */
public class PropertyChain extends Properties
{
    /**
     * Required by Serializable interface.
     */
    private static final long serialVersionUID = 1L;

    protected String _name;

    protected Properties _parent;

    public PropertyChain (String name)
    {
        this(name, null);
    }

    public PropertyChain (String name, PropertyChain parentProps)
    {
        setName(name);
        setParent(parentProps);
    }

    @Override
    public String getProperty (String key)
    {
        String value = super.getProperty(key);
        if (value == null && _parent != null) {
            value = _parent.getProperty(key);
        }
        return value;
    }

    public String getName ()
    {
        return _name;
    }

    public void setName (String name)
    {
        _name = name;
    }

    public Properties getParent ()
    {
        return _parent;
    }

    public void setParent (Properties parent)
    {
        _parent = parent;
    }
}
