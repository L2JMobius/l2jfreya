package com.l2jserver.scripts.kamaloka;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * @author L2jPrivateDevelopersTeam
 */
public class OlAriosh extends L2AttackableAIScript
{
	private static final int ARIOSH = 18555;
	private static final int GUARD = 18556;
	private static L2Npc guard = null;
	private final Set<Integer> _lockedSpawns = ConcurrentHashMap.newKeySet();
	private final Map<Integer, Integer> _spawnedGuards = new ConcurrentHashMap<>();
	
	public OlAriosh()
	{
		super(-1, OlAriosh.class.getSimpleName(), "Kamaloka");
		AttackNpcs(ARIOSH);
		KillNpcs(ARIOSH, GUARD);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("time_to_spawn"))
		{
			if (!_spawnedGuards.containsKey(npc.getObjectId()))
			{
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 1, npc.getName(), "What are you doing ? Rather, help me!"));
				guard = addSpawn(GUARD, player.getX() + 100, player.getY() + 100, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
				_lockedSpawns.remove(npc.getObjectId());
				_spawnedGuards.put(guard.getObjectId(), npc.getObjectId());
				guard.setTarget(player);
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		if (npc.getNpcId() == ARIOSH)
		{
			if (!_spawnedGuards.containsKey(npc.getObjectId()))
			{
				if (!_lockedSpawns.contains(npc.getObjectId()))
				{
					startQuestTimer("time_to_spawn", 60000, npc, player);
					_lockedSpawns.add(npc.getObjectId());
				}
			}
		}
		return super.onAttack(npc, player, damage, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		switch (npc.getNpcId())
		{
			case GUARD:
				_spawnedGuards.remove(npc.getObjectId());
				break;
			case ARIOSH:
				_spawnedGuards.remove(guard.getObjectId());
				guard.decayMe();
				cancelQuestTimer("time_to_spawn", npc, killer);
				break;
		}
		return super.onKill(npc, killer, isPet);
	}
}