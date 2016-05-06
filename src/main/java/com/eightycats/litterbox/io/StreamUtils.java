package com.eightycats.litterbox.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.eightycats.litterbox.logging.*;
import java.io.Writer;

public class StreamUtils
{
    protected static final int BUFFER_SIZE = 1024;

    /**
     * Flush and close an output stream quietly. Any exceptions thrown by the close are ignored.
     */
    public static void close (OutputStream output)
    {
        try {
            output.flush();
        } catch (IOException ex) {
            Logger.logStackTrace(ex);
        }

        try {
            output.close();
        } catch (IOException ex) {
            Logger.logStackTrace(ex);
        }
    }

    public static void close (InputStream input)
    {
        try {
            input.close();
        } catch (IOException ex) {
            Logger.logStackTrace(ex);
        }
    }

    /**
     * Flush and close a Writer quietly. Any exceptions thrown by the close are ignored.
     */
    public static void close (Writer output)
    {
        try {
            output.flush();
        } catch (IOException ex) {
            Logger.logStackTrace(ex);
        }

        try {
            output.close();
        } catch (IOException ex) {
            Logger.logStackTrace(ex);
        }
    }

    public static long copy (InputStream input, OutputStream output) throws IOException
    {
        long totalBytes = 0;

        byte[] buffer = new byte[BUFFER_SIZE];
        int byteCount = input.read(buffer);

        while (byteCount > -1) {
            output.write(buffer, 0, byteCount);
            totalBytes += byteCount;
            byteCount = input.read(buffer);
        }

        return totalBytes;
    }

}
