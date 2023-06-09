/*
 * Copyright (C) 2004-2013 L2J DataPack
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
package com.l2jserver.scripts.teleports.TeleportToFantasy;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;

import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * Fantasy Island teleport AI.<br>
 * Original python script by Kerberos.
 * @author Plim
 */
public class TeleportToFantasy extends L2AttackableAIScript
{
	// NPC
	private static final int PADDIES = 32378;
	// Misc
	private static final Map<Integer, Integer> TELEPORTERS = new HashMap<>();
	// Locations
	private static final Location[] RETURN_LOCS =
	{
		new Location(-80826, 149775, -3043),
		new Location(-12672, 122776, -3116),
		new Location(15670, 142983, -2705),
		new Location(83400, 147943, -3404),
		new Location(111409, 219364, -3545),
		new Location(82956, 53162, -1495),
		new Location(146331, 25762, -2018),
		new Location(116819, 76994, -2714),
		new Location(43835, -47749, -792),
		new Location(147930, -55281, -2728),
		new Location(87386, -143246, -1293),
		new Location(12882, 181053, -3560)
	};
	
	private static final Location[] ISLE_LOCS =
	{
		new Location(-58752, -56898, -2032),
		new Location(-59716, -57868, -2032),
		new Location(-60691, -56893, -2032),
		new Location(-59720, -55921, -2032)
	};
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		
		if (st == null)
		{
			return null;
		}
		
		if (TELEPORTERS.containsKey(npc.getNpcId()))
		{
			int random_id = getRandom(ISLE_LOCS.length);
			
			player.teleToLocation(ISLE_LOCS[random_id], false);
			st.setState(State.STARTED);
			st.set("id", String.valueOf(TELEPORTERS.get(npc.getNpcId())));
		}
		else if (npc.getNpcId() == PADDIES)
		{
			if ((st.getState() == State.STARTED) && (st.getInt("id") > 0))
			{
				int return_id = st.getInt("id") - 1;
				player.teleToLocation(RETURN_LOCS[return_id], false);
				st.unset("id");
			}
			
			else
			{
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 1, npc.getName(), "If your means of arrival was a bit unconventional, then I'll be sending you back to Rune Township, which is the nearest town."));
				player.teleToLocation(43835, -47749, -792);
			}
			st.exitQuest(true);
		}
		return htmltext;
	}
	
	public TeleportToFantasy()
	{
		super(-1, TeleportToFantasy.class.getSimpleName(), "teleports");
		TELEPORTERS.put(30059, 3); // TRISHA
		TELEPORTERS.put(30080, 4); // CLARISSA
		TELEPORTERS.put(30177, 6); // VALENTIA
		TELEPORTERS.put(30233, 8); // ESMERALDA
		TELEPORTERS.put(30256, 2); // BELLA
		TELEPORTERS.put(30320, 1); // RICHLIN
		TELEPORTERS.put(30848, 7); // ELISA
		TELEPORTERS.put(30899, 5); // FLAUEN
		TELEPORTERS.put(31320, 9); // ILYANA
		TELEPORTERS.put(31275, 10); // TATIANA
		TELEPORTERS.put(31964, 11); // BILIA
		
		for (int npcId : TELEPORTERS.keySet())
		{
			StartNpcs(npcId);
			TalkNpcs(npcId);
		}
		
		StartNpcs(PADDIES);
		TalkNpcs(PADDIES);
	}
}
