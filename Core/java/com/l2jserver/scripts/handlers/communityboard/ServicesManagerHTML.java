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

import com.l2jserver.Config;
import com.l2jserver.gameserver.communitybbs.Managers.BaseBBSManager;
import com.l2jserver.gameserver.communitybbs.Managers.TopBBSManager;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.handler.interfaces.IBypassHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.variables.PlayerVariables;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author L2jPDT
 */
public class ServicesManagerHTML implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"_show_nobles",
		"_show_change_char_name",
		"_show_change_clan_name",
		"_show_get_fames",
		"_show_get_recs",
		"_show_premium",
		"_bbstopchange"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		PlayerVariables vars = activeChar.getVariables();
		int traders = TopBBSManager.getTraders();
		int onlineplayers = TopBBSManager.getOnline();
		
		if (command.equals("_show_nobles"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(5);
			html.setFile(activeChar, "data/html/CommunityBoard/services_nobless.htm");
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
			// OTHERS Replaces
			html.replace("%COUNT%", "<font color=LEVEL>" + Config.SERVICES_GET_NOBLES_ID_COUNT + " </font>");
			html.replace("%ID%", "<font color=LEVEL>" + ItemTable.getInstance().createDummyItem(Config.SERVICES_GET_NOBLES_ID).getItemName() + " </font>");
			html.replace("%MIN_LV%", "<font color=LEVEL>" + Config.SERVICES_CB_NOBLESS_NEED_MIN_LV + " </font>");
			html.replace("%FOR_PREMIUM%", "<font color=LEVEL>" + Config.SERVICES_CB_NOBLESS_NEED_PREMIUM + " </font>");
			BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
		}
		else if (command.equals("_show_change_char_name"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(5);
			html.setFile(activeChar, "data/html/CommunityBoard/services_char_name.htm");
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
			// OTHERS Replaces
			html.replace("%COUNT%", "<font color=LEVEL>" + Config.SERVICES_CHANGE_NAME_ID_COUNT + " </font>");
			html.replace("%ID%", "<font color=LEVEL>" + ItemTable.getInstance().createDummyItem(Config.SERVICES_CHANGE_NAME_ID).getItemName() + " </font>");
			html.replace("%MIN_LV%", "<font color=LEVEL>" + Config.SERVICES_CHANGE_NAME_MIN_LV + " </font>");
			html.replace("%FOR_PREMIUM%", "<font color=LEVEL>" + Config.SERVICES_CHANGE_NAME_NEED_PREMIUM + " </font>");
			BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
		}
		else if (command.equals("_show_change_clan_name"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(5);
			html.setFile(activeChar, "data/html/CommunityBoard/services_clan_name.htm");
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
			// OTHERS Replaces
			html.replace("%COUNT%", "<font color=LEVEL>" + Config.SERVICES_CLAN_CHANGE_NAME_ID_COUNT + " </font>");
			html.replace("%ID%", "<font color=LEVEL>" + ItemTable.getInstance().createDummyItem(Config.SERVICES_CLAN_CHANGE_NAME_ID).getItemName() + " </font>");
			html.replace("%CLAN_MIN_LV%", "<font color=LEVEL>" + Config.SERVICES_CLAN_CHANGE_MIN_LV + " </font>");
			BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
		}
		else if (command.equals("_show_get_fames"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(5);
			html.setFile(activeChar, "data/html/CommunityBoard/services_get_fames.htm");
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
			// OTHERS Replaces
			html.replace("%COUNT%", "<font color=LEVEL>" + Config.SERVICES_GET_FAMES_ID_COUNT + " </font>");
			html.replace("%ID%", "<font color=LEVEL>" + ItemTable.getInstance().createDummyItem(Config.SERVICES_GET_FAMES_ID).getItemName() + " </font>");
			html.replace("%MIN_LV%", "<font color=LEVEL>" + Config.SERVICES_FAMES_MIN_LV + " </font>");
			html.replace("%FOR_PREMIUM%", "<font color=LEVEL>" + Config.SERVICES_FAMES_NEED_PREMIUM + " </font>");
			BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
		}
		else if (command.startsWith("_show_get_recs"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(5);
			html.setFile(activeChar, "data/html/CommunityBoard/services_get_recs.htm");
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
			// OTHERS Replaces
			html.replace("%COUNT%", "<font color=LEVEL>" + Config.SERVICES_GET_RECS_ID_COUNT + " </font>");
			html.replace("%ID%", "<font color=LEVEL>" + ItemTable.getInstance().createDummyItem(Config.SERVICES_GET_RECS_ID).getItemName() + " </font>");
			html.replace("%MIN_LV%", "<font color=LEVEL>" + Config.SERVICES_RECS_MIN_LV + " </font>");
			html.replace("%FOR_PREMIUM%", "<font color=LEVEL>" + Config.SERVICES_RECS_NEED_PREMIUM + " </font>");
			BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
		}
		else if (command.startsWith("_show_premium"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(5);
			html.setFile(activeChar, "data/html/CommunityBoard/services_premium.htm");
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
			// OTHERS Replaces
			html.replace("%COUNT%", "<font color=LEVEL>" + Config.SERVICES_GET_PREMIUM_ID_COUNT + " </font>");
			html.replace("%ID%", "<font color=LEVEL>" + ItemTable.getInstance().createDummyItem(Config.SERVICES_GET_PREMIUM_ID).getItemName() + " </font>");
			BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}