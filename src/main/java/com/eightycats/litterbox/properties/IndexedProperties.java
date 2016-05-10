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

import java.util.*;

/**
 * This is a utility for getting a list of numbered property values from a Properties object.
 */
public class IndexedProperties
{
    /**
     * This method gets a list of indexed properties with a given prefix.
     *
     * <p>
     * For example, the following property values could be retrieved by passing "foo" as the prefix
     * to getPropertyList():
     *
     * <pre>
     *  foo0=bar0
     *  foo1=bar1
     *  foo2=bar2
     *  foo3=bar3
     * </pre>
     *
     * In the case of this example, the returned list would contain the values:
     * ["bar0,"bar1","bar2","bar3"]
     *
     * @param props
     *            the properties.
     * @param prefix
     *            the name prefix to look for.
     * @return the list values.
     */
    public static List<String> getPropertyList (Properties props, String prefix)
    {
        int count = 0;

        List<String> results = new ArrayList<String>();

        String nextValue = props.getProperty(prefix + count);

        while (nextValue != null) {
            results.add(nextValue);

            count++;
            nextValue = props.getProperty(prefix + count);
        }

        return results;
    }

    /**
     * Mainly for testing purposes.
     */
    public static void main (String[] args)
    {
        try {
            String file = args[0];
            String prefix = args[1];

            Properties props = PropertyUtil.readProperties(file);

            System.out.println(getPropertyList(props, prefix));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
