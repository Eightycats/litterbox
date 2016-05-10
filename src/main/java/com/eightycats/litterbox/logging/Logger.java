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

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.StringTokenizer;

public class Logger
    implements LoggerConstants
{
    public static final String LOGGING_LEVEL_PROP = "LOGGING_LEVEL";

    private static LoggerInstance logger = new LoggerInstance();

    static {
        enableDefault();
        init();
    }

    /**
     * Initialize (or reinitialize) the logging levels from System property values.
     */
    public static void init ()
    {
        String loggingLevel = System.getProperty(LOGGING_LEVEL_PROP);

        if (loggingLevel != null) {
            // reset the enabled levels
            disableAll();

            StringTokenizer tokens = new StringTokenizer(loggingLevel, ",");

            while (tokens.hasMoreTokens()) {

                String next = tokens.nextToken();

                // check if "all" levels should be enabled
                if (next.equalsIgnoreCase(ALL)) {
                    enableAll();
                } else if (next.equalsIgnoreCase(DEFAULT)) {
                    enableDefault();
                } else if (next.equalsIgnoreCase(NONE)) {
                    disableAll();
                } else if (next.equalsIgnoreCase(THREADS)) {
                    setLoggingThreads(true);
                } else if (next.equalsIgnoreCase(NO_THREADS)) {
                    setLoggingThreads(false);
                } else {

                    try {
                        int level = Integer.parseInt(next);
                        enable(level);
                    } catch (NumberFormatException nfex) {
                        System.err.println("Could not parse logging level: [" + loggingLevel
                            + "]: " + nfex);
                    }

                }

            }

        }

    }

    public static void log (String message)
    {
        log(NORMAL, message);
    }

    public static void log (int level, String message)
    {
        logger.log(level, message);
    }

    public static void log (int level, String message, Class<?> source)
    {
        log(level, message);
    }

    public static void debug (String message)
    {
        log(DEBUG, message);
    }

    public static void warning (String message)
    {
        log(WARNING, message);
    }

    public static void warning (String message, Class<?> source)
    {
        log(WARNING, message, source);
    }

    public static void error (String message)
    {
        log(ERROR, message);
    }

    public static void error (String message, Class<?> source)
    {
        log(ERROR, message, source);
    }

    public static void logStackTrace (Throwable oops)
    {
        logger.logStackTrace(oops);
    }

    public static void enableDefault ()
    {
        disableAll();
        enable(NORMAL);
        enable(WARNING);
        enable(ERROR);
    }

    public static void enableAll ()
    {
        enable(NORMAL);
        enable(WARNING);
        enable(ERROR);
        enable(DEBUG);
        setLoggingThreads(true);
    }

    public static void disableAll ()
    {
        logger.disableAll();
    }

    public static void enable (int level)
    {
        logger.enable(level);
    }

    public static void disable (int level)
    {
        logger.disable(level);
    }

    public static boolean isEnabled (int level)
    {
        return logger.isEnabled(level);
    }

    public static void setLoggingThreads (boolean enable)
    {
        logger.setLoggingThreads(enable);
    }

    public static boolean isLoggingThreads ()
    {
        return logger.isLoggingThreads();
    }

    public static void setLogFile (String filePath) throws FileNotFoundException
    {
        logger.setLogFile(filePath);
    }

    public static void setLogStream (OutputStream stream)
    {
        logger.setLogStream(stream);
    }

    public static void setExtraLine (boolean extraLine)
    {
        logger.setExtraLine(extraLine);
    }

    public static boolean isExtraLine ()
    {
        return logger.isExtraLine();
    }

    public static LoggerInstance getInstance ()
    {
        return logger;
    }

}
