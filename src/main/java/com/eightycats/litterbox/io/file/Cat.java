package com.eightycats.litterbox.io.file;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Prints the contents of a file to an output writer.
 */
public class Cat extends LineReader
{
    /**
     * Where any output from print() and println() will get sent.
     */
    protected PrintWriter _out;

    public Cat ()
    {
        // by default, send output, if any, to stdout
        _out = new PrintWriter(new OutputStreamWriter(System.out));
    }

    public void setWriter (Writer output)
    {
        _out = new PrintWriter(output);
    }

    /**
     * Print some text out to the current writer.
     */
    protected void print (String text)
    {
        _out.print(text);
    }

    /**
     * Print some text out to the current writer.
     */
    protected void println (String line)
    {
        _out.println(line);
    }

    @Override
    protected void process (String line)
    {
        println(line);
    }

    @Override
    protected void done (File file)
    {
        _out.flush();
    }

    public static void main (String[] args)
    {
        new Cat().read(args[0]);
    }
}
