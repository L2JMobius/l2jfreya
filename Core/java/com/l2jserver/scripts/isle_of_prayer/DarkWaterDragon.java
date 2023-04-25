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
package com.l2jserver.scripts.isle_of_prayer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.l2jserver.Config;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import com.l2jserver.scripts.L2AttackableAIScript;

public class DarkWaterDragon extends L2AttackableAIScript
{
	private static final int DRAGON = 22267;
	private static final int SHADE1 = 22268;
	private static final int SHADE2 = 22269;
	private static final int FAFURION = 18482;
	private static final int DETRACTOR1 = 22270;
	private static final int DETRACTOR2 = 22271;
	private static Set<Integer> secondSpawn = ConcurrentHashMap.newKeySet(); // Used to track if second Shades were already spawned
	private static Set<Integer> myTrackingSet = ConcurrentHashMap.newKeySet(); // Used to track instances of npcs
	private static Map<Integer, L2PcInstance> _idmap = new ConcurrentHashMap<>(); // Used to track instances of npcs
	
	private static final int DELAY_SPAWN = Config.DELAY_SPAWN_MONSTERS_DARK_WATER_DRAGON;
	
	public DarkWaterDragon()
	{
		super(-1, DarkWaterDragon.class.getSimpleName(), "isle_of_prayer");
		int[] mobs =
		{
			DRAGON,
			SHADE1,
			SHADE2,
			FAFURION,
			DETRACTOR1,
			DETRACTOR2
		};
		registerMobs(mobs);
		myTrackingSet.clear();
		secondSpawn.clear();
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (npc != null)
		{
			switch (event)
			{
				case "first_spawn":
					startQuestTimer("1", DELAY_SPAWN, npc, null, true);
					break;
				case "second_spawn":
					startQuestTimer("2", DELAY_SPAWN, npc, null, true);
					break;
				case "third_spawn":
					startQuestTimer("3", DELAY_SPAWN, npc, null, true);
					break;
				case "fourth_spawn":
					startQuestTimer("4", DELAY_SPAWN, npc, null, true);
					break;
				case "1":
					addSpawn(DETRACTOR1, (npc.getX() + 100), (npc.getY() + 100), npc.getZ(), 0, false, DELAY_SPAWN);
					break;
				case "2":
					addSpawn(DETRACTOR2, (npc.getX() + 100), (npc.getY() - 100), npc.getZ(), 0, false, DELAY_SPAWN);
					break;
				case "3":
					addSpawn(DETRACTOR1, (npc.getX() - 100), (npc.getY() + 100), npc.getZ(), 0, false, DELAY_SPAWN);
					break;
				case "4":
					addSpawn(DETRACTOR2, (npc.getX() - 100), (npc.getY() - 100), npc.getZ(), 0, false, DELAY_SPAWN);
					break;
				case "fafurion_despawn":
					cancelQuestTimer("fafurion_poison", npc, null);
					cancelQuestTimer("1", npc, null);
					cancelQuestTimer("2", npc, null);
					cancelQuestTimer("3", npc, null);
					cancelQuestTimer("4", npc, null);
					
					myTrackingSet.remove(npc.getObjectId());
					player = _idmap.remove(npc.getObjectId());
					if (player != null)
					{
						((L2Attackable) npc).doItemDrop(NpcTable.getInstance().getTemplate(18485), player);
					}
					
					npc.deleteMe();
					break;
				case "fafurion_poison":
					if (npc.getCurrentHp() <= 500)
					{
						cancelQuestTimer("fafurion_despawn", npc, null);
						cancelQuestTimer("first_spawn", npc, null);
						cancelQuestTimer("second_spawn", npc, null);
						cancelQuestTimer("third_spawn", npc, null);
						cancelQuestTimer("fourth_spawn", npc, null);
						cancelQuestTimer("1", npc, null);
						cancelQuestTimer("2", npc, null);
						cancelQuestTimer("3", npc, null);
						cancelQuestTimer("4", npc, null);
						myTrackingSet.remove(npc.getObjectId());
						_idmap.remove(npc.getObjectId());
					}
					npc.reduceCurrentHp(500, npc, null); // poison kills Fafurion if he is not healed
					break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (npc.getNpcId() == DRAGON)
		{
			if (!myTrackingSet.contains(npc.getObjectId())) // this allows to handle multiple instances of npc
			{
				myTrackingSet.add(npc.getObjectId());
				// Spawn first 5 shades on first attack on Dark Water Dragon
				L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
				spawnShade(originalAttacker, SHADE1, npc.getX() + 100, npc.getY() + 100, npc.getZ());
				spawnShade(originalAttacker, SHADE2, npc.getX() + 100, npc.getY() - 100, npc.getZ());
				spawnShade(originalAttacker, SHADE1, npc.getX() - 100, npc.getY() + 100, npc.getZ());
				spawnShade(originalAttacker, SHADE2, npc.getX() - 100, npc.getY() - 100, npc.getZ());
				spawnShade(originalAttacker, SHADE1, npc.getX() - 150, npc.getY() + 150, npc.getZ());
			}
			else if ((npc.getCurrentHp() < (npc.getMaxHp() / 2.0)) && !(secondSpawn.contains(npc.getObjectId())))
			{
				secondSpawn.add(npc.getObjectId());
				// Spawn second 5 shades on half hp of on Dark Water Dragon
				L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
				spawnShade(originalAttacker, SHADE2, npc.getX() + 100, npc.getY() + 100, npc.getZ());
				spawnShade(originalAttacker, SHADE1, npc.getX() + 100, npc.getY() - 100, npc.getZ());
				spawnShade(originalAttacker, SHADE2, npc.getX() - 100, npc.getY() + 100, npc.getZ());
				spawnShade(originalAttacker, SHADE1, npc.getX() - 100, npc.getY() - 100, npc.getZ());
				spawnShade(originalAttacker, SHADE2, npc.getX() - 150, npc.getY() + 150, npc.getZ());
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		if (npc.getNpcId() == DRAGON)
		{
			myTrackingSet.remove(npc.getObjectId());
			secondSpawn.remove(npc.getObjectId());
			L2Attackable faf = (L2Attackable) addSpawn(FAFURION, npc.getX(), npc.getY(), npc.getZ(), 0, false, 0); // spawns Fafurion Kindred when Dard Water Dragon is dead
			_idmap.put(faf.getObjectId(), killer);
		}
		else if (npc.getNpcId() == FAFURION)
		{
			cancelQuestTimer("fafurion_poison", npc, null);
			cancelQuestTimer("fafurion_despawn", npc, null);
			cancelQuestTimer("first_spawn", npc, null);
			cancelQuestTimer("second_spawn", npc, null);
			cancelQuestTimer("third_spawn", npc, null);
			cancelQuestTimer("fourth_spawn", npc, null);
			cancelQuestTimer("1", npc, null);
			cancelQuestTimer("2", npc, null);
			cancelQuestTimer("3", npc, null);
			cancelQuestTimer("4", npc, null);
			myTrackingSet.remove(npc.getObjectId());
			_idmap.remove(npc.getObjectId());
		}
		return super.onKill(npc, killer, isPet);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		if (npc.getNpcId() == FAFURION)
		{
			if (!myTrackingSet.contains(npc.getObjectId()))
			{
				myTrackingSet.add(npc.getObjectId());
				// Spawn 4 Detractors on spawn of Fafurion
				int x = npc.getX();
				int y = npc.getY();
				addSpawn(DETRACTOR2, x + 100, y + 100, npc.getZ(), 0, false, 40000);
				addSpawn(DETRACTOR1, x + 100, y - 100, npc.getZ(), 0, false, 40000);
				addSpawn(DETRACTOR2, x - 100, y + 100, npc.getZ(), 0, false, 40000);
				addSpawn(DETRACTOR1, x - 100, y - 100, npc.getZ(), 0, false, 40000);
				startQuestTimer("first_spawn", 2000, npc, null); // timer to delay timer "1"
				startQuestTimer("second_spawn", 4000, npc, null); // timer to delay timer "2"
				startQuestTimer("third_spawn", 8000, npc, null); // timer to delay timer "3"
				startQuestTimer("fourth_spawn", 10000, npc, null); // timer to delay timer "4"
				startQuestTimer("fafurion_poison", 3000, npc, null, true); // Every three seconds reduces Fafurions hp like it is poisoned
				startQuestTimer("fafurion_despawn", 120000, npc, null); // Fafurion Kindred disappears after two minutes
			}
		}
		return super.onSpawn(npc);
	}
	
	public void spawnShade(L2Character attacker, int npcId, int x, int y, int z)
	{
		final L2Npc shade = addSpawn(npcId, x, y, z, 0, false, 0);
		shade.setRunning();
		((L2Attackable) shade).addDamageHate(attacker, 0, 999);
		shade.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
	}
}