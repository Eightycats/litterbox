package com.eightycats.litterbox.io.file;

import java.io.FileNotFoundException;

/**
 * Takes the output from query in the mysql client and converts it to CSV.
 */
public class QueryResultsToCSV extends ReadWrite
{
    public QueryResultsToCSV (String outputPath)
    {
        super(outputPath);
    }

    @Override
    protected void process (String line)
    {
        line = line.replaceAll("\\|", ",");
        if (line.startsWith(",")) {
            line = line.substring(1);
        }

        line = line.replaceAll("\\+-*", "");
        line = line.trim();

        if (line.length() > 0) {
            println(line);
        }
    }

    public static void processFile (String fileName)
        throws FileNotFoundException
    {
        new QueryResultsToCSV(fileName.replaceAll(".txt", ".csv")).read(fileName);
    }

    public static void main (String[] args)
    {
        try {
            processFile(args[0]);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
