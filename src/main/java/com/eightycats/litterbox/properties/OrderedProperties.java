package com.eightycats.litterbox.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is a Properties class that preserves the order of properties as they were read and any
 * comments as loaded from an input stream/file.
 */
public class OrderedProperties extends Properties
    implements Serializable
{
    /**
     * Expected by Serializable interface.
     */
    private static final long serialVersionUID = 1L;

    /**
     * This list keeps track of the order of properties and comments in this collection. This is the
     * order in which they will be written out by the store() method.
     */
    protected List<Object> _orderedElements;

    /**
     * Constructor.
     */
    public OrderedProperties ()
    {
        _orderedElements = new ArrayList<Object>();
    }

    /**
     * This overrides the put() method so that the order in which properties are added is preserved.
     *
     * @param key
     *            Object a property name.
     * @param value
     *            Object a property value.
     * @return Object the previous value of the property if it already exists.
     */
    @Override
    public synchronized Object put (Object key, Object value)
    {
        Object oldValue = null;

        // If the given value is already a Property instance, then
        // get the value out of this property.
        if (value instanceof Property) {
            value = ((Property) value).getValue();
        }

        // check to see if this property already exists
        Property property = getPropertyInstance(key);

        if (property == null) {

            property = new Property(key.toString(), value);
            _orderedElements.add(property);
            super.put(key, property);

        } else {
            oldValue = property.getValue();

            // set the new value on the existing property
            property.setValue(value);
        }

        return oldValue;
    }

    /**
     * Puts the given key and value at the given index in the ordered properties. The ordering of
     * existing properties will be changed so that they will be at the new index. An
     * IndexOutOfBoundsException will be thrown if the index is less than 0 or greater than the
     * result of getElementCount().
     *
     * @param key
     *            Object a property name.
     * @param value
     *            Object a property value.
     * @param index
     *            int the index at which the property should be inserted.
     * @return Object the previous value of the property if it already exists.
     */
    public synchronized Object put (Object key, Object value, int index)
    {
        Object oldValue = null;

        // If the given value is already a Property instance, then
        // get the value out of this property.
        if (value instanceof Property) {
            value = ((Property) value).getValue();
        }

        // check to see if this property already exists
        Property property = getPropertyInstance(key);

        if (property == null) {
            property = new Property(key.toString(), value);
            _orderedElements.add(index, property);
            super.put(key, property);

        } else {
            oldValue = property.getValue();
            // set the new value on the existing property
            property.setValue(value);

            // move the existing property to the given index
            _orderedElements.remove(property);
            _orderedElements.add(index, property);
        }
        return oldValue;
    }

    /**
     * Overrides the get() method from Hashtable so that the property value is returned instead of
     * the internal representation of that property.
     *
     * @param key
     *            Object the name of the property to be found.
     * @return Object the value of the property or null if it does not exist.
     */
    @Override
    public synchronized Object get (Object key)
    {
        Object result = null;
        Property property = getPropertyInstance(key);
        if (property != null) {
            result = property.getValue();
        }
        return result;
    }

    /**
     * Searches for the property with the specified key in this property list. If the key is not
     * found in this property list, the default property list, and its defaults, recursively, are
     * then checked. The method returns <code>null</code> if the property is not found.
     *
     * @param key String the property key.
     * @return the value in this property list with the specified key value.
     */
    @Override
    public String getProperty (String key)
    {
        String result = (String) get(key);
        result = ((result == null) && (super.defaults != null)) ? defaults.getProperty(key)
            : result;
        return result;
    }

    /**
     * Gets the element (Property or Comment) at the given index.
     *
     * @param index
     *            int the index relative to the ordering of the elements in this collection.
     * @return Object the Property or Comment instance found at the given index.
     * @throws IndexOutOfBoundsException
     *             if the given index is out of range.
     */
    public synchronized Object get (int index)
    {
        return _orderedElements.get(index);
    }

    /**
     * Gets the Property object for the given property name.
     *
     * @param key
     *            Object the name of the property to be retrieved.
     * @return Property the Property with the given name or null if not found.
     */
    public synchronized Property getPropertyInstance (Object key)
    {
        Property result = null;
        Object value = super.get(key);
        if (value instanceof Property) {
            result = (Property) value;
        }
        return result;
    }

    /**
     * This removes the property with the given name.
     *
     * @param key
     *            Object the name of the property to be removed.
     * @return Object the value of the property that was removed.
     */
    @Override
    public synchronized Object remove (Object key)
    {
        Object oldValue = null;
        Property property = getPropertyInstance(key);
        if (property != null) {

            oldValue = property.getValue();

            super.remove(key);
            _orderedElements.remove(property);

        }
        return oldValue;
    }

    /**
     * Clears this collection so that it contains no properties or comments.
     */
    @Override
    public synchronized void clear ()
    {
        super.clear();
        _orderedElements.clear();
    }

    /**
     * Adds a comment at the end of the ordered collection.
     *
     * @param comment
     *            String the value of the comment.
     * @return Comment the Comment object created to wrap the comment string.
     */
    public Comment addComment (String comment)
    {
        Comment newComment = new Comment(comment);
        _orderedElements.add(newComment);
        return newComment;
    }

    /**
     * Adds a comment at a particular location in the ordered collection.
     *
     * @param index
     *            the location at which the comment should be inserted.
     * @param comment
     *            String the value of the comment.
     * @return Comment the Comment object created to wrap the comment string.
     */
    public Comment addComment (int index, String comment)
    {
        Comment newComment = new Comment(comment);
        _orderedElements.add(index, newComment);
        return newComment;
    }

    /**
     * Reads the properties (and comments) from the given input stream.
     *
     * @param input
     *            InputStream the stream to read from.
     * @throws IOException
     *             if an error occurs while reading from the stream.
     */
    @Override
    public synchronized void load (InputStream input) throws IOException
    {
        clear();
        OrderedPropertyReader.read(this, input);
    }

    /**
     * Writes the properties and comments in their current order to the given stream.
     *
     * @param stream
     *            OutputStream the stream.
     * @param header
     *            String an optional header.
     * @throws IOException
     *             if an error occurs while writing to the stream.
     */
    @Override
    public synchronized void store (OutputStream stream, String header) throws IOException
    {
        OrderedPropertyWriter.write(this, stream, header);
    }

    /**
     * This gets the number of property and comment elements contained in this collection. This is
     * different from size() which only returns the number of properties.
     *
     * @return int the total number of Property and Comment objects.
     */
    public int getElementCount ()
    {
        return _orderedElements.size();
    }

    @Override
    public synchronized void putAll (Map<?, ?> table)
    {
        if (table instanceof OrderedProperties) {
            OrderedProperties addMe = (OrderedProperties) table;

            int count = addMe.getElementCount();

            for (int i = 0; i < count; i++) {
                Object next = addMe.get(i);

                if (next instanceof Comment) {
                    Comment comment = (Comment) next;
                    addComment(comment.getValue());
                } else if (next instanceof Property) {
                    Property prop = (Property) next;
                    put(prop.getKey(), prop.getValue());
                }
            }
        } else {
            super.putAll(table);
        }
    }

    @Override
    public String toString ()
    {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < _orderedElements.size(); i++) {
            buffer.append(_orderedElements.get(i));
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
