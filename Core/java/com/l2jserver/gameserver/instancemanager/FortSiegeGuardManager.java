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
package com.l2jserver.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.instance.L2FortBallistaInstance;
import com.l2jserver.gameserver.model.entity.Fort;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;

public class FortSiegeGuardManager
{
	
	private static final Logger _log = Logger.getLogger(FortSiegeGuardManager.class.getName());
	
	private final Fort _fort;
	protected Map<Integer, List<L2Spawn>> _siegeGuards = new HashMap<>();
	protected List<L2Spawn> _siegeGuardsSpawns;
	
	public FortSiegeGuardManager(Fort fort)
	{
		_fort = fort;
	}
	
	/**
	 * Spawn guards.<BR>
	 * <BR>
	 */
	public void spawnSiegeGuard()
	{
		try
		{
			List<L2Spawn> monsterList = getSiegeGuardSpawn().get(getFort().getFortId());
			if (monsterList != null)
			{
				for (L2Spawn spawnDat : monsterList)
				{
					spawnDat.doSpawn();
					if (spawnDat.getLastSpawn() instanceof L2FortBallistaInstance)
					{
						spawnDat.stopRespawn();
					}
					else
					{
						spawnDat.startRespawn();
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Error spawning siege guards for fort " + getFort().getName() + ":" + e.getMessage(), e);
		}
	}
	
	/**
	 * Unspawn guards.<BR>
	 * <BR>
	 */
	public void unspawnSiegeGuard()
	{
		try
		{
			List<L2Spawn> monsterList = getSiegeGuardSpawn().get(getFort().getFortId());
			
			if (monsterList != null)
			{
				for (L2Spawn spawnDat : monsterList)
				{
					spawnDat.stopRespawn();
					if (spawnDat.getLastSpawn() != null)
					{
						spawnDat.getLastSpawn().doDie(spawnDat.getLastSpawn());
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Error unspawning siege guards for fort " + getFort().getName() + ":" + e.getMessage(), e);
		}
	}
	
	/**
	 * Load guards.<BR>
	 * <BR>
	 */
	void loadSiegeGuard()
	{
		_siegeGuards.clear();
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM fort_siege_guards Where fortId = ? "))
		{
			statement.setInt(1, getFort().getFortId());
			
			L2Spawn spawn1;
			L2NpcTemplate template1;
			_siegeGuardsSpawns = new ArrayList<>();
			
			try (ResultSet rs = statement.executeQuery())
			{
				while (rs.next())
				{
					int fortId = rs.getInt("fortId");
					template1 = NpcTable.getInstance().getTemplate(rs.getInt("npcId"));
					if (template1 != null)
					{
						spawn1 = new L2Spawn(template1);
						spawn1.setAmount(1);
						spawn1.setXLocation(rs.getInt("x"));
						spawn1.setYLocation(rs.getInt("y"));
						spawn1.setZLocation(rs.getInt("z"));
						spawn1.setHeading(rs.getInt("heading"));
						spawn1.setRespawnDelay(rs.getInt("respawnDelay"));
						spawn1.setLocation(0);
						
						_siegeGuardsSpawns.add(spawn1);
					}
					else
					{
						_log.warning("Missing npc data in npc table for id: " + rs.getInt("npcId"));
					}
					_siegeGuards.put(fortId, _siegeGuardsSpawns);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Error loading siege guard for fort " + getFort().getName() + ": " + e.getMessage(), e);
		}
	}
	
	public final Fort getFort()
	{
		return _fort;
	}
	
	public final Map<Integer, List<L2Spawn>> getSiegeGuardSpawn()
	{
		return _siegeGuards;
	}
}
