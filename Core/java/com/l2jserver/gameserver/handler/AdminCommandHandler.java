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
package com.l2jserver.gameserver.handler;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.gameserver.handler.interfaces.IAdminCommandHandler;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminAdmin;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminAnnouncements;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminBBS;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminBan;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminBuffs;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminCTFEngine;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminCache;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminChangeAccessLevel;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminClan;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminCreateItem;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminCursedWeapons;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminDMEngine;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminDebug;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminDelete;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminDisconnect;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminDoorControl;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminEditChar;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminEditNpc;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminEffects;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminElement;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminEnchant;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminEventEngine;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminEvents;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminExpSp;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminFightCalculator;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminFortSiege;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminGB;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminGeoEditor;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminGeodata;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminGm;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminGmChat;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminGraciaSeeds;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminHeal;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminHelpPage;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminInstance;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminInstanceZone;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminInventory;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminInvul;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminKick;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminKill;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminLevel;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminLogin;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminMammon;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminManor;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminMenu;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminMessages;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminMobGroup;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminMonsterRace;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminMonsterRush;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminPForge;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminPathNode;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminPetition;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminPledge;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminPolymorph;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminPremium;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminRepairChar;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminRes;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminRide;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminShop;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminShowQuests;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminShutdown;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminSiege;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminSkill;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminSpawn;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminSummon;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminTarget;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminTeleport;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminTerritoryWar;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminTest;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminTownWar;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminTvTEvent;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminTvTRoundEvent;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminUnblockIp;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminVitality;
import com.l2jserver.scripts.handlers.admincommandhandlers.AdminZone;

/**
 * @author L2jPDT
 */
public class AdminCommandHandler
{
	private static Logger _log = Logger.getLogger(AdminCommandHandler.class.getName());
	private final Map<Integer, IAdminCommandHandler> _datatable = new HashMap<>();
	
	public static AdminCommandHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected AdminCommandHandler()
	{
		registerAdminCommandHandler(new AdminTownWar());
		registerAdminCommandHandler(new AdminCTFEngine());
		registerAdminCommandHandler(new AdminDMEngine());
		registerAdminCommandHandler(new AdminAdmin());
		registerAdminCommandHandler(new AdminAnnouncements());
		registerAdminCommandHandler(new AdminBan());
		registerAdminCommandHandler(new AdminBBS());
		registerAdminCommandHandler(new AdminBuffs());
		registerAdminCommandHandler(new AdminCache());
		registerAdminCommandHandler(new AdminChangeAccessLevel());
		registerAdminCommandHandler(new AdminClan());
		registerAdminCommandHandler(new AdminCreateItem());
		registerAdminCommandHandler(new AdminCursedWeapons());
		registerAdminCommandHandler(new AdminDebug());
		registerAdminCommandHandler(new AdminDelete());
		registerAdminCommandHandler(new AdminDisconnect());
		registerAdminCommandHandler(new AdminDoorControl());
		registerAdminCommandHandler(new AdminEditChar());
		registerAdminCommandHandler(new AdminEditNpc());
		registerAdminCommandHandler(new AdminEffects());
		registerAdminCommandHandler(new AdminElement());
		registerAdminCommandHandler(new AdminEnchant());
		registerAdminCommandHandler(new AdminEventEngine());
		registerAdminCommandHandler(new AdminEvents());
		registerAdminCommandHandler(new AdminExpSp());
		registerAdminCommandHandler(new AdminFightCalculator());
		registerAdminCommandHandler(new AdminFortSiege());
		registerAdminCommandHandler(new AdminGB());
		registerAdminCommandHandler(new AdminGeodata());
		registerAdminCommandHandler(new AdminGeoEditor());
		registerAdminCommandHandler(new AdminGm());
		registerAdminCommandHandler(new AdminGmChat());
		registerAdminCommandHandler(new AdminGraciaSeeds());
		registerAdminCommandHandler(new AdminHeal());
		registerAdminCommandHandler(new AdminHelpPage());
		registerAdminCommandHandler(new AdminInstance());
		registerAdminCommandHandler(new AdminInstanceZone());
		registerAdminCommandHandler(new AdminInventory());
		registerAdminCommandHandler(new AdminInvul());
		registerAdminCommandHandler(new AdminKick());
		registerAdminCommandHandler(new AdminKill());
		registerAdminCommandHandler(new AdminLevel());
		registerAdminCommandHandler(new AdminLogin());
		registerAdminCommandHandler(new AdminMammon());
		registerAdminCommandHandler(new AdminManor());
		registerAdminCommandHandler(new AdminMenu());
		registerAdminCommandHandler(new AdminMessages());
		registerAdminCommandHandler(new AdminMobGroup());
		registerAdminCommandHandler(new AdminMonsterRace());
		registerAdminCommandHandler(new AdminMonsterRush());
		registerAdminCommandHandler(new AdminPathNode());
		registerAdminCommandHandler(new AdminPetition());
		registerAdminCommandHandler(new AdminPremium());
		registerAdminCommandHandler(new AdminPForge());
		registerAdminCommandHandler(new AdminPledge());
		registerAdminCommandHandler(new AdminPolymorph());
		registerAdminCommandHandler(new AdminRepairChar());
		registerAdminCommandHandler(new AdminRes());
		registerAdminCommandHandler(new AdminRide());
		registerAdminCommandHandler(new AdminShop());
		registerAdminCommandHandler(new AdminShowQuests());
		registerAdminCommandHandler(new AdminShutdown());
		registerAdminCommandHandler(new AdminSiege());
		registerAdminCommandHandler(new AdminSkill());
		registerAdminCommandHandler(new AdminSpawn());
		registerAdminCommandHandler(new AdminSummon());
		registerAdminCommandHandler(new AdminTarget());
		registerAdminCommandHandler(new AdminTeleport());
		registerAdminCommandHandler(new AdminTerritoryWar());
		registerAdminCommandHandler(new AdminTest());
		registerAdminCommandHandler(new AdminTvTEvent());
		registerAdminCommandHandler(new AdminTvTRoundEvent());
		registerAdminCommandHandler(new AdminUnblockIp());
		registerAdminCommandHandler(new AdminVitality());
		registerAdminCommandHandler(new AdminZone());
		_log.info(getText("TG9hZGVkIEFkbWluIGhhbmRsZXJzOiA=") + _datatable.size());
	}
	
	public void registerAdminCommandHandler(IAdminCommandHandler handler)
	{
		for (String id : handler.getAdminCommandList())
		{
			_datatable.put(id.hashCode(), handler);
		}
	}
	
	public IAdminCommandHandler getAdminCommandHandler(String adminCommand)
	{
		if (adminCommand.indexOf(" ") != -1)
		{
			adminCommand = adminCommand.substring(0, adminCommand.indexOf(" "));
		}
		return _datatable.get(adminCommand.hashCode());
	}
	
	public int size()
	{
		return _datatable.size();
	}
	
	private static class SingletonHolder
	{
		protected static final AdminCommandHandler _instance = new AdminCommandHandler();
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
}
