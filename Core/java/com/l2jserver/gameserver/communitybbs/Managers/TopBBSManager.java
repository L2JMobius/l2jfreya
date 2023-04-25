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

import java.util.StringTokenizer;

import com.l2jserver.Config;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.variables.PlayerVariables;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.ShowBoard;
import com.l2jserver.util.Rnd;

public class TopBBSManager extends BaseBBSManager
{
	public static TopBBSManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	// VARS
	private static final String PARTY_REFUSE = "@PARTY_REFUSE";
	private static final String TRADE_REFUSE = "@TRADE_REFUSE";
	private static final String BUFFS_REFUSE = "@BUFFS_REFUSE";
	private static final String PM_REFUSE = "@PM_REFUSE";
	private static final String GAIN_XP_SP = "@GAIN_XP_SP";
	private static final String AUTO_LOOT = "@AUTO_LOOT";
	private static final String AUTO_SS = "@AUTO_SS";
	private static final String RED_SKY_DIE = "@RED_SKY_DIE";
	private static final String AUTO_TOD = "@AUTO_TOD";
	public static final String[] VAR_NAMES =
	{
		PARTY_REFUSE,
		TRADE_REFUSE,
		BUFFS_REFUSE,
		PM_REFUSE,
		PM_REFUSE,
		GAIN_XP_SP,
		GAIN_XP_SP,
		AUTO_LOOT,
		AUTO_SS,
		RED_SKY_DIE,
		AUTO_TOD
	};
	
	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
		PlayerVariables vars = activeChar.getVariables();
		int traders = getTraders();
		int onlineplayers = getOnline();
		
		if (command.equals("_bbstop") || command.equals("_bbshome"))
		{
			showMain(activeChar);
		}
		else if (command.startsWith("_bbstop;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			String string = st.nextToken();
			NpcHtmlMessage html = new NpcHtmlMessage(5);
			html.setFile(activeChar, "data/html/CommunityBoard/" + string + ".htm");
			html.replace("%playername%", activeChar.getName());
			html.replace("%servername%", Config.SERVER_NAME);
			html.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
			html.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
			html.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
			html.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
			html.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
			html.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
			html.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
			html.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
			html.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
			html.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
			html.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
			html.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
			for (String var : TopBBSManager.VAR_NAMES)
			{
				html.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
			}
			BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
		}
		else if (command.startsWith("_bbstopchange"))
		{
			String varName = command.split(" ")[1];
			switch (varName)
			{
				case "@PARTY_REFUSE":
				{
					vars.set(varName, !vars.getBoolean(varName, false));
					if (vars.getBoolean("@PARTY_REFUSE"))
					{
						activeChar.sendMessage("Service Manager: You activated party refusal.");
						break;
					}
					activeChar.sendMessage("Service Manager: You can get request party from any character.");
					break;
				}
				case "@TRADE_REFUSE":
				{
					if (activeChar.getTradeRefusal())
					{
						activeChar.setTradeRefusal(false);
						activeChar.sendMessage("Service Manager: Trade refusal disabled.");
						vars.set("@TRADE_REFUSE", false);
						break;
					}
					activeChar.setTradeRefusal(true);
					activeChar.sendMessage("Service Manager: You can get request trade from any character.");
					vars.set("@TRADE_REFUSE", true);
					break;
				}
				case "@BUFFS_REFUSE":
				{
					vars.set(varName, !vars.getBoolean(varName, false));
					if (vars.getBoolean("@BUFFS_REFUSE"))
					{
						activeChar.sendMessage("Service Manager: From now you cannot get any buff from players.");
						break;
					}
					activeChar.sendMessage("Service Manager: You can get any buff from players.");
					break;
				}
				case "@PM_REFUSE":
				{
					if (activeChar.isSilenceMode())
					{
						activeChar.setSilenceMode(false);
						activeChar.sendMessage("Service Manager: Message acceptance mode.");
						vars.set("@PM_REFUSE", false);
						break;
					}
					activeChar.setSilenceMode(true);
					activeChar.sendMessage("Service Manager: Message refusal mode.");
					vars.set("@PM_REFUSE", true);
					break;
				}
				case "@GAIN_XP_SP":
				{
					if (activeChar.isGainXp())
					{
						activeChar.setGainXp(false);
						vars.set("@GAIN_XP_SP", false);
						activeChar.sendMessage("Service Manager: You will not get any exp.");
						break;
					}
					activeChar.setGainXp(true);
					vars.set("@GAIN_XP_SP", true);
					activeChar.sendMessage("Service Manager: You get all exp.");
					break;
				}
				case "@AUTO_LOOT":
				{
					vars.set(varName, !vars.getBoolean(varName, false));
					if (vars.getBoolean("@AUTO_LOOT"))
					{
						activeChar.sendMessage("Service Manager: You activated auto loot items.");
						break;
					}
					activeChar.sendMessage("Service Manager: You deactivated auto loot items.");
					break;
				}
				case "@AUTO_SS":
				{
					vars.set(varName, !vars.getBoolean(varName, false));
					if (vars.getBoolean("@AUTO_SS"))
					{
						activeChar.sendMessage("Service Manager: You activated auto shots on enter world.");
						break;
					}
					activeChar.sendMessage("Service Manager: You deactivated auto shots on enter world.");
					break;
				}
				case "@RED_SKY_DIE":
				{
					vars.set(varName, !vars.getBoolean(varName, false));
					if (vars.getBoolean("@RED_SKY_DIE"))
					{
						activeChar.sendMessage("Service Manager: You activated red sky on your death.");
						break;
					}
					activeChar.sendMessage("Service Manager: You deactivated red sky on your death.");
					break;
				}
				case "@AUTO_TOD":
				{
					long items_count = activeChar.getInventory().getInventoryItemCount(9599, 0);
					if (items_count == 0L)
					{
						activeChar.sendMessage("Service Manager: You dont have any Ancient Tome of the Demons.");
						break;
					}
					if (activeChar.getInventory().getInventoryItemCount(9599, 0) < items_count)
					{
						break;
					}
					int a = 0;
					int b = 0;
					int c = 0;
					for (int i = 0; i < items_count; ++i)
					{
						int rnd = Rnd.get(100);
						if ((rnd <= 100) && (rnd > 44))
						{
							++a;
						}
						else if ((rnd <= 44) && (rnd > 14))
						{
							++b;
						}
						else if (rnd <= 14)
						{
							++c;
						}
					}
					if (!activeChar.destroyItemByItemId("ATOD", 9599, a + b + c, null, true))
					{
						activeChar.sendMessage("Service Manager: You dont have any Ancient Tome of the Demons.");
						break;
					}
					if (a > 0)
					{
						activeChar.addItem("ATOD", 9600, a, null, true);
					}
					if (b > 0)
					{
						activeChar.addItem("ATOD", 9601, b, null, true);
					}
					if (c > 0)
					{
						activeChar.addItem("ATOD", 9602, c, null, true);
						break;
					}
					break;
				}
			}
			TopBBSManager.getInstance().showMain(activeChar);
		}
		else if (command.startsWith("_bbsAugment;add"))
		{
			sendHtm(activeChar, "data/html/CommunityBoard/7.htm");
		}
		else if (command.startsWith("_bbsAugment;remove"))
		{
			sendHtm(activeChar, "data/html/CommunityBoard/7.htm");
		}
		else
		{
			ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + command + " is not implemented yet</center><br><br></body></html>", "101");
			activeChar.sendPacket(sb);
			activeChar.sendPacket(new ShowBoard(null, "102"));
			activeChar.sendPacket(new ShowBoard(null, "103"));
		}
	}
	
	public void showMain(L2PcInstance activeChar)
	{
		PlayerVariables vars = activeChar.getVariables();
		int traders = getTraders();
		int onlineplayers = getOnline();
		NpcHtmlMessage html = new NpcHtmlMessage(5);
		html.setFile(activeChar, "data/html/CommunityBoard/index.htm");
		html.replace("%playername%", activeChar.getName());
		html.replace("%servername%", Config.SERVER_NAME);
		html.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
		html.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
		html.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
		html.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
		html.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
		html.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
		html.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
		html.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
		html.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
		html.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
		html.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
		html.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		for (String var : TopBBSManager.VAR_NAMES)
		{
			html.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
		}
		BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
	}
	
	public static int getOnline()
	{
		return L2World.getInstance().getAllPlayersCount();
	}
	
	public static int getTraders()
	{
		return (int) L2World.getInstance().getAllPlayers().values().stream().filter(player -> player.getPrivateStoreType() != 0).count();
	}
	
	private boolean sendHtm(L2PcInstance player, String path)
	{
		String oriPath = path;
		if ((player.getLang() != null) && !player.getLang().equalsIgnoreCase("en") && path.contains("html/"))
		{
			path = path.replace("html/", "html-" + player.getLang() + "/");
		}
		String content = HtmCache.getInstance().getHtm(path);
		if ((content == null) && !oriPath.equals(path))
		{
			content = HtmCache.getInstance().getHtm(oriPath);
		}
		if (content == null)
		{
			return false;
		}
		BaseBBSManager.separateAndSend(content, player);
		return true;
	}
	
	private static class SingletonHolder
	{
		protected static TopBBSManager _instance = new TopBBSManager();
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
	}
}
