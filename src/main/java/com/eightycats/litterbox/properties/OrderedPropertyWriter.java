package com.eightycats.litterbox.properties;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class OrderedPropertyWriter
{
    public static void write (OrderedProperties properties, OutputStream stream, String header)
        throws IOException
    {
        OutputStreamWriter writer = new OutputStreamWriter(stream,
            PropertyConstants.DEFAULT_CHARSET);
        BufferedWriter buffer = new BufferedWriter(writer);
        PrintWriter out = new PrintWriter(buffer);

        if (header != null) {
            out.print(PropertyConstants.COMMENT_CHAR);
            out.println(header);
        }

        int count = properties.getElementCount();

        for (int i = 0; i < count; i++) {
            Object element = properties.get(i);
            if (element instanceof Property) {
                Property property = (Property) element;
                String key = property.getKey();
                String value = property.getValue().toString();

                key = convert(key);
                value = convertValue(value);
                out.println(key + PropertyConstants.KEY_VALUE_SEPARATOR + value);

            } else if (element instanceof Comment) {

                // write out any comments
                String comments = element.toString();

                // escape any unicode chars
                comments = convertUnicode(comments);

                out.println(comments);
            }
        }
        buffer.flush();
    }

    /*
     * Converts unicodes to encoded &#92;uxxxx and writes out any of the characters in
     * specialSaveChars with a preceding slash
     */
    private static String convert (String string)
    {
        return convert(string, true, PropertyConstants.SPECIAL_SAVE_CHARS);
    }

    private static String convertValue (String string)
    {
        return convert(string, false, PropertyConstants.SPECIAL_VALUE_CHARS);
    }

    private static String convert (String string, boolean escapeSpace, String specialChars)
    {

        int len = string.length();
        StringBuffer outBuffer = new StringBuffer(len * 2);

        for (int x = 0; x < len; x++) {
            char aChar = string.charAt(x);

            switch (aChar) {
            case ' ':
                if (escapeSpace) {
                    outBuffer.append('\\');
                }
                outBuffer.append(' ');
                break;

            case '\\':
                outBuffer.append('\\');
                outBuffer.append('\\');
                break;
            case '\t':
                outBuffer.append('\\');
                outBuffer.append('t');
                break;
            case '\n':
                outBuffer.append('\\');
                outBuffer.append('n');
                break;
            case '\r':
                outBuffer.append('\\');
                outBuffer.append('r');
                break;
            case '\f':
                outBuffer.append('\\');
                outBuffer.append('f');
                break;
            default:
                convertUnicode(aChar, outBuffer, specialChars);
            }
        }
        return outBuffer.toString();
    }

    private static String convertUnicode (String string)
    {
        int len = string.length();
        StringBuffer outBuffer = new StringBuffer(len * 2);
        for (int x = 0; x < len; x++) {
            char aChar = string.charAt(x);
            convertUnicode(aChar, outBuffer, "");
        }
        return outBuffer.toString();
    }

    /**
     * Converts unicode chars to encoded &#92;uxxxx
     *
     * @param aChar
     *            char
     * @param outBuffer
     *            StringBuffer
     */
    private static void convertUnicode (char aChar, StringBuffer outBuffer, String specialChars)
    {
        if ((aChar < 0x0020) || (aChar > 0x007e)) {
            outBuffer.append('\\');
            outBuffer.append('u');
            outBuffer.append(toHex(aChar >> 12));
            outBuffer.append(toHex(aChar >> 8));
            outBuffer.append(toHex(aChar >> 4));
            outBuffer.append(toHex(aChar));

        } else {
            if (specialChars.indexOf(aChar) != -1) {
                outBuffer.append('\\');
            }

            outBuffer.append(aChar);
        }
    }

    /**
     * Convert a nibble to a hex character
     *
     * @param nibble
     *            the nibble to convert.
     */
    private static char toHex (int nibble)
    {
        nibble &= 0xF;
        String result = Integer.toHexString(nibble);
        return result.charAt(0);
    }

}
