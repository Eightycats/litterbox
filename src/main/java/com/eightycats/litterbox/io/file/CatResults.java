package com.eightycats.litterbox.io.file;

import java.io.File;

/**
 * Base class for readers that return a collection of results at the end.
 */
public abstract class CatResults extends Cat
{
    @Override
    protected void done (File file)
    {
        dump();
        super.done(file);
    }

    /**
     * Print all of the results to the output Writer.
     */
    public void dump ()
    {
        for (String line : getResults()) {
            println(line);
        }
    }

    public abstract Iterable<String> getResults ();
}
