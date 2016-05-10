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
 * Base class for readers that return a collection of results at the end.
 */
public abstract class CatResults extends Cat
{
    @Override
    protected void done (File file)
    {
        dump();
        super.done(file);
    }

    /**
     * Print all of the results to the output Writer.
     */
    public void dump ()
    {
        for (String line : getResults()) {
            println(line);
        }
    }

    public abstract Iterable<String> getResults ();
}
