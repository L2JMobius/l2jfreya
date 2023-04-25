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
package com.l2jserver.scripts.custom.EvoMonster;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.scripts.L2AttackableAIScript;
import com.l2jserver.util.Rnd;

/**
 * @author Slyce
 */
public class EvoMonsters extends L2AttackableAIScript
{
	private static final Map<Integer, Integer[]> MOBSPAWNS = new HashMap<>();
	static
	{
		MOBSPAWNS.put(51001, new Integer[]
		{
			51002,
			100,
			100,
			0
		}); // Fallen Orc Shaman -> Sharp Talon Tiger (always polymorphs)
		MOBSPAWNS.put(51002, new Integer[]
		{
			51003,
			100,
			100,
			1
		}); // Ol Mahum Transcender 1st stage
		MOBSPAWNS.put(51003, new Integer[]
		{
			51004,
			100,
			100,
			2
		}); // Ol Mahum Transcender 2nd stage
		MOBSPAWNS.put(51004, new Integer[]
		{
			51005,
			100,
			100,
			3
		}); // Ol Mahum Transcender 3rd stage
		MOBSPAWNS.put(51005, new Integer[]
		{
			51006,
			100,
			100,
			4
		}); // Cave Ant Larva -> Cave Ant
		MOBSPAWNS.put(51006, new Integer[]
		{
			51007,
			100,
			100,
			5
		}); // Cave Ant Larva -> Cave Ant (always polymorphs)
		MOBSPAWNS.put(51007, new Integer[]
		{
			51008,
			100,
			100,
			6
		}); // Cave Ant Larva -> Cave Ant Soldier (always polymorphs)
		MOBSPAWNS.put(51008, new Integer[]
		{
			51009,
			100,
			100,
			7
		}); // Cave Ant -> Cave Ant Soldier
		MOBSPAWNS.put(51009, new Integer[]
		{
			51010,
			100,
			100,
			2
		}); // Cave Ant Soldier -> Cave Noble Ant
		MOBSPAWNS.put(51010, new Integer[]
		{
			51011,
			100,
			100,
			8
		}); // Claws of Splendor
		MOBSPAWNS.put(51011, new Integer[]
		{
			51012,
			100,
			100,
			9
		}); // Anger of Splendor
		MOBSPAWNS.put(51012, new Integer[]
		{
			51013,
			100,
			100,
			10
		}); // Alliance of Splendor
		MOBSPAWNS.put(51013, new Integer[]
		{
			51014,
			100,
			100,
			11
		}); // Fang of Splendor
	}
	protected static final String[][] MOBTEXTS =
	{
		new String[]
		{
			"Enough fooling around. Get ready to die!",
			"You idiot! I've just been toying with you!",
			"Now the fun starts!"
		},
		new String[]
		{
			"I must admit, no one makes my blood boil quite like you do!",
			"Now the battle begins!",
			"Witness my true power!"
		},
		new String[]
		{
			"Prepare to die!",
			"I'll double my strength!",
			"You have more skill than I thought"
		}
	};
	
	public EvoMonsters()
	{
		super(-1, EvoMonsters.class.getSimpleName(), "ai");
		for (int id : MOBSPAWNS.keySet())
		{
			super.AttackNpcs(id);
		}
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (npc.isVisible() && !npc.isDead())
		{
			final Integer[] tmp = MOBSPAWNS.get(npc.getNpcId());
			if (tmp != null)
			{
				if ((npc.getCurrentHp() <= ((npc.getMaxHp() * tmp[1]) / 100.0)) && (Rnd.get(100) < tmp[2]))
				{
					if (tmp[3] >= 0)
					{
						String text = MOBTEXTS[tmp[3]][Rnd.get(MOBTEXTS[tmp[3]].length)];
						npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), text));
					}
					npc.deleteMe();
					L2Attackable newNpc = (L2Attackable) addSpawn(tmp[0], npc.getX(), npc.getY(), npc.getZ() + 10, npc.getHeading(), false, 0, true);
					L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
					newNpc.setRunning();
					newNpc.addDamageHate(originalAttacker, 0, 500);
					newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalAttacker);
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
}