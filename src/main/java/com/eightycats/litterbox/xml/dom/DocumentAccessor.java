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

package com.eightycats.litterbox.xml.dom;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.eightycats.litterbox.xml.BadPathException;
import com.eightycats.litterbox.xml.IConstructivePath;
import com.eightycats.litterbox.xml.IPath;
import com.eightycats.litterbox.xml.XMLException;

/**
 * Parses an XML document into and DOM and provides methods for getting at its juicy contents.
 */
public class DocumentAccessor extends DocumentParser
{
    public DocumentAccessor (String xml)
        throws XMLException
    {
        super(xml);
    }

    public DocumentAccessor (String xml, DocumentBuilderConfig config)
        throws XMLException
    {
        super(xml, config);
    }

    public DocumentAccessor (Document doc)
    {
        super(doc);
    }

    public DocumentAccessor (File xmlFile)
        throws XMLException
    {
        this(parse(xmlFile));
    }

    public DocumentAccessor (File xmlFile, DocumentBuilderConfig config)
        throws XMLException
    {
        this(parse(xmlFile, config));
    }

    public Node getNode (IPath path)
    {
        return path.getNode(_document);
    }

    public Node getNode (String path)
        throws BadPathException
    {
        return getNode(path, false);
    }

    public Node getNode (String path, boolean create)
        throws BadPathException
    {
        return getNode(new SimplePath(path), create);
    }

    public Node getNode (IConstructivePath path, boolean create)
    {
        return path.getNode(getDocument(), create);
    }

    public Node[] getNodes (IPath path)
    {
        return path.getNodes(getDocument());
    }

    public boolean exists (String path)
        throws BadPathException
    {
        return getNode(new SimplePath(path)) != null;
    }

    public boolean exists (IPath path)
    {
        return getNode(path) != null;
    }

    public String getText (String path)
        throws BadPathException
    {
        String text = null;
        Node node = getFirstTextNode(path);
        if (node != null) {
            text = node.getNodeValue();
        }
        return text;
    }

    public String getText (IPath path)
    {
        String text = null;
        Node node = getNode(path);
        Node textNode = getFirstTextNode(node, false);
        if (textNode != null) {
            text = textNode.getNodeValue();
        }
        return text;
    }

    public Node getFirstTextNode (String path)
        throws BadPathException
    {
        return getFirstTextNode(path, false);
    }

    public Node getFirstTextNode (String path, boolean create)
        throws BadPathException
    {
        Node node = getNode(path, create);
        return getFirstTextNode(node, create);
    }

    public static Node getFirstTextNode (Node node, boolean create)
    {
        Node result = null;
        if (node != null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength() && result == null; i++) {
                Node child = children.item(i);
                if (child instanceof Text) {
                    result = child;
                }
            }

            if (result == null && create) {
                result = node.getOwnerDocument().createTextNode("\n");
                node.appendChild(result);
            }
        }
        return result;
    }

    public void setText (String path, String text)
        throws BadPathException
    {
        Node node = getFirstTextNode(path, true);
        if (node != null) {
            node.setNodeValue(text);
        }
    }

    public static void setText (Node node, String text)
    {
        Node textNode = getFirstTextNode(node, true);
        if (textNode != null) {
            textNode.setNodeValue(text);
        }
    }

    public boolean attributeExists (String path, String attributeName)
        throws BadPathException
    {
        return (getAttributeNode(path, attributeName) != null);
    }

    public Node getAttributeNode (String path, String attributeName)
        throws BadPathException
    {
        return getAttributeNode(path, attributeName, false);
    }

    public Node getAttributeNode (String path, String attributeName, boolean create)
        throws BadPathException
    {
        return getAttributeNode(getNode(path, create), attributeName, create);
    }

    public Node getAttributeNode (Node parent, String attributeName, boolean create)
    {
        Node result = null;
        if (parent != null) {
            NamedNodeMap attributes = parent.getAttributes();
            result = attributes.getNamedItem(attributeName);
            if (result == null && create) {
                // create and add a new attribute with the given name
                result = _document.createAttribute(attributeName);
                attributes.setNamedItem(result);
            }
        }
        return result;

    }

    public String getAttribute (String path, String attributeName)
        throws BadPathException
    {
        return getAttribute(getNode(path), attributeName);
    }

    public static String getAttribute (Node node, String attributeName)
    {
        String attrValue = null;
        if (node != null && node.hasAttributes()) {
            NamedNodeMap attributes = node.getAttributes();
            Node attribute = attributes.getNamedItem(attributeName);
            if (attribute != null) {
                attrValue = attribute.getNodeValue();
            }
        }
        return attrValue;
    }

    public void setAttribute (String path, String attributeName, String value)
        throws BadPathException
    {
        Node attribute = getAttributeNode(path, attributeName, true);
        if (attribute != null) {
            attribute.setNodeValue(value);
        }
    }

    public void setAttribute (Node parent, String attributeName, String value)
    {
        Node attribute = getAttributeNode(parent, attributeName, true);
        if (attribute != null) {
            attribute.setNodeValue(value);
        }
    }

    public Node addChild (Node parentNode, String childName)
    {
        Element newChild = _document.createElement(childName);
        parentNode.appendChild(newChild);
        return newChild;
    }

    public void addChild (String parentPath, Node child) throws BadPathException
    {
        addChild(new SimplePath(parentPath), child);
    }

    public void addChild (IConstructivePath parentPath, Node child)
    {
        Node parentNode = getNode(parentPath, true);

        // this prevents an error if the child does not already belong to this document
        child = importNode(child);

        parentNode.appendChild(child);
    }

    public void insertChildAt (String parentPath, String childName, int childIndex)
        throws BadPathException
    {
        Element newChild = _document.createElement(childName);
        insertChildAt(parentPath, newChild, childIndex);
    }

    public void insertChildAt (String parentPath, Node child, int childIndex)
        throws BadPathException
    {
        SimplePath path = new SimplePath(parentPath);
        insertChildAt(path, child, childIndex, true);

    }

    public void insertChildAt (String parentPath, Node child, int childIndex,
        boolean matchChildName)
        throws BadPathException
    {
        insertChildAt(new SimplePath(parentPath), child, childIndex, matchChildName);
    }

    public void insertChildAt (IConstructivePath parentPath, Node child, int childIndex,
        boolean matchChildName)
    {
        // prevents error if the child does not already belong to this document
        child = importNode(child);

        Node parentNode = getNode(parentPath, true);
        String name = child.getNodeName();

        NodeList children = parentNode.getChildNodes();
        int childCount = children.getLength();

        int matchCount = -1;

        for (int i = 0; i < childCount; i++) {
            Node existing = children.item(i);
            if (existing instanceof Element) {
                if (matchChildName) {
                    if (existing.getNodeName().equals(name)) {
                        matchCount++;
                    }
                } else {
                    matchCount++;
                }

                if (matchCount == childIndex) {
                    parentNode.insertBefore(child, existing);
                    return;
                }
            }
        }

        while (matchCount < childIndex - 1) {
            // create placeholder child node
            parentNode.appendChild(_document.createTextNode("\n"));
            parentNode.appendChild(_document.createElement(name));
        }

        // append the child node
        parentNode.appendChild(_document.createTextNode("\n"));
        parentNode.appendChild(child);
    }

    protected Node importNode (Node node)
    {
        if (node.getOwnerDocument() != getDocument()) {
            // this clones the node, setting our document as the owner doc of the clone
            node = _document.importNode(node, true);
        }
        return node;
    }

    public static int getChildElementCount (Node node)
    {
        int count = 0;
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                count++;
            }
        }
        return count;
    }

    public static Text getIndent (Node parentNode)
    {
        Text result = null;
        Text lastWhitespace = null;
        Node current = parentNode.getLastChild();

        boolean betweenElements = false;

        while (current != null) {
            if (current instanceof Text) {
                String text = current.getNodeValue();
                if (text != null && text.trim().length() == 0) {
                    result = (Text) current;
                    if (lastWhitespace == null) {
                        lastWhitespace = result;
                    }
                }

                if (betweenElements) {
                    return result;
                }

            } else if (current instanceof Element) {
                betweenElements = true;
            }

            current = current.getPreviousSibling();
        }

        if (result == null) {
            String indent = "";
            int depth = getDepth(parentNode);
            for (int i = 0; i < depth; i++) {
                indent += "   ";
            }
            result = parentNode.getOwnerDocument().createTextNode("\n" + indent);
        }

        return result;
    }

    protected static int getDepth (Node node)
    {
        int depth = 0;
        while (node != null) {
            node = node.getParentNode();
            depth++;
        }
        return depth;
    }
}
