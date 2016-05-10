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

package com.eightycats.litterbox.io.file;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;

import com.eightycats.litterbox.io.StreamUtils;

/**
 * Some utility methods for zipping and unzipping archive files.
 */
public class ZipUtils
{

    public static void zip (String zipFile, File dir) throws FileNotFoundException, IOException
    {
        zip(zipFile, new File[] { dir });
    }

    public static void zip (String zipFile, File[] entries) throws FileNotFoundException,
        IOException
    {
        File file = new File(zipFile);

        // make sure that the output directory exists
        FileUtils.makeParentDirs(file);

        FileOutputStream stream = new FileOutputStream(file);
        ZipOutputStream output = new ZipOutputStream(stream);

        try {
            zip(output, entries);
        } finally {
            try {
                output.flush();
            } catch (Exception ex) {
            }

            try {
                output.close();
            } catch (Exception ex) {
                StreamUtils.close(stream);
            }

        }

    }

    public static void zip (ZipOutputStream output, File[] entries) throws FileNotFoundException,
        IOException
    {

        for (int i = 0; i < entries.length; i++) {
            addEntry(output, entries[i]);
        }

    }

    public static void addEntry (ZipOutputStream output, File entry) throws FileNotFoundException,
        IOException
    {
        if (entry.isDirectory()) {
            addEntry(output, entry, entry);
        } else {
            addEntry(output, entry.getParentFile(), entry);
        }
    }

    public static void addEntry (ZipOutputStream output, File parentDir, File entry)
        throws FileNotFoundException, IOException
    {
        if (entry.isDirectory()) {
            File[] files = entry.listFiles();
            for (int i = 0; i < files.length; i++) {
                addEntry(output, parentDir, files[i]);
            }

        } else {

            // read the input file
            FileInputStream input = new FileInputStream(entry);

            try {
                String fileName = FileUtils.getRelativePath(parentDir, entry);
                // Use forward slashes for file separators
                // in the entry names. Backslashes seem to cause
                // problems when used to create jar files.
                fileName = fileName.replace('\\', '/');
                output.putNextEntry(new ZipEntry(fileName));
                StreamUtils.copy(input, output);
                output.closeEntry();
            } finally {
                input.close();
            }
        }
    }

    public static void unzip (String zipFile) throws FileNotFoundException, IOException
    {
        unzip(zipFile, ".");
    }

    public static void unzip (String zipFile, String destination) throws FileNotFoundException,
        IOException
    {
        FileInputStream zipStream = new FileInputStream(zipFile);
        BufferedInputStream bufferStream = new BufferedInputStream(zipStream);
        ZipInputStream input = new ZipInputStream(bufferStream);

        try {

            ZipEntry entry = input.getNextEntry();

            while (entry != null) {
                if (!entry.isDirectory()) {
                    String fileName = destination + File.separator + entry.getName();

                    // make sure that the output directory exists
                    File file = new File(fileName);
                    FileUtils.makeParentDirs(file);

                    // Write the file to the file system
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    BufferedOutputStream output = new BufferedOutputStream(fileOutput);

                    try {
                        StreamUtils.copy(input, output);
                    } finally {
                        StreamUtils.close(output);
                    }

                }

                entry = input.getNextEntry();
            }

        } finally {
            zipStream.close();
        }
    }

    public static String[] list (String zipFile) throws IOException
    {
        List<String> results = new ArrayList<String>();

        ZipFile zip = new ZipFile(zipFile);
        Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                String name = entry.getName();
                results.add(name);
            }
        }
        zip.close();

        return results.toArray(new String[results.size()]);
    }

    public static void main (String[] args)
    {
        try {
            File[] files = new File[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                files[i - 1] = new File(args[i]);
            }
            zip(args[0], files);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
