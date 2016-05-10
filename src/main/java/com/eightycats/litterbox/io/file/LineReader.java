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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Base class for reading a text file line by line.
 */
public abstract class LineReader
    implements FileReader
{
    @Override
    public void read (String filePath)
    {
        read(filePath, null);
    }

    @Override
    public void read (String filePath, String encoding)
    {
        File file = new File(filePath);
        start(file);
        try {
            BufferedReader in = createReader(file, getEncoding(encoding));
            try {
                String line = in.readLine();
                while (line != null) {
                    process(line);
                    line = in.readLine();
                }
            } finally {
                in.close();
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        done(file);
    }

    protected BufferedReader createReader (File file, String encoding)
        throws IOException, FileNotFoundException
    {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
    }

    /**
     * Callout before we start reading the file.
     */
    protected void start (File file)
    {
    }

    /**
     * Callout once we are done processing the file.
     */
    protected void done (File file)
    {
    }

    /**
     * Called line by line to do something with the file content.
     */
    protected abstract void process (String line);

    /**
     * If the given encoding is null, this returns UTF-8 by default.
     */
    protected String getEncoding (String encoding)
    {
        return encoding == null ? StandardCharsets.UTF_8.name() : encoding;
    }
}
