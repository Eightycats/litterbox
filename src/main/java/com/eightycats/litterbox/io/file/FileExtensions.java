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

import java.io.File;

/**
 * Some constants and utility methods for checking common file extensions.
 */
public abstract class FileExtensions
{
    public static final String TXT = "txt";

    public static final String HTML = "html";

    public static final String HTM = "htm";

    public static final String CSV = "csv";

    public static final String XML = "xml";

    public static final String TAR = "tar";

    public static final String ZIP = "zip";

    public static final String JAR = "jar";

    /**
     * This checks to see if the given filename ends with the given file extension.
     *
     * @param filename
     *            String the file name.
     * @param extension
     *            String the file extension to check for.
     * @return boolean true if the filename ends with the given extension, false otherwise.
     */
    public static boolean check (String filename, String extension)
    {
        boolean match = false;

        int extLength = extension.length();
        int fileLength = filename.length();

        if (fileLength >= extLength) {
            String fileExt = filename.substring(fileLength - extLength);
            match = fileExt.equalsIgnoreCase(extension);
        }

        return match;
    }

    /**
     * This checks to see if the given filename ends with "htm" or "html".
     */
    public static boolean checkHtml (String filename)
    {
        return check(filename, HTML) || check(filename, HTM);
    }

    /**
     * Returns the file extension. If the file does not have an extension, this returns an empty
     * string.
     */
    public static String getExtension (String filePath)
    {
        String extension = "";

        File file = new File(filePath);
        String name = file.getName();

        int dot = name.lastIndexOf(".");

        if (dot != -1) {
            extension = name.substring(dot + 1);
        }

        return extension;
    }
}
