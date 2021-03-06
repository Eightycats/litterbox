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

package com.eightycats.litterbox.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A XML "handler" for visiting tags in SAX XML parsing.
 */
public abstract class XMLHandlerBase extends DefaultHandler
{
    /**
     * The name of the current element in the XML. For example, if we are inside of a <foo> tag,
     * then the currentTag would be "foo".
     */
    protected String _currentTag = null;

    /**
     * Receive notification of the start of an element.
     */
    @Override
    public void startElement (String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        _currentTag = qName;
    }

    /**
     * Receive notification of the end of an element.
     */
    @Override
    public void endElement (String uri, String localName, String qName) throws SAXException
    {
        _currentTag = null;
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException
    {
        if (_currentTag != null) {
            String s = new String(ch, start, length);
            textField(_currentTag, s);
        }
    }

    protected String getCurrentTag ()
    {
        return _currentTag;
    }

    protected abstract void textField (String tagName, String text);

    /**
     * Check if an attribute with the given name exists.
     */
    public static boolean attributeExists (String name, Attributes attributes)
    {
        return attributes.getValue(name) != null;
    }

    /**
     * Get an attribute value. Throw an exception if it is missing.
     */
    public static String getRequiredAttr (String name, Attributes attributes)
        throws SAXException
    {
        String value = null;
        if (attributeExists(name, attributes)) {
            value = attributes.getValue(name);
        } else {
            throw new SAXException("Required attribute [" + name + "] does not exist.");
        }
        return value;
    }
}
