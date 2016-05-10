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

/**
 * Properties implementation that can inherit values from a chain of parent Properties. If a
 * property is not set in this instance, it will check its parents for a default value.
 */
public class PropertyChain extends Properties
{
    /**
     * Required by Serializable interface.
     */
    private static final long serialVersionUID = 1L;

    protected String _name;

    protected Properties _parent;

    public PropertyChain (String name)
    {
        this(name, null);
    }

    public PropertyChain (String name, PropertyChain parentProps)
    {
        setName(name);
        setParent(parentProps);
    }

    @Override
    public String getProperty (String key)
    {
        String value = super.getProperty(key);
        if (value == null && _parent != null) {
            value = _parent.getProperty(key);
        }
        return value;
    }

    public String getName ()
    {
        return _name;
    }

    public void setName (String name)
    {
        _name = name;
    }

    public Properties getParent ()
    {
        return _parent;
    }

    public void setParent (Properties parent)
    {
        _parent = parent;
    }
}
