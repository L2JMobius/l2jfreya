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

import com.l2jserver.gameserver.handler.interfaces.IItemHandler;
import com.l2jserver.gameserver.templates.item.L2EtcItem;
import com.l2jserver.scripts.handlers.itemhandlers.BeastSoulShot;
import com.l2jserver.scripts.handlers.itemhandlers.BeastSpice;
import com.l2jserver.scripts.handlers.itemhandlers.BeastSpiritShot;
import com.l2jserver.scripts.handlers.itemhandlers.BlessedSpiritShot;
import com.l2jserver.scripts.handlers.itemhandlers.Book;
import com.l2jserver.scripts.handlers.itemhandlers.Disguise;
import com.l2jserver.scripts.handlers.itemhandlers.Elixir;
import com.l2jserver.scripts.handlers.itemhandlers.EnchantAttribute;
import com.l2jserver.scripts.handlers.itemhandlers.EnchantScrolls;
import com.l2jserver.scripts.handlers.itemhandlers.EnergyStarStone;
import com.l2jserver.scripts.handlers.itemhandlers.EventItem;
import com.l2jserver.scripts.handlers.itemhandlers.ExtractableItems;
import com.l2jserver.scripts.handlers.itemhandlers.FishShots;
import com.l2jserver.scripts.handlers.itemhandlers.Harvester;
import com.l2jserver.scripts.handlers.itemhandlers.ItemSkills;
import com.l2jserver.scripts.handlers.itemhandlers.ItemSkillsTemplate;
import com.l2jserver.scripts.handlers.itemhandlers.ManaPotion;
import com.l2jserver.scripts.handlers.itemhandlers.Maps;
import com.l2jserver.scripts.handlers.itemhandlers.MercTicket;
import com.l2jserver.scripts.handlers.itemhandlers.NevitHourglass;
import com.l2jserver.scripts.handlers.itemhandlers.NicknameColor;
import com.l2jserver.scripts.handlers.itemhandlers.PaganKeys;
import com.l2jserver.scripts.handlers.itemhandlers.PetFood;
import com.l2jserver.scripts.handlers.itemhandlers.Recipes;
import com.l2jserver.scripts.handlers.itemhandlers.RollingDice;
import com.l2jserver.scripts.handlers.itemhandlers.ScrollOfResurrection;
import com.l2jserver.scripts.handlers.itemhandlers.Seed;
import com.l2jserver.scripts.handlers.itemhandlers.SevenSignsRecord;
import com.l2jserver.scripts.handlers.itemhandlers.SoulShots;
import com.l2jserver.scripts.handlers.itemhandlers.SpecialXMas;
import com.l2jserver.scripts.handlers.itemhandlers.SpiritShot;
import com.l2jserver.scripts.handlers.itemhandlers.SummonItems;
import com.l2jserver.scripts.handlers.itemhandlers.TeleportBookmark;

/**
 * @author L2jPDT
 */
public class ItemHandler
{
	private static Logger _log = Logger.getLogger(ItemHandler.class.getName());
	private final Map<Integer, IItemHandler> _datatable = new HashMap<>();
	
	public static ItemHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected ItemHandler()
	{
		registerItemHandler(new ScrollOfResurrection());
		registerItemHandler(new SoulShots());
		registerItemHandler(new SpiritShot());
		registerItemHandler(new BlessedSpiritShot());
		registerItemHandler(new BeastSoulShot());
		registerItemHandler(new BeastSpiritShot());
		registerItemHandler(new PaganKeys());
		registerItemHandler(new Maps());
		registerItemHandler(new NicknameColor());
		registerItemHandler(new Recipes());
		registerItemHandler(new RollingDice());
		registerItemHandler(new EnchantAttribute());
		registerItemHandler(new EnchantScrolls());
		registerItemHandler(new ExtractableItems());
		registerItemHandler(new Book());
		registerItemHandler(new SevenSignsRecord());
		registerItemHandler(new ItemSkills());
		registerItemHandler(new ItemSkillsTemplate());
		registerItemHandler(new Seed());
		registerItemHandler(new Harvester());
		registerItemHandler(new MercTicket());
		registerItemHandler(new NevitHourglass());
		registerItemHandler(new FishShots());
		registerItemHandler(new PetFood());
		registerItemHandler(new SpecialXMas());
		registerItemHandler(new SummonItems());
		registerItemHandler(new BeastSpice());
		registerItemHandler(new TeleportBookmark());
		registerItemHandler(new Elixir());
		registerItemHandler(new Disguise());
		registerItemHandler(new ManaPotion());
		registerItemHandler(new EnergyStarStone());
		registerItemHandler(new EventItem());
		_log.info(getText("TG9hZGVkIEl0ZW1zIGhhbmRsZXJzOiA=") + _datatable.size());
	}
	
	public void registerItemHandler(IItemHandler handler)
	{
		_datatable.put(handler.getClass().getSimpleName().intern().hashCode(), handler);
	}
	
	public IItemHandler getItemHandler(L2EtcItem item)
	{
		if ((item == null) || (item.getHandlerName() == null))
		{
			return null;
		}
		
		return _datatable.get(item.getHandlerName().hashCode());
	}
	
	public int size()
	{
		return _datatable.size();
	}
	
	private static class SingletonHolder
	{
		protected static final ItemHandler _instance = new ItemHandler();
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
}