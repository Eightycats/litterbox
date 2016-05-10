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

package com.eightycats.litterbox.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Reads an OrderedProperties instance from an input stream.
 */
public class OrderedPropertyReader
{
    /**
     * Reads properties and comments from a stream and adds them to the given OrderedProperties
     * instance.
     *
     * @param properties
     *            OrderedProperties any properties and comments found will be added to this output
     *            param.
     * @param input
     *            InputStream the stream from which the input will be read.
     * @throws IOException
     *             thrown on any IO errors.
     */
    public static void read (OrderedProperties properties, InputStream input)
        throws IOException
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
                key = StringEscapeUtils.unescapeJava(key);
                value = StringEscapeUtils.unescapeJava(value);

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

    /**
     * Utility method for getting the end a property name from a line of input.
     */
    protected static int getKeyEndIndex (String line, int keyStartIndex)
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
     * Gets the start a property value from a line of input.
     */
    protected static int getValueStartIndex (String line, int keyEndIndex)
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
