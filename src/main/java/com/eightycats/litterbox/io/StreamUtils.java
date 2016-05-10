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
