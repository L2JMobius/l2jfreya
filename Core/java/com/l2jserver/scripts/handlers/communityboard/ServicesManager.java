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
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.Announcements;
import com.l2jserver.gameserver.datatables.CharNameTable;
import com.l2jserver.gameserver.datatables.ClanTable;
import com.l2jserver.gameserver.handler.interfaces.IBypassHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.PartySmallWindowAll;
import com.l2jserver.gameserver.network.serverpackets.PartySmallWindowDeleteAll;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * @author L2jPDT
 */
public class ServicesManager implements IBypassHandler
{
	// SQL
	private static final String UPDATE_PREMIUMSERVICE = "UPDATE account_premium SET premium_service=?,enddate=? WHERE account_name=?";
	
	// LOGGER
	private static final Logger _log = Logger.getLogger(ServicesManager.class.getName());
	
	private static final String[] COMMANDS =
	{
		"_set_nobles",
		"_change_char_name",
		"_change_clan_name",
		"_set_fames",
		"_set_recs",
		"_set_premium"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (command.startsWith("_set_nobles"))
		{
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_GET_NOBLES_ID) == null)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_GET_NOBLES_ID).getCount() < Config.SERVICES_GET_NOBLES_ID_COUNT)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.isNoble())
			{
				activeChar.sendMessage("Nobles Manager: You already have nobless.");
				return true;
			}
			if (activeChar.getLevel() < Config.SERVICES_CB_NOBLESS_NEED_MIN_LV)
			{
				activeChar.sendMessage("Nobles Manager: Your level must be over " + Config.SERVICES_CB_NOBLESS_NEED_MIN_LV);
				return true;
			}
			
			activeChar.destroyItemByItemId("ShopBBS", Config.SERVICES_GET_NOBLES_ID, Config.SERVICES_GET_NOBLES_ID_COUNT, activeChar, true);
			Announcements.getInstance().announceToAll("Server Information: Player " + activeChar.getName() + " just buy Nobless.", true);
			activeChar.sendMessage("Nobles Manager: You nobless Congratulation.");
			activeChar.setNoble(true);
			activeChar.broadcastUserInfo();
		}
		else if (command.startsWith("_change_char_name save "))
		{
			String[] params = command.split(" ");
			if (params.length < 3)
			{
				activeChar.sendMessage("Rename Manager: Cannot complete, please enter a name!");
				return true;
			}
			String new_name = params[2];
			
			if ((new_name.length() < 1) || (new_name.length() > 16))
			{
				activeChar.sendMessage("Rename Manager: Incorrect name between 1-16 characters.");
				return true;
			}
			if (Config.FORBIDDEN_NAMES.length > 1)
			{
				for (String st : Config.FORBIDDEN_NAMES)
				{
					if (new_name.toLowerCase().contains(st.toLowerCase()))
					{
						activeChar.sendMessage("Rename Manager: Please choose another name, because is forbidden.");
						return true;
					}
				}
			}
			if (!Util.isAlphaNumeric(new_name) || !Util.isValidName(new_name))
			{
				activeChar.sendMessage("Rename Manager: Please choose another name.");
				return true;
			}
			if (activeChar.getLevel() < Config.SERVICES_CHANGE_NAME_MIN_LV)
			{
				activeChar.sendMessage("Rename Manager: Your level is too low. You need to be " + Config.SERVICES_CHANGE_NAME_MIN_LV + " Lv.");
				return true;
			}
			if ((Config.SERVICES_CHANGE_NAME_NEED_PREMIUM) && (activeChar.getPremiumService() == 0))
			{
				activeChar.sendMessage("Rename Manager: You must be a premium player.");
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_CHANGE_NAME_ID) == null)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_CHANGE_NAME_ID).getCount() < Config.SERVICES_CHANGE_NAME_ID_COUNT)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (CharNameTable.getInstance().getIdByName(new_name) > 0)
			{
				activeChar.sendMessage("Rename Manager: Warning, name " + new_name + " already exists.");
				return true;
			}
			activeChar.destroyItemByItemId("Name Change", Config.SERVICES_CHANGE_NAME_ID, Config.SERVICES_CHANGE_NAME_ID_COUNT, activeChar, true);
			Announcements.getInstance().announceToAll("Server Information: Player " + activeChar.getName() + " just renamed to " + new_name, true);
			activeChar.setName(new_name);
			activeChar.getAppearance().setVisibleName(new_name);
			activeChar.store();
			activeChar.sendMessage("Rename Manager: Your name has been changed to " + new_name);
			activeChar.broadcastUserInfo();
			if (activeChar.isInParty())
			{
				activeChar.getParty().broadcastToPartyMembers(activeChar, new PartySmallWindowDeleteAll());
				for (L2PcInstance member : activeChar.getParty().getPartyMembers())
				{
					if (member != activeChar)
					{
						member.sendPacket(new PartySmallWindowAll(member, activeChar.getParty()));
					}
				}
			}
			if (activeChar.getClan() != null)
			{
				activeChar.getClan().broadcastClanStatus();
			}
		}
		else if (command.startsWith("_change_clan_name save "))
		{
			String[] params = command.split(" ");
			if (params.length < 3)
			{
				activeChar.sendMessage("Rename Manager: Cannot complete, please enter a name with 3 minimum characters.");
				return true;
			}
			String new_name = params[2];
			
			if ((new_name.length() < 1) || (new_name.length() > 16))
			{
				activeChar.sendMessage("Rename Manager: Incorrect name between 1-16 characters.");
				return true;
			}
			if (Config.FORBIDDEN_NAMES.length > 1)
			{
				for (String st : Config.FORBIDDEN_NAMES)
				{
					if (new_name.toLowerCase().contains(st.toLowerCase()))
					{
						activeChar.sendMessage("Rename Manager: Please choose another name, because is forbidden.");
						return true;
					}
				}
			}
			if (!Util.isAlphaNumeric(new_name) || !Util.isValidName(new_name))
			{
				activeChar.sendMessage("Rename Manager: Please choose another name.");
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_CLAN_CHANGE_NAME_ID) == null)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_CLAN_CHANGE_NAME_ID).getCount() < Config.SERVICES_CLAN_CHANGE_NAME_ID_COUNT)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if ((activeChar.getClan() == null) || !activeChar.isClanLeader())
			{
				activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return true;
			}
			if (activeChar.getClan().getLevel() < Config.SERVICES_CLAN_CHANGE_MIN_LV)
			{
				activeChar.sendMessage("Rename Manager: You clan must be minimum " + Config.SERVICES_CLAN_CHANGE_MIN_LV + "lv.");
				return true;
			}
			if (!Util.isAlphaNumeric(new_name))
			{
				activeChar.sendPacket(SystemMessageId.CLAN_NAME_INCORRECT);
				return true;
			}
			if (ClanTable.getInstance().getClanByName(new_name) != null)
			{
				activeChar.sendMessage("Rename Manager: Warning, clan name " + new_name + " already exists.");
				return true;
			}
			activeChar.destroyItemByItemId("Clan Name Change", Config.SERVICES_CLAN_CHANGE_NAME_ID, Config.SERVICES_CLAN_CHANGE_NAME_ID_COUNT, activeChar, true);
			Announcements.getInstance().announceToAll("Server Information: Clan " + activeChar.getClan().getName() + " just renamed to " + new_name, true);
			activeChar.getClan().setName(new_name);
			activeChar.getClan().updateClanNameInDB();
			activeChar.sendMessage("Rename Manager: Your clan name has been changed to " + new_name);
			activeChar.broadcastUserInfo();
			if (activeChar.isInParty())
			{
				activeChar.getParty().broadcastToPartyMembers(activeChar, new PartySmallWindowDeleteAll());
				for (L2PcInstance member : activeChar.getParty().getPartyMembers())
				{
					if (member != activeChar)
					{
						member.sendPacket(new PartySmallWindowAll(member, activeChar.getParty()));
					}
				}
			}
			if (activeChar.getClan() != null)
			{
				activeChar.getClan().broadcastClanStatus();
			}
		}
		else if (command.startsWith("_set_fames save "))
		{
			String[] params = command.split(" ");
			int value = Integer.parseInt(params[2]);
			
			if (params.length < 3)
			{
				activeChar.sendMessage("Fame Manager: Cannot complete, please enter a value!");
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_GET_FAMES_ID) == null)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_GET_FAMES_ID).getCount() < Config.SERVICES_GET_FAMES_ID_COUNT)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getRecomHave() == Config.MAX_PERSONAL_FAME_POINTS)
			{
				activeChar.sendMessage("Fame Manager: You reached maximum fames.");
				return true;
			}
			
			if (activeChar.getLevel() < Config.SERVICES_FAMES_MIN_LV)
			{
				activeChar.sendMessage("Fame Manager: Your level is too low. You need to be " + Config.SERVICES_FAMES_MIN_LV + " Lv.");
				return true;
			}
			if ((Config.SERVICES_FAMES_NEED_PREMIUM) && (activeChar.getPremiumService() == 0))
			{
				activeChar.sendMessage("Fame Manager: You must be a premium player.");
				return true;
			}
			
			activeChar.destroyItemByItemId("Fame Manager", Config.SERVICES_GET_FAMES_ID, Config.SERVICES_GET_FAMES_ID_COUNT * value, activeChar, true);
			activeChar.setFame(activeChar.getFame() + value);
			activeChar.store();
			Announcements.getInstance().announceToAll("Server Information: Player " + activeChar.getName() + " just increased Fame Points to " + activeChar.getFame(), true);
			activeChar.sendMessage("Fame Manager: Your Fame Points has been increased to " + activeChar.getFame());
			activeChar.broadcastUserInfo();
		}
		else if (command.startsWith("_set_recs save "))
		{
			String[] params = command.split(" ");
			int value = Integer.parseInt(params[2]);
			
			if (params.length < 3)
			{
				activeChar.sendMessage("Recommendation Manager: Cannot complete, please enter a value!");
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_GET_RECS_ID) == null)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_GET_RECS_ID).getCount() < Config.SERVICES_GET_RECS_ID_COUNT)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getRecomHave() == 255)
			{
				activeChar.sendMessage("Recommendation Manager: You reached maximum recommendations.");
				return true;
			}
			if (activeChar.getLevel() < Config.SERVICES_RECS_MIN_LV)
			{
				activeChar.sendMessage("Recommendation Manager: Your level is too low. You need to be " + Config.SERVICES_RECS_MIN_LV + " Lv.");
				return true;
			}
			if ((Config.SERVICES_RECS_NEED_PREMIUM) && (activeChar.getPremiumService() == 0))
			{
				activeChar.sendMessage("Recommendation Manager: You must be a premium player.");
				return true;
			}
			activeChar.destroyItemByItemId("Recommendation Manager", Config.SERVICES_GET_RECS_ID, Config.SERVICES_GET_RECS_ID_COUNT * value, activeChar, true);
			activeChar.setRecomHave(activeChar.getRecomHave() + value);
			activeChar.store();
			Announcements.getInstance().announceToAll("Server Information: Player " + activeChar.getName() + " just increased Fame Points to " + activeChar.getRecomHave(), true);
			activeChar.sendMessage("Recommendation Manager: Your Fame Points has been increased to " + activeChar.getRecomHave());
			activeChar.broadcastUserInfo();
		}
		else if (command.startsWith("_set_premium save "))
		{
			String[] params = command.split(" ");
			int value = Integer.parseInt(params[2]);
			if (params.length < 3)
			{
				activeChar.sendMessage("Premium Manager: Cannot complete, please enter a value!");
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_GET_PREMIUM_ID) == null)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getInventory().getItemByItemId(Config.SERVICES_GET_PREMIUM_ID).getCount() < (value * Config.SERVICES_GET_PREMIUM_ID_COUNT))
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
				return true;
			}
			if (activeChar.getPremiumService() == 1)
			{
				activeChar.sendMessage("Premium Manager: You are already premium player. Just comeback later.");
				return true;
			}
			activeChar.destroyItemByItemId("Premium Manager", Config.SERVICES_GET_PREMIUM_ID, value * Config.SERVICES_GET_PREMIUM_ID_COUNT, activeChar, true);
			addPremiumServices(value, activeChar.getAccountName());
			activeChar.sendMessage("Get Premium: " + value + " months and acc: " + activeChar.getAccountName() + " Im premium? :" + activeChar.isPremiumAccount());
			Announcements.getInstance().announceToAll("Server Information: Player " + activeChar.getName() + " just received premium account status for " + value + " Month(s).", true);
			activeChar.sendMessage("Premium Manager: Your Premium Status is activated. Relog your character for apply effects.");
			activeChar.broadcastUserInfo();
		}
		return false;
	}
	
	private void addPremiumServices(int Months, String AccName)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(UPDATE_PREMIUMSERVICE))
		{
			Calendar finishtime = Calendar.getInstance();
			finishtime.setTimeInMillis(System.currentTimeMillis());
			finishtime.set(Calendar.SECOND, 0);
			finishtime.add(Calendar.MONTH, Months);
			
			statement.setInt(1, 1);
			statement.setLong(2, finishtime.getTimeInMillis());
			statement.setString(3, AccName);
			statement.execute();
		}
		catch (SQLException e)
		{
			_log.info("PremiumService:  Could not increase data");
		}
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}