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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Reads a file in with one character encoding and writes it out in place.
 */
public class ChangeEncoding extends ReadWrite
{
    public ChangeEncoding ()
        throws IOException
    {
        this(null);
    }

    public ChangeEncoding (String outputEncoding)
        throws IOException
    {
        setOutputEncoding(outputEncoding);
    }

    public static void read (String inputFile, String inputEncoding, String outputEncoding)
        throws UnsupportedEncodingException, IOException
    {
        new ChangeEncoding(outputEncoding).read(inputFile, inputEncoding);
    }

    @Override
    protected void process (String line)
    {
        println(line);
    }

    public static void main (String[] args)
    {
        try {
            read(args[0], args[1], args[2]);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
