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
package com.l2jserver.scripts.ai;

import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * Mithril Mines AI.
 * @author malyelfik
 */
public final class MithrilMinesLocation extends L2AttackableAIScript
{
	// NPCs
	private static final int GRAVE_ROBBER_SUMMONER = 22678; // Grave Robber Summoner (Lunatic)
	private static final int GRAVE_ROBBER_MAGICIAN = 22679; // Grave Robber Magician (Lunatic)
	private static final int[] SUMMONER_MINIONS =
	{
		22683, // Servitor of Darkness
		22684, // Servitor of Darkness
	};
	private static final int[] MAGICIAN_MINIONS =
	{
		22685, // Servitor of Darkness
		22686, // Servitor of Darkness
	};
	
	public MithrilMinesLocation()
	{
		super(-1, MithrilMinesLocation.class.getSimpleName(), "ai");
		SpawnNpcs(GRAVE_ROBBER_SUMMONER, GRAVE_ROBBER_MAGICIAN);
		
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(GRAVE_ROBBER_SUMMONER))
		{
			onSpawn(spawn.getLastSpawn());
		}
		
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(GRAVE_ROBBER_MAGICIAN))
		{
			onSpawn(spawn.getLastSpawn());
		}
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		final int[] minions = (npc.getNpcId() == GRAVE_ROBBER_SUMMONER) ? SUMMONER_MINIONS : MAGICIAN_MINIONS;
		addMinion((L2MonsterInstance) npc, minions[getRandom(minions.length)]);
		return super.onSpawn(npc);
	}
}