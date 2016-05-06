package com.eightycats.litterbox.properties.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.eightycats.litterbox.logging.Logger;
import com.eightycats.litterbox.properties.IndexedProperties;

/**
 * This class is used to parse named groups of Properties from a custom XML format. This XML format
 * also allows for property sets to extend from one another, and this class takes care of merging
 * properties with their parent property sets.
 *
 * This class provides access to the parsed Properties objects.
 */
public class XMLProperties
{

    /**
     * This maps property group names to the Properties object with that name.
     */
    protected Map<String, Properties> propertySets = new HashMap<String, Properties>();

    /**
     * This table maps a child property set name to the name of its parent properties.
     */
    protected Map<String, String> parentTable = new HashMap<String, String>();

    /**
     * This parses the named Properties objects found in the given XML file.
     *
     * @param xmlFilePath
     *            the path to an XML properties file.
     */
    public XMLProperties (String xmlFilePath) throws Exception
    {
        this(new String[] { xmlFilePath });
    }

    public XMLProperties (String[] xmlFiles) throws Exception
    {
        // First, parse the XML and read in
        // all of the properties sets. This
        // populates propertySets with
        // Properties objects and populates
        // parentTable with a mapping of child
        // properties to their parent sets.
        XMLPropertiesParser parser = createParser();

        for (int i = 0; i < xmlFiles.length; i++) {
            parser.parse(xmlFiles[i]);
        }

        // get the parsed properties and parent names
        propertySets.putAll(parser.getPropertiesSets());
        parentTable.putAll(parser.getParentNames());

        // The second pass merges properties with their parent properties.
        mergeParentProperties();
    }

    private void mergeParentProperties ()
    {
        Set<String> done = new HashSet<String>(propertySets.size());

        Iterator<String> names = propertySets.keySet().iterator();

        while (names.hasNext()) {
            String name = names.next();

            // check to see if the next name hasn't already been processed
            if (!done.contains(name)) {
                List<String> ancestors = getAncestry(name);

                // process ancestors from the
                // root of the family tree
                Collections.reverse(ancestors);

                // accumulate all of the inherited properties
                Properties allMerged = createProperties();

                for (int i = 0; i < ancestors.size(); i++) {

                    String ancestorName = ancestors.get(i);
                    Properties props = getProperties(ancestorName);

                    if (props == null) {
                        Logger.error("The properties set [" + ancestorName + "] inherited by ["
                            + name + "] does not exist.");
                    } else {
                        // overwrite any inherited properties
                        // with the current properties
                        allMerged.putAll(props);

                        // update the props with
                        // any inherited values
                        props.putAll(allMerged);

                        // this step really isn't necessary,
                        // since the propertySets map already
                        // refers to props, but just to be sure:
                        propertySets.put(ancestorName, props);

                    }

                    // mark the properties so that
                    // they don't get processed again
                    done.add(ancestorName);

                }

            }

        }

    }

    /**
     * Gets the list of available property set names.
     */
    public String[] getNames ()
    {
        Set<String> keys = propertySets.keySet();
        String[] names = new String[keys.size()];
        return keys.toArray(names);
    }

    /**
     * This checks to see whether the named property set exists.
     */
    public boolean exists (String propertySetName)
    {
        return propertySets.containsKey(propertySetName);
    }

    /**
     * Gets the Properties object with the given name.
     *
     * @param propertySetName
     *            the name of the Properties to return.
     * @return the Properties object with the given name or null if no properties exist with that
     *         name.
     */
    public Properties getProperties (String propertySetName)
    {
        return propertySets.get(propertySetName);
    }

    /**
     * This merges the named property sets into a single Properties object. The later property Sets
     * in the <source>names</source> list will override duplicate property values in earlier
     * property sets.
     *
     * @param names
     *            String[] a list of names of the property Sets to merge.
     * @return Properties
     */
    public Properties mergeProperties (String[] names)
    {

        Properties merged = createProperties();

        for (int i = 0; i < names.length; i++) {
            Properties next = getProperties(names[i]);

            if (next == null) {
                Logger.debug("WARNING: The referenced properties set [" + names[i]
                    + "] does not exist and could " + "not be merged.");
            } else {
                merged.putAll(next);
            }

        }

        return merged;

    }

    /**
     * This is a convenience method for getting a particular property out of a particular property
     * group.
     *
     * @param propertySet
     *            the name of the property group.
     * @param propertyName
     *            the name of the property to get from that group.
     * @return the property value, or null if either the property set or named property do not
     *         exist.
     */
    public String getProperty (String propertySet, String propertyName)
    {
        String result = null;

        Properties properties = getProperties(propertySet);

        if (properties != null) {
            result = properties.getProperty(propertyName);
        }

        return result;
    }

    /**
     * This gets the name of the parent properties for the given property set name.
     *
     * @param propertySet
     * @return the parent's name, or null if the set has no parents.
     */
    public String getParentName (String propertySet)
    {
        return parentTable.get(propertySet);
    }

    /**
     * Gets the list of the names of the property sets that the named group extends from.
     *
     * @param name
     *            the name of a property set.
     * @return a list containing the property set name and the names of its ancestors.
     */
    protected List<String> getAncestry (String name)
    {
        List<String> ancestors = new ArrayList<String>();
        do {
            ancestors.add(name);
            name = parentTable.get(name);
        } while (name != null);

        return ancestors;

    }

    /**
     * This method gets a list of indexed properties with a given prefix.
     *
     * <p>
     * For example, the following property values could be retrieved by passing "foo" as the prefix
     * to getPropertyList():
     *
     * <pre>
     *  foo0=bar0
     *  foo1=bar1
     *  foo2=bar2
     *  foo3=bar3
     * </pre>
     *
     * In the case of this example, the returned list would contain the values:
     * ["bar0,"bar1","bar2","bar3"]
     *
     * @param propertySet
     *            the name of the property set from which to get the property values.
     * @param prefix
     *            the property name prefix to look for.
     * @return a list of String values.
     */
    public List<String> getPropertyList (String propertySet, String propertyPrefix)
    {
        Properties props = getProperties(propertySet);

        if (props == null) {
            // return an empty list
            return new ArrayList<String>();
        }

        return IndexedProperties.getPropertyList(props, propertyPrefix);
    }

    /**
     * This lists all of the Properties object available in this instance.
     */
    @Override
    public String toString ()
    {
        String[] names = getNames();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (int i = 0; i < names.length; i++) {
            Properties next = getProperties(names[i]);
            try {
                next.store(out, names[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new String(out.toByteArray());

    }

    /**
     * This iterates through all of the Properties in this instance and calls store() on them,
     * writing them out to the given output stream as they would be listed in a java .properties
     * file.
     */
    public void store (OutputStream out) throws IOException
    {
        String[] names = getNames();

        for (int i = 0; i < names.length; i++) {
            Properties next = getProperties(names[i]);
            next.store(out, names[i]);
        }

    }

    protected XMLPropertiesParser createParser ()
    {
        return new XMLPropertiesParser();
    }

    /**
     * This callout allows the child classes to use an alternate implementation of the Properties
     * class.
     */
    protected Properties createProperties ()
    {
        return new Properties();
    }

    /**
     * For testing purposes. Reads an XML file and lists the properties found.
     */
    public static void main (String[] args)
    {
        try {
            System.out.println(new XMLProperties(args));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}