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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Reads the lines of a file into a Set.
 */
public class SetReader extends LineReader
{
    protected Set<String> _lines = new HashSet<String>();

    public void setSorted (boolean sorted)
    {
        if (sorted) {
            _lines = new TreeSet<String>(_lines);
        } else {
            _lines = new HashSet<String>(_lines);
        }
    }

    @Override
    protected void start (File file)
    {
        _lines.clear();
    }

    @Override
    protected void process (String line)
    {
        _lines.add(line);
    }

    public Set<String> getResults ()
    {
        return _lines;
    }

    public static void main (String[] args)
    {
        SetReader lines = new SetReader();
        lines.setSorted(true);
        lines.read(args[0]);
        for (String line : lines.getResults()) {
            System.out.println(line);
        }
    }
}
