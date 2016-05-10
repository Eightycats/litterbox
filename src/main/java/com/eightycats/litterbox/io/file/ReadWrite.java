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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.eightycats.litterbox.io.StreamUtils;

/**
 * Support for reading a file line by line, processing each line, and writing the results to an
 * output file. If no output file is set, this will try to write the results to a temp file, and
 * then overwrite the original file with the results.
 */
public abstract class ReadWrite extends Cat
{
    /**
     * The output file path.
     */
    protected String _outputPath;

    /**
     * Output encoding.
     */
    protected String _outputEncoding;

    /**
     * Saves off the path to the temp file, if we had to use one.
     */
    protected String _tempPath;

    /**
     * No output file specified. Will try to modify the input file in place.
     */
    public ReadWrite ()
    {
        this(null, null);
    }

    /**
     * Prints to the give output file.
     */
    public ReadWrite (String outputPath)
    {
        this(outputPath, null);
    }

    /**
     * Prints to the given output file using the given character encoding.
     */
    public ReadWrite (String outputPath, String encoding)
    {
        _outputPath = outputPath;
        setOutputEncoding(encoding);
    }

    @Override
    public void read (String filePath, String inputEncoding)
    {
        try {
            String outputPath;
            // if not output file was specified, use a temp file
            if (_outputPath == null) {
                outputPath = _tempPath = File.createTempFile("littlebox", ".txt").getCanonicalPath();
            } else {
                outputPath = _outputPath;
            }
            String outputEncoding = _outputEncoding != null ? _outputEncoding :
                getEncoding(inputEncoding);
            read(filePath, inputEncoding, new OutputStreamWriter(new FileOutputStream(outputPath),
                outputEncoding));

        } catch (UnsupportedEncodingException unex) {
            unex.printStackTrace();
        } catch (IOException fex) {
            fex.printStackTrace();
        }
    }

    protected void read (String filePath, String inputEncoding, Writer output)
    {
        setWriter(output);
        // read the input file, process each line
        super.read(filePath, inputEncoding);
    }

    @Override
    protected void done (File file)
    {
        super.done(file);
        StreamUtils.close(_out);

        if (_tempPath != null) {
            // copy temp file over original
            try {
                FileUtils.copy(_tempPath, file.getCanonicalPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                // delete the temp file
                FileUtils.delete(_tempPath);
                _tempPath = null;
            }
        }
    }

    /**
     * Optionally sets the character encoding for the output file. If not set, the same encoding
     * as the input file will be used.
     */
    public void setOutputEncoding (String encoding)
    {
        _outputEncoding = encoding;
    }
}
