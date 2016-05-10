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

package com.eightycats.litterbox.logging;

/**
 * Some constant values used by the logging package.
 */
public interface LoggerConstants
{
   public static final int NORMAL = 0;

   public static final int WARNING = 1;

   public static final int ERROR = 2;

   public static final int DEBUG = 3;

   public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

   public static final String ALL = "all";

   public static final String DEFAULT = "default";

   public static final String NONE = "none";

   public static final String THREADS = "threads";

   public static final String NO_THREADS = "no_threads";
}
