/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;

/**
 * @author L2jPDT
 */
public class PremiumManager
{
	// LOGGER
	private final static Logger _log = Logger.getLogger(PremiumManager.class.getName());
	
	private long _end_pr_date;
	
	public static final PremiumManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public long getPremServiceData(String playerAcc)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT premium_service,enddate FROM account_premium WHERE account_name=?"))
		{
			statement.setString(1, playerAcc);
			try (ResultSet rset = statement.executeQuery())
			{
				while (rset.next())
				{
					if (Config.USE_PREMIUMSERVICE)
					{
						_end_pr_date = rset.getLong("enddate");
					}
					
				}
			}
		}
		catch (Exception e)
		{
			
		}
		return _end_pr_date;
	}
	
	public static void getPremiumAccountsTotal()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT premium_service FROM account_premium"))
		{
			int pa = 0;
			try (ResultSet rset = statement.executeQuery())
			{
				while (rset.next())
				{
					int premium_service = rset.getInt("premium_service");
					if (premium_service > 0)
					{
						pa++;
					}
				}
			}
			_log.info("Loaded: " + pa + " premium user(s).");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private static class SingletonHolder
	{
		protected static final PremiumManager _instance = new PremiumManager();
	}
}