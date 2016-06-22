package com.eightycats.litterbox.io.file;

import java.util.Set;
import java.util.TreeSet;

/**
 * Reads lines from a file into a sorted set.
 */
public class SortedSetReader extends SetReader
{
    @Override
    protected Set<String> createSet ()
    {
        return new TreeSet<String>();
    }

    public static void main (String[] args)
    {
        SortedSetReader lines = new SortedSetReader();
        lines.read(args[0]);
        lines.dump();
    }
}
