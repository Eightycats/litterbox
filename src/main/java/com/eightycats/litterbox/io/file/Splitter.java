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

import java.util.StringTokenizer;

/**
 * Base class for reading a text file and parsing each line into tokens.
 */
public abstract class Splitter extends LineReader
{
    protected String _delimiters;

    public Splitter ()
    {
        this(",|\t ");
    }

    public Splitter (String delimiters)
    {
        setDelimiters(delimiters);
    }

    @Override
    protected void process (String line)
    {
        process(new StringTokenizer(line, getDelimiters()));
    }

    protected abstract void process (StringTokenizer tokens);

    protected String getDelimiters ()
    {
        return _delimiters;
    }

    protected void setDelimiters (String delimiters)
    {
        _delimiters = delimiters;
    }
}
