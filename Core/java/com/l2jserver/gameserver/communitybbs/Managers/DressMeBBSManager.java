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
import java.util.Base64;
import java.util.StringTokenizer;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.DressMeData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.variables.PlayerVariables;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.ShowBoard;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author L2jPDT
 */
public class DressMeBBSManager extends BaseBBSManager
{
	public static DressMeBBSManager _Instance = null;
	public static boolean _transformed = DressMeData._transformed;
	
	public static DressMeBBSManager getInstance()
	{
		if (_Instance == null)
		{
			_Instance = new DressMeBBSManager();
		}
		return _Instance;
	}
	
	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
		if (Config.DRESS_ME_ONLY_FOR_PREMIUM)
		{
			if (activeChar.isPremiumAccount() == false)
			{
				activeChar.sendMessage(getText("WW91IG11c3QgYmUgYSBwcmVtaXVtIHVzZXIgZm9yIHVzZSAuZHJlc3NtZS4="));
				return;
			}
		}
		if (command.equals("_bbsdressme;"))
		{
			sendMainWindow(activeChar);
		}
		else if (command.startsWith("_bbsdressme;delete;"))
		{
			StringTokenizer stDell = new StringTokenizer(command, ";");
			stDell.nextToken();
			stDell.nextToken();
			int TpNameDell = Integer.parseInt(stDell.nextToken());
			deleteDressMe(activeChar, TpNameDell);
			activeChar.sendMessage("Dressme: You deleted dressme template: " + TpNameDell);
			DressMeBBSManager.sendMainWindow(activeChar);
		}
		else if (command.startsWith("_bbsdressme;save;"))
		{
			String[] params = command.split(";");
			if (params.length < 3)
			{
				activeChar.sendMessage("Dressme: Please enter a name!");
				DressMeBBSManager.sendMainWindow(activeChar);
				return;
			}
			String TpNameAdd = params[2];
			saveDressMe(activeChar, TpNameAdd);
			activeChar.sendMessage("Dressme: Your currently template has saved as " + TpNameAdd);
			DressMeBBSManager.sendMainWindow(activeChar);
		}
		else if (command.startsWith("_bbsdressme;use;"))
		{
			StringTokenizer stGoTp = new StringTokenizer(command, " ");
			stGoTp.nextToken();
			int agathion = Integer.parseInt(stGoTp.nextToken());
			int boots = Integer.parseInt(stGoTp.nextToken());
			int chest = Integer.parseInt(stGoTp.nextToken());
			int cloak = Integer.parseInt(stGoTp.nextToken());
			int gloves = Integer.parseInt(stGoTp.nextToken());
			int height = Integer.parseInt(stGoTp.nextToken());
			int legs = Integer.parseInt(stGoTp.nextToken());
			int radius = Integer.parseInt(stGoTp.nextToken());
			int transform = Integer.parseInt(stGoTp.nextToken());
			int weapon = Integer.parseInt(stGoTp.nextToken());
			LoadAndUse(activeChar, agathion, boots, chest, cloak, gloves, height, legs, radius, transform, weapon);
		}
		else
		{
			ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + command + " is not implemented yet</center><br><br></body></html>", "101");
			activeChar.sendPacket(sb);
			activeChar.sendPacket(new ShowBoard(null, "102"));
			activeChar.sendPacket(new ShowBoard(null, "103"));
		}
	}
	
	public static void sendMainWindow(L2PcInstance activeChar)
	{
		DressMeSQL tp;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement st = con.prepareStatement("SELECT * FROM dressme WHERE charId=?;");
			st.setLong(1, activeChar.getObjectId());
			ResultSet rs = st.executeQuery();
			StringBuilder html = new StringBuilder();
			html.append("<table width=150>");
			while (rs.next())
			{
				
				tp = new DressMeSQL();
				tp.dressme_id = rs.getInt("dressme_id");
				tp.charId = rs.getInt("charId");
				tp.agathion = rs.getInt("agathion");
				tp.boots = rs.getInt("boots");
				tp.chest = rs.getInt("chest");
				tp.cloak = rs.getInt("cloak");
				tp.gloves = rs.getInt("gloves");
				tp.height = rs.getInt("height");
				tp.legs = rs.getInt("legs");
				tp.radius = rs.getInt("radius");
				tp.transform = rs.getInt("transform");
				tp.weapon = rs.getInt("weapon");
				tp.name = rs.getString("name");
				
				html.append("<tr>");
				html.append("<td>");
				html.append("<button value=" + tp.name + " action=\"bypass -h _bbsdressme;use; " + tp.agathion + " " + tp.boots + " " + tp.chest + " " + tp.cloak + " " + tp.gloves + " " + tp.height + " " + tp.legs + " " + tp.radius + " " + tp.transform + " " + tp.weapon + "\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				html.append("</td>");
				html.append("<td>");
				html.append("<button value=\"Del.\" action=\"bypass -h _bbsdressme;delete;" + tp.dressme_id + "\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				html.append("</td>");
				html.append("</tr>");
			}
			html.append("</table>");
			
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(activeChar, "/data/html/CommunityBoard/dressme_main.htm");
			adminReply.replace("%tp%", html.toString());
			adminReply.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
			adminReply.replace("%slots%", "" + Config.DRESS_ME_MAXIMUM_TEMPLATES);
			PlayerVariables vars = activeChar.getVariables();
			int traders = TopBBSManager.getTraders();
			int onlineplayers = TopBBSManager.getOnline();
			adminReply.replace("%playername%", activeChar.getName());
			adminReply.replace("%servername%", Config.SERVER_NAME);
			adminReply.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
			adminReply.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
			adminReply.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
			adminReply.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
			adminReply.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
			adminReply.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
			adminReply.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
			adminReply.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
			adminReply.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
			adminReply.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
			adminReply.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
			adminReply.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
			for (final String var : TopBBSManager.VAR_NAMES)
			{
				adminReply.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
			}
			separateAndSend(adminReply.getHtm(), activeChar);
			return;
		}
		catch (Exception e)
		{
			
		}
	}
	
	private void LoadAndUse(L2PcInstance activeChar, int agathion, int boots, int chest, int cloak, int gloves, int height, int legs, int radius, int transform, int weapon)
	{
		if (activeChar.isDead() || activeChar.isAlikeDead() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isInJail() || activeChar.isFlying() || (activeChar.getKarma() > 0) || activeChar.isInDuel())
		{
			activeChar.sendMessage("Dress me cannot be used now.");
			return;
		}
		newDressData(activeChar);
		activeChar.getDressMeData().setAgathionId(agathion);
		activeChar.getDressMeData().setBootsId(boots);
		activeChar.getDressMeData().setChestId(chest);
		activeChar.getDressMeData().setCloakId(cloak);
		activeChar.getDressMeData().setGlovesId(gloves);
		activeChar.getDressMeData().setHeight(height);
		activeChar.getDressMeData().setLegsId(legs);
		activeChar.getDressMeData().setRadius(radius);
		activeChar.getDressMeData().setTransformId(transform);
		activeChar.getDressMeData().setWeapId(weapon);
		activeChar.setDressMeEnabled(true);
		activeChar.broadcastUserInfo();
		sendMainWindow(activeChar);
	}
	
	public void deleteDressMe(L2PcInstance activeChar, int TpNameDell)
	{
		try (Connection conDel = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stDel = conDel.prepareStatement("DELETE FROM dressme WHERE charId=? AND dressme_id=?;");
			stDel.setInt(1, activeChar.getObjectId());
			stDel.setInt(2, TpNameDell);
			stDel.execute();
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * @param activeChar
	 * @param TemplateName
	 */
	private void saveDressMe(L2PcInstance activeChar, String DressMeName)
	{
		if (DressMeName.equals("") || DressMeName.equals(null))
		{
			activeChar.sendMessage("You didn't enter a template name.");
			sendMainWindow(activeChar);
			return;
		}
		
		if (activeChar.isInCombat() || activeChar.isDead() || activeChar.isAlikeDead() || activeChar.isCastingNow() || activeChar.isAttackingNow())
		{
			activeChar.sendMessage("You can't to save a template in your status. Casting || Dead || Attacking || Combat.");
			sendMainWindow(activeChar);
			return;
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM dressme WHERE charId=?;");
			st.setLong(1, activeChar.getObjectId());
			ResultSet rs = st.executeQuery();
			rs.next();
			if (rs.getInt(1) < Config.DRESS_ME_MAXIMUM_TEMPLATES)
			{
				PreparedStatement st1 = con.prepareStatement("SELECT COUNT(*) FROM dressme WHERE charId=? AND name=?;");
				st1.setLong(1, activeChar.getObjectId());
				st1.setString(2, DressMeName);
				ResultSet rs1 = st1.executeQuery();
				rs1.next();
				if (rs1.getInt(1) == 0)
				{
					PreparedStatement stAdd = con.prepareStatement("INSERT INTO dressme (charId,agathion,boots,chest,cloak,gloves,height,legs,radius,transform,weapon,name) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
					stAdd.setInt(1, activeChar.getObjectId());
					stAdd.setInt(2, activeChar.getDressMeData().getAgathionId());
					stAdd.setInt(3, activeChar.getDressMeData().getBootsId());
					stAdd.setInt(4, activeChar.getDressMeData().getChestId());
					stAdd.setInt(5, activeChar.getDressMeData().getCloakId());
					stAdd.setInt(6, activeChar.getDressMeData().getGlovesId());
					stAdd.setDouble(7, activeChar.getDressMeData().getHeight());
					stAdd.setInt(8, activeChar.getDressMeData().getLegsId());
					stAdd.setDouble(9, activeChar.getDressMeData().getRadius());
					stAdd.setInt(10, activeChar.getDressMeData().getTransformId());
					stAdd.setInt(11, activeChar.getDressMeData().getWeapId());
					stAdd.setString(12, DressMeName);
					stAdd.execute();
				}
				else
				{
					PreparedStatement stAdd = con.prepareStatement("UPDATE dressme SET agathion=?,boots=?,chest=?,cloak=?,gloves=?,height=?,legs=?,radius=?,transform=?,weapon=? WHERE charId=? AND name=?;");
					stAdd.setInt(1, activeChar.getObjectId());
					stAdd.setInt(2, activeChar.getDressMeData().getAgathionId());
					stAdd.setInt(3, activeChar.getDressMeData().getBootsId());
					stAdd.setInt(4, activeChar.getDressMeData().getChestId());
					stAdd.setInt(5, activeChar.getDressMeData().getCloakId());
					stAdd.setInt(6, activeChar.getDressMeData().getGlovesId());
					stAdd.setDouble(7, activeChar.getDressMeData().getHeight());
					stAdd.setInt(8, activeChar.getDressMeData().getLegsId());
					stAdd.setDouble(9, activeChar.getDressMeData().getRadius());
					stAdd.setInt(10, activeChar.getDressMeData().getTransformId());
					stAdd.setInt(11, activeChar.getDressMeData().getWeapId());
					stAdd.setString(12, DressMeName);
					stAdd.execute();
				}
			}
			else
			{
				activeChar.sendMessage("You can't save more than " + Config.DRESS_ME_MAXIMUM_TEMPLATES + " templates.");
			}
			
		}
		catch (Exception e)
		{
			// _log.info("Somethink bad with DressMeData.java - save button: " + e);
		}
	}
	
	public static void newDressData(L2PcInstance activeChar)
	{
		if (activeChar.getDressMeData() == null)
		{
			DressMeData dmd = new DressMeData();
			activeChar.setDressMeData(dmd);
		}
	}
	
	public static void untransform(L2PcInstance activeChar)
	{
		DressMeBBSManager.newDressData(activeChar);
		activeChar.getDressMeData().setTransformId(0);
		activeChar.getCollisionHeight();
		activeChar.getCollisionRadius();
		activeChar.setDressMeEnabled(true);
		activeChar.broadcastUserInfo();
		sendTransformMenu(activeChar);
		_transformed = false;
	}
	
	public static void sendTransformMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/setupmytransform.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendAgathionMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/setupmyagathion.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendArmorMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/setupmyarmor.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendCursedWeaponMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/wea_demonic.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendSWeaponMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/wea_s.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendDynWeaponMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/wea_s80_dynasty.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendIcarusWeaponMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/wea_s80_icarus.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendVesperWeaponMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/wea_s84_vesper.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendFreyaWeaponMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/wea_s84_freya.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	public static void sendTriumphWeaponMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(activeChar, "./data/html/CommunityBoard/wea_s84_triumph.htm");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		htm.replace("%playername%", activeChar.getName());
		htm.replace("%servername%", Config.SERVER_NAME);
		htm.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		htm.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		htm.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		htm.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		htm.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		htm.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		htm.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		htm.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		htm.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		htm.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		htm.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		htm.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (final String var : TopBBSManager.VAR_NAMES)
		{
			htm.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(htm.getHtm(), activeChar);
	}
	
	/**
	 * @param activeChar
	 */
	public static void consumeForWeapon(L2PcInstance activeChar)
	{
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_WEAPON_CONSUME_ID) == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_WEAPON_CONSUME_ID).getCount() < Config.DRESS_ME_WEAPON_CONSUME_COUNT)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		activeChar.destroyItemByItemId(".dressme", Config.DRESS_ME_WEAPON_CONSUME_ID, Config.DRESS_ME_WEAPON_CONSUME_COUNT, activeChar, true);
	}
	
	/**
	 * @param activeChar
	 */
	public static void consumeForArmor(L2PcInstance activeChar)
	{
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_ARMOR_CONSUME_ID) == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_ARMOR_CONSUME_ID).getCount() < Config.DRESS_ME_ARMOR_CONSUME_COUNT)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		activeChar.destroyItemByItemId(".dressme", Config.DRESS_ME_ARMOR_CONSUME_ID, Config.DRESS_ME_ARMOR_CONSUME_COUNT, activeChar, true);
	}
	
	/**
	 * @param activeChar
	 */
	public static void consumeForCloak(L2PcInstance activeChar)
	{
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_CLOAK_CONSUME_ID) == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_CLOAK_CONSUME_ID).getCount() < Config.DRESS_ME_CLOAK_CONSUME_COUNT)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		activeChar.destroyItemByItemId(".dressme", Config.DRESS_ME_CLOAK_CONSUME_ID, Config.DRESS_ME_CLOAK_CONSUME_COUNT, activeChar, true);
	}
	
	/**
	 * @param activeChar
	 */
	public static void consumeForAgathion(L2PcInstance activeChar)
	{
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_AGATHION_CONSUME_ID) == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_AGATHION_CONSUME_ID).getCount() < Config.DRESS_ME_AGATHION_CONSUME_COUNT)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		activeChar.destroyItemByItemId(".dressme", Config.DRESS_ME_AGATHION_CONSUME_ID, Config.DRESS_ME_AGATHION_CONSUME_COUNT, activeChar, true);
	}
	
	/**
	 * @param activeChar
	 */
	public static void consumeForTarget(L2PcInstance activeChar)
	{
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_TARGET_CONSUME_ID) == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_TARGET_CONSUME_ID).getCount() < Config.DRESS_ME_TARGET_CONSUME_COUNT)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		activeChar.destroyItemByItemId(".dressme", Config.DRESS_ME_TARGET_CONSUME_ID, Config.DRESS_ME_TARGET_CONSUME_COUNT, activeChar, true);
	}
	
	/**
	 * @param activeChar
	 */
	public static void consumeForTransform(L2PcInstance activeChar)
	{
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_TRANSFORM_CONSUME_ID) == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		if (activeChar.getInventory().getItemByItemId(Config.DRESS_ME_TRANSFORM_CONSUME_ID).getCount() < Config.DRESS_ME_TRANSFORM_CONSUME_COUNT)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			return;
		}
		activeChar.destroyItemByItemId(".dressme", Config.DRESS_ME_TRANSFORM_CONSUME_ID, Config.DRESS_ME_TRANSFORM_CONSUME_COUNT, activeChar, true);
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
	}
}