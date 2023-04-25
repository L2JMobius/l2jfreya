package com.l2jserver.scripts.ai;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * @author L2jPrivateDevelopersTeam
 */
public class Golkonda extends L2AttackableAIScript
{
	private static final int GOLKONDA = 25126;
	
	public Golkonda()
	{
		super(-1, Golkonda.class.getSimpleName(), "ai");
		registerMobs(GOLKONDA);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (!npc.isInsideRadius(116263, 15916, 6992, 700, true, false))
		{
			attacker.sendMessage("Golkonda: I too far from my spawn point. I returning.");
			npc.teleToLocation(116263, 15916, 6992);
			npc.getStatus().setCurrentHp(npc.getMaxHp());
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
}
