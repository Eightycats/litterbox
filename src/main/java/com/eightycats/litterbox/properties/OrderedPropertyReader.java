package com.eightycats.litterbox.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Reads an OrderedProperties instance from an input stream.
 *
 * Most of the functionality here duplicates code from java.util.Properties, because
 * we want this class's behavior to mimic the way the Properties class loads and parses properties.
 * Some functions in the Properties class only had private access, so here we are...
 */
public class OrderedPropertyReader
{
    /**
     * Reads properties and comments from a stream and adds them to the given OrderedProperties
     * instance.
     *
     *
     * @param properties
     *            OrderedProperties any properties and comments found will be added to this output
     *            param.
     * @param input
     *            InputStream the stream from which the input will be read.
     * @throws IOException
     *             thrown on any IO errors.
     */
    public static void read (OrderedProperties properties, InputStream input) throws IOException
    {
        InputStreamReader inputReader = new InputStreamReader(input,
            PropertyConstants.DEFAULT_CHARSET);
        BufferedReader buffer = new BufferedReader(inputReader);

        String line = buffer.readLine();

        while (line != null) {

            String trimmed = line.trim();

            if (trimmed.length() == 0 || trimmed.startsWith("#") || trimmed.startsWith("!")) {

                // This line is a comment or is entirely whitespace.
                // Create a placeholder to preserve the location of comments and
                // whitespace in the original source
                // (for example, a property file).
                properties.addComment(line);

            } else {

                // skip any leading whitespace
                int keyStart = skipWhitespace(line);

                // while the line ends in a backslash,
                // append the following line
                line = getWrappedLine(buffer, line);

                int length = line.length();

                // Find the ending of the key string
                int keyStop = getKeyEndIndex(line, keyStart);

                // Find the start of the value string
                int valueStart = getValueStartIndex(line, keyStop);

                String key = line.substring(keyStart, keyStop);
                String value = (keyStop < length) ? line.substring(valueStart, length) : "";

                // Convert any escape sequences (uuencoded chars, '\n', etc.)
                // and then store new key and value
                key = convert(key);
                value = convert(value);

                properties.put(key, value);

            }

            line = buffer.readLine();
        }
    }

    /**
     * This utility method returns the offset of the first non-whitespace character in the given
     * string.
     *
     * @param s
     *            String a line of input.
     * @return int the index of the first non-whitespace char in the given string.
     */
    public static int skipWhitespace (String s)
    {
        return skipWhitespace(s, 0);
    }

    /**
     * This utility method returns the offset of the first non-whitespace character in the given
     * string.
     *
     * @param s
     *            String a line of input.
     * @param startIndex
     *            the index from which to start skipping whitespace.
     * @return int the index of the first non-whitespace char after the gievn index in the given
     *         string.
     */
    public static int skipWhitespace (String s, int startIndex)
    {
        int length = s.length();

        // increment the start index until a non-whitespace char
        // is found or until we run out of string
        for (; startIndex < length; startIndex++) {

            char nextChar = s.charAt(startIndex);
            if (!Character.isWhitespace(nextChar)) {
                break;
            }

        }

        return startIndex;
    }

    /**
     * If the end of the given line is escaped by a backslash, then this appends any following
     * line(s) and returns the value as one long line.
     *
     * @param buffer
     *            BufferedReader the source for getting further lines.
     * @param line
     *            String the current line.
     * @return String the given line plus any continued lines following it.
     */
    public static String getWrappedLine (BufferedReader buffer, String line) throws IOException
    {
        while (lineContinues(line)) {

            String nextLine = buffer.readLine();

            if (nextLine == null) {
                nextLine = "";
            }

            // chop off the \ at the end of the line
            String loppedLine = line.substring(0, line.length() - 1);

            // Skip leading whitespace on the new line
            int startIndex = skipWhitespace(loppedLine);
            nextLine = nextLine.substring(startIndex, nextLine.length());

            line = new String(loppedLine + nextLine);
        }
        return line;
    }

    /**
     * Returns true if the given line ends with a backslash, indicating that the line separator
     * should be ignored.
     */
    public static boolean lineContinues (String line)
    {
        int slashCount = 0;
        int index = line.length() - 1;

        while ((index >= 0) && (line.charAt(index--) == '\\')) {
            slashCount++;
        }

        return (slashCount % 2 == 1);
    }

    /*
     * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars to their
     * original forms.
     *
     * This is taken from java.util.Properties.
     */
    public static String convert (String theString)
    {

        char aChar;
        int length = theString.length();
        StringBuffer outBuffer = new StringBuffer(length);

        for (int x = 0; x < length;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * Utility method for getting the end a property name from a line of input.
     *
     * @param line
     *            String
     * @param keyStartIndex
     *            int
     * @return int
     */
    private static int getKeyEndIndex (String line, int keyStartIndex)
    {

        // the key string ends where the key-value separator begins
        int separatorIndex = keyStartIndex;
        int length = line.length();

        for (; separatorIndex < length; separatorIndex++) {

            char currentChar = line.charAt(separatorIndex);

            // skip escaped characters
            if (currentChar == '\\') {
                separatorIndex++;
            } else if (PropertyConstants.KEY_VALUE_SEPARATORS.indexOf(currentChar) != -1) {
                break;
            }

        }

        return separatorIndex;

    }

    /**
     * Utility method for getting the start a property value from a line of input.
     *
     * @param line
     *            String
     * @param keyEndIndex
     *            int
     * @return int
     */
    private static int getValueStartIndex (String line, int keyEndIndex)
    {

        // Skip over whitespace after key if any
        int valueStart = skipWhitespace(line, keyEndIndex);
        int length = line.length();

        // Skip over the key value separator character (e.g. '=') if any
        if (valueStart < length) {

            char nextChar = line.charAt(valueStart);

            if (PropertyConstants.STRICT_SEPARATORS.indexOf(nextChar) != -1) {
                valueStart++;
            }

        }

        return valueStart;

    }

}
