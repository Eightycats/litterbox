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
import java.io.Writer;

/**
 * Wraps up a Cat implementation giving it file writing abilities.
 */
public class CatWriter extends ReadWrite
{
    /**
     * The delegate reader whose functionality we want to borrow.
     */
    protected Cat _reader;

    public CatWriter (Cat reader)
    {
        this(reader, null, null);
    }

    public CatWriter (Cat reader, String outputPath)
    {
        this(reader, outputPath, null);
    }

    public CatWriter (Cat reader, String outputPath, String encoding)
    {
        super(outputPath, encoding);
        _reader = reader;
    }

    @Override
    protected void read (String filePath, String inputEncoding, Writer output)
    {
        setWriter(output);
        _reader.setWriter(output);

        // read the input file, process each line
        File inputFile = new File(filePath);
        start(inputFile);
        _reader.read(filePath, inputEncoding);
        // close the output file, etc.
        done(inputFile);
    }

    /**
     * Process the specified file in place. Use the give reader to process it.
     */
    public static void process (String filePath, Cat reader)
    {
        process(filePath, null, reader);
    }

    /**
     * Process the specified file in place. Use the give reader to process it.
     */
    public static void process (String filePath, String inputEncoding, Cat reader)
    {
        CatWriter processor = new CatWriter(reader);
        processor.read(filePath, inputEncoding);
    }
}
