package com.l2jserver.scripts.ai;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * @author L2jPrivateDevelopersTeam
 */
public class Kernon extends L2AttackableAIScript
{
	private static final int KERNON = 25054;
	
	public Kernon()
	{
		super(-1, Kernon.class.getSimpleName(), "ai");
		AttackNpcs(KERNON);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (!npc.isInsideRadius(113432, 16403, 3960, 1000, true, false))
		{
			attacker.sendMessage("Kernon: I too far from my spawn point. I returning.");
			npc.teleToLocation(113432, 16403, 3960);
			npc.getStatus().setCurrentHp(npc.getMaxHp());
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
}
