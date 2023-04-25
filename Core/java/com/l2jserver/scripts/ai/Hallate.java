package com.l2jserver.scripts.ai;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * @author L2jPrivateDevelopersTeam
 */
public class Hallate extends L2AttackableAIScript
{
	private static final int HALLATE = 25220;
	
	public Hallate()
	{
		super(-1, Hallate.class.getSimpleName(), "ai");
		AttackNpcs(HALLATE);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (!npc.isInsideRadius(113551, 17083, -2120, 2000, true, false))
		{
			attacker.sendMessage("Hallate: I too far from my spawn point. I returning.");
			npc.teleToLocation(113551, 17083, -2120);
			npc.getStatus().setCurrentHp(npc.getMaxHp());
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
}
