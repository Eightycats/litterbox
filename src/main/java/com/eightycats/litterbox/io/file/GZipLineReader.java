package com.eightycats.litterbox.io.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

/**
 * Reads a gzipped text file and processes it line by line.
 */
public abstract class GZipLineReader extends LineReader
{
    @Override
    protected BufferedReader createReader (File file, String encoding)
        throws IOException
    {
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(
            file)), encoding));
    }
}
