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

package com.eightycats.litterbox.format;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadLocalDateFormat extends ThreadLocal<DateFormat>
{
    protected String _dateFormat;

    public ThreadLocalDateFormat (String format)
    {
        _dateFormat = format;
    }

    @Override
    protected DateFormat initialValue ()
    {
        return new SimpleDateFormat(_dateFormat);
    }

    public String format (Date date)
    {
        return get().format(date);
    }

    public Date parse (String date)
        throws ParseException
    {
        return get().parse(date);
    }
}
