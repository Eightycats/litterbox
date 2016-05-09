package com.eightycats.litterbox.io.file;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Reads a file in with one character encoding and writes it out in place.
 */
public class ChangeEncoding extends ReadWrite
{
    public ChangeEncoding ()
        throws IOException
    {
        this(null);
    }

    public ChangeEncoding (String outputEncoding)
        throws IOException
    {
        setOutputEncoding(outputEncoding);
    }

    public static void read (String inputFile, String inputEncoding, String outputEncoding)
        throws UnsupportedEncodingException, IOException
    {
        new ChangeEncoding(outputEncoding).read(inputFile, inputEncoding);
    }

    @Override
    protected void process (String line)
    {
        println(line);
    }

    public static void main (String[] args)
    {
        try {
            read(args[0], args[1], args[2]);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
