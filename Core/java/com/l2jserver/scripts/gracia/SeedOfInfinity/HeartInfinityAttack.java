/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.scripts.gracia.SeedOfInfinity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.l2jserver.Config;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.instancemanager.InstanceManager.InstanceWorld;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.instancemanager.gracia.SoIManager;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Instance;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

public class HeartInfinityAttack extends SeedOfInfinityController
{
	private class HIAWorld extends InstanceWorld
	{
		public List<L2Npc> npcList = new ArrayList<>();
		public List<L2Npc> startroom = new ArrayList<>();
		public List<L2Npc> deadTumors = new ArrayList<>();
		protected L2Npc ekimus;
		protected L2Npc deadTumor;
		protected List<L2Npc> hounds = new ArrayList<>(2);
		public int tumorCount = 0;
		public boolean isBossAttacked = false;
		public long startTime = 0;
		protected ScheduledFuture<?> timerTask;
		
		public synchronized void addTumorCount(int value)
		{
			tumorCount += value;
		}
		
		public synchronized void addTag(int value)
		{
			tag += value;
		}
		
		public HIAWorld()
		{
			tag = -1;
		}
	}
	
	private long tumorRespawnTime;
	private boolean conquestBegun = false;
	protected boolean conquestEnded = false;
	private boolean houndBlocked = false;
	
	public HeartInfinityAttack()
	{
		super(-1, HeartInfinityAttack.class.getSimpleName(), "Heart Infinity Attack");
		StartNpcs(SeedOfInfinityController.GATEKEEPER_OF_ABYSS_BLUE, SeedOfInfinityController.DESTROYED_TUMOR_2, SeedOfInfinityController.DESTROYED_TUMOR_3_START);
		TalkNpcs(SeedOfInfinityController.GATEKEEPER_OF_ABYSS_BLUE, SeedOfInfinityController.DESTROYED_TUMOR_2, SeedOfInfinityController.DESTROYED_TUMOR_3_START);
		SpawnNpcs(SeedOfInfinityController.HIA_NOT_MOVE);
		AggroRangeEnterNpcs(SeedOfInfinityController.WARD_OF_DEATH_2, SeedOfInfinityController.WARD_OF_DEATH_1);
		AttackNpcs(SeedOfInfinityController.EKIMUS);
		KillNpcs(SeedOfInfinityController.TUMOR_OF_DEATH_ALIVE_81, SeedOfInfinityController.EKIMUS, SeedOfInfinityController.SOUL_COFIN_2);
		
		tumorRespawnTime = 180 * 1000;
	}
	
	private void teleportPlayer(L2PcInstance player, int[] coords, int instanceId)
	{
		SeedOfInfinityController.removeBuffs(player);
		player.setInstanceId(instanceId);
		player.teleToLocation(coords[0], coords[1], coords[2]);
	}
	
	private boolean checkConditions(L2PcInstance player)
	{
		L2Party party = player.getParty();
		if (party == null)
		{
			player.sendPacket(SystemMessageId.NOT_IN_PARTY_CANT_ENTER);
			return false;
		}
		if (party.getLeader() != player)
		{
			player.sendPacket(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER);
			return false;
		}
		if ((party.getCommandChannel() == null) || (party.getCommandChannel().getChannelLeader() != player))
		{
			player.sendMessage("Only a channel leader can try to enter");
			return false;
		}
		if ((party.getCommandChannel().getMembers().size() < Config.HEART_ATTACK_MIN_PLAYERS) || (party.getCommandChannel().getMembers().size() > Config.HEART_ATTACK_MAX_PLAYERS))
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_S_LEVEL_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED);
			party.getCommandChannel().broadcastPacket(sm);
			return false;
		}
		for (L2PcInstance partyMember : party.getCommandChannel().getMembers())
		{
			if ((partyMember.getLevel() < Config.HEART_INFINITY_ATTACK_MIN_LV) || (partyMember.getLevel() > Config.HEART_INFINITY_ATTACK_MAX_LV))
			{
				SystemMessage sm = SystemMessage.getSystemMessage(2097);
				sm.addPcName(partyMember);
				party.getCommandChannel().broadcastPacket(sm);
				return false;
			}
			if (!Util.checkIfInRange(1000, player, partyMember, true))
			{
				SystemMessage sm = SystemMessage.getSystemMessage(2096);
				sm.addPcName(partyMember);
				party.getCommandChannel().broadcastPacket(sm);
				return false;
			}
			Long reentertime = InstanceManager.getInstance().getInstanceTime(partyMember.getObjectId(), SeedOfInfinityController.INSTANCE_ID_HEART_INFINITY_ATTACK);
			if (System.currentTimeMillis() < reentertime)
			{
				SystemMessage sm = SystemMessage.getSystemMessage(2100);
				sm.addPcName(partyMember);
				party.getCommandChannel().broadcastPacket(sm);
				return false;
			}
		}
		return true;
	}
	
	protected void enterInstance(L2PcInstance player, String template, int[] coords)
	{
		InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
		if (world != null)
		{
			if (!(world instanceof HIAWorld))
			{
				player.sendPacket(SystemMessageId.YOU_HAVE_ENTERED_ANOTHER_INSTANT_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON);
				return;
			}
			teleportPlayer(player, coords, world.getInstanceId());
			return;
		}
		if (checkConditions(player))
		{
			world = new HIAWorld();
			world.setInstanceId(InstanceManager.getInstance().createDynamicInstance(template));
			world.setTemplateId(SeedOfInfinityController.INSTANCE_ID_HEART_INFINITY_ATTACK);
			world.setStatus(0);
			InstanceManager.getInstance().addWorld(world);
			_log.info("Heart Infinity Attack started " + template + " Instance: " + world.getInstanceId() + " created by player: " + player.getName());
			if ((player.getParty() == null) || (player.getParty().getCommandChannel() == null))
			{
				teleportPlayer(player, coords, world.getInstanceId());
				world.addAllowed(player.getObjectId());
			}
			else
			{
				for (L2PcInstance partyMember : player.getParty().getCommandChannel().getMembers())
				{
					teleportPlayer(partyMember, coords, world.getInstanceId());
					world.addAllowed(partyMember.getObjectId());
					if (partyMember.getQuestState(HeartInfinityAttack.class.getSimpleName()) == null)
					{
						newQuestState(partyMember);
					}
				}
			}
			broadCastPacket(((HIAWorld) world), new ExShowScreenMessage("You will participate. Be prepared for anything.", 8000));
			L2Npc npc = addSpawn(SeedOfInfinityController.DESTROYED_TUMOR_3_START, -179376, 206111, -15538, 16384, false, 0, false, ((HIAWorld) world).getInstanceId());
			((HIAWorld) world).startroom.add(npc);
		}
	}
	
	protected void notifyEchmusEntrance(final HIAWorld world)
	{
		if (conquestBegun)
		{
			return;
		}
		
		conquestBegun = true;
		ThreadPoolManager.getInstance().scheduleGeneral(() ->
		{
			for (int objId : world.getAllowed())
			{
				L2PcInstance player = L2World.getInstance().getPlayer(objId);
				player.showQuestMovie(2);
			}
			
			ThreadPoolManager.getInstance().scheduleGeneral(() -> conquestBegins(world), 62500);
		}, 20000);
	}
	
	protected void conquestBegins(HIAWorld world)
	{
		if (!world.startroom.isEmpty())
		{
			for (L2Npc npc : world.startroom)
			{
				if (npc != null)
				{
					npc.deleteMe();
				}
			}
			world.startroom.clear();
		}
		for (int[] spawn : SeedOfInfinityController.ROOMS_TUMORS_HIA)
		{
			for (int i = 0; i < spawn[6]; i++)
			{
				L2Npc npc = addSpawn(spawn[0], spawn[1], spawn[2], spawn[3], spawn[4], false, 0, false, world.getInstanceId());
				npc.setCurrentHp(npc.getMaxHp() * .5);
				world.npcList.add(npc);
			}
		}
		for (int[] spawn : SeedOfInfinityController.ROOMS_MOBS_HIA)
		{
			for (int i = 0; i < spawn[6]; i++)
			{
				L2Npc npc = addSpawn(spawn[0], spawn[1], spawn[2], spawn[3], spawn[4], false, 0, false, world.getInstanceId());
				npc.getSpawn().setRespawnDelay(spawn[5]);
				npc.getSpawn().setAmount(1);
				if (spawn[5] > 0)
				{
					npc.getSpawn().startRespawn();
				}
				else
				{
					npc.getSpawn().stopRespawn();
				}
			}
		}
		InstanceManager.getInstance().getInstance(world.getInstanceId()).getDoor(14240102).openMe();
		broadCastPacket(world, new ExShowScreenMessage("You can hear the undead of Ekimus rushing toward you. It has now begun!", 8000));
		world.ekimus = addSpawn(SeedOfInfinityController.EKIMUS, -179537, 208854, -15504, 16384, false, 0, false, world.getInstanceId());
		CreatureSay cs = new CreatureSay(world.ekimus.getObjectId(), Say2.SHOUT, world.ekimus.getName(), "I shall accept your challenge! Come and die in the arms of immortality!");
		world.ekimus.broadcastPacket(cs);
		world.hounds.add(addSpawn(SeedOfInfinityController.HOUND, -179224, 209624, -15504, 16384, false, 0, false, world.getInstanceId()));
		world.hounds.add(addSpawn(SeedOfInfinityController.HOUND, -179880, 209464, -15504, 16384, false, 0, false, world.getInstanceId()));
		world.startTime = System.currentTimeMillis();
		world.timerTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new TimerTask(world), 298 * 1000, 5 * 60 * 1000);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		InstanceWorld tmpworld = InstanceManager.getInstance().getPlayerWorld(player);
		if (tmpworld instanceof HIAWorld)
		{
			HIAWorld world = (HIAWorld) tmpworld;
			switch (event)
			{
				case "warpechmus":
					broadCastPacket(world, new ExShowScreenMessage(player.getParty().getPartyLeaderName() + " party has moved to a different location via Tumor!", 8000));
					for (L2PcInstance partyMember : player.getParty().getPartyMembers())
					{
						if (Util.checkIfInRange(10000, player, partyMember, true))
						{
							partyMember.teleToLocation(-179548, 209584, -15504, true);
						}
					}
					notifyEchmusEntrance(world);
					break;
				case "reenterechmus":
					player.destroyItemByItemId("SOI", SeedOfInfinityController.TEARS_OF_A_FREED_SOUL, 3, player, true);
					notifyEkimusRoomEntrance(world);
					for (L2PcInstance partyMember : player.getParty().getPartyMembers())
					{
						if (Util.checkIfInRange(10000, player, partyMember, true))
						{
							partyMember.teleToLocation(-179548, 209584, -15504, true);
						}
					}
					break;
				case "warp":
					L2Npc victim = null;
					victim = world.deadTumor;
					if (victim != null)
					{
						world.deadTumors.add(victim);
					}
					player.destroyItemByItemId("SOI", SeedOfInfinityController.TEARS_OF_A_FREED_SOUL, 1, player, true);
					Location loc = world.deadTumors.get(Rnd.get(world.deadTumors.size())).getLocationXYZ();
					if (loc != null)
					{
						broadCastPacket(world, new ExShowScreenMessage(player.getParty().getPartyLeaderName() + " party has moved to a different location via Tumor!", 8000));
						for (L2PcInstance partyMember : player.getParty().getPartyMembers())
						{
							if (Util.checkIfInRange(10000, player, partyMember, true))
							{
								partyMember.teleToLocation(loc, true);
							}
						}
					}
					break;
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		int npcId = npc.getNpcId();
		QuestState st = player.getQuestState(HeartInfinityAttack.class.getSimpleName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		if (npcId == SeedOfInfinityController.GATEKEEPER_OF_ABYSS_BLUE)
		{
			enterInstance(player, SeedOfInfinityController.HEART_INFINITY_ATTACK_XML, SeedOfInfinityController.ENTER_TELEPORT_HIA);
		}
		return "";
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
		if (tmpworld instanceof HIAWorld)
		{
			final HIAWorld world = (HIAWorld) tmpworld;
			switch (npc.getNpcId())
			{
				case SeedOfInfinityController.WARD_OF_DEATH_1:
					for (int i = 0; i < Rnd.get(1, 4); i++)
					{
						spawnNpc(SeedOfInfinityController.GROUP_SPAWN_LIST_HIA_HID[Rnd.get(SeedOfInfinityController.GROUP_SPAWN_LIST_HIA_HID.length)], npc.getLocationXYZ(), 0, world.getInstanceId());
					}
					npc.doDie(npc);
					break;
				case SeedOfInfinityController.WARD_OF_DEATH_2:
					for (int i = 0; i < Rnd.get(1, 4); i++)
					{
						spawnNpc(SeedOfInfinityController.GROUP_SPAWN_LIST_HIA_HID[Rnd.get(SeedOfInfinityController.GROUP_SPAWN_LIST_HIA_HID.length)], npc.getLocationXYZ(), 0, world.getInstanceId());
					}
					npc.doDie(npc);
					break;
			}
		}
		return super.onAggroRangeEnter(npc, player, isSummon);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, L2Skill skill)
	{
		InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
		if (tmpworld instanceof HIAWorld)
		{
			final HIAWorld world = (HIAWorld) tmpworld;
			if (!world.isBossAttacked)
			{
				world.isBossAttacked = true;
				Calendar reenter = Calendar.getInstance();
				reenter.add(Calendar.HOUR, SeedOfInfinityController.INSTANCE_PENALTY);
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.INSTANT_ZONE_FROM_HERE_S1_S_ENTRY_HAS_BEEN_RESTRICTED);
				sm.addInstanceName(tmpworld.getTemplateId());
				for (int objectId : tmpworld.getAllowed())
				{
					L2PcInstance player = L2World.getInstance().getPlayer(objectId);
					if ((player != null) && player.isOnline())
					{
						InstanceManager.getInstance().setInstanceTime(objectId, tmpworld.getTemplateId(), reenter.getTimeInMillis());
						player.sendPacket(sm);
					}
				}
			}
			if (npc.getNpcId() == SeedOfInfinityController.EKIMUS)
			{
				for (L2Npc mob : world.hounds)
				{
					((L2MonsterInstance) mob).addDamageHate(attacker, 0, 1000);
					mob.setRunning();
					mob.setTarget(attacker);
					mob.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		if (Util.contains(SeedOfInfinityController.HIA_NOT_MOVE, npc.getNpcId()))
		{
			npc.setIsNoRndWalk(true);
			npc.setIsImmobilized(true);
		}
		if (npc.getNpcId() == SeedOfInfinityController.HOUND)
		{
			npc.setIsNoRndWalk(true);
			npc.setIsImmobilized(true);
		}
		InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
		if (tmpworld instanceof HIAWorld)
		{
			HIAWorld world = (HIAWorld) tmpworld;
			if (npc.getNpcId() == SeedOfInfinityController.TUMOR_OF_DEATH_ALIVE_81)
			{
				world.addTumorCount(1);
			}
			if (npc.getNpcId() == SeedOfInfinityController.DESTROYED_TUMOR_2)
			{
				world.addTag(1);
			}
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
		if (tmpworld instanceof HIAWorld)
		{
			HIAWorld world = (HIAWorld) tmpworld;
			Location loc = npc.getLocationXYZ();
			if (npc.getNpcId() == SeedOfInfinityController.TUMOR_OF_DEATH_ALIVE_81)
			{
				world.addTumorCount(-1);
				npc.dropItem(player, SeedOfInfinityController.TEARS_OF_A_FREED_SOUL, Rnd.get(2, 5));
				npc.deleteMe();
				world.deadTumor = spawnNpc(SeedOfInfinityController.DESTROYED_TUMOR_2, loc, 0, world.getInstanceId());
				world.deadTumors.add(world.deadTumor);
				ThreadPoolManager.getInstance().scheduleGeneral(new TumorRevival(world.deadTumor, world), tumorRespawnTime);
				ThreadPoolManager.getInstance().scheduleGeneral(new RegenerationCoffinSpawn(world.deadTumor, world), 20000);
				if (world.tumorCount < 1)
				{
					houndBlocked = true;
					for (L2Npc hound : world.hounds)
					{
						hound.block();
					}
					broadCastPacket(world, new ExShowScreenMessage("With all connections to the tumor severed, Ekimus has lost its power to control the Feral Hound!", 8000));
				}
				else
				{
					broadCastPacket(world, new ExShowScreenMessage("The tumor inside that has provided energy to Ekimus is destroyed!", 8000));
				}
			}
			if (npc.getNpcId() == SeedOfInfinityController.EKIMUS)
			{
				conquestConclusion(world);
				SoIManager.notifyEkimusKill();
			}
			if (npc.getNpcId() == SeedOfInfinityController.SOUL_COFIN_2)
			{
				tumorRespawnTime += 8 * 1000;
			}
		}
		return "";
	}
	
	private class TumorRevival implements Runnable
	{
		private final L2Npc _deadTumor;
		private final HIAWorld _world;
		
		public TumorRevival(L2Npc deadTumor, HIAWorld world)
		{
			_deadTumor = world.deadTumor;
			_world = world;
		}
		
		@Override
		public void run()
		{
			if (conquestEnded)
			{
				return;
			}
			L2Npc alivetumor = spawnNpc(SeedOfInfinityController.TUMOR_OF_DEATH_ALIVE_81, _deadTumor.getLocationXYZ(), 0, _world.getInstanceId());
			alivetumor.setCurrentHp(alivetumor.getMaxHp() * .25);
			notifyTumorRevival(_world);
			_world.npcList.add(alivetumor);
			_deadTumor.deleteMe();
			_world.addTag(-1);
		}
	}
	
	private class TimerTask implements Runnable
	{
		private final HIAWorld _world;
		
		TimerTask(HIAWorld world)
		{
			_world = world;
		}
		
		@Override
		public void run()
		{
			long time = ((_world.startTime + (25 * 60 * 1000L)) - System.currentTimeMillis()) / 60000;
			if (time == 0)
			{
				broadCastPacket(_world, new ExShowScreenMessage("You have failed... The instance will shortly expire.", 8000));
				if (_world != null)
				{
					conquestEnded = true;
					final Instance inst = InstanceManager.getInstance().getInstance(_world.getInstanceId());
					if (inst != null)
					{
						inst.removeNpcs();
						if (inst.getPlayers().isEmpty())
						{
							inst.setDuration(SeedOfInfinityController.EXIT_TIME);
						}
						else
						{
							inst.setDuration(SeedOfInfinityController.EXIT_TIME);
							inst.setEmptyDestroyTime(SeedOfInfinityController.EXIT_TIME);
						}
					}
				}
			}
			else
			{
				for (int[] spawn : SeedOfInfinityController.ROOMS_BOSSES_HIA)
				{
					for (int i = 0; i < spawn[6]; i++)
					{
						addSpawn(spawn[0], spawn[1], spawn[2], spawn[3], spawn[4], false, 0, false, _world.getInstanceId());
					}
				}
			}
		}
	}
	
	protected void notifyTumorRevival(HIAWorld world)
	{
		if ((world.tumorCount == 1) && houndBlocked)
		{
			houndBlocked = false;
			for (L2Npc hound : world.hounds)
			{
				hound.unblock();
			}
			broadCastPacket(world, new ExShowScreenMessage("Connection to the tumor restored, Ekimus has regained control over the Feral Hound...", 8000));
		}
		else
		{
			broadCastPacket(world, new ExShowScreenMessage("Tumor inside has been resurrected and started to energize Ekimus again...", 8000));
		}
	}
	
	private class RegenerationCoffinSpawn implements Runnable
	{
		private final L2Npc _deadTumor;
		private final HIAWorld _world;
		
		public RegenerationCoffinSpawn(L2Npc deadTumor, HIAWorld world)
		{
			_deadTumor = world.deadTumor;
			_world = world;
		}
		
		@Override
		public void run()
		{
			if (conquestEnded)
			{
				return;
			}
			for (int i = 0; i < 4; i++)
			{
				L2Npc worm = spawnNpc(SeedOfInfinityController.SOUL_COFIN_1, _deadTumor.getLocationXYZ(), 0, _world.getInstanceId());
				_world.npcList.add(worm);
			}
		}
	}
	
	public void notifyEkimusRoomEntrance(final HIAWorld world)
	{
		for (L2PcInstance ch : ZoneManager.getInstance().getZoneById(SeedOfInfinityController.EKIMUS_ZONE).getPlayersInside())
		{
			if (ch != null)
			{
				ch.teleToLocation(-179537, 211233, -15472, true);
			}
		}
		ThreadPoolManager.getInstance().scheduleGeneral(() -> broadCastPacket(world, new ExShowScreenMessage("Ekimus has sensed abnormal activity. The advancing party is forcefully expelled!", 8000)), 10000);
	}
	
	protected void conquestConclusion(HIAWorld world)
	{
		if (world.timerTask != null)
		{
			world.timerTask.cancel(false);
		}
		broadCastPacket(world, new ExShowScreenMessage("Congratulations! You have succeeded! The instance will shortly expire.", 8000));
		conquestEnded = true;
		final Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
		if (inst != null)
		{
			inst.removeNpcs();
			if (inst.getPlayers().isEmpty())
			{
				inst.setDuration(SeedOfInfinityController.EXIT_TIME);
			}
			else
			{
				inst.setDuration(SeedOfInfinityController.EXIT_TIME);
				inst.setEmptyDestroyTime(SeedOfInfinityController.EXIT_TIME);
			}
		}
	}
	
	protected void broadCastPacket(HIAWorld world, L2GameServerPacket packet)
	{
		for (int objId : world.getAllowed())
		{
			L2PcInstance player = L2World.getInstance().getPlayer(objId);
			if ((player != null) && player.isOnline() && (player.getInstanceId() == world.getInstanceId()))
			{
				player.sendPacket(packet);
			}
		}
	}
}