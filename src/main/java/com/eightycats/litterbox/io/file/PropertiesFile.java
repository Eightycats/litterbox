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

package com.eightycats.litterbox.io.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.eightycats.litterbox.io.StreamUtils;
import com.eightycats.litterbox.properties.OrderedProperties;

/**
 * Utility for reading and writing Properties to and from .properties files.
 */
public class PropertiesFile
{
    /**
     * Read properties from a file.
     */
    public static Properties readProperties (String filename)
        throws FileNotFoundException, IOException
    {
        Properties props = new Properties();
        loadProperties(filename, props);
        return props;
    }

    /**
     * Utility method to read properties in from a file keeping the property file's order and
     * contents intact.
     */
    public static Properties readOrderedProperties (String filename)
        throws FileNotFoundException, IOException
    {
        Properties props = new OrderedProperties();
        loadProperties(filename, props);
        return props;
    }

    /**
     * Reads properties from the named file into the given properties object.
     *
     * @param filename
     *            String the path to the properties file.
     * @param props
     *            Properties the properties object into which the file will be loaded.
     * @throws FileNotFoundException
     *             if the properties file can not be found.
     * @throws IOException
     */
    public static void loadProperties (String filename, Properties props)
        throws FileNotFoundException, IOException
    {
        FileInputStream input = new FileInputStream(filename);
        try {
            props.load(input);
        } finally {
            input.close();
        }
    }

    /**
     * Writes the given properties to the named file.
     *
     * @param filename
     *            String the path to the properties file.
     * @param props
     *            Properties the properties to write.
     * @throws IOException
     */
    public static void writeProperties (String filename, Properties props) throws IOException
    {
        writeProperties(filename, props, "");
    }

    /**
     * Writes the given properties to the named file.
     *
     * @param filename
     *            String the path to the properties file.
     * @param props
     *            Properties the properties to write.
     * @param header
     *            String the property file header.
     */
    public static void writeProperties (String filename, Properties props, String header)
        throws IOException
    {
        FileOutputStream output = new FileOutputStream(filename);
        try {
            props.store(output, header);
        } finally {
            StreamUtils.close(output);
        }
    }
}
