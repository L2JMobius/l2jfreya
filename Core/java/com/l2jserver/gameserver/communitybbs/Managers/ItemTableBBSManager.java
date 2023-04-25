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
 * this program. If not, see <http://l2jpsproject.eu/>.
 */
package com.l2jserver.gameserver.communitybbs.Managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.L2DatabaseFactory;

public class ItemTableBBSManager
{
	private static Logger _log = Logger.getLogger(ItemTableBBSManager.class.getName());
	
	private static String _icon = null;
	
	public static void main(String[] args)
	{
	}
	
	public static String LoadIconData(int itemid)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT icon FROM item_icons WHERE item_id=?"))
		{
			statement.setInt(1, itemid);
			
			try (ResultSet recorddata = statement.executeQuery())
			{
				while (recorddata.next())
				{
					_icon = recorddata.getString("icon");
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "error while creating record table " + e);
		}
		return _icon;
	}
	
	public static String getIcon(int itemid)
	{
		return LoadIconData(itemid);
	}
}