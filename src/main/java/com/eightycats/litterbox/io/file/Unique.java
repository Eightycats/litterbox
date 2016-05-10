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
import java.util.LinkedHashSet;

/**
 * Removes duplicate lines from a file. Note that this trims the lines.
 */
public class Unique extends CatResults
{
    protected LinkedHashSet<String> _existing = new LinkedHashSet<String>();

    @Override
    protected void start (File file)
    {
        _existing.clear();
    }

    @Override
    protected void process (String line)
    {
        _existing.add(line.trim());
    }

    @Override
    public Iterable<String> getResults ()
    {
        return _existing;
    }

    public static void main (String[] args)
    {
        Unique unique = new Unique();
        unique.setVerbose(true);
        // remove duplicates in place
        CatWriter.process(args[0], unique);
    }
}
