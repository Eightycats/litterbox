package com.eightycats.litterbox.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.eightycats.litterbox.io.StreamUtils;

public abstract class ReadWrite extends Cat
{
    public ReadWrite (String outputPath)
    {
        this(outputPath, null);
    }

    public ReadWrite (String outputPath, String encoding)
    {
        _outputPath = outputPath;
        _outputEncoding = encoding;
    }

    @Override
    public void read (String filePath, String inputEncoding)
    {
        try {
            String outputEncoding = _outputEncoding != null ? _outputEncoding : inputEncoding;
            setWriter(new OutputStreamWriter(new FileOutputStream(_outputPath),
                outputEncoding));
            super.read(filePath, inputEncoding);

        } catch (UnsupportedEncodingException unex) {
            unex.printStackTrace();
        } catch (FileNotFoundException fex) {
            fex.printStackTrace();
        }
    }

    @Override
    protected void done (File file)
    {
        StreamUtils.close(_out);
    }

    protected String _outputPath;

    protected String _outputEncoding;
}
