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
package com.l2jserver.gameserver.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.util.lib.Log;

public class GMAudit
{
	static
	{
		new File("log/GMAudit").mkdirs();
	}
	
	private static final Logger _log = Logger.getLogger(Log.class.getName());
	private static final SimpleDateFormat _formatter = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
	
	public static void auditGMAction(String gmName, String action, String target, String params)
	{
		String today = _formatter.format(new Date());
		
		try (FileWriter save = new FileWriter(new File("log/GMAudit/" + gmName + ".txt"), true))
		{
			String out = (today + ">" + gmName + ">" + action + ">" + target + ">" + params + "\r\n");
			save.write(out);
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, "GMAudit for GM " + gmName + " could not be saved: ", e);
		}
	}
	
	public static void auditGMAction(String gmName, String action, String target)
	{
		auditGMAction(gmName, action, target, "");
	}
}