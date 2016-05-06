package com.eightycats.litterbox.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Path for getting at a Node that allows for creating the Node if it does not already exist.
 */
public interface IConstructivePath extends IPath
{
    Node getNode (Document doc, boolean create);
}
