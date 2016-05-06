package com.eightycats.litterbox.io.file;

import java.util.StringTokenizer;

/**
 * Base class for reading a text file and parsing each line into tokens.
 */
public abstract class Splitter extends LineReader
{
    protected String _delimiters;

    public Splitter ()
    {
        this(",|\t ");
    }

    public Splitter (String delimiters)
    {
        setDelimiters(delimiters);
    }

    @Override
    protected void process (String line)
    {
        process(new StringTokenizer(line, getDelimiters()));
    }

    protected abstract void process (StringTokenizer tokens);

    protected String getDelimiters ()
    {
        return _delimiters;
    }

    protected void setDelimiters (String delimiters)
    {
        _delimiters = delimiters;
    }
}
