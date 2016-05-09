package com.eightycats.litterbox.io.file;

/**
 * Replace text on each line.
 */
public class Replace extends Cat
{
    protected String _pattern;

    protected String _replacement;

    protected boolean _all;

    public Replace (String pattern, String replacement)
    {
        this(pattern, replacement, true);
    }

    public Replace (String pattern, String replacement, boolean all)
    {
        _pattern = pattern;
        _replacement = replacement;
        _all = all;
    }

    @Override
    protected void process (String line)
    {
        if (_all) {
            line = line.replaceAll(_pattern, _replacement);
        } else {
            line = line.replace(_pattern, _pattern);
        }
        println(line);
    }

    public static void main (String[] args)
    {
        Replace replacer = new Replace(args[1], args[2]);
        replacer.setVerbose(true);
        CatWriter.process(args[0], replacer);
    }
}
