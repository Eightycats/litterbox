package com.eightycats.litterbox.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.eightycats.litterbox.logging.Logger;
import com.eightycats.litterbox.util.StringUtils;

public class ReflectionUtils
{
    public static final Class<?>[] EMPTY_TYPES = new Class[0];

    public static final Object[] EMPTY_PARAMS = new Object[0];

    public static Object getValue (Object instance, String propertyName)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Object result = null;
        Method getter = getAccessMethod(instance, propertyName);
        try {
            result = getter.invoke(instance, EMPTY_PARAMS);
        } catch (IllegalArgumentException illex) {
            // this should not happen
            Logger.logStackTrace(illex);
        }

        return result;
    }

    public static Method getAccessMethod (Object instance, String propertyName)
        throws NoSuchMethodException
    {
        Method method = null;

        try {
            method = getIsMethod(instance, propertyName);
        } catch (NoSuchMethodException nsmex) {
            method = getGetMethod(instance, propertyName);
        }

        return method;
    }

    public static Method getGetMethod (Object instance, String propertyName)
        throws NoSuchMethodException
    {
        return getAccessMethod(instance, propertyName, "get");
    }

    public static Method getIsMethod (Object instance, String propertyName)
        throws SecurityException, NoSuchMethodException
    {
        return getAccessMethod(instance, propertyName, "is");
    }

    private static Method getAccessMethod (Object instance, String propertyName, String methodPrefix)
        throws SecurityException, NoSuchMethodException
    {

        String methodName = methodPrefix + getTitleCase(propertyName);

        return instance.getClass().getMethod(methodName, EMPTY_TYPES);

    }

    public static void setValue (Object instance, String propertyName, Object newValue)
        throws NoSuchMethodException, InvocationTargetException, IllegalArgumentException,
        IllegalAccessException
    {

        Method setMethod = null;

        try {
            setMethod = getAssignmentMethod(instance, propertyName, newValue.getClass());
        } catch (NoSuchMethodException nsmex) {
            // special case for primitive fields
            if (newValue instanceof Number) {
                try {
                    Class<?> primitiveType = (Class<?>) newValue.getClass().getField("TYPE")
                        .get(newValue);

                    setMethod = getAssignmentMethod(instance, propertyName, primitiveType);
                } catch (Exception ex) {
                    Logger.error(ex.toString());
                    throw nsmex;
                }

            }
        }

        if (setMethod != null) {
            Object[] arg = new Object[1];
            arg[0] = newValue;

            setMethod.invoke(instance, arg);
        }

    }

    public static Method getAssignmentMethod (Object instance, String propertyName,
        Class<?> paramType)
        throws NoSuchMethodException
    {
        String methodName = "set" + getTitleCase(propertyName);
        Class<?>[] typeArray = new Class[1];
        typeArray[0] = paramType;

        return instance.getClass().getMethod(methodName, typeArray);
    }

    /**
     * Capitalize the first letter of the given string.
     */
    public static String getTitleCase (String propertyName)
    {
        return StringUtils.toSentenceCase(propertyName, true);
    }
}
