package com.eightycats.litterbox.properties;

import java.io.Serializable;

/**
 * This represents a comment found in a .properties file. This is used to preserve (or add/edit)
 * comments a file.
 */
public class Comment implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * The comment value.
     */
    protected String _comment;

    public Comment (String comment)
    {
        setValue(comment);
    }

    /**
     * This is used to modify the value of the comment.
     *
     * @param value
     *            String the new comment string.
     */
    public void setValue (String value)
    {
        // check that first char is a comment char
        if (value.length() > 0) {

            String trimmed = value.trim();

            // if the line was all whitespace, then it does not need an
            // escape character: #
            if (trimmed.length() > 0) {
                char firstChar = trimmed.charAt(0);
                if (PropertyConstants.COMMENT_CHARS.indexOf(firstChar) == -1) {
                    // if the comment does not yet start with a comment char, then prepend a #.
                    value = PropertyConstants.COMMENT_CHAR + value;
                }
            }
        }
        _comment = value;
    }

    /**
     * Gets the current comment string.
     */
    public String getValue ()
    {
        return _comment;
    }

    @Override
    public String toString ()
    {
        return getValue();
    }
}
