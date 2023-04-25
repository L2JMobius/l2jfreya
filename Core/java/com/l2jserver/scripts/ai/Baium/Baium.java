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
package com.l2jserver.scripts.ai.Baium;

import static com.l2jserver.gameserver.ai.CtrlIntention.AI_INTENTION_FOLLOW;
import static com.l2jserver.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import com.l2jserver.Config;
import com.l2jserver.gameserver.GeoData;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.instancemanager.GrandBossManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Playable;
import com.l2jserver.gameserver.model.actor.instance.L2DecoyInstance;
import com.l2jserver.gameserver.model.actor.instance.L2GrandBossInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.QuestTimer;
import com.l2jserver.gameserver.model.zone.type.L2BossZone;
import com.l2jserver.gameserver.network.serverpackets.Earthquake;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.network.serverpackets.MoveToPawn;
import com.l2jserver.gameserver.network.serverpackets.PlaySound;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.templates.StatsSet;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.scripts.L2AttackableAIScript;
import com.l2jserver.util.Rnd;

/***
 * @author L2jPDT
 */
public class Baium extends L2AttackableAIScript
{
	// NPCs
	private static final int STONE_BAIUM = 29025;
	private static final int ANGELIC_VORTEX = 31862;
	private static final int TELEPORT_OUT_CUBIC = 29055;
	private static final int LIVE_BAIUM = 29020;
	private static final int ARCHANGEL = 29021;
	
	// Baium Status
	private static final byte STATUS_STONE = 0; // Baium is in the stone version, waiting to be woken up. Entry is unlocked
	private static final byte STATUS_FIGHTING = 1; // Baium is awake and fighting. Entry is locked.
	private static final byte STATUS_DEAD = 2; // Baium has been killed and has not yet spawned. Entry is locked
	
	// Baium Skills
	private static final int BAIUM_GENERAL_ATTACK = 4127;
	private static final int WIND_OF_FORCE = 4128;
	private static final int EARTH_QUAKE = 4129;
	private static final int STRIKING_OF_THUDERBOLT = 4130;
	private static final int STUN = 4131;
	private static final int SPEAR_POUND_THE_GROUND = 4132;
	private static final int ANGEL_HEAL = 4133;
	private static final int BAIUM_HEAL = 4135;
	// private static final int BAIUM_GIFT = 4136;
	private static final int HINDER_STRIDER = 4258;
	
	// Angel Locations
	private static final Location[] ANGEL_LOCATION =
	{
		new Location(115792, 16608, 10136, 0),
		new Location(115168, 17200, 10136, 0),
		new Location(115780, 15564, 10136, 13620),
		new Location(114880, 16236, 10136, 5400),
		new Location(114239, 17168, 10136, -1992)
	};
	
	// ETC
	private long _LastAttackVsBaiumTime = 0;
	private final List<L2Npc> _Minions = new ArrayList<>(5);
	private final List<L2Npc> _Baium = new ArrayList<>();// Maybe in future usage
	private L2Character _target;
	private L2Skill _skill;
	
	// Zone
	private L2BossZone _Zone;
	
	public Baium()
	{
		super(-1, Baium.class.getSimpleName(), "ai");
		registerMobs(LIVE_BAIUM);
		StartNpcs(STONE_BAIUM, ANGELIC_VORTEX);
		TalkNpcs(STONE_BAIUM, ANGELIC_VORTEX);
		_Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
		StatsSet info = GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM);
		int status = GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM);
		if (status == STATUS_DEAD)
		{
			long temp = (info.getLong("respawn_time") - System.currentTimeMillis());
			if (temp > 0)
			{
				startQuestTimer("BAIUM_READY", temp, null, null);
			}
			else
			{
				addSpawn(STONE_BAIUM, 116033, 17447, 10104, 40188, false, 0);
				GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, STATUS_STONE);
			}
		}
		else if (status == STATUS_FIGHTING)
		{
			int loc_x = info.getInt("loc_x");
			int loc_y = info.getInt("loc_y");
			int loc_z = info.getInt("loc_z");
			int heading = info.getInt("heading");
			final int hp = info.getInt("currentHP");
			final int mp = info.getInt("currentMP");
			L2GrandBossInstance baium = (L2GrandBossInstance) addSpawn(LIVE_BAIUM, loc_x, loc_y, loc_z, heading, false, 0);
			GrandBossManager.getInstance().addBoss(baium);
			final L2Npc _baium = baium;
			ThreadPoolManager.getInstance().scheduleGeneral(() ->
			{
				try
				{
					_baium.setCurrentHpMp(hp, mp);
					_baium.setIsInvul(true);
					_baium.setIsImmobilized(true);
					_baium.setRunning();
					_baium.broadcastPacket(new SocialAction(_baium, 2));
					startQuestTimer("BAIUM_WAKE_UP", 15000, _baium, null);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}, 100L);
		}
		else
		{
			addSpawn(STONE_BAIUM, 116033, 17447, 10104, 40188, false, 0);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		switch (event)
		{
			case "EXIT":
			{
				player.teleToLocation(108784, 16000, -4928);
				break;
			}
			case "BAIUM_READY":
			{
				GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, STATUS_STONE);
				addSpawn(STONE_BAIUM, 116033, 17447, 10104, 40188, false, 0);
				break;
			}
			case "BAIUM_WAKE_UP":
			{
				if (npc.getNpcId() == LIVE_BAIUM)
				{
					npc.broadcastPacket(new SocialAction(npc, 1));
					npc.broadcastPacket(new Earthquake(npc.getX(), npc.getY(), npc.getZ(), 40, 5));
					// start monitoring baium's inactivity
					_LastAttackVsBaiumTime = System.currentTimeMillis();
					startQuestTimer("BAIUM_DESPAWN", 60000, npc, null, true);
					startQuestTimer("RANGE_ATTACK_SKILLS", 500, npc, null, true);
					final L2Npc baium = npc;
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						try
						{
							baium.setIsInvul(false);
							baium.setIsImmobilized(false);
							for (L2Npc minion : _Minions)
							{
								minion.setShowSummonAnimation(false);
							}
						}
						catch (Exception e)
						{
							_log.log(Level.WARNING, "", e);
						}
					}, 11100L);
					for (L2PcInstance players : _Zone.getPlayersInside())
					{
						if (players.isHero())
						{
							_Zone.broadcastPacket(new ExShowScreenMessage("Not event the gods themselves could touch me, but you " + players.getName() + " dare challenge me ignorant mortal!", 4000));
						}
					}
					for (Location location : ANGEL_LOCATION)
					{
						final L2Npc angel = addSpawn(ARCHANGEL, location, false, 0, true);
						angel.setIsInvul(true);
						_Minions.add(angel);
					}
				}
				break;
			}
			case "BAIUM_DESPAWN":
			{
				if (npc.getNpcId() == LIVE_BAIUM)
				{
					// just in case the zone reference has been lost (somehow...), restore the reference
					if (_Zone == null)
					{
						_Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
					}
					if ((_LastAttackVsBaiumTime + 1800000) < System.currentTimeMillis())
					{
						npc.deleteMe(); // despawn the live-baium
						for (L2Npc minion : _Minions)
						{
							if (minion != null)
							{
								minion.getSpawn().stopRespawn();
								minion.deleteMe();
							}
						}
						_Minions.clear();
						addSpawn(STONE_BAIUM, 116033, 17447, 10104, 40188, false, 0); // spawn stone-baium
						GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, STATUS_STONE); // mark that Baium is not awake any more
						_Zone.oustAllPlayers();
						cancelQuestTimer("BAIUM_DESPAWN", npc, null);
					}
					else if (((_LastAttackVsBaiumTime + 300000) < System.currentTimeMillis()) && (npc.getCurrentHp() < ((npc.getMaxHp() * 3) / 4.0)))
					{
						npc.setIsCastingNow(false); // just in case
						npc.setTarget(npc);
						npc.doCast(SkillTable.getInstance().getInfo(BAIUM_HEAL, 1));
						npc.setIsCastingNow(true);
					}
					else if (!_Zone.isInsideZone(npc))
					{
						npc.teleToLocation(116033, 17447, 10104);
					}
				}
				break;
			}
			case "DESPAWN_MINIONS":
			{
				for (L2Npc minion : _Minions)
				{
					if (minion != null)
					{
						minion.getSpawn().stopRespawn();
						minion.deleteMe();
					}
				}
				_Minions.clear();
				break;
			}
			case "ABORT_FIGHT":
			{
				if (getStatus() == STATUS_FIGHTING)
				{
					setStatus(STATUS_STONE);
					this.notifyEvent("DESPAWN_MINIONS", null, player);
					for (L2Npc baium : _Baium)
					{
						if (baium != null)
						{
							baium.getSpawn().stopRespawn();
							baium.deleteMe();
						}
					}
					_Baium.clear();
					addSpawn(STONE_BAIUM, 116033, 17447, 10104, 40188, false, 0);
					player.sendMessage("Baium: Aborting attack.");
				}
				else
				{
					player.sendMessage("Baium: You cannot abort attack if is not in fight.");
				}
				break;
			}
			case "RESPAWN_BAIUM":
			{
				if (getStatus() == STATUS_DEAD)
				{
					setRespawn(0);
					setStatus(STATUS_STONE);
					addSpawn(STONE_BAIUM, 116033, 17447, 10104, 40188, false, 0);
					player.sendMessage("Baium: has been respawned.");
				}
				else
				{
					player.sendMessage("Baium: You cant respawn Baium while is alive!");
				}
				break;
			}
			case "RANGE_ATTACK_SKILLS":
			{
				if (!npc.isCastingNow())
				{
					callSkillAI(npc);
				}
				break;
			}
			case "RANDOM_TARGET":
			{
				if (_target == null)
				{
					cancelQuestTimer("RANDOM_TARGET", npc, null);
					startQuestTimer("RANDOM_TARGET", 20000, npc, null);
				}
				else
				{
					_target = getRandomTarget(npc);
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		int npcId = npc.getNpcId();
		String htmltext = "";
		if (_Zone == null)
		{
			_Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
			return "<html><body>Angelic Vortex:<br>You may not enter while admin disabled this zone</body></html>";
		}
		if ((npcId == STONE_BAIUM) && (GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM) == STATUS_STONE))
		{
			if (_Zone.isPlayerAllowed(player))
			{
				// once Baium is awaken, no more people may enter until he dies, the server reboots, or
				// 30 minutes pass with no attacks made against Baium.
				GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, STATUS_FIGHTING);
				npc.deleteMe();
				L2GrandBossInstance baium = (L2GrandBossInstance) addSpawn(LIVE_BAIUM, npc, true);
				_Baium.add(baium);
				GrandBossManager.getInstance().addBoss(baium);
				final L2Npc _baium = baium;
				ThreadPoolManager.getInstance().scheduleGeneral(() ->
				{
					try
					{
						_baium.setIsInvul(true);
						_baium.setRunning();
						_baium.broadcastPacket(new SocialAction(_baium, 2));
						startQuestTimer("BAIUM_WAKE_UP", 15000, _baium, null);
						_baium.setShowSummonAnimation(false);
					}
					catch (Throwable e)
					{
						_log.log(Level.WARNING, "", e);
					}
				}, 100L);
			}
			else
			{
				htmltext = "Conditions are not right to wake up Baium";
			}
		}
		else if (npcId == ANGELIC_VORTEX)
		{
			if (player.isFlying())
			{
				// print "Player "+player.getName()+" attempted to enter Baium's lair while flying!";
				return "<html><body>Angelic Vortex:<br>You may not enter while flying a wyvern</body></html>";
			}
			
			if ((GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM) == STATUS_STONE) && (player.getQuestState(Baium.class.getSimpleName()).getQuestItemsCount(4295) > 0)) // bloody fabric
			{
				player.getQuestState(Baium.class.getSimpleName()).takeItems(4295, 1);
				// allow entry for the player for the next 30 secs (more than enough time for the TP to happen)
				// Note: this just means 30secs to get in, no limits on how long it takes before we get out.
				_Zone.allowPlayerEntry(player, 30);
				player.teleToLocation(113100, 14500, 10077);
			}
			else
			{
				npc.showChatWindow(player, 1);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onSpellFinished(L2Npc npc, L2PcInstance player, L2Skill skill)
	{
		if (npc.isInvul())
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			return null;
		}
		else if ((npc.getNpcId() == LIVE_BAIUM) && !npc.isInvul())
		{
			if (!npc.isCastingNow())
			{
				callSkillAI(npc);
			}
		}
		return super.onSpellFinished(npc, player, skill);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		npc.disableCoreAI(true);
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (!_Zone.isInsideZone(attacker))
		{
			attacker.reduceCurrentHp(attacker.getCurrentHp(), attacker, false, false, null);
			return super.onAttack(npc, attacker, damage, isPet);
		}
		if (npc.isInvul())
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			return super.onAttack(npc, attacker, damage, isPet);
		}
		else if ((npc.getNpcId() == LIVE_BAIUM) && !npc.isInvul())
		{
			if (attacker.getMountType() == 1)
			{
				if ((attacker.getMountType() == 1) && !attacker.isAffected(HINDER_STRIDER))
				{
					npc.setTarget(attacker);
					npc.doCast(SkillTable.getInstance().getSkill(HINDER_STRIDER, 1));
				}
			}
			// update a variable with the last action against baium
			_LastAttackVsBaiumTime = System.currentTimeMillis();
			if (!npc.isCastingNow())
			{
				callSkillAI(npc);
			}
		}
		else
		{
			final L2Attackable mob = (L2Attackable) npc;
			final L2Character mostHated = mob.getMostHated();
			
			if ((getRandom(100) < 10) && mob.checkDoCastConditions(SkillTable.getInstance().getSkill(SPEAR_POUND_THE_GROUND, 1)))
			{
				if ((mostHated != null) && (npc.calculateDistance(mostHated.getLocationXYZ(), true, false) < 1000) && _Zone.isCharacterInZone(mostHated))
				{
					mob.setTarget(mostHated);
					mob.doCast(SkillTable.getInstance().getSkill(SPEAR_POUND_THE_GROUND, 1));
				}
				else if (_Zone.isCharacterInZone(attacker))
				{
					mob.setTarget(attacker);
					mob.doCast(SkillTable.getInstance().getSkill(SPEAR_POUND_THE_GROUND, 1));
				}
			}
			
			if ((getRandom(100) < 5) && (npc.getCurrentHp() < (npc.getMaxHp() * 0.5)) && mob.checkDoCastConditions(SkillTable.getInstance().getSkill(ANGEL_HEAL, 1)))
			{
				npc.setTarget(npc);
				npc.doCast(SkillTable.getInstance().getSkill(ANGEL_HEAL, 1));
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		cancelQuestTimer("BAIUM_DESPAWN", npc, null);
		npc.broadcastPacket(new PlaySound(1, "BS01_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
		// spawn the "Teleportation Cubic" for 15 minutes (to allow players to exit the lair)
		addSpawn(TELEPORT_OUT_CUBIC, 115203, 16620, 10078, 0, false, 900000); //// should we teleport everyone out if the cubic despawns??
		// "lock" baium for 5 days and 1 to 8 hours [i.e. 432,000,000 + 1*3,600,000 + random-less-than(8*3,600,000) millisecs]
		long respawnTime = (long) Config.INTERVAL_OF_BAIUM_SPAWN + Rnd.get(Config.RANDOM_OF_BAIUM_SPAWN);
		GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, STATUS_DEAD);
		startQuestTimer("BAIUM_READY", respawnTime, null, null);
		// also save the respawn time so that the info is maintained past reboots
		StatsSet info = GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM);
		info.set("respawn_time", (System.currentTimeMillis()) + respawnTime);
		GrandBossManager.getInstance().setStatsSet(LIVE_BAIUM, info);
		for (L2Npc minion : _Minions)
		{
			if (minion != null)
			{
				minion.getSpawn().stopRespawn();
				minion.deleteMe();
			}
		}
		_Minions.clear();
		if (getQuestTimer("RANGE_ATTACK_SKILLS", npc, null) != null)
		{
			getQuestTimer("RANGE_ATTACK_SKILLS", npc, null).cancel();
		}
		return super.onKill(npc, killer, isPet);
	}
	
	public L2Character getRandomTarget(L2Npc npc)
	{
		List<L2Character> result = new ArrayList<>();
		Collection<L2Object> objs = npc.getKnownList().getKnownObjects().values();
		{
			for (L2Object obj : objs)
			{
				if ((obj instanceof L2Playable) || (obj instanceof L2DecoyInstance))
				{
					if (obj instanceof L2PcInstance)
					{
						if (((L2PcInstance) obj).getAppearance().getInvisible())
						{
							continue;
						}
					}
					
					if (((((L2Character) obj).getZ() < (npc.getZ() - 100)) && (((L2Character) obj).getZ() > (npc.getZ() + 100))) || !(GeoData.getInstance().canSeeTarget(((L2Character) obj).getX(), ((L2Character) obj).getY(), ((L2Character) obj).getZ(), npc.getX(), npc.getY(), npc.getZ())))
					{
						continue;
					}
				}
				if ((obj instanceof L2Playable) || (obj instanceof L2DecoyInstance))
				{
					if (Util.checkIfInRange(9000, npc, obj, true) && !((L2Character) obj).isDead())
					{
						result.add((L2Character) obj);
					}
				}
			}
		}
		if (result.isEmpty())
		{
			for (L2Npc minion : _Minions)
			{
				if (minion != null)
				{
					result.add(minion);
				}
			}
		}
		if (result.isEmpty())
		{
			return null;
		}
		
		Object[] characters = result.toArray();
		QuestTimer timer = getQuestTimer("RANDOM_TARGET", npc, null);
		if (timer != null)
		{
			timer.cancel();
		}
		startQuestTimer("RANDOM_TARGET", 20000, npc, null);
		
		L2Character target = (L2Character) characters[Rnd.get(characters.length)];
		
		return target;
	}
	
	public synchronized void callSkillAI(L2Npc npc)
	{
		if (npc.isInvul() || npc.isCastingNow())
		{
			return;
		}
		
		if ((_target == null) || _target.isDead() || !(_Zone.isInsideZone(_target)))
		{
			_target = getRandomTarget(npc);
			if (_target != null)
			{
				_skill = SkillTable.getInstance().getInfo(getRandomSkill(npc), 1);
			}
		}
		
		L2Character target = _target;
		L2Skill skill = _skill;
		if (skill == null)
		{
			skill = SkillTable.getInstance().getInfo(getRandomSkill(npc), 1);
		}
		if ((target == null) || target.isDead() || !(_Zone.isInsideZone(target)))
		{
			npc.setIsCastingNow(false);
			return;
		}
		
		if (Util.checkIfInRange(skill.getCastRange(), npc, target, true))
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			npc.setTarget(target);
			npc.setIsCastingNow(true);
			npc.isMovementDisabled();
			_target = null;
			_skill = null;
			if (getDist(skill.getCastRange()) > 0)
			{
				npc.broadcastPacket(new MoveToPawn(npc, target, getDist(skill.getCastRange())));
			}
			try
			{
				Thread.sleep(1000);
				npc.stopMove(null);
				npc.doCast(skill);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			npc.getAI().setIntention(AI_INTENTION_FOLLOW, target, null);
			npc.setIsCastingNow(false);
		}
	}
	
	public int getRandomSkill(L2Npc npc)
	{
		int skill;
		if (npc.getCurrentHp() > (npc.getMaxHp() * 0.75))
		{
			if (Rnd.get(100) < 10)
			{
				skill = WIND_OF_FORCE;
			}
			else if (Rnd.get(100) < 10)
			{
				skill = EARTH_QUAKE;
			}
			else
			{
				skill = BAIUM_GENERAL_ATTACK;
			}
		}
		else if (npc.getCurrentHp() > (npc.getMaxHp() * 0.50))
		{
			if (Rnd.get(100) < 10)
			{
				skill = STUN;
			}
			else if (Rnd.get(100) < 10)
			{
				skill = WIND_OF_FORCE;
			}
			else if (Rnd.get(100) < 10)
			{
				skill = EARTH_QUAKE;
			}
			else
			{
				skill = BAIUM_GENERAL_ATTACK;
			}
		}
		else if (npc.getCurrentHp() > (npc.getMaxHp() * 0.25))
		{
			if (Rnd.get(100) < 10)
			{
				skill = STRIKING_OF_THUDERBOLT;
			}
			else if (Rnd.get(100) < 10)
			{
				skill = STUN;
			}
			else if (Rnd.get(100) < 10)
			{
				skill = WIND_OF_FORCE;
			}
			else if (Rnd.get(100) < 10)
			{
				skill = EARTH_QUAKE;
			}
			else
			{
				skill = BAIUM_GENERAL_ATTACK;
			}
		}
		else if (Rnd.get(100) < 10)
		{
			skill = STRIKING_OF_THUDERBOLT;
		}
		else if (Rnd.get(100) < 10)
		{
			skill = STUN;
		}
		else if (Rnd.get(100) < 10)
		{
			skill = WIND_OF_FORCE;
		}
		else if (Rnd.get(100) < 10)
		{
			skill = EARTH_QUAKE;
		}
		else
		{
			skill = BAIUM_GENERAL_ATTACK;
		}
		return skill;
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (npc.isInvul())
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			return null;
		}
		npc.setTarget(caster);
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	public int getDist(int range)
	{
		int dist = 0;
		switch (range)
		{
			case -1:
				break;
			case 100:
				dist = 85;
				break;
			default:
				dist = range - 85;
				break;
		}
		return dist;
	}
	
	private int getStatus()
	{
		return GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM);
	}
	
	private void setRespawn(long respawnTime)
	{
		GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM).set("respawn_time", (System.currentTimeMillis() + respawnTime));
	}
	
	private void setStatus(int status)
	{
		GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, status);
	}
	
}