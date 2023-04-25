/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Noctarius
 */

public final class L2Properties extends Properties
{
	private static final long serialVersionUID = 1L;
	
	private static Logger _log = Logger.getLogger(L2Properties.class.getName());
	
	public L2Properties()
	{
	}
	
	public L2Properties(String name) throws IOException
	{
		try (FileInputStream fis = new FileInputStream(name);
			InputStreamReader reader = new InputStreamReader(fis, Charset.defaultCharset()))
		{
			super.load(reader);
		}
	}
	
	@Override
	public void load(InputStream inStream) throws IOException
	{
		try (InputStreamReader reader = new InputStreamReader(inStream, Charset.defaultCharset()))
		{
			super.load(reader);
		}
	}
	
	@Override
	public String getProperty(String key)
	{
		String property = super.getProperty(key);
		
		if (property == null)
		{
			_log.info("L2Properties: Missing property for key - " + key);
			
			return null;
		}
		
		return property.trim();
	}
	
	@Override
	public String getProperty(String key, String defaultValue)
	{
		String property = super.getProperty(key, defaultValue);
		
		if (property == null)
		{
			_log.warning("L2Properties: Missing defaultValue for key - " + key);
			
			return null;
		}
		
		return property.trim();
	}
}
