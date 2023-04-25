/*
 * Copyright (C) 2004-2014 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.scripts.handlers.bypasshandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.interfaces.IBypassHandler;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.buffshop.BuffShopManager;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.skills.SkillHolder;
import com.l2jserver.gameserver.util.IConvert;
import com.l2jserver.gameserver.util.Util;

/**
 * @author Administrator
 */
public class BuffShop implements IBypassHandler, IConvert
{
	private static final String[] COMMAND =
	{
		"buffshop"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character bypassOrigin)
	{
		String[] commands = command.split(" ");
		if (commands[1].equals("add"))
		{
			try
			{
				int price = toInteger(commands[4]);
				if (price < BuffShopManager.MIN_PRICE)
				{
					activeChar.sendPacket(new ExShowScreenMessage("Minimum price for set buff is " + BuffShopManager.MIN_PRICE + " Adena(s)", 3000));
					return false;
				}
				
				L2Skill skill = L2Skill.valueOf(toInteger(commands[2]), toInteger(commands[3]));
				
				if (!activeChar.getSkills().containsValue(skill) || !Util.contains(Config.BUFFSHOP_ALLOWED_BUFFS, skill.getId()) || isItemSkill(activeChar, skill))
				{
					activeChar.sendPacket(new ExShowScreenMessage("You selected forbidden skills. These skills cannot be sold.", 3000));
					return false;
				}
				
				BuffShopManager.getInstance().addBuff(activeChar, toInteger(commands[2]), toInteger(commands[3]), toInteger(commands[4]));
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Try to set again :) You trying to something bad :P ");
			}
		}
		else if (commands[1].equals("del"))
		{
			BuffShopManager.getInstance().removeBuff(activeChar, toInteger(commands[2]));
		}
		else if (commands[1].equals("title"))
		{
			if (commands.length < BuffShopManager.MIN_LETTERS_FOR_TITLE)
			{
				activeChar.sendPacket(new ExShowScreenMessage("You need to set min. " + BuffShopManager.MIN_LETTERS_FOR_TITLE + "x letters title.", 3000));
				return false;
			}
			
			if (commands[2].length() <= BuffShopManager.MAX_LETTERS_FOR_TITLE)
			{
				BuffShopManager.getInstance().getShops().get(activeChar.getObjectId()).setTitle(commands[2]);
				BuffShopManager.getInstance().list(activeChar, 1);
			}
			else
			{
				activeChar.sendPacket(new ExShowScreenMessage("Your title is too long. Set short title.", 3000));
			}
		}
		else if (commands[1].equals("list"))
		{
			BuffShopManager.getInstance().list(activeChar, toInteger(commands[2]));
		}
		else if (commands[1].equals("start"))
		{
			if (BuffShopManager.ENABLE_BUFF_SHOP_ZONE)
			{
				if (!activeChar.isInsideZone(L2Character.BUFF_SHOP))
				{
					activeChar.sendPacket(new ExShowScreenMessage(activeChar.getName() + ": You must be in Buff Shop zone.", 5000));
					return false;
				}
			}
			for (L2PcInstance pl : activeChar.getKnownList().getKnownPlayers().values())
			{
				if (BuffShopManager.getInstance().getSellers().containsKey(pl.getObjectId()) && (pl.calculateDistance(activeChar.getLocationXYZ(), false, false) < BuffShopManager.MIN_DISTANCE_NEAR_THE_SHOP))
				{
					activeChar.sendPacket(new ExShowScreenMessage(activeChar.getName() + ": You cannot set close to near character. Go far from character.", 5000));
					return false;
				}
			}
			
			BuffShopManager.getInstance().startShop(activeChar);
		}
		else if (commands[1].equals("stop"))
		{
			BuffShopManager.getInstance().stopShop(activeChar);
		}
		else if (commands[1].equals("cast"))
		{
			int sellerId = toInteger(commands[2]);
			int buff = toInteger(commands[3]);
			L2PcInstance seller = L2World.getInstance().getPlayer(sellerId);
			
			if ((seller != null))
			{
				if ((seller.calculateDistance(activeChar.getLocationXYZ(), false, false) > BuffShopManager.MAX_RANGE_FOR_CAST))
				{
					activeChar.sendPacket(new ExShowScreenMessage("You are to far from buffer.", 5000));
					return false;
				}
				
				if (BuffShopManager.getInstance().getSellers().get(seller.getObjectId()) == activeChar.getObjectId())
				{
					activeChar.sendPacket(new ExShowScreenMessage("You cannot buff yourself.", 5000));
					return false;
				}
				
				BuffShopManager.getInstance().sellBuff(seller, activeChar, buff);
			}
		}
		return false;
	}
	
	/**
	 * @param activeChar
	 * @param skill
	 * @return
	 */
	private boolean isItemSkill(L2PcInstance activeChar, L2Skill skill)
	{
		for (L2ItemInstance item : activeChar.getInventory().getItems())
		{
			if (item.getItem().getSkills() == null)
			{
				continue;
			}
			
			for (SkillHolder holder : item.getItem().getSkills())
			{
				if (holder.getSkill() == skill)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMAND;
	}
}
