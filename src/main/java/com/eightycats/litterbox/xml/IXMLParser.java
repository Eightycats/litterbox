package com.eightycats.litterbox.xml;

import org.w3c.dom.Node;

/**
 * Interface for accessing values in parsed XML.
 */
interface IXMLParser
{
    boolean exists (String path);

    Node getNode (String path);

    Node[] getChildren (String path);

    String getText (String path);

    void setText (String path, String value);

    String getAttribute (String path, String attributeName);

    String setAttribute (String path, String attributeName);
}
