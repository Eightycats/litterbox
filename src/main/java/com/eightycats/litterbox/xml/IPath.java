package com.eightycats.litterbox.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface IPath
{
    Node getNode (Document document);

    Node[] getNodes (Document document);
}
