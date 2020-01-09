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

package com.eightycats.litterbox.util;

import java.util.HashSet;
import java.util.Set;

public class StringUtils
{
    /**
     * Words that you don't normally bother capitalizing. Must be in sorted order.
     */
    public static final Set<String> PREPOSITIONS_ETC = new HashSet();

    static {
        PREPOSITIONS_ETC.add("a");
        PREPOSITIONS_ETC.add("an");
        PREPOSITIONS_ETC.add("and");
        PREPOSITIONS_ETC.add("at");
        PREPOSITIONS_ETC.add("but");
        PREPOSITIONS_ETC.add("by");
        PREPOSITIONS_ETC.add("for");
        PREPOSITIONS_ETC.add("in");
        PREPOSITIONS_ETC.add("of");
        PREPOSITIONS_ETC.add("on");
        PREPOSITIONS_ETC.add("or");
        PREPOSITIONS_ETC.add("the");
        PREPOSITIONS_ETC.add("to");
        PREPOSITIONS_ETC.add("with");
    }

    public static int indexOfIgnoreCase (String string, String substring)
    {
        // normalize the strings
        string = string.toLowerCase();
        substring = substring.toLowerCase();

        return string.indexOf(substring);
    }

    /**
     * Capitalizes the first letter of each word in the given string. Ignores prepositions and
     * articles, unless they are the first word in the string.
     */
    public static String toTitleCase (String title)
    {
        String result = "";
        String[] parts = title.split(" ");
        for (String part : parts) {
            if (result.length() == 0) {
                result += toSentenceCase(part, true);
            } else {
                result += " ";
                String word = part.toLowerCase();
                if (shouldTitleCase(word)) {
                    word = toSentenceCase(word);
                }
                result += word;
            }
        }
        return result;
    }

    /**
     * Capitalizes the first letter of the given string.
     */
    public static String toSentenceCase (String word)
    {
        return toSentenceCase(word, false);
    }

    /**
     * Capitalizes the first letter of the given string.
     */
    public static String toSentenceCase (String title, boolean force)
    {
        String result = "";
        if (title != null && !title.isEmpty()) {
            if (force || result.isEmpty()) {
                result += Character.toUpperCase(title.charAt(0));
                if (title.length() > 1) {
                    result += title.substring(1);
                }
            } else {
                result = title;
            }
        }
        return result;
    }

    /**
     * Checks to see if the given word is one you would normally capitalize in a title.
     */
    public static boolean shouldTitleCase (String word)
    {
        return !PREPOSITIONS_ETC.contains(word);
    }

    /**
     * Iterates over the list, converts each item to a string, and returns concatenated string.
     */
    public static String join(Iterable<?> list, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (Object item : list) {
            if (result.length() > 0) {
                result.append(delimiter);
            }
            result.append(item);
        }
        return result.toString();
    }
}
