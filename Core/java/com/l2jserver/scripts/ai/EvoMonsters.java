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
package com.l2jserver.scripts.ai;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * Angel spawns...when one of the angels in the keys dies, the other angel will spawn.
 */
public class EvoMonsters extends L2AttackableAIScript
{
	
	private static final Map<Integer, Integer> ANGELSPAWNS = new HashMap<>();
	static
	{
		ANGELSPAWNS.put(50001, 50002);
		ANGELSPAWNS.put(50003, 50004);
		ANGELSPAWNS.put(50005, 50006);
		ANGELSPAWNS.put(50007, 50008);
		ANGELSPAWNS.put(50009, 50010);
	}
	
	public EvoMonsters()
	{
		super(-1, EvoMonsters.class.getSimpleName(), "ai");
		int[] temp =
		{
			50001,
			50003,
			50005,
			50007,
			50009
		};
		registerMobs(temp);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (ANGELSPAWNS.containsKey(npcId))
		{
			L2Attackable newNpc = (L2Attackable) this.addSpawn(ANGELSPAWNS.get(npcId), npc);
			newNpc.setRunning();
		}
		return super.onKill(npc, killer, isPet);
	}
}