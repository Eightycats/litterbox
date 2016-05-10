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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reads a file, sorts it, and spits out the result.
 */
public class Sort extends CatResults
{
    protected List<String> _results = new ArrayList<String>();

    @Override
    protected void start (File file)
    {
        _results.clear();
    }

    @Override
    protected void process (String line)
    {
        _results.add(line);
    }

    @Override
    protected void done (File file)
    {
        Collections.sort(_results);
        super.done(file);
    }

    @Override
    public Iterable<String> getResults ()
    {
        return _results;
    }

    public static void main (String[] args)
    {
        Sort sorter = new Sort();
        sorter.setVerbose(true);
        // file will get sorter in place
        CatWriter.process(args[0], sorter);
    }
}
