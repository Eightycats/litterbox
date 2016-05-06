package com.eightycats.litterbox.properties;

import java.io.Serializable;

/**
 * A class that represents a name-value Property pair.
 */
public class Property implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * The property name.
     */
    protected String _key;

    /**
     * The property value.
     */
    protected Object _value;

    public Property (String key, Object value)
    {
        _key = key;
        _value = value;
    }

    /**
     * Gets the property's name.
     */
    public String getKey ()
    {
        return _key;
    }

    /**
     * Gets the property's value.
     */
    public Object getValue ()
    {
        return _value;
    }

    /**
     * Sets a new value for the property.
     */
    public void setValue (Object value)
    {
        _value = value;
    }

    /**
     * Return: "propertyName=propertyValue".
     */
    @Override
    public String toString ()
    {
        return getKey() + PropertyConstants.KEY_VALUE_SEPARATOR + getValue();
    }
}
