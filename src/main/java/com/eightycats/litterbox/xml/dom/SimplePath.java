package com.eightycats.litterbox.xml.dom;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.eightycats.litterbox.xml.BadPathException;
import com.eightycats.litterbox.xml.IConstructivePath;

/**
 * Describes the location of nodes within an XML document. Essentially a poor man's XPath.
 */
public class SimplePath implements IConstructivePath
{
    public static final String DELIMITER = "/";

    public static final String INDEX_START = "[";

    public static final String INDEX_STOP = "]";

    public static final String WILDCARD = "*";

    public static final int WILDCARD_INDEX = -1;

    protected String _nodeName;

    protected int _index = 1;

    protected SimplePath _parent;

    protected SimplePath _next;

    protected SimplePath (SimplePath parentPath)
    {
        setParent(parentPath);
    }

    public SimplePath (String path)
        throws BadPathException
    {
        StringTokenizer subpaths = new StringTokenizer(path, DELIMITER);
        SimplePath current = this;
        while (subpaths.hasMoreElements()) {
            String subpath = subpaths.nextToken();

            // by default, get the first matching instance of the nodeName
            int index = 1;
            int parenIndex = subpath.indexOf(INDEX_START);
            if (parenIndex != -1) {
                int closeParenIndex = subpath.indexOf(INDEX_STOP);
                if (closeParenIndex == -1) {
                    throw new BadPathException("The '" + INDEX_START + "' in subpath [" + subpath
                        + "] is not properly closed.");

                } else {
                    String indexString = subpath.substring(parenIndex + 1, closeParenIndex);

                    // check to see if the index is a wildcard [*]
                    if (indexString.equals(WILDCARD)) {
                        index = WILDCARD_INDEX;

                    } else {
                        try {
                            index = Integer.parseInt(indexString);
                        } catch (NumberFormatException nfex) {
                            throw new BadPathException("The index [" + indexString
                                + "] found in subpath [" + subpath
                                + "] is not a valid numeric value.");
                        }
                    }

                    // get the just node name without its index value
                    subpath = subpath.substring(0, parenIndex);
                }
            }

            current.setNodeName(subpath);
            current.setIndex(index);

            if (subpaths.hasMoreElements()) {
                // create next child path
                SimplePath parent = current;
                current = new SimplePath(parent);
            }
        }
    }

    public void setNodeName (String nodeName)
    {
        _nodeName = nodeName;
    }

    public String getNodeName ()
    {
        return _nodeName;
    }

    public void setIndex (int index)
    {
        _index = index;
    }

    public int getIndex ()
    {
        return _index;
    }

    protected void setParent (SimplePath parent)
    {
        _parent = parent;
        parent.setNext(this);
    }

    public SimplePath getParent ()
    {
        return _parent;
    }

    public boolean isRoot ()
    {
        return _parent == null;
    }

    public boolean hasNext ()
    {
        return _next != null;
    }

    public SimplePath getNext ()
    {
        return _next;
    }

    public void setNext (SimplePath next)
    {
        _next = next;
    }

    public boolean isWildcard ()
    {
        return WILDCARD.equals(_nodeName);
    }

    /**
     * This returns the first node in the given document that matches this path.
     */
    @Override
    public Node getNode (Document document)
    {
        return getNode(document, false);
    }

    @Override
    public Node getNode (Document document, boolean create)
    {
        Node result = null;
        Node currentNode = document.getDocumentElement();
        if (currentNode.getNodeName().equals(_nodeName) || isWildcard()) {
            result = getNode(document, currentNode, this, create);
        }
        return result;
    }

    public static Node getNode (Document document, Node parentNode, SimplePath path, boolean create)
    {
        Node result = null;
        Node currentNode = parentNode.getFirstChild();
        if (path.hasNext()) {
            int matchCount = 0;

            SimplePath subpath = path.getNext();
            int index = subpath.getIndex();
            String name = subpath.getNodeName();

            while (currentNode != null && result == null) {
                String currentName = currentNode.getNodeName();
                if (currentName.equals(name) || path.isWildcard()) {
                    matchCount++;
                }

                if (matchCount == index || index == WILDCARD_INDEX) {
                    if (subpath.hasNext()) {
                        result = getNode(document, currentNode, subpath, create);
                    } else {
                        result = currentNode;
                    }
                }
                currentNode = currentNode.getNextSibling();
            }

            if (result == null) {
                if (create) {
                    for (int i = matchCount; i < index; i++) {
                        // add a new node to the parent node
                        result = document.createElement(name);
                        parentNode.appendChild(result);

                        // add padding
                        String indent = "";
                        int depth = subpath.getDepth();
                        for (int d = 0; d < depth; d++) {
                            indent += "   ";
                        }
                        Text padding = document.createTextNode("\n" + indent);
                        parentNode.appendChild(padding);
                        parentNode.normalize();

                        // create any subnodes described by the path
                        if (subpath.hasNext()) {
                            result = getNode(document, result, subpath, create);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Node[] getNodes (Document document)
    {
        List<Node> results = new LinkedList<Node>();
        Node currentNode = document.getDocumentElement();
        if (currentNode.getNodeName().equals(_nodeName) || isWildcard()) {
            getNodes(document, currentNode, this, results);
        }
        Node[] nodes = new Node[results.size()];
        return results.toArray(nodes);
    }

    public Node[] getNodes (Document document, Node parentNode, SimplePath path)
    {
        List<Node> results = new LinkedList<Node>();
        getNodes(document, parentNode, path);

        Node[] nodes = new Node[results.size()];
        return results.toArray(nodes);
    }

    public void getNodes (Document document, Node parentNode, SimplePath path, List<Node> results)
    {
        Node currentNode = parentNode.getFirstChild();

        if (path.hasNext()) {
            int matchCount = 0;

            SimplePath subpath = path.getNext();
            int index = subpath.getIndex();
            String name = subpath.getNodeName();
            boolean wildcardIndex = (index == WILDCARD_INDEX);

            while (currentNode != null && (wildcardIndex || matchCount < index)) {
                // we are only looking for element nodes
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    String currentName = currentNode.getNodeName();
                    if (currentName.equals(name) || path.isWildcard()) {
                        matchCount++;
                        if (matchCount == index || wildcardIndex) {
                            if (subpath.hasNext()) {
                                getNodes(document, currentNode, subpath, results);
                            } else {
                                results.add(currentNode);
                            }
                        }
                    }
                }
                currentNode = currentNode.getNextSibling();
            }
        }
    }

    public int getDepth ()
    {
        int depth = 0;
        SimplePath previous = getParent();
        while (previous != null) {
            depth++;
            previous = previous.getParent();
        }
        return depth;
    }

    public SimplePath getRoot ()
    {
        SimplePath current = this;
        while (!current.isRoot()) {
            current = current.getParent();
        }
        return current;
    }

    public static String pathForNode (Node node)
    {
        String result = "";
        Node current = node;
        while (current != null) {
            if (current.getNodeType() != Node.DOCUMENT_NODE) {
                int index = getRelativeIndex(current);
                result = DELIMITER + current.getNodeName() + INDEX_START + index + INDEX_STOP
                    + result;
            }
            current = current.getParentNode();
        }
        return result;
    }

    protected static int getRelativeIndex (Node node)
    {
        int index = 1;

        // get index relative to other child nodes
        Node previous = node.getPreviousSibling();
        while (previous != null) {
            if (previous.getNodeType() == Node.ELEMENT_NODE) {
                index++;
            }
            previous = previous.getPreviousSibling();
        }
        return index;
    }

    public String getFullPath ()
    {
        SimplePath current = getRoot();
        StringBuffer result = new StringBuffer();
        while (current != null) {
            result.append(DELIMITER);
            result.append(current.toString());
            current = current.getNext();
        }
        return result.toString();
    }

    @Override
    public String toString ()
    {
        String result = getNodeName();
        if (_index > 1) {
            result = result + INDEX_START + _index + INDEX_STOP;
        } else if (_index == WILDCARD_INDEX) {
            result = result + INDEX_START + WILDCARD + INDEX_STOP;
        }
        return result;
    }

}
