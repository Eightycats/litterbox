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

/**
 * Replace text on each line.
 */
public class Replace extends Cat
{
    protected String _pattern;

    protected String _replacement;

    protected boolean _all;

    public Replace (String pattern, String replacement)
    {
        this(pattern, replacement, true);
    }

    public Replace (String pattern, String replacement, boolean all)
    {
        _pattern = pattern;
        _replacement = replacement;
        _all = all;
    }

    @Override
    protected void process (String line)
    {
        if (_all) {
            line = line.replaceAll(_pattern, _replacement);
        } else {
            line = line.replace(_pattern, _pattern);
        }
        println(line);
    }

    public static void main (String[] args)
    {
        Replace replacer = new Replace(args[1], args[2]);
        replacer.setVerbose(true);
        CatWriter.process(args[0], replacer);
    }
}
