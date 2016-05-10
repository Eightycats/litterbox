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

import java.io.Serializable;

/**
 * A class that represents a name-value Property pair.
 */
public class Property implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * The property name.
     */
    protected String _key;

    /**
     * The property value.
     */
    protected Object _value;

    public Property (String key, Object value)
    {
        _key = key;
        _value = value;
    }

    /**
     * Gets the property's name.
     */
    public String getKey ()
    {
        return _key;
    }

    /**
     * Gets the property's value.
     */
    public Object getValue ()
    {
        return _value;
    }

    /**
     * Sets a new value for the property.
     */
    public void setValue (Object value)
    {
        _value = value;
    }

    /**
     * Return: "propertyName=propertyValue".
     */
    @Override
    public String toString ()
    {
        return getKey() + PropertyConstants.KEY_VALUE_SEPARATOR + getValue();
    }
}
