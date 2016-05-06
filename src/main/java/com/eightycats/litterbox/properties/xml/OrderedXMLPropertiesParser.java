package com.eightycats.litterbox.properties.xml;

import java.util.Properties;

import com.eightycats.litterbox.properties.OrderedProperties;

public class OrderedXMLPropertiesParser extends XMLPropertiesParser
{
    /**
     * This overrides the properties parser to use an OrderedProperties object instead of just of
     * Properties object.
     */
    @Override
    protected Properties createProperties ()
    {
        return new OrderedProperties();
    }
}
