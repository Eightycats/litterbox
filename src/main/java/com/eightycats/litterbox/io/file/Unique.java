package com.eightycats.litterbox.io.file;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Removes duplicate lines from a file. Note that this trims the lines.
 */
public class Unique extends LineReader
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

    public Set<String> getResults ()
    {
        return _existing;
    }

    public void dump ()
    {
        Set<String> lines = getResults();
        for (String line : lines) {
            System.out.println(line);
        }
    }

    public static void main (String[] args) throws IOException
    {
        Unique unique = new Unique();
        unique.read(args[0]);
        unique.dump();
    }
}
