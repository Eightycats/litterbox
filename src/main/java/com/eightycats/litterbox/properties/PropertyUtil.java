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

package com.eightycats.litterbox.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Utility for setting the value of a property in a file without harming the structure of the
 * property file or blowing away its comments.
 */
public class PropertyUtil
{
    /**
     * Command-line usage.
     */
    private static final String USAGE = "java " + PropertyUtil.class.getName()
        + " <property file path> " + "<propertyName=propertyValue> "
        + "[-properties <input property file path>]" + "\n\ne.g. java "
        + PropertyUtil.class.getName() + " filename.properties prop_name" + "=someValue "
        + "\"another_prop=some value with spaces\"" + " ...\n";

    /**
     * The updates the values of multiple properties in a property file. The structure of the file
     * and any comments therein will be unchanged.
     *
     * @param filePath
     *            String the path to the property file.
     * @param newProperties
     *            Properties the file will be updated with the properties found in this Properties
     *            instance.
     * @throws FileNotFoundException
     *             thrown if the file does not exist.
     * @throws IOException
     *             thrown if some other IO error occurs while trying to read from the file.
     */
    public static void setProperties (String filePath, Properties newProperties)
        throws FileNotFoundException, IOException
    {
        // read and write properties using the same file
        setProperties(filePath, filePath, newProperties);
    }

    /**
     * Same as other setProperties() method except that an output file other than the input file can
     * be specified.
     *
     * @param filePath
     *            String the path to the source property file.
     * @param outputFilePath
     *            String the path where the updated property file will be written.
     * @param newProperties
     *            Properties the file will be updated with the properties found in this Properties
     *            instance.
     * @throws FileNotFoundException
     *             thrown if the file does not exist.
     * @throws IOException
     *             thrown if some other IO error occurs while trying to read from the file.
     */
    public static void setProperties (String filePath, String outputFilePath,
        Properties newProperties) throws FileNotFoundException, IOException
    {
        Properties properties = readProperties(filePath);
        properties.putAll(newProperties);
        writeProperties(outputFilePath, properties);
    }

    /**
     * This sets the values of multiple properties in a property file ONLY if the values do not
     * currently exist or have no value.
     *
     * The structure of the file and any comments therein will be unchanged.
     *
     * @param filePath
     *            String the path to the source property file.
     * @param outputFilePath
     *            String the path where the updated property file will be written.
     * @param defaults
     *            Properties the file will be updated with these property values if they do not
     *            already exist.
     *
     * @throws FileNotFoundException
     *             thrown if the file does not exist.
     * @throws IOException
     *             thrown if some other IO error occurs while trying to read from the file.
     */
    public static void setPropertyDefaults (String filePath, String outputFilePath,
        Properties defaults)
        throws FileNotFoundException, IOException
    {
        Properties properties = readProperties(filePath);
        Enumeration<Object> keys = defaults.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            String existing = properties.getProperty(key);

            // only set the value if the property does not exist or if
            // its value is entirely whitespace
            if (existing == null || existing.trim().length() == 0) {
                properties.setProperty(key, defaults.getProperty(key));
            }
        }
        writeProperties(outputFilePath, properties);
    }

    /**
     * This sets the value of a single property in the given file. The structure of the file and any
     * comments will be unchanged.
     *
     * @param filePath
     *            String the path to the property file.
     * @param propertyName
     *            String the name of the property to be set.
     * @param value
     *            String the new value for the property.
     * @throws FileNotFoundException
     *             thrown if the file does not exist.
     * @throws IOException
     *             thrown if some other IO error occurs while trying to read from the file.
     */
    public static void setProperty (String filePath, String propertyName, String value)
        throws FileNotFoundException, IOException
    {
        Properties properties = readProperties(filePath);
        properties.setProperty(propertyName, value);
        writeProperties(filePath, properties);
    }

    /**
     * This sets the value of a single property in the given file ONLY if a value does not already
     * exist for the property. The structure of the file and any comments will be unchanged.
     *
     * @param filePath
     *            String the path to the property file.
     * @param propertyName
     *            String the name of the property.
     * @param value
     *            String the default value for the property if it is not already set.
     * @throws FileNotFoundException
     *             thrown if the file does not exist.
     * @throws IOException
     *             thrown if some other IO error occurs while trying to read from the file.
     */
    public static void setPropertyDefault (String filePath, String propertyName, String value)
        throws FileNotFoundException, IOException
    {
        Properties properties = readProperties(filePath);
        String existingValue = properties.getProperty(propertyName);
        // only set the value if the property does not exist or if
        // its value is entirely whitespace
        if (existingValue == null || existingValue.trim().length() == 0) {
            properties.setProperty(propertyName, value);
            writeProperties(filePath, properties);
        }
    }

    /**
     * Reads the properties from the given file path.
     */
    public static Properties readProperties (String filePath)
        throws FileNotFoundException, IOException
    {
        return readProperties(new File(filePath));
    }

    /**
     * Reads the properties from the given file.
     */
    public static Properties readProperties (File file)
        throws FileNotFoundException, IOException
    {
        if (!file.exists()) {
            throw new FileNotFoundException(file + " does not exist.");
        }

        OrderedProperties properties = new OrderedProperties();
        FileInputStream input = new FileInputStream(file);
        try {
            properties.load(input);
        } finally {
            input.close();
        }

        return properties;
    }

    public static Properties readProps (String filePath)
        throws FileNotFoundException, IOException
    {
        return readProps(new File(filePath));
    }

    public static Properties readProps (File file)
        throws FileNotFoundException, IOException
    {
        if (!file.exists()) {
            throw new FileNotFoundException(file + " does not exist.");
        }

        Properties properties = new Properties();
        FileInputStream input = new FileInputStream(file);
        try {
            properties.load(input);
        } finally {
            input.close();
        }
        return properties;
    }

    /**
     * Writes the given Properties instance to a file.
     *
     * @param filePath
     *            String the output file.
     * @param properties
     *            Properties the Properties to write out.
     * @throws IOException
     */
    public static void writeProperties (String filePath, Properties properties) throws IOException
    {
        writeProperties(filePath, properties, null);
    }

    /**
     * Writes the given Properties instance to a file.
     *
     * @param filePath
     *            String the output file.
     * @param properties
     *            Properties Properties the Properties to write out.
     * @param header
     *            String an optional header string for the file.
     * @throws IOException
     */
    public static void writeProperties (String filePath, Properties properties, String header)
        throws IOException
    {
        writeProperties(new File(filePath), properties, header);
    }

    /**
     * Writes the given Properties instance to a file.
     *
     * @param file
     *            File the output file.
     * @param properties
     *            Properties Properties the Properties to write out.
     * @param header
     *            String an optional header string for the file.
     * @throws IOException
     */
    public static void writeProperties (File file, Properties properties, String header)
        throws IOException
    {
        FileOutputStream output = new FileOutputStream(file);
        try {
            properties.store(output, header);
        } finally {
            output.close();
        }
    }

    /**
     * This is used to append a value to an existing property value.
     */
    public static void appendValue (Properties props, String name, String value)
    {
        appendValue(props, name, value, "");
    }

    /**
     * This is used to append a value to an existing property value. The given delimiter value is
     * inserted between the given value and any existing property value.
     */
    public static void appendValue (Properties props, String name, String value, String delimiter)
    {
        String existing = props.getProperty(name);
        if (existing != null) {
            value = existing + delimiter + value;
        }
        props.setProperty(name, value);
    }

    /**
     * Creates a string containing the given property values, one per line.
     */
    public static String toString (Map<?, ?> props) throws IOException
    {
        StringWriter result = new StringWriter();
        PrintWriter output = new PrintWriter(result);

        for (Entry<?, ?> entry : props.entrySet()) {
            if (entry.getValue() instanceof Property) {
                output.print(entry.getValue());
            } else {
                output.print(entry.getKey());
                output.print("=");
                output.print(entry.getValue());
            }
            output.println();
        }

        return result.getBuffer().toString();
    }

    /**
     * Gets a required property. If the property is not present, this throws an exception
     * saying so.
     *
     * @param props
     *            the properties.
     * @param propertyName
     *            the name of the required property to find.
     * @return the property value.
     * @throws Exception
     */
    public static String getRequired (Properties props, String propertyName) throws Exception
    {
        String value = props.getProperty(propertyName);
        if (value == null) {
            throw new Exception("Could not find required property: [" + propertyName + "]");
        }
        return value;
    }

    public static int getInt (Properties props, String propertyName, int defaultValue)
        throws Exception
    {
        int value = defaultValue;
        String property = props.getProperty(propertyName);
        if (property != null) {
            try {
                value = Integer.parseInt(property);
            } catch (NumberFormatException ex) {
                throw new Exception("The value [" + property + "] of property [" + property
                    + "] is not a valid numeric value.");

            }
        }
        return value;
    }

    public static int getRequiredInt (Properties props, String propertyName) throws Exception
    {
        int value;
        String property = getRequired(props, propertyName);
        try {
            value = Integer.parseInt(property);
        } catch (NumberFormatException ex) {
            throw new Exception("The value [" + property + "] of property [" + property
                + "] is not a valid numeric value.");

        }
        return value;
    }

    public static long getLong (Properties props, String propertyName, long defaultValue)
        throws Exception
    {
        long value = defaultValue;
        String property = props.getProperty(propertyName);
        if (property != null) {
            try {
                value = Long.parseLong(property);
            } catch (NumberFormatException ex) {
                throw new Exception("The value [" + property + "] of property [" + property
                    + "] is not a valid numeric value.");

            }
        }
        return value;
    }

    public static long getRequiredLong (Properties props, String propertyName)
        throws Exception
    {
        long value;
        String property = getRequired(props, propertyName);
        try {
            value = Long.parseLong(property);
        } catch (NumberFormatException ex) {
            throw new Exception("The value [" + property + "] of property [" + property
                + "] is not a valid numeric value.");
        }
        return value;
    }

    public static boolean getBoolean (Properties props, String propertyName, boolean defaultValue)
    {
        boolean value = defaultValue;
        String property = props.getProperty(propertyName);
        if (property != null) {
            value = Boolean.valueOf(property).booleanValue();
        }
        return value;
    }

    /**
     * The command-line interface for setting property values in a file.
     */
    public static void main (String[] args)
    {
        String filePath = null;
        String inputFile = null;
        Properties properties = new Properties();

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (args[i].equals("-properties")) {
                    inputFile = args[++i];
                } else {
                    die();
                }

            } else if (filePath == null) {
                filePath = args[i];

            } else {
                int separatorIndex = args[i].indexOf("=");
                if (separatorIndex == -1) {
                    // no value given for prop
                    die();

                } else {
                    String name = args[i].substring(0, separatorIndex);
                    String value = args[i].substring(separatorIndex + 1);
                    properties.setProperty(name, value);
                }
            }
        }

        if (filePath == null) {
            System.err.println("No file path was specified");
            die();
        }

        if (properties.size() == 0 && inputFile == null) {
            System.err.println("No property values were given.");
            die();
        }

        try {
            if (inputFile != null) {
                setProperties(filePath, readProperties(inputFile));
            }
            setProperties(filePath, properties);

        } catch (IOException ioex) {
            System.err.println(USAGE);
            System.err.println(ioex.getMessage());
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Print command-line usage and exit.
     */
    private static void die ()
    {
        System.err.println(USAGE);
        System.exit(0);
    }
}
