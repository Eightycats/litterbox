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

package com.eightycats.litterbox.util;

/**
 * Some utilities for getting command line options and printing usage, not that I'm being good
 * about printing actual usage anywhere.
 */
public class CmdLineUtils
{
    protected String _usage;

    public CmdLineUtils (String usage)
    {
        _usage = usage;
    }

    public String getUsage ()
    {
        return _usage;
    }

    public void die (String message)
    {
        die(_usage, message);
    }

    public String getOption (String optionName, int index, String[] args)
    {
        return getOption(optionName, index, args, _usage);
    }

    /**
     * If the arg value is null, this will print an error message and die.
     */
    public void checkRequiredArg (String argName, Object argValue)
    {
        if (argValue == null) {
            die("The <" + argName + "> argument is required.");
        }
    }

    public int getIntValue (String argName, String argValue)
    {
        int value = 0;

        try {
            value = Integer.parseInt(argValue);
        } catch (NumberFormatException nfex) {
            die("[" + argValue + "] is not a valid numeric value for " + "the \"" + argName
                + "\" parameter.");
        }

        return value;

    }

    public static void die (String usage, String message)
    {
        System.err.println(usage);
        System.err.println();
        System.err.println(message);
        System.exit(1);
    }

    public static String getOption (String optionName, int index, String[] args, String usage)
    {
        String result = null;
        try {
            result = args[index];
        } catch (Exception ex) {
            die(usage, "The " + optionName + " option requires an argument.");
        }
        return result;
    }
}
