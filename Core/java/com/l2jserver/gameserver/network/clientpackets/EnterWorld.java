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
package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.Announcements;
import com.l2jserver.gameserver.GameServerRestart;
import com.l2jserver.gameserver.GmListTable;
import com.l2jserver.gameserver.LoginServerThread;
import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.TaskPriority;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.datatables.AdminCommandAccessRights;
import com.l2jserver.gameserver.datatables.GMSkillTable;
import com.l2jserver.gameserver.datatables.MapRegionTable;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.ClanHallManager;
import com.l2jserver.gameserver.instancemanager.CoupleManager;
import com.l2jserver.gameserver.instancemanager.CursedWeaponsManager;
import com.l2jserver.gameserver.instancemanager.DimensionalRiftManager;
import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.instancemanager.FortSiegeManager;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.instancemanager.MailManager;
import com.l2jserver.gameserver.instancemanager.PetitionManager;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.instancemanager.SiegeManager;
import com.l2jserver.gameserver.instancemanager.TerritoryWarManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2ClassMasterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.CTF;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.model.entity.ClanHall;
import com.l2jserver.gameserver.model.entity.Couple;
import com.l2jserver.gameserver.model.entity.DM;
import com.l2jserver.gameserver.model.entity.Fort;
import com.l2jserver.gameserver.model.entity.FortSiege;
import com.l2jserver.gameserver.model.entity.L2Event;
import com.l2jserver.gameserver.model.entity.Siege;
import com.l2jserver.gameserver.model.entity.TvTEvent;
import com.l2jserver.gameserver.model.entity.TvTRoundEvent;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.Die;
import com.l2jserver.gameserver.network.serverpackets.EtcStatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.ExAutoSoulShot;
import com.l2jserver.gameserver.network.serverpackets.ExBasicActionList;
import com.l2jserver.gameserver.network.serverpackets.ExBirthdayPopup;
import com.l2jserver.gameserver.network.serverpackets.ExBrPremiumState;
import com.l2jserver.gameserver.network.serverpackets.ExGetBookMarkInfoPacket;
import com.l2jserver.gameserver.network.serverpackets.ExNoticePostArrived;
import com.l2jserver.gameserver.network.serverpackets.ExNotifyPremiumItem;
import com.l2jserver.gameserver.network.serverpackets.ExPCCafePointInfo;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.network.serverpackets.ExStorageMaxCount;
import com.l2jserver.gameserver.network.serverpackets.ExVoteSystemInfo;
import com.l2jserver.gameserver.network.serverpackets.FriendList;
import com.l2jserver.gameserver.network.serverpackets.HennaInfo;
import com.l2jserver.gameserver.network.serverpackets.ItemList;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.PledgeShowMemberListAll;
import com.l2jserver.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import com.l2jserver.gameserver.network.serverpackets.PledgeSkillList;
import com.l2jserver.gameserver.network.serverpackets.PledgeStatusChanged;
import com.l2jserver.gameserver.network.serverpackets.QuestList;
import com.l2jserver.gameserver.network.serverpackets.ShortCutInit;
import com.l2jserver.gameserver.network.serverpackets.SkillCoolTime;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.templates.item.L2Item;
import com.l2jserver.gameserver.util.Broadcast;
import com.l2jserver.scripts.quests.Q00255_Tutorial;

/**
 * Enter World Packet Handler
 * <p>
 * <p>
 * 0000: 03
 * <p>
 * packet format rev87 bddddbdcccccccccccccccccccc
 * <p>
 */
public class EnterWorld extends L2GameClientPacket
{
	private static final String _C__03_ENTERWORLD = "[C] 03 EnterWorld";
	private static Logger _log = Logger.getLogger(EnterWorld.class.getName());
	
	private final int[][] tracert = new int[5][4];
	boolean showClanNotice = false;
	
	public TaskPriority getPriority()
	{
		return TaskPriority.PR_URGENT;
	}
	
	@Override
	protected void readImpl()
	{
		readB(new byte[32]); // Unknown Byte Array
		readD(); // Unknown Value
		readD(); // Unknown Value
		readD(); // Unknown Value
		readD(); // Unknown Value
		readB(new byte[32]); // Unknown Byte Array
		readD(); // Unknown Value
		for (int i = 0; i < 5; i++)
		{
			for (int o = 0; o < 4; o++)
			{
				tracert[i][o] = readC();
			}
		}
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			_log.warning("EnterWorld failed! activeChar returned 'null'.");
			getClient().closeNow();
			return;
		}
		
		String[] adress = new String[5];
		for (int i = 0; i < 5; i++)
		{
			adress[i] = tracert[i][0] + "." + tracert[i][1] + "." + tracert[i][2] + "." + tracert[i][3];
		}
		
		LoginServerThread.getInstance().sendClientTracert(activeChar.getAccountName(), adress);
		
		getClient().setClientTracert(tracert);
		
		// Restore to instanced area if enabled
		if (Config.RESTORE_PLAYER_INSTANCE)
		{
			activeChar.setInstanceId(InstanceManager.getInstance().getPlayerInstance(activeChar.getObjectId()));
		}
		else
		{
			int instanceId = InstanceManager.getInstance().getPlayerInstance(activeChar.getObjectId());
			if (instanceId > 0)
			{
				InstanceManager.getInstance().getInstance(instanceId).removePlayer(activeChar.getObjectId());
			}
		}
		
		if (L2World.getInstance().findObject(activeChar.getObjectId()) != null)
		{
			if (Config.DEBUG)
			{
				_log.warning("User already exists in Object ID map! User " + activeChar.getName() + " is a character clone.");
			}
		}
		
		activeChar.sendWelcome(activeChar);
		sendPacket(SystemMessage.getSystemMessage(SystemMessageId.WELCOME_TO_LINEAGE));
		activeChar.setDressMeEnabled(false);
		activeChar.sendMessage("Dress Me: Disabled"); // Just for now announce on login
		
		L2Skill skill2 = SkillTable.getInstance().getInfo(Config.BACK_BUFF_ID, 1);
		activeChar.addSkill(skill2);
		
		activeChar.getVariables().set("@PLAYER_HELP", false);
		
		if (activeChar.getVariables().getBoolean("@TRADE_REFUSE", false))
		{
			activeChar.setTradeRefusal(true);
		}
		if (activeChar.getVariables().getBoolean("@PM_REFUSE", false))
		{
			activeChar.setSilenceMode(true);
		}
		if (activeChar.getVariables().getBoolean("@AUTO_SS", false))
		{
			ThreadPoolManager.getInstance().scheduleGeneral(() ->
			{
				verifyAndLoadShots(activeChar);
			}, 3000);
		}
		
		activeChar.setGainXp(activeChar.getVariables().getBoolean("@GAIN_XP_SP", false));
		
		if (activeChar.isPremiumAccount())
		{
			activeChar.sendPacket(new ExBrPremiumState(activeChar.getObjectId(), 1));
			activeChar.sendMessage("Premium Account: Active");
			_log.info("Premium Account: " + activeChar.getAccountName() + " and player " + activeChar.getName() + " enter to world.");
		}
		else
		{
			activeChar.sendPacket(new ExBrPremiumState(activeChar.getObjectId(), 0));
			activeChar.sendMessage("Premium Account: Deactivated");
		}
		
		// Apply special GM properties to the GM when entering
		if (activeChar.isGM())
		{
			if (Config.GM_STARTUP_INVULNERABLE && AdminCommandAccessRights.getInstance().hasAccess("admin_invul", activeChar.getAccessLevel()))
			{
				activeChar.setIsInvul(true);
			}
			
			if (Config.GM_STARTUP_INVISIBLE && AdminCommandAccessRights.getInstance().hasAccess("admin_invisible", activeChar.getAccessLevel()))
			{
				activeChar.getAppearance().setInvisible();
			}
			
			if (Config.GM_STARTUP_SILENCE && AdminCommandAccessRights.getInstance().hasAccess("admin_silence", activeChar.getAccessLevel()))
			{
				activeChar.setSilenceMode(true);
			}
			
			if (Config.GM_STARTUP_DIET_MODE && AdminCommandAccessRights.getInstance().hasAccess("admin_diet", activeChar.getAccessLevel()))
			{
				activeChar.setDietMode(true);
				activeChar.refreshOverloaded();
			}
			
			if (Config.GM_STARTUP_AUTO_LIST && AdminCommandAccessRights.getInstance().hasAccess("admin_gmliston", activeChar.getAccessLevel()))
			{
				GmListTable.getInstance().addGm(activeChar, false);
			}
			else
			{
				GmListTable.getInstance().addGm(activeChar, true);
			}
			
			if (Config.GM_GIVE_SPECIAL_SKILLS)
			{
				GMSkillTable.getInstance().addSkills(activeChar);
			}
		}
		
		// Set dead status if applies
		if (activeChar.getCurrentHp() < 0.5)
		{
			activeChar.setIsDead(true);
		}
		
		// Clan related checks are here
		if (activeChar.getClan() != null)
		{
			activeChar.sendPacket(new PledgeSkillList(activeChar.getClan()));
			
			notifyClanMembers(activeChar);
			
			notifySponsorOrApprentice(activeChar);
			
			ClanHall clanHall = ClanHallManager.getInstance().getClanHallByOwner(activeChar.getClan());
			
			if (clanHall != null)
			{
				if (!clanHall.getPaid())
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW));
				}
			}
			
			for (Siege siege : SiegeManager.getInstance().getSieges())
			{
				if (!siege.getIsInProgress())
				{
					continue;
				}
				
				if (siege.checkIsAttacker(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(siege.getCastle().getCastleId());
				}
				
				else if (siege.checkIsDefender(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 2);
					activeChar.setSiegeSide(siege.getCastle().getCastleId());
				}
			}
			
			for (FortSiege siege : FortSiegeManager.getInstance().getSieges())
			{
				if (!siege.getIsInProgress())
				{
					continue;
				}
				
				if (siege.checkIsAttacker(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(siege.getFort().getFortId());
				}
				
				else if (siege.checkIsDefender(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 2);
					activeChar.setSiegeSide(siege.getFort().getFortId());
				}
			}
			
			sendPacket(new PledgeShowMemberListAll(activeChar.getClan(), activeChar));
			sendPacket(new PledgeStatusChanged(activeChar.getClan()));
			
			// Residential skills support
			if (activeChar.getClan().getHasCastle() > 0)
			{
				CastleManager.getInstance().getCastleByOwner(activeChar.getClan()).giveResidentialSkills(activeChar);
			}
			
			if (activeChar.getClan().getHasFort() > 0)
			{
				FortManager.getInstance().getFortByOwner(activeChar.getClan()).giveResidentialSkills(activeChar);
			}
			
			showClanNotice = activeChar.getClan().isNoticeEnabled();
		}
		
		if (TerritoryWarManager.getInstance().getRegisteredTerritoryId(activeChar) > 0)
		{
			if (TerritoryWarManager.getInstance().isTWInProgress())
			{
				activeChar.setSiegeState((byte) 1);
			}
			activeChar.setSiegeSide(TerritoryWarManager.getInstance().getRegisteredTerritoryId(activeChar));
		}
		
		// Updating Seal of Strife Buff/Debuff
		if (SevenSigns.getInstance().isSealValidationPeriod() && (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) != SevenSigns.CABAL_NULL))
		{
			int cabal = SevenSigns.getInstance().getPlayerCabal(activeChar.getObjectId());
			if (cabal != SevenSigns.CABAL_NULL)
			{
				if (cabal == SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE))
				{
					activeChar.addSkill(SkillTable.FrequentSkill.THE_VICTOR_OF_WAR.getSkill());
				}
				else
				{
					activeChar.addSkill(SkillTable.FrequentSkill.THE_VANQUISHED_OF_WAR.getSkill());
				}
			}
		}
		else
		{
			activeChar.removeSkill(SkillTable.FrequentSkill.THE_VICTOR_OF_WAR.getSkill());
			activeChar.removeSkill(SkillTable.FrequentSkill.THE_VANQUISHED_OF_WAR.getSkill());
		}
		
		if (Config.ENABLE_VITALITY && Config.RECOVER_VITALITY_ON_RECONNECT)
		{
			float points = (Config.RATE_RECOVERY_ON_RECONNECT * (System.currentTimeMillis() - activeChar.getLastAccess())) / 60000;
			if (points > 0)
			{
				activeChar.updateVitalityPoints(points, false, true);
			}
		}
		
		activeChar.checkRecoBonusTask();
		activeChar.broadcastUserInfo();
		
		// Send Macro List
		activeChar.getMacroses().sendUpdate();
		// Send Item List
		sendPacket(new ItemList(activeChar, false));
		// Send GG check
		activeChar.queryGameGuard();
		// Send Teleport Bookmark List
		sendPacket(new ExGetBookMarkInfoPacket(activeChar));
		// Send Shortcuts
		sendPacket(new ShortCutInit(activeChar));
		// Send Action list
		activeChar.sendPacket(ExBasicActionList.getStaticPacket(activeChar));
		// Send Skill list
		activeChar.sendSkillList();
		// Send Dye Information
		activeChar.sendPacket(new HennaInfo(activeChar));
		
		Quest.playerEnter(activeChar);
		
		if (!Config.DISABLE_TUTORIAL)
		{
			loadTutorial(activeChar);
		}
		
		for (Quest quest : QuestManager.getInstance().getAllManagedScripts())
		{
			if ((quest != null) && quest.getOnEnterWorld())
			{
				quest.notifyEnterWorld(activeChar);
			}
		}
		activeChar.sendPacket(new QuestList());
		
		if (Config.PLAYER_SPAWN_PROTECTION > 0)
		{
			activeChar.setProtection(true);
		}
		
		activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
		
		if (L2Event.active && L2Event.connectionLossData.containsKey(activeChar.getName()) && L2Event.isOnEvent(activeChar))
		{
			L2Event.restoreChar(activeChar);
		}
		else if (L2Event.connectionLossData.containsKey(activeChar.getName()))
		{
			L2Event.restoreAndTeleChar(activeChar);
		}
		
		// Wedding Checks
		if (Config.L2JMOD_ALLOW_WEDDING)
		{
			engage(activeChar);
			notifyPartner(activeChar, activeChar.getPartnerId());
		}
		
		/** Add by CTF */
		if (CTF._savePlayers.contains(activeChar.getName()))
		{
			CTF.addDisconnectedPlayer(activeChar);
		}
		
		if (DM._savePlayers.contains(activeChar.getName()))
		{
			DM.addDisconnectedPlayer(activeChar);
		}
		
		if (Config.ANNOUNCE_HERO_LOGIN)
		{
			if (activeChar.isHero())
			{
				Broadcast.toAllOnlinePlayers(new CreatureSay(1, Say2.BATTLEFIELD, "Hero System", "Hero " + activeChar.getName() + " has been logged in."));
			}
		}
		
		if (Config.ANNOUNCE_CASTLE_LORDS)
		{
			notifyCastleOwner(activeChar);
		}
		
		if (Config.ANNOUNCE_GM_LOGIN)
		{
			if (activeChar.isGM())
			{
				Announcements.getInstance().announceToAll("The Admin/GM " + activeChar.getName() + " Has Logged in");
			}
		}
		
		if (Config.PC_BANG_ENABLED)
		{
			if (activeChar.getPcBangPoints() > 0)
			{
				activeChar.sendPacket(new ExPCCafePointInfo(activeChar.getPcBangPoints(), 0, false, false, 1));
			}
			else
			{
				activeChar.sendPacket(new ExPCCafePointInfo());
			}
		}
		
		if (Config.AUTO_RESTART_ENABLE)
		{
			CreatureSay msg3 = new CreatureSay(2, Say2.ALL, "[SERVER]", "Next Restart at " + GameServerRestart.getInstance().getRestartNextTime() + " hs.");
			activeChar.sendPacket(msg3);
		}
		
		if (activeChar.isCursedWeaponEquipped())
		{
			CursedWeaponsManager.getInstance().getCursedWeapon(activeChar.getCursedWeaponEquippedId()).cursedOnLogin();
		}
		
		activeChar.updateEffectIcons();
		
		activeChar.sendPacket(new EtcStatusUpdate(activeChar));
		
		// Expand Skill
		activeChar.sendPacket(new ExStorageMaxCount(activeChar));
		
		sendPacket(new FriendList(activeChar));
		
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.FRIEND_S1_HAS_LOGGED_IN);
		sm.addString(activeChar.getName());
		for (int id : activeChar.getFriendList())
		{
			L2Object obj = L2World.getInstance().findObject(id);
			if (obj != null)
			{
				obj.sendPacket(sm);
			}
		}
		
		SevenSigns.getInstance().sendCurrentPeriodMsg(activeChar);
		Announcements.getInstance().showAnnouncements(activeChar);
		
		if (showClanNotice)
		{
			NpcHtmlMessage notice = new NpcHtmlMessage(1);
			notice.setFile(activeChar, "data/html/clanNotice.htm");
			notice.replace("%clan_name%", activeChar.getClan().getName());
			notice.replace("%notice_text%", activeChar.getClan().getNotice().replaceAll("\r\n", "<br>"));
			notice.disableValidation();
			sendPacket(notice);
		}
		else if (Config.SERVER_NEWS)
		{
			String serverNews = HtmCache.getInstance().getHtm(activeChar, "data/html/servnews.htm");
			if (serverNews != null)
			{
				sendPacket(new NpcHtmlMessage(1, serverNews));
			}
		}
		
		if (Config.PETITIONING_ALLOWED)
		{
			PetitionManager.getInstance().checkPetitionMessages(activeChar);
		}
		
		if (activeChar.isAlikeDead()) // dead or fake dead
		{
			// no broadcast needed since the player will already spawn dead to others
			sendPacket(new Die(activeChar));
		}
		
		activeChar.onPlayerEnter();
		
		sendPacket(new SkillCoolTime(activeChar));
		sendPacket(new ExVoteSystemInfo(activeChar));
		
		for (L2ItemInstance i : activeChar.getInventory().getItems())
		{
			if (i.isTimeLimitedItem())
			{
				i.scheduleLifeTimeTask();
			}
			if (i.isShadowItem() && i.isEquipped())
			{
				i.decreaseMana(false);
			}
		}
		
		for (L2ItemInstance i : activeChar.getWarehouse().getItems())
		{
			if (i.isTimeLimitedItem())
			{
				i.scheduleLifeTimeTask();
			}
		}
		
		if (DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getX(), activeChar.getY(), activeChar.getZ(), false))
		{
			DimensionalRiftManager.getInstance().teleportToWaitingRoom(activeChar);
		}
		
		if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBERSHIP_TERMINATED));
		}
		
		// remove combat flag before teleporting
		if (activeChar.getInventory().getItemByItemId(9819) != null)
		{
			Fort fort = FortManager.getInstance().getFort(activeChar);
			
			if (fort != null)
			{
				FortSiegeManager.getInstance().dropCombatFlag(activeChar, fort.getFortId());
			}
			else
			{
				int slot = activeChar.getInventory().getSlotFromItem(activeChar.getInventory().getItemByItemId(9819));
				activeChar.getInventory().unEquipItemInBodySlot(slot);
				activeChar.destroyItem("CombatFlag", activeChar.getInventory().getItemByItemId(9819), null, true);
			}
		}
		
		// Attacker or spectator logging in to a siege zone. Actually should be checked for inside castle only?
		if (!activeChar.isGM()
			// inside siege zone
			&& activeChar.isInsideZone(L2Character.ZONE_SIEGE)
			// but non-participant or attacker
			&& (!activeChar.isInSiege() || (activeChar.getSiegeState() < 2)))
		{
			activeChar.teleToLocation(MapRegionTable.TeleportWhereType.TOWN);
		}
		
		if (Config.ALLOW_MAIL)
		{
			if (MailManager.getInstance().hasUnreadPost(activeChar))
			{
				sendPacket(ExNoticePostArrived.valueOf(false));
			}
		}
		
		TvTEvent.onLogin(activeChar);
		TvTRoundEvent.onLogin(activeChar);
		if (Config.WELCOME_MESSAGE_ENABLED)
		{
			activeChar.sendPacket(new ExShowScreenMessage(Config.WELCOME_MESSAGE_TEXT, Config.WELCOME_MESSAGE_TIME));
		}
		
		L2ClassMasterInstance.showQuestionMark(activeChar);
		
		int birthday = activeChar.checkBirthDay();
		if (birthday == 0)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_BIRTHDAY_GIFT_HAS_ARRIVED));
			activeChar.sendPacket(new ExBirthdayPopup());
		}
		else if (birthday != -1)
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.THERE_ARE_S1_DAYS_UNTIL_YOUR_CHARACTERS_BIRTHDAY);
			sm.addString(Integer.toString(birthday));
			activeChar.sendPacket(sm);
		}
		
		if (!activeChar.getPremiumItemList().isEmpty())
		{
			activeChar.sendPacket(new ExNotifyPremiumItem());
		}
	}
	
	private void verifyAndLoadShots(L2PcInstance activeChar)
	{
		int soulId = -1;
		int spiritId = -1;
		int bspiritId = -1;
		if (!activeChar.isDead() && (activeChar.getActiveWeaponItem() != null))
		{
			switch (activeChar.getActiveWeaponItem().getCrystalType())
			{
				case L2Item.CRYSTAL_NONE:
					soulId = 1835;
					spiritId = 2509;
					bspiritId = 3947;
					break;
				case L2Item.CRYSTAL_D:
					soulId = 1463;
					spiritId = 2510;
					bspiritId = 3948;
					break;
				case L2Item.CRYSTAL_C:
					soulId = 1464;
					spiritId = 2511;
					bspiritId = 3949;
					break;
				case L2Item.CRYSTAL_B:
					soulId = 1465;
					spiritId = 2512;
					bspiritId = 3950;
					break;
				case L2Item.CRYSTAL_A:
					soulId = 1466;
					spiritId = 2513;
					bspiritId = 3951;
					break;
				case L2Item.CRYSTAL_S:
					soulId = 1467;
					spiritId = 2514;
					bspiritId = 3952;
					break;
				case L2Item.CRYSTAL_S80:
					soulId = 1467;
					spiritId = 2514;
					bspiritId = 3952;
					break;
				case L2Item.CRYSTAL_S84:
					soulId = 1467;
					spiritId = 2514;
					bspiritId = 3952;
					break;
			}
			
			if ((soulId > -1))
			{
				
				activeChar.addAutoSoulShot(soulId);
				activeChar.sendPacket(new ExAutoSoulShot(soulId, 1));
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.USE_OF_S1_WILL_BE_AUTO);
				sm.addItemName(soulId);
				activeChar.sendPacket(sm);
				activeChar.rechargeAutoSoulShot(true, true, true);
			}
			
			if ((bspiritId > -1))
			{
				activeChar.addAutoSoulShot(bspiritId);
				activeChar.sendPacket(new ExAutoSoulShot(bspiritId, 1));
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.USE_OF_S1_WILL_BE_AUTO);
				sm.addItemName(bspiritId);
				activeChar.sendPacket(sm);
				activeChar.rechargeAutoSoulShot(true, true, true);
			}
			else if ((spiritId > -1))
			{
				activeChar.addAutoSoulShot(spiritId);
				activeChar.sendPacket(new ExAutoSoulShot(spiritId, 1));
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.USE_OF_S1_WILL_BE_AUTO);
				sm.addItemName(spiritId);
				activeChar.sendPacket(sm);
				activeChar.rechargeAutoSoulShot(true, true, false);
			}
		}
	}
	
	/**
	 * @param activeChar
	 */
	private void engage(L2PcInstance cha)
	{
		int _chaid = cha.getObjectId();
		
		for (Couple cl : CoupleManager.getInstance().getCouples())
		{
			if ((cl.getPlayer1Id() == _chaid) || (cl.getPlayer2Id() == _chaid))
			{
				if (cl.getMaried())
				{
					cha.setMarried(true);
				}
				
				cha.setCoupleId(cl.getId());
				
				if (cl.getPlayer1Id() == _chaid)
				{
					cha.setPartnerId(cl.getPlayer2Id());
				}
				else
				{
					cha.setPartnerId(cl.getPlayer1Id());
				}
			}
		}
	}
	
	/**
	 * @param activeChar partnerid
	 */
	private void notifyPartner(L2PcInstance cha, int partnerId)
	{
		if (cha.getPartnerId() != 0)
		{
			int objId = cha.getPartnerId();
			
			try
			{
				L2PcInstance partner = L2World.getInstance().getPlayer(objId);
				
				if (partner != null)
				{
					partner.sendMessage("Your Partner has logged in.");
				}
				
				partner = null;
			}
			catch (ClassCastException cce)
			{
				_log.warning("Wedding Error: ID " + objId + " is now owned by a(n) " + L2World.getInstance().findObject(objId).getClass().getSimpleName());
			}
		}
	}
	
	/**
	 * @param activeChar
	 */
	private void notifyClanMembers(L2PcInstance activeChar)
	{
		L2Clan clan = activeChar.getClan();
		
		// This null check may not be needed anymore since notifyClanMembers is called from within a null check already. Please remove if we're certain it's ok to do so.
		if (clan != null)
		{
			clan.getClanMember(activeChar.getObjectId()).setPlayerInstance(activeChar);
			SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_LOGGED_IN);
			msg.addString(activeChar.getName());
			clan.broadcastToOtherOnlineMembers(msg, activeChar);
			msg = null;
			clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListUpdate(activeChar), activeChar);
		}
	}
	
	/**
	 * @param activeChar
	 */
	private void notifySponsorOrApprentice(L2PcInstance activeChar)
	{
		if (activeChar.getSponsor() != 0)
		{
			L2PcInstance sponsor = L2World.getInstance().getPlayer(activeChar.getSponsor());
			
			if (sponsor != null)
			{
				SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOUR_APPRENTICE_S1_HAS_LOGGED_IN);
				msg.addString(activeChar.getName());
				sponsor.sendPacket(msg);
			}
		}
		else if (activeChar.getApprentice() != 0)
		{
			L2PcInstance apprentice = L2World.getInstance().getPlayer(activeChar.getApprentice());
			
			if (apprentice != null)
			{
				SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOUR_SPONSOR_C1_HAS_LOGGED_IN);
				msg.addString(activeChar.getName());
				apprentice.sendPacket(msg);
			}
		}
	}
	
	private void loadTutorial(L2PcInstance player)
	{
		QuestState qs = player.getQuestState(Q00255_Tutorial.class.getSimpleName());
		
		if (qs != null)
		{
			qs.getQuest().notifyEvent("UC", null, player);
		}
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
	
	private void notifyCastleOwner(L2PcInstance activeChar)
	{
		L2Clan clan = activeChar.getClan();
		
		if (clan != null)
		{
			if (clan.getHasCastle() > 0)
			{
				Castle castle = CastleManager.getInstance().getCastleById(clan.getHasCastle());
				if ((castle != null) && (activeChar.getObjectId() == clan.getLeaderId()))
				{
					Broadcast.toAllOnlinePlayers(new CreatureSay(1, Say2.BATTLEFIELD, "Castle System", "Lord " + activeChar.getName() + " Ruler Of " + castle.getName() + " Castle is Now Online!"));
				}
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__03_ENTERWORLD;
	}
}
