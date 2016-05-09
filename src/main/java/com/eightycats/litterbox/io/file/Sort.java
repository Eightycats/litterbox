package com.eightycats.litterbox.io.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reads a file, sorts it, and spits out the result.
 */
public class Sort extends CatResults
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

    @Override
    public Iterable<String> getResults ()
    {
        return _results;
    }

    public static void main (String[] args)
    {
        Sort sorter = new Sort();
        sorter.setVerbose(true);
        // file will get sorter in place
        CatWriter.process(args[0], sorter);
    }
}
