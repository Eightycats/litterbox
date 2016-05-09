package com.eightycats.litterbox.io.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.eightycats.litterbox.io.StreamUtils;

/**
 * Support for reading a file line by line, processing each line, and writing the results to an
 * output file. If no output file is set, this will try to write the results to a temp file, and
 * then overwrite the original file with the results.
 */
public abstract class ReadWrite extends Cat
{
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
                outputPath = _tempPath = File.createTempFile("littlebox", "tmp").getCanonicalPath();
            } else {
                outputPath = _outputPath;
            }
            String outputEncoding = _outputEncoding != null ? _outputEncoding : inputEncoding;
            setWriter(new OutputStreamWriter(new FileOutputStream(outputPath),
                outputEncoding));
            // read the input file, process each line
            super.read(filePath, inputEncoding);

        } catch (UnsupportedEncodingException unex) {
            unex.printStackTrace();
        } catch (IOException fex) {
            fex.printStackTrace();
        }
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
}
