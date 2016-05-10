/**
 * Copyright 2016 Matthew A Jensen <eightycats@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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

    /**
     * If true, also print output to System.out (in addition to the current PrintWriter).
     */
    protected boolean _verbose;

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
        if (_verbose) {
            System.out.print(text);
        }
    }

    /**
     * Print some text out to the current writer.
     */
    protected void println (String line)
    {
        _out.println(line);
        if (_verbose) {
            System.out.println(line);
        }
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

    /**
     * If true, output will also get printed to System.out.
     */
    public void setVerbose (boolean verbose)
    {
        _verbose = verbose;
    }

    public static void main (String[] args)
    {
        new Cat().read(args[0]);
    }
}
