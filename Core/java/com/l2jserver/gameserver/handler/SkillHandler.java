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

import com.l2jserver.gameserver.handler.interfaces.ISkillHandler;
import com.l2jserver.gameserver.templates.skills.L2SkillType;
import com.l2jserver.scripts.handlers.skillhandlers.BalanceLife;
import com.l2jserver.scripts.handlers.skillhandlers.BallistaBomb;
import com.l2jserver.scripts.handlers.skillhandlers.BeastSkills;
import com.l2jserver.scripts.handlers.skillhandlers.Blow;
import com.l2jserver.scripts.handlers.skillhandlers.Cancel;
import com.l2jserver.scripts.handlers.skillhandlers.Charge;
import com.l2jserver.scripts.handlers.skillhandlers.CombatPointHeal;
import com.l2jserver.scripts.handlers.skillhandlers.Continuous;
import com.l2jserver.scripts.handlers.skillhandlers.CpDam;
import com.l2jserver.scripts.handlers.skillhandlers.CpDamPercent;
import com.l2jserver.scripts.handlers.skillhandlers.Craft;
import com.l2jserver.scripts.handlers.skillhandlers.DeluxeKey;
import com.l2jserver.scripts.handlers.skillhandlers.Detection;
import com.l2jserver.scripts.handlers.skillhandlers.Disablers;
import com.l2jserver.scripts.handlers.skillhandlers.Dummy;
import com.l2jserver.scripts.handlers.skillhandlers.Extractable;
import com.l2jserver.scripts.handlers.skillhandlers.Fishing;
import com.l2jserver.scripts.handlers.skillhandlers.FishingSkill;
import com.l2jserver.scripts.handlers.skillhandlers.GetPlayer;
import com.l2jserver.scripts.handlers.skillhandlers.GiveReco;
import com.l2jserver.scripts.handlers.skillhandlers.GiveSp;
import com.l2jserver.scripts.handlers.skillhandlers.GiveVitality;
import com.l2jserver.scripts.handlers.skillhandlers.Harvest;
import com.l2jserver.scripts.handlers.skillhandlers.Heal;
import com.l2jserver.scripts.handlers.skillhandlers.HealPercent;
import com.l2jserver.scripts.handlers.skillhandlers.InstantJump;
import com.l2jserver.scripts.handlers.skillhandlers.ManaHeal;
import com.l2jserver.scripts.handlers.skillhandlers.Manadam;
import com.l2jserver.scripts.handlers.skillhandlers.Mdam;
import com.l2jserver.scripts.handlers.skillhandlers.NornilsPower;
import com.l2jserver.scripts.handlers.skillhandlers.Pdam;
import com.l2jserver.scripts.handlers.skillhandlers.RefuelAirShip;
import com.l2jserver.scripts.handlers.skillhandlers.Resurrect;
import com.l2jserver.scripts.handlers.skillhandlers.ShiftTarget;
import com.l2jserver.scripts.handlers.skillhandlers.Soul;
import com.l2jserver.scripts.handlers.skillhandlers.Sow;
import com.l2jserver.scripts.handlers.skillhandlers.Spoil;
import com.l2jserver.scripts.handlers.skillhandlers.StealBuffs;
import com.l2jserver.scripts.handlers.skillhandlers.StrSiegeAssault;
import com.l2jserver.scripts.handlers.skillhandlers.SummonFriend;
import com.l2jserver.scripts.handlers.skillhandlers.Sweep;
import com.l2jserver.scripts.handlers.skillhandlers.TakeCastle;
import com.l2jserver.scripts.handlers.skillhandlers.TakeFort;
import com.l2jserver.scripts.handlers.skillhandlers.TransformDispel;
import com.l2jserver.scripts.handlers.skillhandlers.Trap;
import com.l2jserver.scripts.handlers.skillhandlers.Unlock;

/**
 * @author L2jPDT
 */
public class SkillHandler
{
	private static Logger _log = Logger.getLogger(SkillHandler.class.getName());
	private final Map<Integer, ISkillHandler> _datatable = new HashMap<>();
	
	public static SkillHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected SkillHandler()
	{
		registerSkillHandler(new Blow());
		registerSkillHandler(new Pdam());
		registerSkillHandler(new Mdam());
		registerSkillHandler(new CpDam());
		registerSkillHandler(new CpDamPercent());
		registerSkillHandler(new Manadam());
		registerSkillHandler(new Heal());
		registerSkillHandler(new HealPercent());
		registerSkillHandler(new CombatPointHeal());
		registerSkillHandler(new ManaHeal());
		registerSkillHandler(new BalanceLife());
		registerSkillHandler(new Charge());
		registerSkillHandler(new Continuous());
		registerSkillHandler(new Detection());
		registerSkillHandler(new Resurrect());
		registerSkillHandler(new ShiftTarget());
		registerSkillHandler(new Spoil());
		registerSkillHandler(new Sweep());
		registerSkillHandler(new StrSiegeAssault());
		registerSkillHandler(new SummonFriend());
		registerSkillHandler(new Disablers());
		registerSkillHandler(new Cancel());
		registerSkillHandler(new StealBuffs());
		registerSkillHandler(new BallistaBomb());
		registerSkillHandler(new TakeCastle());
		registerSkillHandler(new TakeFort());
		registerSkillHandler(new Unlock());
		registerSkillHandler(new Craft());
		registerSkillHandler(new Fishing());
		registerSkillHandler(new FishingSkill());
		registerSkillHandler(new BeastSkills());
		registerSkillHandler(new DeluxeKey());
		registerSkillHandler(new Sow());
		registerSkillHandler(new Soul());
		registerSkillHandler(new Harvest());
		registerSkillHandler(new GetPlayer());
		registerSkillHandler(new TransformDispel());
		registerSkillHandler(new Trap());
		registerSkillHandler(new GiveSp());
		registerSkillHandler(new GiveReco());
		registerSkillHandler(new GiveVitality());
		registerSkillHandler(new InstantJump());
		registerSkillHandler(new Dummy());
		registerSkillHandler(new Extractable());
		registerSkillHandler(new RefuelAirShip());
		registerSkillHandler(new NornilsPower());
		_log.info(getText("TG9hZGVkIFNraWxscyBoYW5kbGVyczog") + _datatable.size());
	}
	
	public void registerSkillHandler(ISkillHandler handler)
	{
		for (L2SkillType t : handler.getSkillIds())
		{
			_datatable.put(t.ordinal(), handler);
		}
	}
	
	public ISkillHandler getSkillHandler(L2SkillType skillType)
	{
		return _datatable.get(skillType.ordinal());
	}
	
	public int size()
	{
		return _datatable.size();
	}
	
	private static class SingletonHolder
	{
		protected static final SkillHandler _instance = new SkillHandler();
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
}