package com.l2jserver.gameserver.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Michael
 */
public interface IConvert
{
	
	default byte toByte(String text)
	{
		return Byte.parseByte(text);
	}
	
	default short toShort(String text)
	{
		return Short.parseShort(text);
	}
	
	default int toInteger(String text)
	{
		return Integer.parseInt(text);
	}
	
	default long toLong(String text)
	{
		return Long.parseLong(text);
	}
	
	default double toDouble(String text)
	{
		return Double.parseDouble(text);
	}
	
	default float toFloat(String text)
	{
		return Float.parseFloat(text);
	}
	
	default boolean toBoolean(String text)
	{
		return Boolean.parseBoolean(text);
	}
	
	default String getString(Number num)
	{
		return String.valueOf(num);
	}
	
	default String getString(boolean value)
	{
		return String.valueOf(value);
	}
	
	default Object[] toArray(String line, Class<? extends Number> claz)
	{
		String[] stringArray = line.split(",");
		Object[] newArray = (Object[]) Array.newInstance(claz, stringArray.length);
		try
		{
			Method method = getClass().getMethod("to" + claz.getSimpleName(), String.class);
			for (int i = 0; i < stringArray.length; i++)
			{
				newArray[i] = method.invoke(this, stringArray[i]);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		return newArray;
	}
	
	@SuppressWarnings(
	{
		"rawtypes",
		"unchecked"
	})
	default Map<Number, Number> toMap(String line, Class<? extends Number> key, Class<? extends Number> value, Class<? extends Map> mapType)
	{
		Map<Number, Number> map = null;
		try
		{
			map = mapType.newInstance();
			String[] stringArray = line.split(";");
			String[] tmpArray;
			Method keyMethod = getClass().getMethod("to" + key.getSimpleName(), String.class);
			Method valueMethod = getClass().getMethod("to" + value.getSimpleName(), String.class);
			for (String tmp : stringArray)
			{
				tmpArray = tmp.split(",");
				map.put((Number) keyMethod.invoke(this, tmpArray[0]), (Number) valueMethod.invoke(this, tmpArray[1]));
			}
		}
		catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return map;
	}
}
