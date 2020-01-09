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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.text.StringEscapeUtils;

public class OrderedPropertyWriter
{
    public static void write (OrderedProperties properties, OutputStream stream, String header)
        throws IOException
    {
        OutputStreamWriter writer = new OutputStreamWriter(stream,
            PropertyConstants.DEFAULT_CHARSET);
        BufferedWriter buffer = new BufferedWriter(writer);
        PrintWriter out = new PrintWriter(buffer);

        if (header != null) {
            out.print(PropertyConstants.COMMENT_CHAR);
            out.println(header);
        }

        int count = properties.getElementCount();

        for (int i = 0; i < count; i++) {
            Object element = properties.get(i);
            if (element instanceof Property) {
                Property property = (Property) element;
                String key = property.getKey();
                String value = property.getValue().toString();

                key = StringEscapeUtils.escapeJava(key);
                value = StringEscapeUtils.escapeJava(value);
                out.println(key + PropertyConstants.KEY_VALUE_SEPARATOR + value);

            } else if (element instanceof Comment) {
                // write out any comments
                String comments = element.toString();

                // escape any unicode chars
                comments = StringEscapeUtils.escapeJava(comments);

                out.println(comments);
            }
        }
        buffer.flush();
    }
}
