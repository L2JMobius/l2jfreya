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
package com.l2jserver.gameserver.instancemanager.achievments_engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.instancemanager.achievments_engine.base.Achievement;
import com.l2jserver.gameserver.instancemanager.achievments_engine.base.Condition;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Adena;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Castle;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.ClanLeader;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.ClanLevel;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.CompleteAchievements;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Crp;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Hero;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.HeroCount;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.ItemsCount;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Karma;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Level;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Mage;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Marry;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.MinCMcount;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Noble;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.OnlineTime;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Pk;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Pvp;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.RaidKill;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.RaidPoints;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.SkillEnchant;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.Sub;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.WeaponEnchant;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.eventKills;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.eventWins;
import com.l2jserver.gameserver.instancemanager.achievments_engine.conditions.events;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class AchievementsManager
{
	private final Map<Integer, Achievement> _achievementList = new HashMap<>();
	
	private final List<String> _binded = new ArrayList<>();
	
	private static Logger _log = Logger.getLogger(AchievementsManager.class.getName());
	
	public AchievementsManager()
	{
		loadAchievements();
		_log.info("Achievements: Engine is enabled.");
	}
	
	private void loadAchievements()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		
		File file = new File(Config.DATAPACK_ROOT, "data/xml/Achievements/Achievements.xml");
		
		if (!file.exists())
		{
			_log.warning("AchievementsEngine: Error achievements xml file does not exist, check directory!");
		}
		try
		{
			InputSource in = new InputSource(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			in.setEncoding("UTF-8");
			Document doc = factory.newDocumentBuilder().parse(in);
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if (n.getNodeName().equalsIgnoreCase("list"))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if (d.getNodeName().equalsIgnoreCase("achievement"))
						{
							int id = checkInt(d, "id");
							
							String name = String.valueOf(d.getAttributes().getNamedItem("name").getNodeValue());
							String description = String.valueOf(d.getAttributes().getNamedItem("description").getNodeValue());
							String reward = String.valueOf(d.getAttributes().getNamedItem("reward").getNodeValue());
							boolean repeat = checkBoolean(d, "repeatable");
							
							List<Condition> conditions = conditionList(d.getAttributes());
							
							_achievementList.put(id, new Achievement(id, name, description, reward, repeat, conditions));
							alterTable(id);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("AchievementsEngine: Error " + e);
			e.printStackTrace();
		}
	}
	
	public void rewardForAchievement(int achievementID, L2PcInstance player)
	{
		Achievement achievement = _achievementList.get(achievementID);
		
		for (int id : achievement.getRewardList().keySet())
		{
			int count = achievement.getRewardList().get(id).intValue();
			player.addItem(achievement.getName(), id, count, player, true);
			
		}
	}
	
	private static boolean checkBoolean(Node d, String nodename)
	{
		boolean b = false;
		
		try
		{
			b = Boolean.valueOf(d.getAttributes().getNamedItem(nodename).getNodeValue());
		}
		catch (Exception e)
		{
			
		}
		return b;
	}
	
	private int checkInt(Node d, String nodename)
	{
		int i = 0;
		
		try
		{
			i = Integer.valueOf(d.getAttributes().getNamedItem(nodename).getNodeValue());
		}
		catch (Exception e)
		{
			
		}
		return i;
	}
	
	private static void alterTable(int fieldID)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();)
		{
			Statement statement = con.createStatement();
			statement.executeUpdate("ALTER TABLE achievements ADD a" + fieldID + " INT DEFAULT 0");
		}
		catch (SQLException e)
		{
			
		}
	}
	
	public List<Condition> conditionList(NamedNodeMap attributesList)
	{
		List<Condition> conditions = new ArrayList<>();
		
		for (int j = 0; j < attributesList.getLength(); j++)
		{
			addToConditionList(attributesList.item(j).getNodeName(), attributesList.item(j).getNodeValue(), conditions);
		}
		
		return conditions;
	}
	
	public Map<Integer, Achievement> getAchievementList()
	{
		return _achievementList;
	}
	
	public List<String> getBinded()
	{
		return _binded;
	}
	
	public boolean isBinded(int obj, int ach)
	{
		for (String binds : _binded)
		{
			String[] spl = binds.split("@");
			if (spl[0].equals(String.valueOf(obj)) && spl[1].equals(String.valueOf(ach)))
			{
				return true;
			}
		}
		return false;
	}
	
	public static AchievementsManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final AchievementsManager _instance = new AchievementsManager();
	}
	
	private static void addToConditionList(String nodeName, Object value, List<Condition> conditions)
	{
		if (nodeName.equals("minLevel"))
		{
			conditions.add(new Level(value));
		}
		else if (nodeName.equals("minPvPCount"))
		{
			conditions.add(new Pvp(value));
		}
		else if (nodeName.equals("minPkCount"))
		{
			conditions.add(new Pk(value));
		}
		else if (nodeName.equals("minClanLevel"))
		{
			conditions.add(new ClanLevel(value));
		}
		else if (nodeName.equals("mustBeHero"))
		{
			conditions.add(new Hero(value));
		}
		else if (nodeName.equals("mustBeNoble"))
		{
			conditions.add(new Noble(value));
		}
		else if (nodeName.equals("minWeaponEnchant"))
		{
			conditions.add(new WeaponEnchant(value));
		}
		else if (nodeName.equals("minKarmaCount"))
		{
			conditions.add(new Karma(value));
		}
		else if (nodeName.equals("minAdenaCount"))
		{
			conditions.add(new Adena(value));
		}
		else if (nodeName.equals("minClanMembersCount"))
		{
			conditions.add(new MinCMcount(value));
		}
		else if (nodeName.equals("mustBeClanLeader"))
		{
			conditions.add(new ClanLeader(value));
		}
		else if (nodeName.equals("mustBeMarried"))
		{
			conditions.add(new Marry(value));
		}
		else if (nodeName.equals("itemAmmount"))
		{
			conditions.add(new ItemsCount(value));
		}
		else if (nodeName.equals("crpAmmount"))
		{
			conditions.add(new Crp(value));
		}
		else if (nodeName.equals("lordOfCastle"))
		{
			conditions.add(new Castle(value));
		}
		else if (nodeName.equals("mustBeMageClass"))
		{
			conditions.add(new Mage(value));
		}
		else if (nodeName.equals("minSubclassCount"))
		{
			conditions.add(new Sub(value));
		}
		else if (nodeName.equals("CompleteAchievements"))
		{
			conditions.add(new CompleteAchievements(value));
		}
		else if (nodeName.equals("minSkillEnchant"))
		{
			conditions.add(new SkillEnchant(value));
		}
		else if (nodeName.equals("minOnlineTime"))
		{
			conditions.add(new OnlineTime(value));
		}
		else if (nodeName.equals("minHeroCount"))
		{
			conditions.add(new HeroCount(value));
		}
		else if (nodeName.equals("raidToKill"))
		{
			conditions.add(new RaidKill(value));
		}
		else if (nodeName.equals("raidToKill1"))
		{
			conditions.add(new RaidKill(value));
		}
		else if (nodeName.equals("raidToKill2"))
		{
			conditions.add(new RaidKill(value));
		}
		else if (nodeName.equals("minRaidPoints"))
		{
			conditions.add(new RaidPoints(value));
		}
		else if (nodeName.equals("eventKills"))
		{
			conditions.add(new eventKills(value));
		}
		else if (nodeName.equals("events"))
		{
			conditions.add(new events(value));
		}
		else if (nodeName.equals("eventWins"))
		{
			conditions.add(new eventWins(value));
		}
	}
	
	public void loadUsed()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			String sql = "SELECT ";
			for (int i = 1; i <= getAchievementList().size(); i++)
			{
				if (i != getAchievementList().size())
				{
					sql = sql + "a" + i + ",";
				}
				else
				{
					sql = sql + "a" + i;
				}
				
			}
			
			sql = sql + " FROM achievements";
			try (PreparedStatement statement = con.prepareStatement(sql))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					while (rs.next())
					{
						for (int i = 1; i <= getAchievementList().size(); i++)
						{
							String ct = rs.getString(i);
							if ((ct.length() > 1) && ct.startsWith("1"))
							{
								_binded.add(ct.substring(ct.indexOf("1") + 1) + "@" + i);
							}
						}
					}
				}
			}
		}
		catch (SQLException e)
		{
			_log.warning("[ACHIEVEMENTS SAVE GETDATA]");
			if (Config.DEVELOPER)
			{
				e.printStackTrace();
			}
		}
	}
}