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
 * this program. If not, see <http://L2J.EternityWorld.ru/>.
 */
package com.l2jserver.gameserver.model.zone.type;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2BabyPetInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.actor.instance.L2SummonInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 * @author L2jPDT
 */
public class L2AntQueenZone extends L2ZoneType
{
	public L2AntQueenZone(final int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(final L2Character character)
	{
		if (character instanceof L2PcInstance)
		{
			character.setInsideZone(L2Character.ZONE_ALTERED, true);
			if ((character.getLevel() >= Config.AQ_ZONE_MAX_LV) && (!character.isGM()))
			{
				character.teleToLocation(47591, 185820, -3486);
				character.sendPacket(new ExShowScreenMessage("You cannot to enter, if you have " + Config.AQ_ZONE_MAX_LV + " level or higher", 5000));
				character.stopSkillEffects(1323);
			}
			if (Config.AQ_ZONE_GIVE_NOBLESS)
			{
				SkillTable.getInstance().getSkill(1323, 1).getEffects(character, character);
				character.sendMessage("You have received Nobless Blessing.");
			}
		}
		else if (character instanceof L2PetInstance)
		{
			character.setInsideZone(L2Character.ZONE_ALTERED, true);
			if (character.getLevel() >= Config.AQ_ZONE_MAX_LV)
			{
				character.teleToLocation(47591, 185820, -3486);
				character.sendPacket(new ExShowScreenMessage("You cannot to enter, if you have " + Config.AQ_ZONE_MAX_LV + " level or higher", 5000));
				character.stopSkillEffects(1323);
			}
			if (Config.AQ_ZONE_GIVE_NOBLESS)
			{
				SkillTable.getInstance().getSkill(1323, 1).getEffects(character, character);
				character.sendMessage("You have received Nobless Blessing.");
			}
		}
		else if (character instanceof L2BabyPetInstance)
		{
			character.setInsideZone(L2Character.ZONE_ALTERED, true);
			if (character.getLevel() >= Config.AQ_ZONE_MAX_LV)
			{
				character.teleToLocation(47591, 185820, -3486);
				character.sendPacket(new ExShowScreenMessage("You cannot to enter, if you have " + Config.AQ_ZONE_MAX_LV + " level or higher", 5000));
				character.stopSkillEffects(1323);
			}
			if (Config.AQ_ZONE_GIVE_NOBLESS)
			{
				SkillTable.getInstance().getSkill(1323, 1).getEffects(character, character);
				character.sendMessage("You have received Nobless Blessing.");
			}
		}
		else if (character instanceof L2SummonInstance)
		{
			character.setInsideZone(L2Character.ZONE_ALTERED, true);
			if (character.getLevel() >= Config.AQ_ZONE_MAX_LV)
			{
				character.teleToLocation(47591, 185820, -3486);
				character.sendPacket(new ExShowScreenMessage("You cannot to enter, if you have " + Config.AQ_ZONE_MAX_LV + " level or higher", 5000));
				character.stopSkillEffects(1323);
			}
			if (Config.AQ_ZONE_GIVE_NOBLESS)
			{
				SkillTable.getInstance().getSkill(1323, 1).getEffects(character, character);
				character.sendMessage("You have received Nobless Blessing.");
			}
		}
	}
	
	@Override
	protected void onExit(final L2Character character)
	{
		if (character instanceof L2PcInstance)
		{
			character.setInsideZone(L2Character.ZONE_ALTERED, false);
			if (Config.AQ_ZONE_GIVE_NOBLESS)
			{
				character.stopSkillEffects(1323);
				character.sendMessage("You have removed Nobless Blessing.");
			}
		}
		else if (character instanceof L2PetInstance)
		{
			character.setInsideZone(L2Character.ZONE_ALTERED, false);
			if (Config.AQ_ZONE_GIVE_NOBLESS)
			{
				character.stopSkillEffects(1323);
				character.sendMessage("You have removed Nobless Blessing.");
			}
		}
		else if (character instanceof L2BabyPetInstance)
		{
			character.setInsideZone(L2Character.ZONE_ALTERED, false);
			if (Config.AQ_ZONE_GIVE_NOBLESS)
			{
				character.stopSkillEffects(1323);
				character.sendMessage("You have removed Nobless Blessing.");
			}
		}
		else if (character instanceof L2SummonInstance)
		{
			character.setInsideZone(L2Character.ZONE_ALTERED, false);
			if (Config.AQ_ZONE_GIVE_NOBLESS)
			{
				character.stopSkillEffects(1323);
				character.sendMessage("You have removed Nobless Blessing.");
			}
		}
	}
	
	@Override
	public void onDieInside(final L2Character character)
	{
		
	}
	
	@Override
	public void onReviveInside(final L2Character character)
	{
	}
	
	@Override
	public void onPlayerLoginInside(L2PcInstance player)
	{
	}
}
