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
