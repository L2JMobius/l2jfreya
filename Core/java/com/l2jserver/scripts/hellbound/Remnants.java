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
package com.l2jserver.scripts.hellbound;

import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * @author DS
 */
public class Remnants extends L2AttackableAIScript
{
	private static final int[] NPCS =
	{
		18463,
		18464,
		18465 // DEREK
	};
	
	private static final int HOLY_WATER = 2358;
	
	// TODO: Find retail strings.
	private static final String MSG = "The holy water affects Remnants Ghost. You have freed his soul.";
	private static final String MSG_DEREK = "The holy water affects Derek. You have freed his soul.";
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		npc.setIsMortal(false);
		return super.onSpawn(npc);
	}
	
	@Override
	public final String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (skill.getId() == HOLY_WATER)
		{
			if (!npc.isDead())
			{
				if ((targets.length > 0) && (targets[0] == npc))
				{
					if (npc.getCurrentHp() < (npc.getMaxHp() * 0.02)) // Lower, than 2%
					{
						npc.doDie(caster);
						if (npc.getNpcId() == 18465)
						{
							caster.sendMessage(MSG_DEREK);
						}
						else
						{
							caster.sendMessage(MSG);
						}
					}
				}
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	// Do not override onKill for Derek here. Let's make global Hellbound manipulations in Engine where it is possible.
	
	public Remnants()
	{
		super(-1, Remnants.class.getSimpleName(), "hellbound");
		for (int npcId : NPCS)
		{
			SpawnNpcs(npcId);
			SkillSeeNpcs(npcId);
		}
	}
}
