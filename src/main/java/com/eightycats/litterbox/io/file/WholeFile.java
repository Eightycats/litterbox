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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * This is a utility class for reading or writing the entire contents of a file. Useful for
 * non-enormous files.
 */
public abstract class WholeFile
{
    /**
     * The system-specific end of line.
     */
    public static final String NEWLINE = System.getProperty("line.separator");

    /**
     * Reads the entire contents of a file into a String.
     */
    public static String readFile (String path)
        throws FileNotFoundException, IOException
    {
        return readFile(new File(path));
    }

    /**
     * Reads the entire contents of a file into a String.
     */
    public static String readFile (File file)
        throws FileNotFoundException, IOException
    {
        long size = file.length();
        StringWriter result = new StringWriter((int) size);
        Cat reader = new Cat();
        reader.setWriter(result);
        reader.read(file.getCanonicalPath());
        return result.toString();
    }

    public static void writeFile (String fileName, String content)
        throws IOException
    {
        writeFile(new File(fileName), content);
    }

    public static void writeFile (File file, String content) throws IOException
    {
        writeFile(file, content, false);
    }

    public static void writeFile (File file, String content, boolean append)
        throws IOException
    {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, append);
            writer.write(content);
            writer.flush();
        } finally {
            writer.close();
        }
    }
}
