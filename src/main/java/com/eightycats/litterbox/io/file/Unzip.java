package com.eightycats.litterbox.io.file;

public class Unzip
{
    public static void main (String[] args)
    {
        try {
            ZipUtils.unzip(args[0], args[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
