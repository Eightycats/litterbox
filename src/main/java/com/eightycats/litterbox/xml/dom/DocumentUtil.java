package com.eightycats.litterbox.xml.dom;

import com.eightycats.litterbox.io.file.WholeFile;

/**
 * Edits that value of attributes in an XML file.
 */
public class DocumentUtil
{
    private static final String USAGE = "Usage: java " + DocumentUtil.class.getName()
        + " <xml file> <xml path> " + "[-attribute <attribute name>] " + "[-value <new value>] "
        + "[-prettyPrint]";

    public static void main (String[] args)
    {
        String filePath = null;
        String xmlPath = null;
        String attrName = null;
        String newValue = null;
        boolean prettyPrint = false;

        for (int i = 0; i < args.length; i++) {

            if (args[i].equals("-attribute")) {

                i++;
                if (i < args.length) {
                    attrName = args[i];
                } else {
                    die("No attribute name was specified.");
                }

            } else if (args[i].equals("-value")) {

                i++;
                if (i < args.length) {
                    newValue = args[i];
                } else {
                    die("No new value was specified.");
                }

            } else if (args[i].equals("-prettyPrint")) {
                prettyPrint = true;
            } else if (args[i].startsWith("-")) {
                die();
            } else if (filePath == null) {
                filePath = args[i];
            } else if (xmlPath == null) {
                xmlPath = args[i];
            }

        }

        if (filePath == null) {
            die("No file path was specified.");
        }

        if (xmlPath == null) {
            die("No XML path was specified.");
        }

        try {

            String xml = WholeFile.readFile(args[0]);
            DocumentAccessor parser = new DocumentAccessor(xml);

            // The pretty print option formats the XML nicely, but
            // does not preserve the original file spacing
            if (prettyPrint) {
                parser.setPreservingWhitespace(false);
                parser.getDocument().normalize();
            }

            boolean exists = parser.exists(xmlPath);
            String existsString = exists ? " exists." : " does not exist.";

            System.out.println("Path " + xmlPath + existsString);

            if (attrName != null) {

                if (exists) {
                    System.out.println("Attribute [" + attrName + "]: "
                        + parser.getAttribute(xmlPath, attrName));
                }

                if (newValue != null) {
                    parser.setAttribute(xmlPath, attrName, newValue);
                    System.out.println("Set attribute [" + attrName + "] to [" + newValue + "].");
                }

            } else {

                if (exists) {
                    System.out.println("Text: " + parser.getText(xmlPath));
                }

                if (newValue != null) {
                    parser.setText(xmlPath, newValue);
                    System.out.println("Set text at path [" + xmlPath + "] to [" + newValue + "].");
                }

            }

            if (newValue != null) {
                WholeFile.writeFile(filePath, parser.generate());
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
        System.err.println(USAGE);
        System.err.println(message);
        System.exit(1);
    }
}
