package com.eightycats.litterbox.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Reads a file in with one character encoding and writes it out in place.
 */
public class ChangeEncoding extends ReadWrite
{
    public ChangeEncoding ()
        throws IOException
    {
        this(StandardCharsets.UTF_8.name());
    }

    public ChangeEncoding (String outputEncoding)
        throws IOException
    {
        super(File.createTempFile("changeencoding", "tmp").getCanonicalPath(), outputEncoding);
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

    @Override
    protected void done (File file)
    {
        super.done(file);

        // copy temp file over original
        try {
            FileUtils.copy(_outputPath, file.getCanonicalPath());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // delete the temp file
            FileUtils.delete(_outputPath);
        }
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
