package com.l2jserver.scripts.ai;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * @author L2jPrivateDevelopersTeam
 */
public class Barakiel extends L2AttackableAIScript
{
	// private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY"); // for future usage
	
	private static final int BARAKIEL = 25325;
	
	public Barakiel()
	{
		super(-1, Barakiel.class.getSimpleName(), "ai");
		registerMobs(BARAKIEL);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (!npc.isInsideRadius(91008, -85904, -2736, 500, true, false))
		{
			attacker.sendMessage("Barakiel: I too far from my spawn point. I returning.");
			npc.teleToLocation(91008, -85904, -2736);
			npc.getStatus().setCurrentHp(npc.getMaxHp());
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
}
