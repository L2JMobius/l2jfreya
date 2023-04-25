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
package com.l2jserver.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.model.L2TeleportLocation;

public class TeleportLocationTable
{
	private static Logger _log = Logger.getLogger(TeleportLocationTable.class.getName());
	
	private Map<Integer, L2TeleportLocation> _teleports;
	
	public static TeleportLocationTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private TeleportLocationTable()
	{
		reloadAll();
	}
	
	public void reloadAll()
	{
		_teleports = new HashMap<>();
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM teleport"))
		{
			L2TeleportLocation teleport;
			
			try (ResultSet rset = statement.executeQuery())
			{
				while (rset.next())
				{
					teleport = new L2TeleportLocation();
					
					teleport.setTeleId(rset.getInt("id"));
					teleport.setLocX(rset.getInt("loc_x"));
					teleport.setLocY(rset.getInt("loc_y"));
					teleport.setLocZ(rset.getInt("loc_z"));
					teleport.setPrice(rset.getInt("price"));
					teleport.setIsForNoble(rset.getInt("fornoble") == 1);
					teleport.setItemId(rset.getInt("itemId"));
					
					_teleports.put(teleport.getTeleId(), teleport);
				}
			}
			_log.info("Loaded " + _teleports.size() + " Teleport Location Templates.");
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error loading Teleport Table.", e);
		}
		
		if (Config.CUSTOM_TELEPORT_TABLE)
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM custom_teleport"))
			{
				L2TeleportLocation teleport;
				int _cTeleCount = _teleports.size();
				try (ResultSet rset = statement.executeQuery();)
				{
					while (rset.next())
					{
						teleport = new L2TeleportLocation();
						teleport.setTeleId(rset.getInt("id"));
						teleport.setLocX(rset.getInt("loc_x"));
						teleport.setLocY(rset.getInt("loc_y"));
						teleport.setLocZ(rset.getInt("loc_z"));
						teleport.setPrice(rset.getInt("price"));
						teleport.setIsForNoble(rset.getInt("fornoble") == 1);
						teleport.setItemId(rset.getInt("itemId"));
						
						_teleports.put(teleport.getTeleId(), teleport);
					}
				}
				_cTeleCount = _teleports.size() - _cTeleCount;
				if (_cTeleCount > 0)
				{
					_log.info("TeleportLocationTable: Loaded " + _cTeleCount + " Custom Teleport Location Templates.");
				}
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "Error while creating custom teleport table " + e.getMessage(), e);
			}
		}
	}
	
	/**
	 * @param template id
	 * @return
	 */
	public L2TeleportLocation getTemplate(int id)
	{
		return _teleports.get(id);
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final TeleportLocationTable _instance = new TeleportLocationTable();
	}
}
