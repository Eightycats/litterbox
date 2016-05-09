package com.eightycats.litterbox.io.file;

import java.io.File;
import java.util.LinkedHashSet;

/**
 * Removes duplicate lines from a file. Note that this trims the lines.
 */
public class Unique extends CatResults
{
    protected LinkedHashSet<String> _existing = new LinkedHashSet<String>();

    @Override
    protected void start (File file)
    {
        _existing.clear();
    }

    @Override
    protected void process (String line)
    {
        _existing.add(line.trim());
    }

    @Override
    public Iterable<String> getResults ()
    {
        return _existing;
    }

    public static void main (String[] args)
    {
        Unique unique = new Unique();
        unique.setVerbose(true);
        // remove duplicates in place
        CatWriter.process(args[0], unique);
    }
}
