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

package com.eightycats.litterbox.properties.xml;

import java.util.Properties;

import com.eightycats.litterbox.properties.OrderedProperties;

public class OrderedXMLPropertiesParser extends XMLPropertiesParser
{
    /**
     * This overrides the properties parser to use an OrderedProperties object instead of just of
     * Properties object.
     */
    @Override
    protected Properties createProperties ()
    {
        return new OrderedProperties();
    }
}
