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

import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2CharPosition;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.PlaySound;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.network.serverpackets.SpecialCamera;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * DrChaos' AI.
 * @author Kerberos
 */
public class DrChaos extends L2AttackableAIScript
{
	private static final int DR_CHAOS = 32033;
	private static final int STRANGE_MACHINE = 32032;
	private static final int CHAOS_GOLEM = 25512;
	private static boolean _IsGolemSpawned;
	
	public DrChaos()
	{
		super(-1, DrChaos.class.getSimpleName(), "ai");
		FirstTalkNpcs(DR_CHAOS);
		_IsGolemSpawned = false;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		switch (event)
		{
			case "1":
				L2Npc machine = null;
				for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(STRANGE_MACHINE))
				{
					if (spawn != null)
					{
						machine = spawn.getLastSpawn();
					}
				}
				if (machine != null)
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, machine);
					machine.broadcastPacket(new SpecialCamera(machine.getObjectId(), 1, -200, 15, 10000, 20000, 0, 0, 1, 0));
				}
				else
				{
					startQuestTimer("2", 2000, npc, player);
				}
				startQuestTimer("3", 10000, npc, player);
				break;
			case "2":
				npc.broadcastPacket(new SocialAction(npc, 3));
				break;
			case "3":
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1, -150, 10, 3000, 20000, 0, 0, 1, 0));
				startQuestTimer("4", 2500, npc, player);
				break;
			case "4":
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(96055, -110759, -3312, 0));
				startQuestTimer("5", 2000, npc, player);
				break;
			case "5":
				player.teleToLocation(94832, -112624, -3304);
				npc.teleToLocation(-113091, -243942, -15536);
				if (!_IsGolemSpawned)
				{
					L2Npc golem = addSpawn(CHAOS_GOLEM, 94640, -112496, -3336, 0, false, 0);
					_IsGolemSpawned = true;
					startQuestTimer("6", 1000, golem, player);
					player.sendPacket(new PlaySound(1, "Rm03_A", 0, 0, 0, 0, 0));
				}
				break;
			case "6":
				npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 30, -200, 20, 6000, 8000, 0, 0, 1, 0));
				break;
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		if (npc.getNpcId() == DR_CHAOS)
		{
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(96323, -110914, -3328, 0));
			startQuestTimer("1", 3000, npc, player);
		}
		return "";
	}
}
