package com.eightycats.litterbox.io.file;

public interface FileReader
{
     void read (String filePath);

     void read (String filePath, String encoding);
}
