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
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eightycats.litterbox.io.file.WholeFile;

public class DocumentSandbox
{

    public static void listChildren (Node parent)
    {

        NodeList children = parent.getChildNodes();

        System.out.println(parent.getNodeName() + " has " + children.getLength() + " children.");

        for (int i = 0; i < children.getLength(); i++) {

            Node child = children.item(i);

            logNode(child);

        }

    }

    public static void listEntities (DocumentType docType)
    {

        NamedNodeMap entityMap = docType.getEntities();

        for (int i = 0; i < entityMap.getLength(); i++) {

            Entity entity = (Entity) entityMap.item(i);

            logNode(entity);

        }

    }

    public static void logNode (Node node)
    {
        System.out.println("Name   : [" + node.getNodeName() + "]");
        System.out.println("Value  : [" + node.getNodeValue() + "]");

        String type = "";

        switch (node.getNodeType()) {

        case Node.ATTRIBUTE_NODE:
            type = "attribute";
            break;

        case Node.CDATA_SECTION_NODE:
            type = "CDATA";
            break;

        case Node.COMMENT_NODE:
            type = "comment";
            break;

        case Node.DOCUMENT_NODE:
            type = "Document";
            break;

        case Node.DOCUMENT_TYPE_NODE:
            type = "Document Type";
            break;

        case Node.ELEMENT_NODE:
            type = "Element";
            break;

        case Node.ENTITY_NODE:
            type = "Entity";
            Entity entity = (Entity) node;

            System.out.println("Notation Name : [" + entity.getNotationName() + "]");
            System.out.println("Public : [" + entity.getPublicId() + "]");
            System.out.println("System : [" + entity.getSystemId() + "]");
            System.out.println("String : [" + node.toString() + "]");
            listChildren(entity);
            break;

        case Node.ENTITY_REFERENCE_NODE:
            type = "Entity Ref";
            break;

        case Node.NOTATION_NODE:
            type = "Notation";
            break;

        case Node.PROCESSING_INSTRUCTION_NODE:
            type = "Processing Instruction";
            break;

        case Node.TEXT_NODE:
            type = "Text";
            break;

        }

        System.out.println("Type   : [" + type + "]\n\n");

    }

    public static void main (String[] args)
    {
        String filePath = null;
        String outputFile = null;

        for (int i = 0; i < args.length; i++) {

            if (args[i].startsWith("-")) {
                die();
            } else if (filePath == null) {
                filePath = args[i];
            } else if (outputFile == null) {
                outputFile = args[i];
            }

        }

        if (filePath == null) {
            die("No file path was specified.");
        }

        try {

            File xmlFile = new File(args[0]);
            DocumentAccessor parser = new DocumentAccessor(xmlFile);

            Document doc = parser.getDocument();

            System.out.println("__________________________");
            System.out.println("Listing Document: ");
            listChildren(doc);

            DocumentType docType = doc.getDoctype();
            System.out.println("__________________________");
            System.out.println("Listing Document Type: ");
            listEntities(docType);

            System.out.println("__________________________");
            System.out.println("Listing Document Element: ");
            listChildren(doc.getDocumentElement());

            if (outputFile != null) {
                System.out.println("Writing to file [" + outputFile + "]");
                WholeFile.writeFile(outputFile, parser.generate());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            die(ex.getMessage());
        }

    }

    private static void die ()
    {
        die("");
    }

    private static void die (String message)
    {
        System.err.println(message);
        System.exit(1);
    }

}
