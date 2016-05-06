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
