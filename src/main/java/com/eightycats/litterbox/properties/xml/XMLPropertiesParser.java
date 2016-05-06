package com.eightycats.litterbox.properties.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eightycats.litterbox.logging.Logger;
import com.eightycats.litterbox.xml.sax.*;

public class XMLPropertiesParser extends XMLHandlerBase
{
    /**
     * The name of the root XML element.
     */
    public static final String ROOT_TAG = "config";

    public static final String PROPERTIES_TAG = "properties";

    public static final String ENTRY_TAG = "entry";

    public static final String COMMENT_TAG = "comment";

    public static final String KEY_ATTR = "key";

    public static final String NAME_ATTR = "name";

    public static final String EXTENDS_ATTR = "extends";

    /**
     * Contains the collection of named Properties objects that have been parsed so far.
     */
    protected Map<String, Properties> _properties = new HashMap<String, Properties>();

    /**
     * Maps Properties' names to their parent Properties' names.
     */
    protected Map<String, String> _parentNames = new HashMap<String, String>();

    /**
     * The current Properties object that is being parsed.
     */
    protected Properties _currentProperties = null;

    /**
     * This is the name of the current property entry being parsed.
     */
    protected String _currentPropertyName = null;

    /**
     * Sometimes, the property value is passed to this parser in chunks. This field is used to
     * reassemble these chunks into a single string.
     */
    protected String _currentValue = null;

    /**
     * This parses the given XML properties file, accumulating the Properties objects as it goes.
     * The results of any calls to this method can be retrieved by calling getPropertiesSets() and
     * getParentNames().
     */
    public void parse (String xmlFilePath) throws Exception
    {
        FileReader file = new FileReader(xmlFilePath);

        try {
            BufferedReader reader = new BufferedReader(file);
            parse(reader);
        } finally {
            file.close();
        }
    }

    public void parse (Reader xmlInput) throws Exception
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        InputSource xmlSource = new InputSource(xmlInput);

        // parse the input stream and build up the app info objects in the
        // app info parser instance
        parser.parse(xmlSource, this);
    }

    /**
     * Clears out any properties that have been parsed so far.
     *
     */
    public void reset ()
    {
        _properties.clear();
        _parentNames.clear();
    }

    /**
     * This gets the named Properties objects that have been parsed by this parser so far.
     */
    public Map<String, Properties> getPropertiesSets ()
    {
        return _properties;
    }

    /**
     * This returns a map of the properties set names to the names of their parent properties sets
     * (if they have a parent).
     */
    public Map<String, String> getParentNames ()
    {
        return _parentNames;
    }

    /**
     * This is called by the XML parser when an XML element (tag) is opened.
     */
    @Override
    public void startElement (String uri, String localName, String tagName, Attributes attributes)
        throws SAXException
    {
        super.startElement(uri, localName, tagName, attributes);

        if (tagName.equals(PROPERTIES_TAG)) {
            String name = attributes.getValue(NAME_ATTR);

            if (name == null) {
                throw new SAXException("[" + PROPERTIES_TAG + "] does not have a [" + NAME_ATTR
                    + "] attribute.");
            }

            // try to warn the user if a name
            // is already in use
            if (_properties.containsKey(name)) {
                Logger.log("WARNING: The properties set [" + name + "] is overriden by later "
                    + "properties with the same name.");
            }

            _currentProperties = createProperties();
            _properties.put(name, _currentProperties);

            // get name of the parent properties
            String parent = attributes.getValue(EXTENDS_ATTR);

            if (parent != null) {
                _parentNames.put(name, parent);
            }

        } else if (tagName.equals(ENTRY_TAG)) {
            if (_currentProperties == null) {
                throw new SAXException("Found tag [" + ENTRY_TAG + "] outside of a ["
                    + PROPERTIES_TAG + "]");
            }

            _currentPropertyName = attributes.getValue(KEY_ATTR);

            if (_currentProperties != null && _currentProperties.containsKey(_currentPropertyName)) {
                Logger.log("WARNING: The property named [" + _currentPropertyName
                    + "] is overriden by a later " + "property with the same name.");
            }

            // reset the property value to null
            _currentValue = null;

            if (_currentPropertyName == null) {
                throw new SAXException("Property entry tag [" + tagName + "] does not have a ["
                    + KEY_ATTR + "] attribute.");
            }

        } else if (tagName.equals(COMMENT_TAG)) {
            // do nothing for now
        } else if (tagName.equals(ROOT_TAG)) {
            // do nothing
        } else {
            throw new SAXException("Encountered unexpected tag [" + tagName + "]");
        }
    }

    /**
     * This is called by the XML parser when an XML element (tag) is closed.
     */
    @Override
    public void endElement (String uri, String localName, String tagName) throws SAXException
    {
        if (tagName.equals(PROPERTIES_TAG)) {
            _currentProperties = null;
        }

        super.endElement(uri, localName, tagName);
    }

    /**
     * This is called by the XML parser when text is encountered within an XML element (tag).
     */
    @Override
    protected void textField (String tagName, String text)
    {
        if (tagName.equals(ENTRY_TAG)) {
            // set the property value to be the given text
            if (_currentProperties != null) {
                // if there is already an existing property
                // value, then append this text to it
                _currentValue = (_currentValue == null) ? text : _currentValue + text;

                _currentProperties.setProperty(_currentPropertyName, _currentValue);
            }
        }
    }

    /**
     * This callout allows the child classes (e.g. the OrderedXMLPropertiesParser) to use an
     * alternate implementation of the Properties class.
     */
    protected Properties createProperties ()
    {
        return new Properties();
    }

}
