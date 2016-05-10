/**
 * Copyright 2016 Matthew A Jensen <eightycats@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
