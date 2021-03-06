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

import java.io.FileNotFoundException;

/**
 * Takes the output from query in the mysql client and converts it to CSV.
 */
public class QueryResultsToCSV extends ReadWrite
{
    public QueryResultsToCSV (String outputPath)
    {
        super(outputPath);
    }

    @Override
    protected void process (String line)
    {
        line = line.replaceAll("\\|", ",");
        if (line.startsWith(",")) {
            line = line.substring(1);
        }

        line = line.replaceAll("\\+-*", "");
        line = line.trim();

        if (line.length() > 0) {
            println(line);
        }
    }

    public static void processFile (String fileName)
        throws FileNotFoundException
    {
        new QueryResultsToCSV(fileName.replaceAll(".txt", ".csv")).read(fileName);
    }

    public static void main (String[] args)
    {
        try {
            processFile(args[0]);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
