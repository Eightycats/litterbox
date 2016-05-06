package com.eightycats.litterbox.util;

import java.util.Arrays;

public class StringUtils
{
    /**
     * Words that you don't normally bother capitalizing. Must be in sorted order.
     */
    public static final String[] PREPOSITIONS_ETC = new String[] { "a", "an", "and", "at", "but",
            "by", "for", "in", "of", "on", "or", "the", "to", "with", };

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
        StringBuilder result = new StringBuilder();
        String[] parts = title.split(" ");
        for (String part : parts) {
            if (result.length() != 0) {
                result.append(" ");
            }
            result.append(toSentenceCase(part));
        }
        return result.toString();
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
            title = title.toLowerCase();
            if (force || shouldTitleCase(title)) {
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
        return Arrays.binarySearch(PREPOSITIONS_ETC, word.toLowerCase()) < 0;
    }
}
