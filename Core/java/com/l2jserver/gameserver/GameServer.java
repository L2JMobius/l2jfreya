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

package com.l2jserver.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.Server;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.cache.ImagesCache;
import com.l2jserver.gameserver.datatables.AccessLevels;
import com.l2jserver.gameserver.datatables.AdminCommandAccessRights;
import com.l2jserver.gameserver.datatables.ArmorSetsTable;
import com.l2jserver.gameserver.datatables.BuyListTable;
import com.l2jserver.gameserver.datatables.CharNameTable;
import com.l2jserver.gameserver.datatables.CharTemplateTable;
import com.l2jserver.gameserver.datatables.ClanTable;
import com.l2jserver.gameserver.datatables.CrestTable;
import com.l2jserver.gameserver.datatables.DoorTable;
import com.l2jserver.gameserver.datatables.EnchantGroupsTable;
import com.l2jserver.gameserver.datatables.EventDroplist;
import com.l2jserver.gameserver.datatables.FakePcsTable;
import com.l2jserver.gameserver.datatables.FishTable;
import com.l2jserver.gameserver.datatables.GMSkillTable;
import com.l2jserver.gameserver.datatables.HelperBuffTable;
import com.l2jserver.gameserver.datatables.HennaTable;
import com.l2jserver.gameserver.datatables.HennaTreeTable;
import com.l2jserver.gameserver.datatables.HerbDropTable;
import com.l2jserver.gameserver.datatables.HeroSkillTable;
import com.l2jserver.gameserver.datatables.ItemDropIndex;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.datatables.LevelUpData;
import com.l2jserver.gameserver.datatables.MapRegionTable;
import com.l2jserver.gameserver.datatables.MerchantPriceConfigTable;
import com.l2jserver.gameserver.datatables.MultiSell;
import com.l2jserver.gameserver.datatables.NobleSkillTable;
import com.l2jserver.gameserver.datatables.NpcBufferTable;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.datatables.OfflineTradersTable;
import com.l2jserver.gameserver.datatables.PetDataTable;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.datatables.StaticObjects;
import com.l2jserver.gameserver.datatables.SummonSkillsTable;
import com.l2jserver.gameserver.datatables.TeleportLocationTable;
import com.l2jserver.gameserver.datatables.UITable;
import com.l2jserver.gameserver.datatables.xml.AugmentationData;
import com.l2jserver.gameserver.datatables.xml.EnchantHPBonusData;
import com.l2jserver.gameserver.datatables.xml.NpcPersonalAIData;
import com.l2jserver.gameserver.datatables.xml.SkillTreesData;
import com.l2jserver.gameserver.datatables.xml.SummonItemsData;
import com.l2jserver.gameserver.geoeditorcon.GeoEditorListener;
import com.l2jserver.gameserver.handler.ActionHandler;
import com.l2jserver.gameserver.handler.AdminCommandHandler;
import com.l2jserver.gameserver.handler.BypassHandler;
import com.l2jserver.gameserver.handler.ChatHandler;
import com.l2jserver.gameserver.handler.ItemHandler;
import com.l2jserver.gameserver.handler.SkillHandler;
import com.l2jserver.gameserver.handler.UserCommandHandler;
import com.l2jserver.gameserver.handler.VoicedCommandHandler;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.instancemanager.AirShipManager;
import com.l2jserver.gameserver.instancemanager.AntiFeedManager;
import com.l2jserver.gameserver.instancemanager.AuctionManager;
import com.l2jserver.gameserver.instancemanager.BoatManager;
import com.l2jserver.gameserver.instancemanager.BonusExpManager;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.CastleManorManager;
import com.l2jserver.gameserver.instancemanager.ClanHallManager;
import com.l2jserver.gameserver.instancemanager.CoupleManager;
import com.l2jserver.gameserver.instancemanager.CursedWeaponsManager;
import com.l2jserver.gameserver.instancemanager.DayNightSpawnManager;
import com.l2jserver.gameserver.instancemanager.DimensionalRiftManager;
import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.instancemanager.FortSiegeManager;
import com.l2jserver.gameserver.instancemanager.FourSepulchersManager;
import com.l2jserver.gameserver.instancemanager.GlobalVariablesManager;
import com.l2jserver.gameserver.instancemanager.GrandBossManager;
import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.instancemanager.ItemAuctionManager;
import com.l2jserver.gameserver.instancemanager.ItemsOnGroundManager;
import com.l2jserver.gameserver.instancemanager.MailManager;
import com.l2jserver.gameserver.instancemanager.MercTicketManager;
import com.l2jserver.gameserver.instancemanager.PcCafePointsManager;
import com.l2jserver.gameserver.instancemanager.PetitionManager;
import com.l2jserver.gameserver.instancemanager.PremiumManager;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.instancemanager.RaidBossPointsManager;
import com.l2jserver.gameserver.instancemanager.RaidBossSpawnManager;
import com.l2jserver.gameserver.instancemanager.SiegeManager;
import com.l2jserver.gameserver.instancemanager.TerritoryWarManager;
import com.l2jserver.gameserver.instancemanager.TransformationManager;
import com.l2jserver.gameserver.instancemanager.WalkingManager;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.instancemanager.achievments_engine.AchievementsManager;
import com.l2jserver.gameserver.instancemanager.gracia.SoDManager;
import com.l2jserver.gameserver.instancemanager.gracia.SoIManager;
import com.l2jserver.gameserver.model.AutoChatHandler;
import com.l2jserver.gameserver.model.AutoSpawnHandler;
import com.l2jserver.gameserver.model.L2Manor;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.PartyMatchRoomList;
import com.l2jserver.gameserver.model.PartyMatchWaitingList;
import com.l2jserver.gameserver.model.buffshop.BuffShopManager;
import com.l2jserver.gameserver.model.entity.CTFManager;
import com.l2jserver.gameserver.model.entity.Hero;
import com.l2jserver.gameserver.model.entity.MRManager;
import com.l2jserver.gameserver.model.entity.TownWarManager;
import com.l2jserver.gameserver.model.entity.TvTManager;
import com.l2jserver.gameserver.model.entity.TvTRoundManager;
import com.l2jserver.gameserver.model.olympiad.Olympiad;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.L2GamePacketHandler;
import com.l2jserver.gameserver.pathfinding.PathFinding;
import com.l2jserver.gameserver.script.faenor.FaenorScriptEngine;
import com.l2jserver.gameserver.taskmanager.AutoAnnounceTaskManager;
import com.l2jserver.gameserver.taskmanager.KnownListUpdateTaskManager;
import com.l2jserver.gameserver.taskmanager.TaskManager;
import com.l2jserver.gameserver.util.ScriptsLoader;
import com.l2jserver.scripts.hellbound.Engine;
import com.l2jserver.util.DeadLockDetector;
import com.l2jserver.util.IPv4Filter;

import org.mmocore.network.SelectorConfig;
import org.mmocore.network.SelectorThread;

public class GameServer
{
	private static final Logger _log = Logger.getLogger(GameServer.class.getName());
	
	private final SelectorThread<L2GameClient> _selectorThread;
	private final L2GamePacketHandler _gamePacketHandler;
	private final DeadLockDetector _deadDetectThread;
	private final IdFactory _idFactory;
	public static GameServer gameServer;
	private final LoginServerThread _loginThread;
	public static final Calendar dateTimeServerStarted = Calendar.getInstance();
	
	public long getUsedMemoryMB()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576; // ;
	}
	
	public SelectorThread<L2GameClient> getSelectorThread()
	{
		return _selectorThread;
	}
	
	public L2GamePacketHandler getL2GamePacketHandler()
	{
		return _gamePacketHandler;
	}
	
	public DeadLockDetector getDeadLockDetectorThread()
	{
		return _deadDetectThread;
	}
	
	public GameServer() throws Exception
	{
		long serverLoadStart = System.currentTimeMillis();
		
		gameServer = this;
		_log.finest("Used memory:" + getUsedMemoryMB() + "MB");
		
		_idFactory = IdFactory.getInstance();
		
		if (!_idFactory.isInitialized())
		{
			_log.severe("Could not read object IDs from DB. Please Check Your Data.");
			throw new Exception("Could not initialize the ID factory");
		}
		
		ThreadPoolManager.getInstance();
		
		new File(Config.DATAPACK_ROOT, "data/crests").mkdirs();
		new File("log/game").mkdirs();
		
		printSection("-");
		printSection("World");
		printSection("-");
		GameTimeController.getInstance();
		InstanceManager.getInstance();
		L2World.getInstance();
		MapRegionTable.getInstance();
		Announcements.getInstance();
		GlobalVariablesManager.getInstance();
		
		printSection("-");
		printSection("Skills");
		printSection("-");
		EnchantGroupsTable.getInstance();
		SkillTable.getInstance();
		SkillTreesData.getInstance();
		NobleSkillTable.getInstance();
		GMSkillTable.getInstance();
		HeroSkillTable.getInstance();
		SummonSkillsTable.getInstance();
		
		printSection("-");
		printSection("Items");
		printSection("-");
		BonusExpManager.getInstance();
		ItemTable.getInstance();
		SummonItemsData.getInstance();
		EnchantHPBonusData.getInstance();
		_log.info("Enchanting for HERO: " + Config.ENABLE_ENCHANT_HERO);
		_log.info("Enchanting for HERO: " + Config.ENABLE_ENCHANT_PVP);
		_log.info("Enchanting for SHADOW: " + Config.ENABLE_ENCHANT_SHADOW);
		_log.info("Enchanting for COMMON: " + Config.ENABLE_ENCHANT_COMMON);
		_log.info("Enchanting for LIMITED: " + Config.ENABLE_ENCHANT_LIMITED);
		MerchantPriceConfigTable.getInstance().loadInstances();
		BuyListTable.getInstance();
		MultiSell.getInstance();
		RecipeController.getInstance();
		ArmorSetsTable.getInstance();
		FishTable.getInstance();
		
		printSection("-");
		printSection("EXP Manager - Calculation");
		printSection("-");
		_log.info("Calculating XP and SP rates for this day.");
		_log.info("Enabled XP/SP rates by day: " + Config.ENABLED_BOOST_EXP);
		if (Config.ENABLED_BOOST_EXP)
		{
			_log.info("Today's EXP rate: " + Config.INT_RATE_XP_OF_TODAY);
			_log.info("Today's SP rate: " + Config.INT_RATE_SP_OF_TODAY);
			if (Config.USE_PREMIUMSERVICE)
			{
				_log.info("Today's EXP rate - Premium acc: " + Config.INT_RATE_XP_OF_TODAY_PREMIUM);
				_log.info("Today's SP rate - Premium acc: " + Config.INT_RATE_SP_OF_TODAY_PREMIUM);
			}
		}
		else
		{
			_log.info("Today's EXP rate: " + Config.RATE_XP);
			_log.info("Today's SP rate: " + Config.RATE_SP);
		}
		
		printSection("-");
		printSection("Characters");
		printSection("-");
		CharTemplateTable.getInstance();
		CharNameTable.getInstance();
		LevelUpData.getInstance();
		AccessLevels.getInstance();
		AdminCommandAccessRights.getInstance();
		GmListTable.getInstance();
		RaidBossPointsManager.getInstance();
		PetDataTable.getInstance();
		
		printSection("-");
		printSection("Clans");
		printSection("-");
		ClanTable.getInstance();
		ClanHallManager.getInstance();
		AuctionManager.getInstance();
		
		printSection("-");
		printSection("Geodata");
		printSection("-");
		GeoData.getInstance();
		if (Config.GEODATA == 2)
		{
			PathFinding.getInstance();
		}
		
		printSection("-");
		printSection("NPCs");
		printSection("-");
		NpcPersonalAIData.getInstance();
		HerbDropTable.getInstance();
		NpcTable.getInstance();
		ItemDropIndex.getInstance().buildIndex();
		ZoneManager.getInstance();
		DoorTable.getInstance();
		StaticObjects.getInstance();
		ItemAuctionManager.getInstance();
		CastleManager.getInstance().loadInstances();
		FortManager.getInstance().loadInstances();
		NpcBufferTable.getInstance();
		SpawnTable.getInstance();
		RaidBossSpawnManager.getInstance();
		DayNightSpawnManager.getInstance().trim().notifyChangeMode();
		GrandBossManager.getInstance().initZones();
		FourSepulchersManager.getInstance().init();
		DimensionalRiftManager.getInstance();
		EventDroplist.getInstance();
		WalkingManager.getInstance();
		
		printSection("-");
		printSection("Features");
		printSection("-");
		SiegeManager.getInstance().getSieges();
		FortSiegeManager.getInstance();
		TerritoryWarManager.getInstance();
		CastleManorManager.getInstance();
		MercTicketManager.getInstance();
		L2Manor.getInstance();
		
		printSection("-");
		printSection("Olympiad");
		printSection("-");
		Olympiad.getInstance();
		Hero.getInstance();
		
		printSection("-");
		printSection("Cache");
		printSection("-");
		HtmCache.getInstance();
		CrestTable.getInstance();
		TeleportLocationTable.getInstance();
		UITable.getInstance();
		PartyMatchWaitingList.getInstance();
		PartyMatchRoomList.getInstance();
		PetitionManager.getInstance();
		HennaTable.getInstance();
		HennaTreeTable.getInstance();
		HelperBuffTable.getInstance();
		CursedWeaponsManager.getInstance();
		
		printSection("-");
		printSection("Augmentation");
		printSection("-");
		AugmentationData.getInstance();
		_log.info("Augmentation: for PVP Items: " + Config.ALT_ALLOW_AUGMENT_PVP_ITEMS);
		_log.info("Augmentation: for HERO Items: " + Config.ALT_ALLOW_AUGMENT_HERO_ITEMS);
		_log.info("Augmentation: for SHADOW Items: " + Config.ALT_ALLOW_AUGMENT_SHADOW_ITEMS);
		_log.info("Augmentation: for COMMON Items: " + Config.ALT_ALLOW_AUGMENT_COMMON_ITEMS);
		_log.info("Augmentation: for LIMITED Items: " + Config.ALT_ALLOW_AUGMENT_LIMITED_TIME_ITEMS);
		
		printSection("-");
		printSection("Handlers");
		printSection("-");
		ActionHandler.getInstance();
		AdminCommandHandler.getInstance();
		BypassHandler.getInstance();
		AutoChatHandler.getInstance();
		ChatHandler.getInstance();
		AutoSpawnHandler.getInstance();
		ItemHandler.getInstance();
		SkillHandler.getInstance();
		UserCommandHandler.getInstance();
		VoicedCommandHandler.getInstance();
		
		printSection("-");
		printSection("Scripts");
		printSection("-");
		TransformationManager.getInstance();
		QuestManager.getInstance();
		ScriptsLoader.loadAllScripts();
		BoatManager.getInstance();
		AirShipManager.getInstance();
		TransformationManager.getInstance().report();
		QuestManager.getInstance().report();
		
		GameServer.printSection("-");
		GameServer.printSection("Seed of Infinity");
		GameServer.printSection("-");
		SoIManager.getInstance();
		
		GameServer.printSection("-");
		GameServer.printSection("Seed of Destruction");
		GameServer.printSection("-");
		SoDManager.getInstance();
		_log.info("Current stage is: " + SoDManager.getSoDState());
		
		GameServer.printSection("-");
		GameServer.printSection("Hellbound");
		GameServer.printSection("-");
		HellboundManager.getInstance();
		_log.info("Loaded: " + Engine.pointsInfo.size() + " trust point reward data");
		_log.info("Loaded: levels 0-3");
		_log.info("Loaded Level: " + HellboundManager.getInstance().getLevel());
		_log.info("Loaded Trust: " + HellboundManager.getInstance().getTrust());
		if (HellboundManager.getInstance().isLocked())
		{
			_log.info("State: locked");
		}
		else
		{
			_log.info("State: unlocked");
		}
		
		GameServer.printSection("-");
		GameServer.printSection("Seven Signs Festival");
		GameServer.printSection("-");
		SevenSigns.getInstance().spawnSevenSignsNPC();
		SevenSignsFestival.getInstance();
		
		FaenorScriptEngine.getInstance();
		
		if (Config.L2JMOD_ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
		}
		
		if (Config.SAVE_DROPPED_ITEM)
		{
			ItemsOnGroundManager.getInstance();
		}
		
		if ((Config.AUTODESTROY_ITEM_AFTER > 0) || (Config.HERB_AUTO_DESTROY_TIME > 0))
		{
			ItemsAutoDestroy.getInstance();
		}
		
		TaskManager.getInstance();
		
		AntiFeedManager.getInstance().registerEvent(AntiFeedManager.GAME_ID);
		MerchantPriceConfigTable.getInstance().updateReferences();
		CastleManager.getInstance().activateInstances();
		FortManager.getInstance().activateInstances();
		
		printSection("-");
		printSection("Event Engines");
		printSection("-");
		if (Config.ENABLE_FAKE_PCS)
		{
			FakePcsTable.getInstance();
			_log.info("Fake Players: Engine load " + FakePcsTable._fakePcs.size() + " Fake Players Npcs.");
		}
		else
		{
			_log.info("Fake Players: Engine is disabled.");
		}
		if (Config.PC_BANG_ENABLED)
		{
			_log.info("PC Bang: Engine is enabled.");
			PcCafePointsManager.getInstance();
		}
		else
		{
			_log.info("PC Bang: Engine is disabled.");
		}
		if (Config.ENABLE_BLOCK_CHECKER_EVENT)
		{
			_log.info("Handys Block Checker: Engine is enabled.");
		}
		else
		{
			_log.info("Handys Block Checker: Engine is disabled.");
		}
		if (Config.HAS_ENABLED)
		{
			_log.info("Hide and Seek: Engine is enabled");
		}
		else
		{
			_log.info("Hide and Seek: Engine is disabled");
		}
		
		MonsterRace.getInstance();
		TvTManager.getInstance();
		TvTRoundManager.getInstance();
		CTFManager.getInstance();
		TownWarManager.getInstance();
		if (Config.MR_ENABLED)
		{
			MRManager.getInstance();
			_log.info("Monster Rush: Engine is enabled.");
		}
		else
		{
			_log.info("Monster Rush: Engine is disabled.");
		}
		AchievementsManager.getInstance();
		
		printSection("-");
		printSection("ETC");
		printSection("-");
		if (Config.ALLOW_MAIL)
		{
			MailManager.getInstance();
		}
		
		if (Config.ACCEPT_GEOEDITOR_CONN)
		{
			GeoEditorListener.getInstance();
		}
		
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		_log.info("Id Factory: Free ObjectID's remaining: " + IdFactory.getInstance().size());
		
		printSection("-");
		printSection("Premium Service");
		printSection("-");
		if (Config.USE_PREMIUMSERVICE)
		{
			_log.info("System - Activated");
			PremiumManager.getInstance();
			PremiumManager.getPremiumAccountsTotal();
		}
		else
		{
			_log.info("System - Deactivated");
		}
		
		KnownListUpdateTaskManager.getInstance();
		
		printSection("-");
		printSection("Offline Traders");
		printSection("-");
		BuffShopManager.getInstance().restoreOfflineTraders();
		OfflineTradersTable.restoreOfflineTraders();
		
		printSection("-");
		printSection("-");
		
		if (Config.DEADLOCK_DETECTOR)
		{
			_deadDetectThread = new DeadLockDetector();
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		}
		else
		{
			_deadDetectThread = null;
		}
		System.gc();
		// maxMemory is the upper limit the jvm can use, totalMemory the size of
		// the current allocation pool, freeMemory the unused memory in the
		// allocation pool
		long freeMem = ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / 1048576;
		long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
		_log.info("GameServer Started, free memory " + freeMem + " Mb of " + totalMem + " Mb");
		
		_loginThread = LoginServerThread.getInstance();
		_loginThread.start();
		
		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = Config.MMO_MAX_READ_PER_PASS;
		sc.MAX_SEND_PER_PASS = Config.MMO_MAX_SEND_PER_PASS;
		sc.SLEEP_TIME = Config.MMO_SELECTOR_SLEEP_TIME;
		sc.HELPER_BUFFER_COUNT = Config.MMO_HELPER_BUFFER_COUNT;
		
		_gamePacketHandler = new L2GamePacketHandler();
		_selectorThread = new SelectorThread<>(sc, _gamePacketHandler, _gamePacketHandler, _gamePacketHandler, new IPv4Filter());
		
		InetAddress bindAddress = null;
		if (!Config.GAMESERVER_HOSTNAME.equals("*"))
		{
			try
			{
				bindAddress = InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
			}
			catch (UnknownHostException e1)
			{
				_log.log(Level.SEVERE, "WARNING: The GameServer bind address is invalid, using all avaliable IPs. Reason: " + e1.getMessage(), e1);
			}
		}
		
		try
		{
			_selectorThread.openServerSocket(bindAddress, Config.PORT_GAME);
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, "FATAL: Failed to open server socket. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		_selectorThread.start();
		long serverLoadEnd = System.currentTimeMillis();
		_log.info("Server Loaded in " + ((serverLoadEnd - serverLoadStart) / 1000) + " seconds");
		
		AutoAnnounceTaskManager.getInstance();
		
		if (Config.ALLOW_ANNOUNCE_ONLINE_PLAYERS)
		{
			AutoAnnounceOnline.getInstance();
		}
		else
		{
			_log.info("[Auto Announce Online]: System is disabled.");
		}
		if (Config.AUTO_RESTART_ENABLE)
		{
			GameServerRestart.getInstance().StartCalculationOfNextRestartTime();
		}
		else
		{
			_log.info("[Auto Restart]: System is disabled.");
		}
		ImagesCache.load();
	}
	
	public static void main(String[] args) throws Exception
	{
		Server.serverMode = Server.MODE_GAMESERVER;
		// Local Constants
		final String LOG_FOLDER = "log"; // Name of folder for log file
		final String LOG_NAME = "./log.cfg"; // Name of log file
		
		/*** Main ***/
		// Create log folder
		File logFolder = new File(Config.DATAPACK_ROOT, LOG_FOLDER);
		logFolder.mkdir();
		
		// Create input stream for log file -- or store file data into memory
		try (InputStream is = new FileInputStream(new File(LOG_NAME)))
		{
			LogManager.getLogManager().readConfiguration(is);
		}
		
		// Initialize config
		Config.loadServerConfig();
		printSection("Database");
		L2DatabaseFactory.getInstance();
		gameServer = new GameServer();
	}
	
	public static void printSection(String s)
	{
		s = "=[ " + s + " ]";
		while (s.length() < 78)
		{
			s = "-" + s;
		}
		_log.info(s);
	}
}
