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
package com.l2jserver.scripts.handlers.communityboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.communitybbs.Managers.BaseBBSManager;
import com.l2jserver.gameserver.communitybbs.Managers.TopBBSManager;
import com.l2jserver.gameserver.handler.interfaces.IBypassHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.variables.PlayerVariables;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author L2jPDT
 */
public class Statistics implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"_show_stats;",
		"_show_stats;pk",
		"_show_stats;clan",
		"_show_stats;pcbang",
		"_show_stats;level",
		"_show_stats;health",
		"_show_stats;mana",
		"_show_stats;cp",
	};
	
	public class CBStatMan
	{
		public int PlayerId = 0;
		public String ChName = "";
		public int ChGameTime = 0;
		public int ChPk = 0;
		public int ChPvP = 0;
		public int ChPcBangPoint = 0;
		public String ChClanName = "";
		public int ChClanLevel = 0;
		public int ChClanRep = 0;
		public String ChClanAlly = "";
		public int ChOnOff = 0;
		public int ChSex = 0;
		public int ChLevel = 0;
		public int ChHealth = 0;
		public int ChMana = 0;
		public int ChCp = 0;
	}
	
	@Override
	public boolean useBypass(String bypass, L2PcInstance activeChar, L2Character target)
	{
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		int i = 0;
		String picture = "MiniGame_DF_Text_Level_";
		CBStatMan tp;
		
		if (bypass.equals("_show_stats;"))
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pvpkills DESC LIMIT 9;"))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					StringBuilder html = new StringBuilder();
					html.append("<table width=450>");
					
					while (rs.next())
					{
						tp = new CBStatMan();
						tp.PlayerId = rs.getInt("charId");
						tp.ChName = rs.getString("char_name");
						tp.ChPvP = rs.getInt("pvpkills");
						++i;
						html.append("<tr>");
						html.append("<td align=center width=40><img src=L2UI_CT1." + picture + i + " width=17 height=32></td>");
						html.append("<td align=center width=210><font name=hs9>" + tp.ChName + "</td>");
						html.append("<td align=center width=200><font color=LEVEL>" + tp.ChPvP + "x PvPs</font></td>");
						html.append("</tr>");
					}
					html.append("</table><br><img src=L2UI.SquareGray width=450 height=1>");
					
					NpcHtmlMessage file = new NpcHtmlMessage(5);
					file.setFile(activeChar, "data/html/CommunityBoard/ranking.htm");
					file.replace("%stat%", html.toString());
					file.replace("%playername%", activeChar.getName());
					file.replace("%servername%", Config.SERVER_NAME);
					file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
					file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
					file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
					file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
					file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
					file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
					file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
					file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
					file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
					file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
					file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
					file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					for (String var : TopBBSManager.VAR_NAMES)
					{
						file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					}
					BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				}
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (bypass.startsWith("_show_stats;pk"))
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pkkills DESC LIMIT 9;"))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					StringBuilder html = new StringBuilder();
					html.append("<table width=450>");
					while (rs.next())
					{
						tp = new CBStatMan();
						tp.PlayerId = rs.getInt("charId");
						tp.ChName = rs.getString("char_name");
						tp.ChPk = rs.getInt("pkkills");
						++i;
						html.append("<tr>");
						html.append("<td align=center width=40><img src=L2UI_CT1." + picture + i + " width=17 height=32></td>");
						html.append("<td align=center width=210><font name=hs9>" + tp.ChName + "</td>");
						html.append("<td align=center width=200><font color=LEVEL>" + tp.ChPk + "x PKs</font></td>");
						html.append("</tr>");
					}
					html.append("</table><br><img src=L2UI.SquareGray width=450 height=1>");
					
					NpcHtmlMessage file = new NpcHtmlMessage(5);
					file.setFile(activeChar, "data/html/CommunityBoard/ranking.htm");
					file.replace("%stat%", html.toString());
					file.replace("%playername%", activeChar.getName());
					file.replace("%servername%", Config.SERVER_NAME);
					file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
					file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
					file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
					file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
					file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
					file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
					file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
					file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
					file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
					file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
					file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
					file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					for (String var : TopBBSManager.VAR_NAMES)
					{
						file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					}
					BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				}
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (bypass.startsWith("_show_stats;level"))
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY level DESC LIMIT 9;"))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					StringBuilder html = new StringBuilder();
					html.append("<table width=450>");
					while (rs.next())
					{
						tp = new CBStatMan();
						tp.PlayerId = rs.getInt("charId");
						tp.ChName = rs.getString("char_name");
						tp.ChLevel = rs.getInt("level");
						++i;
						html.append("<tr>");
						html.append("<td align=center width=40><img src=L2UI_CT1." + picture + i + " width=17 height=32></td>");
						html.append("<td align=center width=210><font name=hs9>" + tp.ChName + "</td>");
						html.append("<td align=center width=200><font color=LEVEL>" + tp.ChLevel + " Level</font></td>");
						html.append("</tr>");
					}
					html.append("</table><br><img src=L2UI.SquareGray width=450 height=1>");
					
					NpcHtmlMessage file = new NpcHtmlMessage(5);
					file.setFile(activeChar, "data/html/CommunityBoard/ranking.htm");
					file.replace("%stat%", html.toString());
					file.replace("%playername%", activeChar.getName());
					file.replace("%servername%", Config.SERVER_NAME);
					file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
					file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
					file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
					file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
					file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
					file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
					file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
					file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
					file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
					file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
					file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
					file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					for (String var : TopBBSManager.VAR_NAMES)
					{
						file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					}
					BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				}
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (bypass.startsWith("_show_stats;health"))
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY maxHp DESC LIMIT 9;"))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					StringBuilder html = new StringBuilder();
					html.append("<table width=450>");
					while (rs.next())
					{
						tp = new CBStatMan();
						tp.PlayerId = rs.getInt("charId");
						tp.ChName = rs.getString("char_name");
						tp.ChHealth = rs.getInt("maxHp");
						++i;
						html.append("<tr>");
						html.append("<td align=center width=40><img src=L2UI_CT1." + picture + i + " width=17 height=32></td>");
						html.append("<td align=center width=210><font name=hs9>" + tp.ChName + "</td>");
						html.append("<td align=center width=200><font color=LEVEL>" + tp.ChHealth + " HPs</font></td>");
						html.append("</tr>");
					}
					html.append("</table><br><img src=L2UI.SquareGray width=450 height=1>");
					
					NpcHtmlMessage file = new NpcHtmlMessage(5);
					file.setFile(activeChar, "data/html/CommunityBoard/ranking.htm");
					file.replace("%stat%", html.toString());
					file.replace("%playername%", activeChar.getName());
					file.replace("%servername%", Config.SERVER_NAME);
					file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
					file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
					file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
					file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
					file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
					file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
					file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
					file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
					file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
					file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
					file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
					file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					for (String var : TopBBSManager.VAR_NAMES)
					{
						file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					}
					BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				}
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (bypass.startsWith("_show_stats;mana"))
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY maxMp DESC LIMIT 9;"))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					StringBuilder html = new StringBuilder();
					html.append("<table width=450>");
					while (rs.next())
					{
						tp = new CBStatMan();
						tp.PlayerId = rs.getInt("charId");
						tp.ChName = rs.getString("char_name");
						tp.ChMana = rs.getInt("maxMp");
						++i;
						html.append("<tr>");
						html.append("<td align=center width=40><img src=L2UI_CT1." + picture + i + " width=17 height=32></td>");
						html.append("<td align=center width=210><font name=hs9>" + tp.ChName + "</td>");
						html.append("<td align=center width=200><font color=LEVEL>" + tp.ChMana + " MPs</font></td>");
						html.append("</tr>");
					}
					html.append("</table><br><img src=L2UI.SquareGray width=450 height=1>");
					
					NpcHtmlMessage file = new NpcHtmlMessage(5);
					file.setFile(activeChar, "data/html/CommunityBoard/ranking.htm");
					file.replace("%stat%", html.toString());
					file.replace("%playername%", activeChar.getName());
					file.replace("%servername%", Config.SERVER_NAME);
					file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
					file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
					file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
					file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
					file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
					file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
					file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
					file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
					file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
					file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
					file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
					file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					for (String var : TopBBSManager.VAR_NAMES)
					{
						file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					}
					BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				}
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (bypass.startsWith("_show_stats;cp"))
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY maxCp DESC LIMIT 9;"))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					StringBuilder html = new StringBuilder();
					html.append("<table width=450>");
					while (rs.next())
					{
						tp = new CBStatMan();
						tp.PlayerId = rs.getInt("charId");
						tp.ChName = rs.getString("char_name");
						tp.ChCp = rs.getInt("maxCp");
						++i;
						html.append("<tr>");
						html.append("<td align=center width=40><img src=L2UI_CT1." + picture + i + " width=17 height=32></td>");
						html.append("<td align=center width=210><font name=hs9>" + tp.ChName + "</td>");
						html.append("<td align=center width=200><font color=LEVEL>" + tp.ChCp + " CPs</font></td>");
						html.append("</tr>");
					}
					html.append("</table><br><img src=L2UI.SquareGray width=450 height=1>");
					
					NpcHtmlMessage file = new NpcHtmlMessage(5);
					file.setFile(activeChar, "data/html/CommunityBoard/ranking.htm");
					file.replace("%stat%", html.toString());
					file.replace("%playername%", activeChar.getName());
					file.replace("%servername%", Config.SERVER_NAME);
					file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
					file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
					file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
					file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
					file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
					file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
					file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
					file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
					file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
					file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
					file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
					file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					for (String var : TopBBSManager.VAR_NAMES)
					{
						file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					}
					BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				}
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (bypass.startsWith("_show_stats;clan"))
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT clan_name,clan_level,ally_name FROM clan_data WHERE clan_level>0 order by clan_level desc LIMIT 9;"))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					StringBuilder html = new StringBuilder();
					html.append("<table width=450>");
					while (rs.next())
					{
						tp = new CBStatMan();
						tp.ChClanName = rs.getString("clan_name");
						tp.ChClanAlly = rs.getString("ally_name");
						tp.ChClanLevel = rs.getInt("clan_level");
						String ClanAlly;
						if (tp.ChClanAlly == null)
						{
							ClanAlly = "<font color=FF0000>NOT</font>";
						}
						else
						{
							ClanAlly = tp.ChClanAlly;
						}
						++i;
						html.append("<tr>");
						html.append("<td align=center width=40><img src=L2UI_CT1." + picture + i + " width=17 height=32></td>");
						html.append("<td align=center width=200>" + tp.ChClanName + "</td>");
						html.append("<td align=center width=200>" + ClanAlly + "</td>");
						html.append("<td align=center width=200><font color=LEVEL>" + tp.ChClanLevel + " Clan Lv.</font></td>");
						html.append("</tr>");
					}
					html.append("</table><br><img src=L2UI.SquareGray width=450 height=1>");
					
					NpcHtmlMessage file = new NpcHtmlMessage(5);
					file.setFile(activeChar, "data/html/CommunityBoard/ranking.htm");
					file.replace("%stat%", html.toString());
					file.replace("%playername%", activeChar.getName());
					file.replace("%servername%", Config.SERVER_NAME);
					file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
					file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
					file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
					file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
					file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
					file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
					file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
					file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
					file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
					file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
					file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
					file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					for (String var : TopBBSManager.VAR_NAMES)
					{
						file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
					}
					BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				}
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (bypass.startsWith("_show_stats;pcbang"))
		{
			if (Config.PC_BANG_ENABLED)
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pccafe_points DESC LIMIT 9;"))
				{
					try (ResultSet rs = statement.executeQuery())
					{
						StringBuilder html = new StringBuilder();
						html.append("<table width=450>");
						while (rs.next())
						{
							tp = new CBStatMan();
							tp.PlayerId = rs.getInt("charId");
							tp.ChName = rs.getString("char_name");
							tp.ChPcBangPoint = rs.getInt("pccafe_points");
							++i;
							html.append("<tr>");
							html.append("<td align=center width=40><img src=L2UI_CT1." + picture + i + " width=17 height=32></td>");
							html.append("<td align=center width=210>" + tp.ChName + "</td>");
							html.append("<td align=center width=200><font color=LEVEL>" + tp.ChPcBangPoint + "x PC Points</font></td>");
							html.append("</tr>");
						}
						html.append("</table><br><img src=L2UI.SquareGray width=450 height=1>");
						
						NpcHtmlMessage file = new NpcHtmlMessage(5);
						file.setFile(activeChar, "data/html/CommunityBoard/ranking.htm");
						file.replace("%stat%", html.toString());
						file.replace("%playername%", activeChar.getName());
						file.replace("%servername%", Config.SERVER_NAME);
						file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
						file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
						file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
						file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
						file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
						file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
						file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
						file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
						file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
						file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
						file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
						file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
						for (String var : TopBBSManager.VAR_NAMES)
						{
							file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
						}
						BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
					}
					return true;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				activeChar.sendMessage("PC Bang System is Deactivated");
			}
		}
		else
		{
			BaseBBSManager.separateAndSend("<html><body><br><br><center>In statistics function: " + bypass + " is not implemented yet.</center><br><br></body></html>", activeChar);
		}
		return true;
		
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}