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

import java.nio.charset.StandardCharsets;

/**
 * Some Properties-related constants.
 */
public interface PropertyConstants
{
    /**
     * An equals sign, this is default separator between a property name and value.
     */
    public static final String KEY_VALUE_SEPARATOR = "=";

    /**
     * The default character, #, used to indicate that a line is commented out.
     */
    public static final String COMMENT_CHAR = "#";

    /**
     * All possible characters that could be used in a properties file to separate a property name
     * and value.
     */
    public static final String KEY_VALUE_SEPARATORS = "=: \t\r\n\f";

    /**
     * All possible comment characters: # or !
     */
    public static final String COMMENT_CHARS = "#!";

    /**
     * Possible whitespace characters: \t \r \n \f
     */
    public static final String WHITESPACE = " \t\r\n\f";

    /**
     * The usual name/value separator chars: = or :
     */
    public static final String STRICT_SEPARATORS = "=:";

    /**
     * All special property file chars that need to be escaped when saved to file. These characters
     * are escaped when writing out the property key.
     */
    public static final String SPECIAL_SAVE_CHARS = "=: \t\r\n\f#!";

    /**
     * These values will be escaped with a backslash when writing out the property value.
     */
    public static final String SPECIAL_VALUE_CHARS = "\t\r\n\f";

    /**
     * The character set used when reading or writing a properties file stream.
     */
    public static final String DEFAULT_CHARSET = StandardCharsets.ISO_8859_1.name();
}
