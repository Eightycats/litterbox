package com.eightycats.litterbox.xml;

/**
 * General XML exception.
 */
public class XMLException extends Exception
{
    private static final long serialVersionUID = 1L;

    public XMLException (String message)
    {
        super(message);
    }

    public XMLException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public XMLException (Throwable cause)
    {
        super(cause);
    }
}
