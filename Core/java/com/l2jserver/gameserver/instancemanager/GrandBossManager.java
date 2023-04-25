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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2GrandBossInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.type.L2BossZone;
import com.l2jserver.gameserver.templates.StatsSet;

/**
 * @author DaRkRaGe Revised by Emperorc
 */
public class GrandBossManager
{
	private static final String DELETE_GRAND_BOSS_LIST = "DELETE FROM grandboss_list";
	private static final String INSERT_GRAND_BOSS_LIST = "INSERT INTO grandboss_list (player_id,zone) VALUES (?,?)";
	private static final String UPDATE_GRAND_BOSS_DATA = "UPDATE grandboss_data set loc_x = ?, loc_y = ?, loc_z = ?, heading = ?, respawn_time = ?, currentHP = ?, currentMP = ?, status = ? where boss_id = ?";
	private static final String UPDATE_GRAND_BOSS_DATA2 = "UPDATE grandboss_data set status = ? where boss_id = ?";
	
	protected static Logger _log = Logger.getLogger(GrandBossManager.class.getName());
	protected static Map<Integer, L2GrandBossInstance> _bosses;
	protected static Map<Integer, StatsSet> _storedInfo;
	private Map<Integer, Integer> _bossStatus;
	private List<L2BossZone> _zones;
	
	public static GrandBossManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private GrandBossManager()
	{
		init();
	}
	
	private void init()
	{
		_zones = new ArrayList<>();
		
		_bosses = new HashMap<>();
		_storedInfo = new HashMap<>();
		_bossStatus = new HashMap<>();
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try (PreparedStatement statement = con.prepareStatement("SELECT * from grandboss_data ORDER BY boss_id"))
			{
				try (ResultSet rset = statement.executeQuery())
				{
					while (rset.next())
					{
						// Read all info from DB, and store it for AI to read and decide what to do
						// faster than accessing DB in real time
						StatsSet info = new StatsSet();
						int bossId = rset.getInt("boss_id");
						info.set("loc_x", rset.getInt("loc_x"));
						info.set("loc_y", rset.getInt("loc_y"));
						info.set("loc_z", rset.getInt("loc_z"));
						info.set("heading", rset.getInt("heading"));
						info.set("respawn_time", rset.getLong("respawn_time"));
						double HP = rset.getDouble("currentHP"); // jython doesn't recognize doubles
						int true_HP = (int) HP; // so use java's ability to type cast
						info.set("currentHP", true_HP); // to convert double to int
						double MP = rset.getDouble("currentMP");
						int true_MP = (int) MP;
						info.set("currentMP", true_MP);
						int status = rset.getInt("status");
						_bossStatus.put(bossId, status);
						_storedInfo.put(bossId, info);
						// These two bosses will load log from DP script
						if ((bossId == 29065) || (bossId == 29062))
						{
							continue;
						}
						_log.info("Grand Boss: " + NpcTable.getInstance().getTemplate(bossId).getName() + "(" + bossId + ") status is " + status + ".");
						if (status > 0)
						{
							if ((bossId == 29065) || (bossId == 29062))
							{
								continue;
							}
							_log.info("Grand Boss: Next spawn date of " + NpcTable.getInstance().getTemplate(bossId).getName() + " is " + new Date(info.getLong("respawn_time")) + ".");
						}
						
						info = null;
					}
					_log.info("Grand Boss: Loaded " + _storedInfo.size() + " Instances");
				}
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, "GrandBossManager: Could not load grandboss_data table: " + e.getMessage(), e);
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Error while initializing GrandBossManager: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Zone Functions
	 */
	public void initZones()
	{
		Map<Integer, List<Integer>> zones = new HashMap<>();
		
		if (_zones == null)
		{
			_log.warning("Could not read Grand Boss zone data");
			return;
		}
		
		for (L2BossZone zone : _zones)
		{
			if (zone == null)
			{
				continue;
			}
			zones.put(zone.getId(), new ArrayList<>());
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * from grandboss_list ORDER BY player_id"))
		{
			try (ResultSet rset = statement.executeQuery())
			{
				while (rset.next())
				{
					int id = rset.getInt("player_id");
					int zone_id = rset.getInt("zone");
					zones.get(zone_id).add(id);
				}
			}
			_log.info("Grand Boss: Initialized " + _zones.size() + " Grand Boss Zones");
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, "GrandBossManager: Could not load grandboss_list table: " + e.getMessage(), e);
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Error while initializing GrandBoss zones: " + e.getMessage(), e);
		}
		
		for (L2BossZone zone : _zones)
		{
			if (zone == null)
			{
				continue;
			}
			zone.setAllowedPlayers(zones.get(zone.getId()));
		}
		
		zones.clear();
	}
	
	public void addZone(L2BossZone zone)
	{
		if (_zones != null)
		{
			_zones.add(zone);
		}
	}
	
	public final L2BossZone getZone(L2Character character)
	{
		if (_zones != null)
		{
			for (L2BossZone temp : _zones)
			{
				if (temp.isCharacterInZone(character))
				{
					return temp;
				}
			}
		}
		return null;
	}
	
	public final L2BossZone getZone(int x, int y, int z)
	{
		if (_zones != null)
		{
			for (L2BossZone temp : _zones)
			{
				if (temp.isInsideZone(x, y, z))
				{
					return temp;
				}
			}
		}
		return null;
	}
	
	public boolean checkIfInZone(String zoneType, L2Object obj)
	{
		L2BossZone temp = getZone(obj.getX(), obj.getY(), obj.getZ());
		if (temp == null)
		{
			return false;
		}
		
		return temp.getName().equalsIgnoreCase(zoneType);
	}
	
	public boolean checkIfInZone(L2PcInstance player)
	{
		if (player == null)
		{
			return false;
		}
		L2BossZone temp = getZone(player.getX(), player.getY(), player.getZ());
		if (temp == null)
		{
			return false;
		}
		
		return true;
	}
	
	public int getBossStatus(int bossId)
	{
		return _bossStatus.get(bossId);
	}
	
	public void setBossStatus(int bossId, int status)
	{
		_bossStatus.put(bossId, status);
		_log.info(getClass().getSimpleName() + ": Updated " + NpcTable.getInstance().getTemplate(bossId).getName() + "(" + bossId + ") status to " + status);
		updateDb(bossId, true);
	}
	
	/**
	 * Adds a L2GrandBossInstance to the list of bosses.
	 */
	public void addBoss(L2GrandBossInstance boss)
	{
		if (boss != null)
		{
			_bosses.put(boss.getNpcId(), boss);
		}
	}
	
	public L2GrandBossInstance getBoss(int bossId)
	{
		return _bosses.get(bossId);
	}
	
	public StatsSet getStatsSet(int bossId)
	{
		return _storedInfo.get(bossId);
	}
	
	public void setStatsSet(int bossId, StatsSet info)
	{
		_storedInfo.put(bossId, info);
		updateDb(bossId, false);
	}
	
	private void storeToDb()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement1 = con.prepareStatement(DELETE_GRAND_BOSS_LIST))
		{
			statement1.executeUpdate();
			
			try (PreparedStatement statement = con.prepareStatement(INSERT_GRAND_BOSS_LIST))
			{
				for (L2BossZone zone : _zones)
				{
					if (zone == null)
					{
						continue;
					}
					Integer id = zone.getId();
					List<Integer> list = zone.getAllowedPlayers();
					if ((list == null) || list.isEmpty())
					{
						continue;
					}
					for (Integer player : list)
					{
						statement.setInt(1, player);
						statement.setInt(2, id);
						statement.executeUpdate();
						statement.clearParameters();
					}
				}
			}
			for (Integer bossId : _storedInfo.keySet())
			{
				final L2GrandBossInstance boss = _bosses.get(bossId);
				StatsSet info = _storedInfo.get(bossId);
				if ((boss == null) || (info == null))
				{
					try (PreparedStatement statement = con.prepareStatement(UPDATE_GRAND_BOSS_DATA2))
					{
						statement.setInt(1, _bossStatus.get(bossId));
						statement.setInt(2, bossId);
					}
				}
				else
				{
					try (PreparedStatement statement = con.prepareStatement(UPDATE_GRAND_BOSS_DATA))
					{
						statement.setInt(1, boss.getX());
						statement.setInt(2, boss.getY());
						statement.setInt(3, boss.getZ());
						statement.setInt(4, boss.getHeading());
						statement.setLong(5, info.getLong("respawn_time"));
						double hp = boss.getCurrentHp();
						double mp = boss.getCurrentMp();
						if (boss.isDead())
						{
							hp = boss.getMaxHp();
							mp = boss.getMaxMp();
						}
						statement.setDouble(6, hp);
						statement.setDouble(7, mp);
						statement.setInt(8, _bossStatus.get(bossId));
						statement.setInt(9, bossId);
						statement.executeUpdate();
						statement.clearParameters();
					}
				}
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, "GrandBossManager: Couldn't store grandbosses to database:" + e.getMessage(), e);
		}
	}
	
	private void updateDb(int bossId, boolean statusOnly)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			L2GrandBossInstance boss = _bosses.get(bossId);
			StatsSet info = _storedInfo.get(bossId);
			
			if (statusOnly || (boss == null) || (info == null))
			{
				PreparedStatement statement = con.prepareStatement(UPDATE_GRAND_BOSS_DATA2);
				{
					statement.setInt(1, _bossStatus.get(bossId));
					statement.setInt(2, bossId);
					statement.executeUpdate();
				}
			}
			else
			{
				try (PreparedStatement statement = con.prepareStatement(UPDATE_GRAND_BOSS_DATA))
				{
					statement.setInt(1, boss.getX());
					statement.setInt(2, boss.getY());
					statement.setInt(3, boss.getZ());
					statement.setInt(4, boss.getHeading());
					statement.setLong(5, info.getLong("respawn_time"));
					double hp = boss.getCurrentHp();
					double mp = boss.getCurrentMp();
					if (boss.isDead())
					{
						hp = boss.getMaxHp();
						mp = boss.getMaxMp();
					}
					statement.setDouble(6, hp);
					statement.setDouble(7, mp);
					statement.setInt(8, _bossStatus.get(bossId));
					statement.setInt(9, bossId);
					statement.executeUpdate();
				}
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, "GrandBossManager: Couldn't update grandbosses to database:" + e.getMessage(), e);
		}
	}
	
	/**
	 * Saves all Grand Boss info and then clears all info from memory, including all schedules.
	 */
	public void cleanUp()
	{
		storeToDb();
		
		_bosses.clear();
		_storedInfo.clear();
		_bossStatus.clear();
		_zones.clear();
	}
	
	public List<L2BossZone> getZones()
	{
		return _zones;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final GrandBossManager _instance = new GrandBossManager();
	}
}
