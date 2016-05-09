package com.eightycats.litterbox.properties;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class SystemProperties
{
    /**
     * Print all of the System properties.
     */
    public static void dump ()
    {
        Properties props = System.getProperties();
        // print them out in alphabetical order by key
        TreeSet<String> sortedKeys = new TreeSet<String>();
        Set<Object> keys = props.keySet();
        for (Object key : keys) {
            sortedKeys.add(key.toString());
        }

        for (String name : sortedKeys) {
            System.out.println(name + " : [" + props.getProperty(name) + "]");
        }
    }

    public static void main (String[] args)
    {
        dump();
    }

}
