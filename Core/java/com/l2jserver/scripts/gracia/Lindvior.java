/*
 * Copyright (C) 2004-2015 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.scripts.gracia;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.ExStartScenePlayer;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * Lindvior Scene AI.
 * @author nonom
 */
public class Lindvior extends L2AttackableAIScript
{
	private static final int LINDVIOR_CAMERA = 18669;
	private static final int TOMARIS = 32552;
	private static final int ARTIUS = 32559;
	
	private static final int RESET_HOUR = Config.LINDVIOR_RESET_HOUR_18;
	private static final int RESET_MIN = Config.LINDVIOR_RESET_HOUR_MIN_58;
	private static final int RESET_DAY_1 = Calendar.TUESDAY;
	private static final int RESET_DAY_2 = Calendar.FRIDAY;
	
	private static boolean ALT_MODE = false;
	private static int ALT_MODE_MIN = 60; // schedule delay in minutes if ALT_MODE enabled
	
	private L2Npc _lindviorCamera = null;
	private L2Npc _tomaris = null;
	private L2Npc _artius = null;
	
	public Lindvior()
	{
		super(-1, Lindvior.class.getSimpleName(), "gracia");
		scheduleNextLindviorVisit();
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		switch (event)
		{
			case "tomaris_shout1":
				broadcastNpcSay(npc, Say2.SHOUT, "Huh? The sky looks funny. What's that?");
				break;
			case "artius_shout":
				broadcastNpcSay(npc, Say2.SHOUT, "A powerful subordinate is being held by the Barrier Orb! This reaction means...!");
				break;
			case "tomaris_shout2":
				broadcastNpcSay(npc, Say2.SHOUT, "Be careful...! Something's coming...!");
				break;
			case "lindvior_scene":
				if (npc != null)
				{
					for (L2PcInstance pl : npc.getKnownList().getKnownPlayersInRadius(4000))
					{
						if ((pl.getZ() >= 1100) && (pl.getZ() <= 3100))
						{
							pl.showQuestMovie(ExStartScenePlayer.LINDVIOR);
						}
					}
				}
				break;
			case "start":
				_lindviorCamera = SpawnTable.getInstance().getFirstSpawn(LINDVIOR_CAMERA).getLastSpawn();
				_tomaris = SpawnTable.getInstance().getFirstSpawn(TOMARIS).getLastSpawn();
				_artius = SpawnTable.getInstance().getFirstSpawn(ARTIUS).getLastSpawn();
				
				startQuestTimer("tomaris_shout1", 1000, _tomaris, null);
				startQuestTimer("artius_shout", 60000, _artius, null);
				startQuestTimer("tomaris_shout2", 90000, _tomaris, null);
				startQuestTimer("lindvior_scene", 120000, _lindviorCamera, null);
				scheduleNextLindviorVisit();
				break;
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	public void scheduleNextLindviorVisit()
	{
		long delay = (ALT_MODE) ? ALT_MODE_MIN * 60000 : scheduleNextLindviorDate();
		startQuestTimer("start", delay, null, null);
	}
	
	protected long scheduleNextLindviorDate()
	{
		GregorianCalendar date = new GregorianCalendar();
		date.set(Calendar.MINUTE, RESET_MIN);
		date.set(Calendar.HOUR_OF_DAY, RESET_HOUR);
		if (System.currentTimeMillis() >= date.getTimeInMillis())
		{
			date.add(Calendar.DAY_OF_WEEK, 1);
		}
		
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek <= RESET_DAY_1)
		{
			date.add(Calendar.DAY_OF_WEEK, RESET_DAY_1 - dayOfWeek);
		}
		else if (dayOfWeek <= RESET_DAY_2)
		{
			date.add(Calendar.DAY_OF_WEEK, RESET_DAY_2 - dayOfWeek);
		}
		else
		{
			date.add(Calendar.DAY_OF_WEEK, 1 + RESET_DAY_1);
		}
		return date.getTimeInMillis() - System.currentTimeMillis();
	}
}