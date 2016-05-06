package com.eightycats.litterbox.xml.dom;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 *
 */
public class DocumentBuilderConfig
{

    private boolean validating = false;

    private boolean namespaceAware = false;

    private boolean whitespace = false;

    private boolean expandEntityRef = true;

    private boolean ignoreComments = false;

    private boolean coalescing = true;

    public DocumentBuilderConfig ()
    {
    }

    public DocumentBuilderConfig (boolean validating, boolean namespaceAware,
        boolean ignoreWhitespace, boolean expandEntityRef, boolean ignoreComments,
        boolean coalescing)
    {

        this.validating = validating;
        this.namespaceAware = namespaceAware;
        this.whitespace = ignoreWhitespace;
        this.expandEntityRef = expandEntityRef;
        this.ignoreComments = ignoreComments;
        this.coalescing = coalescing;

    }

    public void configureFactory (DocumentBuilderFactory factory)
    {

        factory.setValidating(validating);
        factory.setNamespaceAware(namespaceAware);
        factory.setIgnoringElementContentWhitespace(whitespace);
        factory.setExpandEntityReferences(expandEntityRef);
        factory.setIgnoringComments(ignoreComments);
        factory.setCoalescing(coalescing);

    }

    /**
     * Specifies that the parser produced by this code will provide support for XML namespaces. By
     * default the value of this is set to <code>false</code>
     *
     * @param awareness
     *            true if the parser produced will provide support for XML namespaces; false
     *            otherwise.
     */

    public void setNamespaceAware (boolean awareness)
    {
        this.namespaceAware = awareness;
    }

    /**
     * Specifies that the parser produced by this code will validate documents as they are parsed.
     * By default the value of this is set to <code>false</code>.
     *
     * @param validating
     *            true if the parser produced will validate documents as they are parsed; false
     *            otherwise.
     */

    public void setValidating (boolean validating)
    {
        this.validating = validating;
    }

    /**
     * Specifies that the parsers created by this factory must eliminate whitespace in element
     * content (sometimes known loosely as 'ignorable whitespace') when parsing XML documents (see
     * XML Rec 2.10). Note that only whitespace which is directly contained within element content
     * that has an element only content model (see XML Rec 3.2.1) will be eliminated. Due to
     * reliance on the content model this setting requires the parser to be in validating mode. By
     * default the value of this is set to <code>false</code>.
     *
     * @param whitespace
     *            true if the parser created must eliminate whitespace in the element content when
     *            parsing XML documents; false otherwise.
     */
    public void setIgnoringElementContentWhitespace (boolean whitespace)
    {
        this.whitespace = whitespace;
    }

    /**
     * Specifies that the parser produced by this code will expand entity reference nodes. By
     * default the value of this is set to <code>true</code>
     *
     * @param expandEntityRef
     *            true if the parser produced will expand entity reference nodes; false otherwise.
     */

    public void setExpandEntityReferences (boolean expandEntityRef)
    {
        this.expandEntityRef = expandEntityRef;
    }

    /**
     * Specifies that the parser produced by this code will ignore comments. By default the value of
     * this is set to <code>false
     * </code>
     */
    public void setIgnoringComments (boolean ignoreComments)
    {
        this.ignoreComments = ignoreComments;
    }

    /**
     * Specifies that the parser produced by this code will convert CDATA nodes to Text nodes and
     * append it to the adjacent (if any) text node. By default the value of this is set to
     * <code>false</code>
     *
     * @param coalescing
     *            true if the parser produced will convert CDATA nodes to Text nodes and append it
     *            to the adjacent (if any) text node; false otherwise.
     */
    public void setCoalescing (boolean coalescing)
    {
        this.coalescing = coalescing;
    }

    /**
     * Indicates whether or not the factory is configured to produce parsers which are namespace
     * aware.
     *
     * @return true if the factory is configured to produce parsers which are namespace aware; false
     *         otherwise.
     */

    public boolean isNamespaceAware ()
    {
        return namespaceAware;
    }

    /**
     * Indicates whether or not the factory is configured to produce parsers which validate the XML
     * content during parse.
     *
     * @return true if the factory is configured to produce parsers which validate the XML content
     *         during parse; false otherwise.
     */

    public boolean isValidating ()
    {
        return validating;
    }

    /**
     * Indicates whether or not the factory is configured to produce parsers which ignore ignorable
     * whitespace in element content.
     *
     * @return true if the factory is configured to produce parsers which ignore ignorable
     *         whitespace in element content; false otherwise.
     */

    public boolean isIgnoringElementContentWhitespace ()
    {
        return whitespace;
    }

    /**
     * Indicates whether or not the factory is configured to produce parsers which expand entity
     * reference nodes.
     *
     * @return true if the factory is configured to produce parsers which expand entity reference
     *         nodes; false otherwise.
     */

    public boolean isExpandEntityReferences ()
    {
        return expandEntityRef;
    }

    /**
     * Indicates whether or not the factory is configured to produce parsers which ignores comments.
     *
     * @return true if the factory is configured to produce parsers which ignores comments; false
     *         otherwise.
     */

    public boolean isIgnoringComments ()
    {
        return ignoreComments;
    }

    /**
     * Indicates whether or not the factory is configured to produce parsers which converts CDATA
     * nodes to Text nodes and appends it to the adjacent (if any) Text node.
     *
     * @return true if the factory is configured to produce parsers which converts CDATA nodes to
     *         Text nodes and appends it to the adjacent (if any) Text node; false otherwise.
     */

    public boolean isCoalescing ()
    {
        return coalescing;
    }

    @Override
    public boolean equals (Object o)
    {

        boolean result = false;

        if (o instanceof DocumentBuilderConfig) {

            DocumentBuilderConfig that = (DocumentBuilderConfig) o;
            result = (this.validating == that.isValidating())
                && (this.namespaceAware == that.isNamespaceAware())
                && (this.whitespace == that.isIgnoringElementContentWhitespace())
                && (this.expandEntityRef == that.isExpandEntityReferences())
                && (this.ignoreComments == that.isIgnoringComments())
                && (this.coalescing == that.isCoalescing());

        }

        return result;

    }

}
