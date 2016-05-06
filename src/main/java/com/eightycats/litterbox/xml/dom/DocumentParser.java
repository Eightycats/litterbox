package com.eightycats.litterbox.xml.dom;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.eightycats.litterbox.xml.XMLException;

/**
 * XML Document parser.
 */
public class DocumentParser
{
    public static final String XML_SPACE_ATTRIBUTE = "xml:space";

    public static final String PRESERVE_SPACE = "preserve";

    protected Document _document;

    protected boolean _preservingWhitespace = true;

    public DocumentParser (String xml) throws XMLException
    {
        this(parse(xml));
    }

    public DocumentParser (String xml, DocumentBuilderConfig config)
        throws XMLException
    {
        this(parse(xml, config));
    }

    public DocumentParser (File xmlFile) throws XMLException
    {
        this(parse(xmlFile));
    }

    public DocumentParser (File xmlFile, DocumentBuilderConfig config) throws XMLException
    {
        this(parse(xmlFile, config));
    }

    public DocumentParser (Document doc)
    {
        setDocument(doc);
    }

    public void setDocument (Document doc)
    {
        _document = doc;
    }

    public Document getDocument ()
    {
        return _document;
    }

    /**
     * This creates a new, empty DocumentParser with the given name for the document's root element.
     */
    public static DocumentParser createParser (String rootNodeName)
        throws XMLException
    {
        return new DocumentParser(createDocument(rootNodeName));
    }

    public static Document parse (String xml)
        throws XMLException
    {
        return parse(xml, new DocumentBuilderConfig());
    }

    public static Document parse (File xmlFile)
        throws XMLException
    {
        return parse(xmlFile, new DocumentBuilderConfig());
    }

    public static Document parse (String xml, DocumentBuilderConfig config)
        throws XMLException
    {
        StringReader reader = new StringReader(xml);
        InputSource input = new InputSource(reader);
        return parse(input, config);
    }

    public static Document parse (InputSource xml, DocumentBuilderConfig config)
        throws XMLException
    {
        try {
            DocumentBuilder parser = ThreadLocalBuilderFactory.getDocumentBuilder(config);
            return parser.parse(xml);
        } catch (Exception ex) {
            throw new XMLException(ex);
        }
    }

    public static Document parse (File xmlFile, DocumentBuilderConfig config)
        throws XMLException
    {
        try {
            DocumentBuilder parser = ThreadLocalBuilderFactory.getDocumentBuilder(config);
            return parser.parse(xmlFile);

        } catch (Exception ex) {
            throw new XMLException(ex);
        }
    }

    public String generate ()
    {
        StringWriter output = new StringWriter();
        generate(output);
        return output.toString();
    }

    public boolean generate (Writer output)
    {
        return generate(output, null);
    }

    public boolean generate (Writer output, String encoding)
    {
        boolean success = true;
        PrintWriter writer = new PrintWriter(output);
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(output);
            DOMSource source = new DOMSource(_document);
            transformer.transform(source, result);

        } catch (TransformerException e) {
            success = false;
            e.printStackTrace();
        } finally {
            writer.flush();
        }
        return success && writer.checkError();
    }

    public void setPreservingWhitespace (boolean preserve)
    {
        _preservingWhitespace = preserve;
    }

    protected void preserveWhitespace (Element root)
    {
        if (_preservingWhitespace) {
            Node whitespace = root.getAttributeNode(XML_SPACE_ATTRIBUTE);
            if (whitespace == null) {
                Attr preserveWhitespace = root.getOwnerDocument().createAttribute(
                    XML_SPACE_ATTRIBUTE);
                preserveWhitespace.setNodeValue(PRESERVE_SPACE);
                root.setAttributeNode(preserveWhitespace);
            }
        }
    }

    public static Document createDocument (String rootElementName)
        throws XMLException
    {
        try {
            Document doc = ThreadLocalBuilderFactory.getDocumentBuilder().newDocument();
            Element rootElement = doc.createElement(rootElementName);
            doc.appendChild(rootElement);
            return doc;

        } catch (Exception ex) {
            throw new XMLException(ex);
        }
    }
}
