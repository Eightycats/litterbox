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
