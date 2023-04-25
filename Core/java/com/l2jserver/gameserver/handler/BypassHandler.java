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

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.interfaces.IBypassHandler;
import com.l2jserver.scripts.handlers.bypasshandlers.Augment;
import com.l2jserver.scripts.handlers.bypasshandlers.BuffShop;
import com.l2jserver.scripts.handlers.bypasshandlers.Buy;
import com.l2jserver.scripts.handlers.bypasshandlers.BuyShadowItem;
import com.l2jserver.scripts.handlers.bypasshandlers.CPRecovery;
import com.l2jserver.scripts.handlers.bypasshandlers.ChatLink;
import com.l2jserver.scripts.handlers.bypasshandlers.ClanWarehouse;
import com.l2jserver.scripts.handlers.bypasshandlers.DrawHenna;
import com.l2jserver.scripts.handlers.bypasshandlers.DressMeBypasses;
import com.l2jserver.scripts.handlers.bypasshandlers.Festival;
import com.l2jserver.scripts.handlers.bypasshandlers.FortSiege;
import com.l2jserver.scripts.handlers.bypasshandlers.ItemAuctionLink;
import com.l2jserver.scripts.handlers.bypasshandlers.Link;
import com.l2jserver.scripts.handlers.bypasshandlers.Loto;
import com.l2jserver.scripts.handlers.bypasshandlers.ManorManager;
import com.l2jserver.scripts.handlers.bypasshandlers.Multisell;
import com.l2jserver.scripts.handlers.bypasshandlers.Observation;
import com.l2jserver.scripts.handlers.bypasshandlers.OlympiadManagerLink;
import com.l2jserver.scripts.handlers.bypasshandlers.OlympiadObservation;
import com.l2jserver.scripts.handlers.bypasshandlers.PlayerHelp;
import com.l2jserver.scripts.handlers.bypasshandlers.PrivateWarehouse;
import com.l2jserver.scripts.handlers.bypasshandlers.QuestLink;
import com.l2jserver.scripts.handlers.bypasshandlers.QuestList;
import com.l2jserver.scripts.handlers.bypasshandlers.ReceivePremium;
import com.l2jserver.scripts.handlers.bypasshandlers.ReleaseAttribute;
import com.l2jserver.scripts.handlers.bypasshandlers.RemoveDeathPenalty;
import com.l2jserver.scripts.handlers.bypasshandlers.RemoveHennaList;
import com.l2jserver.scripts.handlers.bypasshandlers.RentPet;
import com.l2jserver.scripts.handlers.bypasshandlers.RideWyvern;
import com.l2jserver.scripts.handlers.bypasshandlers.Rift;
import com.l2jserver.scripts.handlers.bypasshandlers.SkillList;
import com.l2jserver.scripts.handlers.bypasshandlers.SupportBlessing;
import com.l2jserver.scripts.handlers.bypasshandlers.SupportMagic;
import com.l2jserver.scripts.handlers.bypasshandlers.TerritoryStatus;
import com.l2jserver.scripts.handlers.bypasshandlers.TerritoryWar;
import com.l2jserver.scripts.handlers.bypasshandlers.VoiceCommand;
import com.l2jserver.scripts.handlers.bypasshandlers.Wear;
import com.l2jserver.scripts.handlers.communityboard.AdminControl;
import com.l2jserver.scripts.handlers.communityboard.ServicesManager;
import com.l2jserver.scripts.handlers.communityboard.ServicesManagerHTML;
import com.l2jserver.scripts.handlers.communityboard.Statistics;

/**
 * @author L2jPDT
 */
public class BypassHandler
{
	private static Logger _log = Logger.getLogger(BypassHandler.class.getName());
	private final Map<Integer, IBypassHandler> _datatable = new HashMap<>();
	
	public static BypassHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected BypassHandler()
	{
		// Community Board
		registerBypassHandler(new AdminControl());
		registerBypassHandler(new ServicesManager());
		registerBypassHandler(new ServicesManagerHTML());
		registerBypassHandler(new Statistics());
		// Continue with BypassHandler
		registerBypassHandler(new Augment());
		registerBypassHandler(new BuffShop());
		registerBypassHandler(new Buy());
		registerBypassHandler(new BuyShadowItem());
		registerBypassHandler(new ChatLink());
		registerBypassHandler(new ClanWarehouse());
		registerBypassHandler(new CPRecovery());
		registerBypassHandler(new DrawHenna());
		registerBypassHandler(new Festival());
		registerBypassHandler(new FortSiege());
		registerBypassHandler(new ItemAuctionLink());
		registerBypassHandler(new Link());
		registerBypassHandler(new Loto());
		registerBypassHandler(new ManorManager());
		registerBypassHandler(new Multisell());
		registerBypassHandler(new Observation());
		registerBypassHandler(new OlympiadObservation());
		registerBypassHandler(new OlympiadManagerLink());
		registerBypassHandler(new QuestLink());
		registerBypassHandler(new PlayerHelp());
		registerBypassHandler(new PrivateWarehouse());
		registerBypassHandler(new QuestList());
		registerBypassHandler(new ReceivePremium());
		registerBypassHandler(new ReleaseAttribute());
		registerBypassHandler(new RemoveDeathPenalty());
		registerBypassHandler(new RemoveHennaList());
		registerBypassHandler(new RentPet());
		registerBypassHandler(new RideWyvern());
		registerBypassHandler(new Rift());
		registerBypassHandler(new SkillList());
		registerBypassHandler(new SupportBlessing());
		registerBypassHandler(new SupportMagic());
		registerBypassHandler(new TerritoryStatus());
		registerBypassHandler(new TerritoryWar());
		registerBypassHandler(new VoiceCommand());
		registerBypassHandler(new Wear());
		if (Config.ALLOW_DRESS_ME_SYSTEM)
		{
			registerBypassHandler(new DressMeBypasses());
		}
		_log.info(getText("TG9hZGVkIEJ5cGFzcyBoYW5kbGVyczog") + _datatable.size());
	}
	
	public void registerBypassHandler(IBypassHandler handler)
	{
		for (String element : handler.getBypassList())
		{
			_datatable.put(element.toLowerCase().hashCode(), handler);
		}
	}
	
	public IBypassHandler getBypassHandler(String BypassCommand)
	{
		if (BypassCommand.indexOf(" ") != -1)
		{
			BypassCommand = BypassCommand.substring(0, BypassCommand.indexOf(" "));
		}
		return _datatable.get(BypassCommand.toLowerCase().hashCode());
	}
	
	public int size()
	{
		return _datatable.size();
	}
	
	private static class SingletonHolder
	{
		protected static final BypassHandler _instance = new BypassHandler();
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
}