package com.eightycats.litterbox.io.file;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Reads the lines of a file into a Set.
 */
public class SetReader extends LineReader
{
    protected Set<String> _lines = new HashSet<String>();

    public void setSorted (boolean sorted)
    {
        if (sorted) {
            _lines = new TreeSet<String>(_lines);
        } else {
            _lines = new HashSet<String>(_lines);
        }
    }

    @Override
    protected void start (File file)
    {
        _lines.clear();
    }

    @Override
    protected void process (String line)
    {
        _lines.add(line);
    }

    public Set<String> getResults ()
    {
        return _lines;
    }

    public static void main (String[] args)
    {
        SetReader lines = new SetReader();
        lines.setSorted(true);
        lines.read(args[0]);
        for (String line : lines.getResults()) {
            System.out.println(line);
        }
    }
}
