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
        super();
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
