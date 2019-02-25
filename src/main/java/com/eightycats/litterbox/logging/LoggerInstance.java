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

package com.eightycats.litterbox.logging;

import com.eightycats.litterbox.format.ThreadLocalDateFormat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class LoggerInstance
    implements LoggerConstants, ILogger
{
    protected PrintWriter output;

    protected int enabledLevels = 0;

    protected boolean loggingThreads = false;

    /**
     * This indicates whether or not to add an extra line separator at the end of each log message.
     * This is meant to aid in legibility.
     */
    protected boolean extraLine = true;

    protected static ThreadLocalDateFormat dateFormat = new ThreadLocalDateFormat(DATE_FORMAT);

    public LoggerInstance ()
    {
        this(System.out);
    }

    public LoggerInstance (OutputStream output)
    {
        this(new PrintWriter(output, true));
    }

    public LoggerInstance (PrintWriter output)
    {
        enableDefault();
        this.output = output;
    }

    public void enableDefault ()
    {
        disableAll();
        enable(NORMAL);
        enable(WARNING);
        enable(ERROR);
    }

    public void enableAll ()
    {
        enable(NORMAL);
        enable(WARNING);
        enable(ERROR);
        enable(DEBUG);
    }

    public void disableAll ()
    {
        enabledLevels = 0;
    }

    public void enable (int level)
    {
        enabledLevels |= getMask(level);
    }

    public void disable (int level)
    {
        synchronized (this) {
            if (isEnabled(level)) {
                enabledLevels -= getMask(level);
            }
        }
    }

    public boolean isEnabled (int level)
    {
        level = getMask(level);
        return (enabledLevels & level) == level;
    }

    protected int getMask (int level)
    {
        return (int) Math.pow(2, level);
    }

    public void setLoggingThreads (boolean enable)
    {
        loggingThreads = enable;
    }

    public boolean isLoggingThreads ()
    {
        return loggingThreads;
    }

    public void setExtraLine (boolean extraLine)
    {
        this.extraLine = extraLine;
    }

    public boolean isExtraLine ()
    {
        return extraLine;
    }

    public void setLogFile (String filePath)
        throws FileNotFoundException
    {
        FileOutputStream stream = new FileOutputStream(filePath);
        setLogStream(stream);
    }

    public void setLogStream (OutputStream stream)
    {
        output = new PrintWriter(stream, true);
    }

    @Override
    public void log (Object message)
    {
        log(NORMAL, message);
    }

    public void log (int level, Object message)
    {
        if (isEnabled(level)) {

            // add a timestamp
            output.print("[" + getTimestamp() + "]");

            // log thread name if enabled
            if (loggingThreads) {
                output.print("[" + Thread.currentThread().getName() + "]");
            }

            // add logging level
            output.print(getLevelLabel(level));

            // message
            output.print(" ");
            output.println(message);

            // extra line
            if (extraLine) {
                output.println();
            }
        }
    }

    public static String getTimestamp ()
    {
        return dateFormat.format(new Date());
    }

    public static String getLevelLabel (int level)
    {
        String label = "";

        switch (level) {

        case NORMAL:
            break; // no label for normal logging

        case WARNING:
            label = "[WARNING]";
            break;

        case ERROR:
            label = "[ERROR]";
            break;

        case DEBUG:
            label = "[VERBOSE]";
            break;

        default:
            label = "[" + level + "]";

        }

        return label;
    }

    @Override
    public void debug (Object message)
    {
        log(DEBUG, message);
    }

    @Override
    public void warning (Object message)
    {
        log(WARNING, message);
    }

    @Override
    public void error (Object message)
    {
        log(ERROR, message);
    }

    @Override
    public void logStackTrace ()
    {
        logStackTrace(new Exception("trace"));
    }

    @Override
    public void logStackTrace (Throwable oops)
    {
        oops.printStackTrace(output);
    }

}
