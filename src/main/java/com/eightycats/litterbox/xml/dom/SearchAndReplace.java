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

import java.io.FileNotFoundException;
import java.io.IOException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.eightycats.litterbox.io.file.WholeFile;
import com.eightycats.litterbox.xml.XMLException;

/**
 * Search and replace within the content of an XML document.
 */
public class SearchAndReplace
{

    public static void updateFile (String fileName, String pattern, String replacement)
        throws FileNotFoundException, IOException, XMLException
    {
        String xml = WholeFile.readFile(fileName);
        xml = replace(xml, pattern, replacement);
        WholeFile.writeFile(fileName, xml);
    }

    public static String replace (String xml, String pattern, String replacement)
        throws XMLException
    {
        DocumentAccessor parser = new DocumentAccessor(xml);

        Document doc = parser.getDocument();
        Element root = doc.getDocumentElement();

        replace(root, pattern, replacement);

        return parser.generate();

    }

    public static void replace (Node node, String pattern, String replacement)
    {
        // update attribute values
        NamedNodeMap attributes = node.getAttributes();

        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Attr attr = (Attr) attributes.item(i);
                String value = attr.getValue();

                value = value.replaceAll(pattern, replacement);

                attr.setValue(value);
            }
        }

        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child instanceof Text) {
                // update text nodes
                String text = child.getNodeValue();
                text = text.replaceAll(pattern, replacement);
                child.setNodeValue(text);
            }

            // recurse
            replace(child, pattern, replacement);
        }
    }

    public static void main (String[] args)
    {
        String file = args[0];
        String pattern = args[1];
        String replacement = args[2];

        try {
            updateFile(file, pattern, replacement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
