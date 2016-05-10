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

package com.eightycats.litterbox.properties;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class SystemProperties
{
    /**
     * Print all of the System properties.
     */
    public static void dump ()
    {
        Properties props = System.getProperties();
        // print them out in alphabetical order by key
        TreeSet<String> sortedKeys = new TreeSet<String>();
        Set<Object> keys = props.keySet();
        for (Object key : keys) {
            sortedKeys.add(key.toString());
        }

        for (String name : sortedKeys) {
            System.out.println(name + " : [" + props.getProperty(name) + "]");
        }
    }

    public static void main (String[] args)
    {
        dump();
    }

}
