package com.eightycats.litterbox.properties.xml;

import java.util.Properties;

import com.eightycats.litterbox.properties.OrderedProperties;

public class OrderedXMLProperties extends XMLProperties
{
    public OrderedXMLProperties (String xmlFilePath) throws Exception
    {
        super(xmlFilePath);
    }

    public OrderedXMLProperties (String[] xmlFiles) throws Exception
    {
        super(xmlFiles);
    }

    /**
     * Overrides the parent method to use the OrderedXMLPropertiesParser.
     */
    @Override
    protected XMLPropertiesParser createParser ()
    {
        return new OrderedXMLPropertiesParser();
    }

    /**
     * Use an alternate implementation of the Properties class.
     */
    @Override
    protected Properties createProperties ()
    {
        return new OrderedProperties();
    }

    /**
     * Reads an XML file and lists the properties found.
     */
    public static void main (String[] args)
    {
        try {
            System.out.println(new OrderedXMLProperties(args));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
