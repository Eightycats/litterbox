package com.eightycats.litterbox.io.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reads a file, sorts it, and spits out the result.
 */
public class Sort extends LineReader
{
    protected List<String> _results = new ArrayList<String>();

    @Override
    protected void start (File file)
    {
        _results.clear();
    }

    @Override
    protected void process (String line)
    {
        _results.add(line);
    }

    @Override
    protected void done (File file)
    {
        Collections.sort(_results);
        super.done(file);
    }

    public List<String> getResults ()
    {
        return _results;
    }

    public void dump ()
    {
        List<String> lines = getResults();
        for (String line : lines) {
            System.out.println(line);
        }
    }

    public static void main (String[] args) throws IOException
    {
        Sort sorter = new Sort();
        sorter.read(args[0]);
        sorter.dump();
    }
}
