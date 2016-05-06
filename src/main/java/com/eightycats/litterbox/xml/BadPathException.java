package com.eightycats.litterbox.xml;

/**
 * Thrown if an XML path could not be parsed.
 */
public class BadPathException extends XMLException
{
    private static final long serialVersionUID = 1L;

    public BadPathException (String message)
    {
        super(message);
    }

    public BadPathException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public BadPathException (Throwable cause)
    {
        super(cause);
    }
}
