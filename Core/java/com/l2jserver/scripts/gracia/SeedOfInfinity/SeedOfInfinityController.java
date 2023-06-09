/*
 * Copyright (C) 2004-2017 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.scripts.gracia.SeedOfInfinity;

import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.quest.Quest;

/**
 * @author L2jPrivateDevelopersTeam
 */
public abstract class SeedOfInfinityController extends Quest
{
	public static final Logger _log = Logger.getLogger(SeedOfInfinityController.class.getName());
	
	public SeedOfInfinityController(int questId, String name, String descr)
	{
		super(questId, name, descr);
	}
	
	//@formatter:off
	// ZONES
	public static final int EKIMUS_ZONE = 201033;
	
	// INSTANCES
	public static final int EXIT_TIME = 300000;
	public static final int INSTANCE_PENALTY = 24;
	public static final int INSTANCE_ID_HALL_OF_EROSION_ATTACK = 119;
	public static final int INSTANCE_ID_HALL_OF_EROSION_DEFENCE = 120;
	public static final int INSTANCE_ID_HALL_OF_SUFFERING_ATTACK = 115;
	public static final int INSTANCE_ID_HALL_OF_SUFFERING_DEFENCE = 116;
	public static final int INSTANCE_ID_HEART_INFINITY_ATTACK = 121;
	public static final int INSTANCE_ID_HEART_INFINITY_DEFENCE = 122;
	
	// ENTERS
	public static final int[] ENTER_TELEPORT_HOEA = {-179659, 211061, -12784};
	public static final int[] ENTER_TELEPORT_HOED = {-179659, 211061, -12784};
	public static final int[] ENTER_TELEPORT_HOSA = {-187567, 205570, -9538};
	public static final int[] ENTER_TELEPORT_HOSD = {-174701, 218109, -9592};
	public static final int[] ENTER_TELEPORT_HIA =	{-179284, 205990, -15520};
	public static final int[] ENTER_TELEPORT_HID =	{-179284, 205990, -15520};
	
	// XMLS
	public static final String HALL_OF_EROSION_ATTACK_XML = "HallOfErosionAttack.xml";
	public static final String HALL_OF_EROSION_DEFENCE_XML = "HallOfErosionDefence.xml";
	public static final String HALL_OF_SUFFERING_ATTACK_XML = "HallOfSufferingAttack.xml";
	public static final String HALL_OF_SUFFERING_DEFENCE_XML = "HallOfSufferingDefence.xml";
	public static final String HEART_INFINITY_ATTACK_XML = "HeartInfinityAttack.xml";
	public static final String HEART_INFINITY_DEFENCE_XML = "HeartInfinityDefence.xml";
	
	// ITEMS
	public static final int TEARS_OF_A_FREED_SOUL = 13797;
	// REWARD IN HOS
	public static final int JEWEL_ORNAMENTED_DUEL_SUPPLIES = 13777;
	public static final int MOTHER_OF_PEARL_ORNAMENTED_DUEL_SUPPLIES = 13778;
	public static final int GOLD_ORNAMENTED_DUEL_SUPPLIES = 13779;
	public static final int SILVER_ORNAMENTED_DUEL_SUPPLIES = 13780;
	public static final int BRONZE_ORNAMENTED_DUEL_SUPPLIES = 13781;
	public static final int NON_ORNAMENTED_DUEL_SUPPLIES = 13782;
	public static final int WEAK_LOOKING_DUEL_SUPPLIES = 13783;
	public static final int SAD_LOOKING_DUEL_SUPPLIES = 13784;
	public static final int POOR_LOOKING_DUEL_SUPPLIES = 13785;
	public static final int WORTHLESS_DUEL_SUPPLIES = 13786;
	// REWARD MULTI-PLIER IN HOS
	public static final int MULTIPLIER_REWARD_HOSA = 1;
	
	// HOSA; HOSD BOSS - ETC
	public static final int BOSS_MINION_SPAWN_TIME = 60000; // in ms
	public static final int BOSS_RESSURECT_TIME = 20000; // in ms
	
	// NPC
	public static final int TEPIOS = 32530;
	public static final int EKIMUS_MOUTH = 32537;
	public static final int TUMOR_OF_DEATH_ALIVE_79 = 18704;
	public static final int TUMOR_OF_DEATH_ALIVE_80 = 18705;
	public static final int TUMOR_OF_DEATH_ALIVE_81 = 18708;
	public static final int DESTROYED_TUMOR_1 = 32531;
	public static final int DESTROYED_TUMOR_2 = 32535; // for examine
	public static final int DESTROYED_TUMOR_3_START = 32536; // for examine
	public static final int COHEMENES = 25634;
	public static final int SOUL_COFIN = 18709;
	public static final int SOUL_COFIN_1 = 18710;
	public static final int SOUL_COFIN_2 = 18711;
	public static final int KLODEKUS = 25665;
	public static final int KLANIKUS = 25666;
	public static final int GATEKEEPER_OF_ABYSS_YELLOW = 32539;
	public static final int GATEKEEPER_OF_ABYSS_BLUE = 32540;
	public static final int WARD_OF_DEATH_1 = 18667;
	public static final int WARD_OF_DEATH_2 = 18668;
	public static final int FANATIC_OF_INFINITY = 22509; 
	public static final int ROTTEN_MESSENGER = 22510; 
	public static final int ZEALOT_OF_INFINITY = 22511; 
	public static final int BODY_SEVERER_1 = 22512; 
	public static final int BODY_HARVESTER = 22513; 
	public static final int SOUL_EXPLOITER = 22514; 
	public static final int SOUL_DEVOURER_1 = 22515;
	public static final int BUTCHER_OF_INFINITY = 22516;
	public static final int BODY_SEVERER_2 = 22520;
	public static final int SOUL_DEVOURER_2 = 22522;
	public static final int SOUL_DEVOURER_3 = 22523;
	public static final int EMISSARY_OF_DEATH = 22524;
	public static final int EMISSARY_OF_DEATH_2 = 22526;
	public static final int LAW_SCHOLAR_OF_CONCLUSIONS = 22532;
	public static final int SYMBOL_OF_COHEMENES = 18780;
	public static final int SEED = 32541;
	public static final int KARPENCHARR = 25637;
	public static final int ROMEROHIV = 25638;
	public static final int HITCHKARHIEK = 25639;
	public static final int FREEDKYILLA = 25640;
	public static final int YEHAN_CRAVENZIZAD = 25641;
	public static final int YEHAN_JAXSIBHAN = 25642;
	public static final int EKIMUS = 29150;
	public static final int PRE_EKIMUS = 29161;
	public static final int HOUND = 29151;
	
	public static final int[] TUMOR_MOBIDS =
	{
		FANATIC_OF_INFINITY, 
		ROTTEN_MESSENGER, 
		ZEALOT_OF_INFINITY, 
		BODY_SEVERER_1, 
		BODY_HARVESTER, 
		SOUL_EXPLOITER, 
		SOUL_DEVOURER_1
	};
	
	public static final int[] TWIN_MOBIDS = 
	{
		FANATIC_OF_INFINITY,
		ROTTEN_MESSENGER, 
		ZEALOT_OF_INFINITY, 
		BODY_SEVERER_1, 
		BODY_HARVESTER
	};
	
	// NOT MOVING NPCs
	public static final int[] HOEA_NOT_MOVE =
	{
		WARD_OF_DEATH_2,
		SOUL_COFIN_2,
		TUMOR_OF_DEATH_ALIVE_81,
		DESTROYED_TUMOR_2
	};
	
	public static final int[] HOED_NOT_MOVE =
	{
		WARD_OF_DEATH_1,
		WARD_OF_DEATH_2,
		TUMOR_OF_DEATH_ALIVE_81,
		SOUL_COFIN,
		SOUL_COFIN_2,
		DESTROYED_TUMOR_2
	};
	
	public static final int[] HIA_NOT_MOVE =
	{
		WARD_OF_DEATH_2,
		SOUL_COFIN_2,
		TUMOR_OF_DEATH_ALIVE_81,
		DESTROYED_TUMOR_2
	};
	
	public static final int[] HID_NOT_MOVE =
	{
		WARD_OF_DEATH_2,
		SOUL_COFIN_2,
		TUMOR_OF_DEATH_ALIVE_81,
		DESTROYED_TUMOR_2
	};
	
	// GROUP SPAWN
	public static final int[] GROUP_SPAWN_LIST_HOEA =
	{
		BUTCHER_OF_INFINITY,
		BODY_SEVERER_2,
		SOUL_DEVOURER_2,
		EMISSARY_OF_DEATH
	};
	
	public static final int[] GROUP_SPAWN_LIST_HOED =
	{
		BUTCHER_OF_INFINITY,
		BODY_SEVERER_2,
		SOUL_DEVOURER_2,
		EMISSARY_OF_DEATH,
		EMISSARY_OF_DEATH_2,
		LAW_SCHOLAR_OF_CONCLUSIONS
	};
	
	public static final int[] GROUP_SPAWN_LIST_HIA_HID =
	{
		BUTCHER_OF_INFINITY,
		BODY_SEVERER_2,
		SOUL_DEVOURER_2,
		EMISSARY_OF_DEATH
	};
	
	// ROOMS SPAWNS
	public static final int[][] ROOMS_MOBS_HOEA =
	{
		{BUTCHER_OF_INFINITY,-180364,211944,-12019,0,60,1},
		{BUTCHER_OF_INFINITY,-181616,211413,-12015,0,60,1},
		{BODY_SEVERER_2,-181404,211042,-12023,0,60,1},
		{SOUL_DEVOURER_2,-181558,212227,-12035,0,60,1},
		{SOUL_DEVOURER_2,-180459,212322,-12018,0,60,1},
		{EMISSARY_OF_DEATH,-180428,211180,-12014,0,60,1},
		{EMISSARY_OF_DEATH,-180718,212162,-12028,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183114,209397,-11923,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-182917,210495,-11925,0,60,1},
		{BUTCHER_OF_INFINITY,-183918,210225,-11934,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183862,209909,-11932,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183246,210631,-11923,0,60,1},
		{SOUL_DEVOURER_2,-182971,210522,-11924,0,60,1},
		{SOUL_DEVOURER_2,-183485,209406,-11921,0,60,1},
		{BUTCHER_OF_INFINITY,-183032,208822,-11923,0,60,1},
		{BUTCHER_OF_INFINITY,-182709,207817,-11929,0,60,1},
		{BODY_SEVERER_2,-182964,207746,-11924,0,60,1},
		{BODY_SEVERER_2,-183385,208847,-11922,0,60,1},
		{EMISSARY_OF_DEATH_2,-183684,208847,-11926,0,60,1},
		{EMISSARY_OF_DEATH_2,-183530,208725,-11926,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183968,207603,-11928,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183608,208567,-11926,0,60,1},
		{EMISSARY_OF_DEATH_2,-181471,207159,-12020,0,60,1},
		{EMISSARY_OF_DEATH_2,-180213,207042,-12013,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-180213,206506,-12010,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-181720,206643,-12016,0,60,1},
		{BUTCHER_OF_INFINITY,-181743,206643,-12018,0,60,1},
		{BUTCHER_OF_INFINITY,-181028,205739,-12030,0,60,1},
		{BODY_SEVERER_2,-181431,205980,-12040,0,60,1},
		{EMISSARY_OF_DEATH,-178964,207168,-12014,0,60,1},
		{EMISSARY_OF_DEATH,-177658,207037,-12019,0,60,1},
		{SOUL_DEVOURER_2,-177730,206558,-12016,0,60,1},
		{SOUL_DEVOURER_2,-179132,206650,-12011,0,60,1},
		{EMISSARY_OF_DEATH_2,-179132,206155,-12017,0,60,1},
		{EMISSARY_OF_DEATH_2,-178277,205754,-12031,0,60,1},
		{BUTCHER_OF_INFINITY,-178716,205802,-12020,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-176565,207839,-11929,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-176281,208822,-11923,0,60,1},
		{BODY_SEVERER_2,-175791,208804,-11923,0,60,1},
		{BODY_SEVERER_2,-176259,207689,-11923,0,60,1},
		{EMISSARY_OF_DEATH_2,-175849,207508,-11929,0,60,1},
		{EMISSARY_OF_DEATH_2,-175453,208250,-11930,0,60,1},
		{EMISSARY_OF_DEATH,-175738,207914,-11946,0,60,1},
		{EMISSARY_OF_DEATH_2,-176339,209425,-11923,0,60,1},
		{EMISSARY_OF_DEATH_2,-176586,210424,-11928,0,60,1},
		{BUTCHER_OF_INFINITY,-176586,210546,-11923,0,60,1},
		{BUTCHER_OF_INFINITY,-175847,209365,-11922,0,60,1},
		{BODY_SEVERER_2,-175496,209498,-11924,0,60,1},
		{BODY_SEVERER_2,-175538,210252,-11940,0,60,1},
		{EMISSARY_OF_DEATH,-175527,209744,-11928,0,60,1},
		{BODY_SEVERER_2,-177940,210876,-12005,0,60,1},
		{BODY_SEVERER_2,-178935,210903,-12018,0,60,1},
		{SOUL_DEVOURER_2,-179331,211365,-12013,0,60,1},
		{SOUL_DEVOURER_2,-177637,211579,-12015,0,60,1},
		{EMISSARY_OF_DEATH_2,-177837,212356,-12037,0,60,1},
		{EMISSARY_OF_DEATH_2,-179030,212261,-12018,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-178367,212328,-12031,0,60,1}
	};
	
	public static final int[][] ROOMS_MOBS_HOED =
	{
		{BUTCHER_OF_INFINITY,-180364,211944,-12019,0,60,1},
		{BUTCHER_OF_INFINITY,-181616,211413,-12015,0,60,1},
		{BODY_SEVERER_2,-181404,211042,-12023,0,60,1},
		{SOUL_DEVOURER_2,-181558,212227,-12035,0,60,1},
		{SOUL_DEVOURER_2,-180459,212322,-12018,0,60,1},
		{EMISSARY_OF_DEATH,-180428,211180,-12014,0,60,1},
		{EMISSARY_OF_DEATH,-180718,212162,-12028,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183114,209397,-11923,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-182917,210495,-11925,0,60,1},
		{BUTCHER_OF_INFINITY,-183918,210225,-11934,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183862,209909,-11932,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183246,210631,-11923,0,60,1},
		{SOUL_DEVOURER_2,-182971,210522,-11924,0,60,1},
		{SOUL_DEVOURER_2,-183485,209406,-11921,0,60,1},
		{BUTCHER_OF_INFINITY,-183032,208822,-11923,0,60,1},
		{BUTCHER_OF_INFINITY,-182709,207817,-11929,0,60,1},
		{BODY_SEVERER_2,-182964,207746,-11924,0,60,1},
		{BODY_SEVERER_2,-183385,208847,-11922,0,60,1},
		{EMISSARY_OF_DEATH_2,-183684,208847,-11926,0,60,1},
		{EMISSARY_OF_DEATH_2,-183530,208725,-11926,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183968,207603,-11928,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-183608,208567,-11926,0,60,1},
		{EMISSARY_OF_DEATH_2,-181471,207159,-12020,0,60,1},
		{EMISSARY_OF_DEATH_2,-180213,207042,-12013,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-180213,206506,-12010,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-181720,206643,-12016,0,60,1},
		{BUTCHER_OF_INFINITY,-181743,206643,-12018,0,60,1},
		{BUTCHER_OF_INFINITY,-181028,205739,-12030,0,60,1},
		{BODY_SEVERER_2,-181431,205980,-12040,0,60,1},
		{EMISSARY_OF_DEATH,-178964,207168,-12014,0,60,1},
		{EMISSARY_OF_DEATH,-177658,207037,-12019,0,60,1},
		{SOUL_DEVOURER_2,-177730,206558,-12016,0,60,1},
		{SOUL_DEVOURER_2,-179132,206650,-12011,0,60,1},
		{EMISSARY_OF_DEATH_2,-179132,206155,-12017,0,60,1},
		{EMISSARY_OF_DEATH_2,-178277,205754,-12031,0,60,1},
		{BUTCHER_OF_INFINITY,-178716,205802,-12020,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-176565,207839,-11929,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-176281,208822,-11923,0,60,1},
		{BODY_SEVERER_2,-175791,208804,-11923,0,60,1},
		{BODY_SEVERER_2,-176259,207689,-11923,0,60,1},
		{EMISSARY_OF_DEATH_2,-175849,207508,-11929,0,60,1},
		{EMISSARY_OF_DEATH_2,-175453,208250,-11930,0,60,1},
		{EMISSARY_OF_DEATH,-175738,207914,-11946,0,60,1},
		{EMISSARY_OF_DEATH_2,-176339,209425,-11923,0,60,1},
		{EMISSARY_OF_DEATH_2,-176586,210424,-11928,0,60,1},
		{BUTCHER_OF_INFINITY,-176586,210546,-11923,0,60,1},
		{BUTCHER_OF_INFINITY,-175847,209365,-11922,0,60,1},
		{BODY_SEVERER_2,-175496,209498,-11924,0,60,1},
		{BODY_SEVERER_2,-175538,210252,-11940,0,60,1},
		{EMISSARY_OF_DEATH,-175527,209744,-11928,0,60,1},
		{BODY_SEVERER_2,-177940,210876,-12005,0,60,1},
		{BODY_SEVERER_2,-178935,210903,-12018,0,60,1},
		{SOUL_DEVOURER_2,-179331,211365,-12013,0,60,1},
		{SOUL_DEVOURER_2,-177637,211579,-12015,0,60,1},
		{EMISSARY_OF_DEATH_2,-177837,212356,-12037,0,60,1},
		{EMISSARY_OF_DEATH_2,-179030,212261,-12018,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-178367,212328,-12031,0,60,1},
		{WARD_OF_DEATH_1,-179664,209443,-12476,16384,120,1},
		{SOUL_COFIN_2,-179093,209738,-12480,40279,120,1},
		{WARD_OF_DEATH_1,-178248,209688,-12479,24320,120,1},
		{WARD_OF_DEATH_2,-177998,209100,-12480,16304,120,1},
		{SOUL_COFIN_2,-178246,208493,-12480,8968,120,1},
		{WARD_OF_DEATH_2,-178808,208339,-12480,-1540,120,1},
		{SOUL_COFIN_2,-179663,208738,-12480,0,120,1},
		{SOUL_COFIN_2,-180498,208330,-12467,3208,120,1},
		{WARD_OF_DEATH_1,-181070,208502,-12467,-7552,120,1},
		{WARD_OF_DEATH_2,-181310,209097,-12467,-16408,120,1},
		{SOUL_COFIN_2,-181069,209698,-12467,-24792,120,1},
		{WARD_OF_DEATH_2,-180228,209744,-12467,25920,120,1}
	};
	
	public static final int[][] ROOMS_MOBS_HIA =
	{
		{BODY_SEVERER_2,-179949,206751,-15521,0,60,1},
		{SOUL_DEVOURER_2,-179949,206505,-15522,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-180207,206362,-15512,0,60,1},
		{BODY_SEVERER_2,-180046,206109,-15511,0,60,1},
		{EMISSARY_OF_DEATH_2,-179843,205686,-15511,0,60,1},
		{SOUL_DEVOURER_2,-179658,206002,-15518,0,60,1},
		{EMISSARY_OF_DEATH_2,-179465,205648,-15498,0,60,1},
		{BODY_SEVERER_2,-179262,205896,-15536,0,60,1},
		{EMISSARY_OF_DEATH_2,-178907,205950,-15509,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-179128,206423,-15528,0,60,1},
		{SOUL_DEVOURER_2,-179262,206890,-15524,0,60,1},
		{BUTCHER_OF_INFINITY,-177238,208020,-15521,0,60,1},
		{SOUL_DEVOURER_2,-177202,207533,-15516,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-176900,207515,-15511,0,60,1},
		{EMISSARY_OF_DEATH,-176847,208022,-15523,0,60,1},
		{BUTCHER_OF_INFINITY,-176187,207648,-15499,0,60,1},
		{SOUL_DEVOURER_2,-176044,208039,-15524,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-176346,208233,-15533,0,60,1},
		{EMISSARY_OF_DEATH,-176440,208594,-15527,0,60,1},
		{BUTCHER_OF_INFINITY,-176938,208543,-15529,0,60,1},
		{EMISSARY_OF_DEATH,-176763,208108,-15523,0,60,1},
		{SOUL_DEVOURER_2,-176483,207762,-15520,0,60,1},
		{EMISSARY_OF_DEATH_2,-177125,210766,-15517,0,60,1},
		{BODY_SEVERER_2,-176810,210587,-15515,0,60,1},
		{SOUL_DEVOURER_2,-176820,210975,-15528,0,60,1},
		{EMISSARY_OF_DEATH,-176412,210873,-15510,0,60,1},
		{EMISSARY_OF_DEATH_2,-176261,211197,-15511,0,60,1},
		{BODY_SEVERER_2,-176241,211649,-15503,0,60,1},
		{SOUL_DEVOURER_2,-176656,211884,-15516,0,60,1},
		{EMISSARY_OF_DEATH,-176798,211540,-15541,0,60,1},
		{EMISSARY_OF_DEATH_2,-177130,211694,-15533,0,60,1},
		{BODY_SEVERER_2,-177436,211384,-15519,0,60,1},
		{SOUL_DEVOURER_2,-177138,211159,-15524,0,60,1},
		{EMISSARY_OF_DEATH_2,-179311,212338,-15521,0,60,1},
		{SOUL_DEVOURER_2,-178985,212502,-15513,0,60,1},
		{BUTCHER_OF_INFINITY,-179313,212730,-15522,0,60,1},
		{BODY_SEVERER_2,-178966,213036,-15510,0,60,1},
		{EMISSARY_OF_DEATH_2,-179352,213172,-15511,0,60,1},
		{SOUL_DEVOURER_2,-179696,213427,-15514,0,60,1},
		{BUTCHER_OF_INFINITY,-180096,213140,-15519,0,60,1},
		{BODY_SEVERER_2,-179957,212724,-15530,0,60,1},
		{SOUL_DEVOURER_2,-180240,212578,-15518,0,60,1},
		{BODY_SEVERER_2,-179891,212271,-15520,0,60,1},
		{BUTCHER_OF_INFINITY,-179593,212488,-15523,0,60,1},
		{BUTCHER_OF_INFINITY,-181746,211163,-15515,0,60,1},
		{SOUL_DEVOURER_2,-182002,211394,-15522,0,60,1},
		{EMISSARY_OF_DEATH_2,-181796,211647,-15511,0,60,1},
		{BUTCHER_OF_INFINITY,-182160,211615,-15518,0,60,1},
		{BODY_SEVERER_2,-182322,211843,-15510,0,60,1},
		{SOUL_DEVOURER_2,-182720,211686,-15519,0,60,1},
		{BODY_SEVERER_2,-182721,211231,-15535,0,60,1},
		{BUTCHER_OF_INFINITY,-182957,210874,-15515,0,60,1},
		{EMISSARY_OF_DEATH_2,-182705,210606,-15526,0,60,1},
		{BODY_SEVERER_2,-182359,210871,-15526,0,60,1},
		{SOUL_DEVOURER_2,-182051,210644,-15521,0,60,1},
		{EMISSARY_OF_DEATH,-181994,208452,-15514,0,60,1},
		{BUTCHER_OF_INFINITY,-182342,208661,-15510,0,60,1},
		{EMISSARY_OF_DEATH_2,-182471,208349,-15519,0,60,1},
		{BODY_SEVERER_2,-182960,208335,-15504,0,60,1},
		{EMISSARY_OF_DEATH,-182751,207925,-15514,0,60,1},
		{BUTCHER_OF_INFINITY,-182671,207442,-15531,0,60,1},
		{BODY_SEVERER_2,-182276,207438,-15527,0,60,1},
		{EMISSARY_OF_DEATH_2,-181975,207353,-15520,0,60,1},
		{EMISSARY_OF_DEATH,-181989,207795,-15527,0,60,1},
		{BUTCHER_OF_INFINITY,-181744,207997,-15520,0,60,1},
		{BODY_SEVERER_2,-182318,208070,-15523,0,60,1},
		{WARD_OF_DEATH_1,-178430,211520,-15504,28672,120,1},
		{WARD_OF_DEATH_2,-177341,209129,-15504,16384,120,1},
		{SOUL_COFIN_2,-178067,207862,-15504,8192,120,1},
		{WARD_OF_DEATH_1,-180679,207614,-15520,61439,120,1},
		{WARD_OF_DEATH_2,-181761,209949,-15520,49151,120,1},
		{SOUL_COFIN_2,-180328,211686,-15504,36863,120,1}
	};
	
	public static final int[][] ROOMS_MOBS_HID =
	{
		{BODY_SEVERER_2,-179949,206751,-15521,0,60,1},
		{SOUL_DEVOURER_2,-179949,206505,-15522,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-180207,206362,-15512,0,60,1},
		{BODY_SEVERER_2,-180046,206109,-15511,0,60,1},
		{EMISSARY_OF_DEATH_2,-179843,205686,-15511,0,60,1},
		{SOUL_DEVOURER_2,-179658,206002,-15518,0,60,1},
		{EMISSARY_OF_DEATH_2,-179465,205648,-15498,0,60,1},
		{BODY_SEVERER_2,-179262,205896,-15536,0,60,1},
		{EMISSARY_OF_DEATH_2,-178907,205950,-15509,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-179128,206423,-15528,0,60,1},
		{SOUL_DEVOURER_2,-179262,206890,-15524,0,60,1},
		{BUTCHER_OF_INFINITY,-177238,208020,-15521,0,60,1},
		{SOUL_DEVOURER_2,-177202,207533,-15516,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-176900,207515,-15511,0,60,1},
		{EMISSARY_OF_DEATH,-176847,208022,-15523,0,60,1},
		{BUTCHER_OF_INFINITY,-176187,207648,-15499,0,60,1},
		{SOUL_DEVOURER_2,-176044,208039,-15524,0,60,1},
		{LAW_SCHOLAR_OF_CONCLUSIONS,-176346,208233,-15533,0,60,1},
		{EMISSARY_OF_DEATH,-176440,208594,-15527,0,60,1},
		{BUTCHER_OF_INFINITY,-176938,208543,-15529,0,60,1},
		{EMISSARY_OF_DEATH,-176763,208108,-15523,0,60,1},
		{SOUL_DEVOURER_2,-176483,207762,-15520,0,60,1},
		{EMISSARY_OF_DEATH_2,-177125,210766,-15517,0,60,1},
		{BODY_SEVERER_2,-176810,210587,-15515,0,60,1},
		{SOUL_DEVOURER_2,-176820,210975,-15528,0,60,1},
		{EMISSARY_OF_DEATH,-176412,210873,-15510,0,60,1},
		{EMISSARY_OF_DEATH_2,-176261,211197,-15511,0,60,1},
		{BODY_SEVERER_2,-176241,211649,-15503,0,60,1},
		{SOUL_DEVOURER_2,-176656,211884,-15516,0,60,1},
		{EMISSARY_OF_DEATH,-176798,211540,-15541,0,60,1},
		{EMISSARY_OF_DEATH_2,-177130,211694,-15533,0,60,1},
		{BODY_SEVERER_2,-177436,211384,-15519,0,60,1},
		{SOUL_DEVOURER_2,-177138,211159,-15524,0,60,1},
		{EMISSARY_OF_DEATH_2,-179311,212338,-15521,0,60,1},
		{SOUL_DEVOURER_2,-178985,212502,-15513,0,60,1},
		{BUTCHER_OF_INFINITY,-179313,212730,-15522,0,60,1},
		{BODY_SEVERER_2,-178966,213036,-15510,0,60,1},
		{EMISSARY_OF_DEATH_2,-179352,213172,-15511,0,60,1},
		{SOUL_DEVOURER_2,-179696,213427,-15514,0,60,1},
		{BUTCHER_OF_INFINITY,-180096,213140,-15519,0,60,1},
		{BODY_SEVERER_2,-179957,212724,-15530,0,60,1},
		{SOUL_DEVOURER_2,-180240,212578,-15518,0,60,1},
		{BODY_SEVERER_2,-179891,212271,-15520,0,60,1},
		{BUTCHER_OF_INFINITY,-179593,212488,-15523,0,60,1},
		{BUTCHER_OF_INFINITY,-181746,211163,-15515,0,60,1},
		{SOUL_DEVOURER_2,-182002,211394,-15522,0,60,1},
		{EMISSARY_OF_DEATH_2,-181796,211647,-15511,0,60,1},
		{BUTCHER_OF_INFINITY,-182160,211615,-15518,0,60,1},
		{BODY_SEVERER_2,-182322,211843,-15510,0,60,1},
		{SOUL_DEVOURER_2,-182720,211686,-15519,0,60,1},
		{BODY_SEVERER_2,-182721,211231,-15535,0,60,1},
		{BUTCHER_OF_INFINITY,-182957,210874,-15515,0,60,1},
		{EMISSARY_OF_DEATH_2,-182705,210606,-15526,0,60,1},
		{BODY_SEVERER_2,-182359,210871,-15526,0,60,1},
		{SOUL_DEVOURER_2,-182051,210644,-15521,0,60,1},
		{EMISSARY_OF_DEATH,-181994,208452,-15514,0,60,1},
		{BUTCHER_OF_INFINITY,-182342,208661,-15510,0,60,1},
		{EMISSARY_OF_DEATH_2,-182471,208349,-15519,0,60,1},
		{BODY_SEVERER_2,-182960,208335,-15504,0,60,1},
		{EMISSARY_OF_DEATH,-182751,207925,-15514,0,60,1},
		{BUTCHER_OF_INFINITY,-182671,207442,-15531,0,60,1},
		{BODY_SEVERER_2,-182276,207438,-15527,0,60,1},
		{EMISSARY_OF_DEATH_2,-181975,207353,-15520,0,60,1},
		{EMISSARY_OF_DEATH,-181989,207795,-15527,0,60,1},
		{BUTCHER_OF_INFINITY,-181744,207997,-15520,0,60,1},
		{BODY_SEVERER_2,-182318,208070,-15523,0,60,1},
		{WARD_OF_DEATH_1,-178430,211520,-15504,28672,120,1},
		{WARD_OF_DEATH_2,-177341,209129,-15504,16384,120,1},
		{SOUL_COFIN_2,-178067,207862,-15504,8192,120,1},
		{WARD_OF_DEATH_1,-180679,207614,-15520,61439,120,1},
		{WARD_OF_DEATH_2,-181761,209949,-15520,49151,120,1},
		{SOUL_COFIN_2,-180328,211686,-15504,36863,120,1}
	};
	
	public static final int[][] ROOMS_TUMORS_HOEA =
	{
		{SYMBOL_OF_COHEMENES,-180911,211652,-12029,49151,240,1},
		{SYMBOL_OF_COHEMENES,-180911,206551,-12029,16384,240,1},
		{SYMBOL_OF_COHEMENES,-178417,206558,-12032,16384,240,1},
		{SYMBOL_OF_COHEMENES,-178418,211653,-12029,49151,240,1},
		{TUMOR_OF_DEATH_ALIVE_81,-183290,210004,-11948,61439,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-183288,208205,-11948,4096,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-176039,208203,-11948,28672,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-176036,210002,-11948,36863,0,1},
		{WARD_OF_DEATH_2,-179664,209443,-12476,16384,120,1},
		{WARD_OF_DEATH_2,-179093,209738,-12480,40279,120,1},
		{WARD_OF_DEATH_2,-178248,209688,-12479,24320,120,1},
		{WARD_OF_DEATH_2,-177998,209100,-12480,16304,120,1},
		{WARD_OF_DEATH_2,-178246,208493,-12480,8968,120,1},
		{WARD_OF_DEATH_2,-178808,208339,-12480,-1540,120,1},
		{WARD_OF_DEATH_2,-179663,208738,-12480,0,120,1},
		{WARD_OF_DEATH_2,-180498,208330,-12467,3208,120,1},
		{WARD_OF_DEATH_2,-181070,208502,-12467,-7552,120,1},
		{WARD_OF_DEATH_2,-181310,209097,-12467,-16408,120,1},
		{WARD_OF_DEATH_2,-181069,209698,-12467,-24792,120,1},
		{WARD_OF_DEATH_2,-180228,209744,-12467,25920,120,1}
	};
	
	public static final int[][] ROOMS_TUMORS_HIA =
	{
		{TUMOR_OF_DEATH_ALIVE_81,-179779,212540,-15520,49151,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-177028,211135,-15520,36863,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-176355,208043,-15520,28672,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-179284,205990,-15520,16384,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-182268,208218,-15520,4096,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-182069,211140,-15520,61439,0,1}
	};
	
	public static final int[][] ROOMS_BOSSES_HIA =
	{
		{KARPENCHARR,-179303,213090,-15504,49151,0,1},
		{ROMEROHIV,-176426,211219,-15504,36863,0,1},
		{HITCHKARHIEK,-177040,207870,-15504,28672,0,1},
		{FREEDKYILLA,-179762,206479,-15504,16384,0,1},
		{YEHAN_CRAVENZIZAD,-182388,207599,-15504,4096,0,1},
		{YEHAN_JAXSIBHAN,-182733,211096,-15504,61439,0,1}
	};
	
	public static final int[][] ROOMS_BOSSES_HID =
	{
		{KARPENCHARR,-179303,213090,-15504,49151,0,1},
		{ROMEROHIV,-176426,211219,-15504,36863,0,1},
		{HITCHKARHIEK,-177040,207870,-15504,28672,0,1},
		{FREEDKYILLA,-179762,206479,-15504,16384,0,1},
		{YEHAN_CRAVENZIZAD,-182388,207599,-15504,4096,0,1},
		{YEHAN_JAXSIBHAN,-182733,211096,-15504,61439,0,1}
	};
	
	public static final int[][] COHEMENES_SPAWN_HOEA =
	{
		{COHEMENES,-178472,211823,-12025,0,0,-1},
		{COHEMENES,-180926,211887,-12029,0,0,-1},
		{COHEMENES,-180906,206635,-12032,0,0,-1},
		{COHEMENES,-178492,206426,-12023,0,0,-1}
	};
	
	public static final int[][] SEEDS_SPAWN_HOED =
	{
		{SEED,-178418,211653,-12029,49151,0,1},
		{SEED,-178417,206558,-12029,16384,0,1},
		{SEED,-180911,206551,-12029,16384,0,1},
		{SEED,-180911,211652,-12029,49151,0,1}
	};
	
	public static final int[][] TUMOR_DEAD_SPAWN_HOED =
	{
		{DESTROYED_TUMOR_2,-176036,210002,-11948,36863,0,1},
		{DESTROYED_TUMOR_2,-176039,208203,-11948,28672,0,1},
		{DESTROYED_TUMOR_2,-183288,208205,-11948,4096,0,1},
		{DESTROYED_TUMOR_2,-183290,210004,-11948,61439,0,1}
	};
	
	public static final int[][] TUMOR_ALIVE_SPAWN_HOED =
	{
		{TUMOR_OF_DEATH_ALIVE_81,-176036,210002,-11948,36863,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-176039,208203,-11948,28672,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-183288,208205,-11948,4096,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-183290,210004,-11948,61439,0,1}
	};
	
	public static final int[][] ROOMS_TUMORS_HID =
	{
		{DESTROYED_TUMOR_2,-179779,212540,-15520,49151,0,1},
		{DESTROYED_TUMOR_2,-177028,211135,-15520,36863,0,1},
		{DESTROYED_TUMOR_2,-176355,208043,-15520,28672,0,1},
		{DESTROYED_TUMOR_2,-179284,205990,-15520,16384,0,1},
		{DESTROYED_TUMOR_2,-182268,208218,-15520,4096,0,1},
		{DESTROYED_TUMOR_2,-182069,211140,-15520,61439,0,1}
	};
	
	public static final int[][] ROOMS_ALIVE_TUMORS_HID =
	{
		{TUMOR_OF_DEATH_ALIVE_81,-179779,212540,-15520,49151,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-177028,211135,-15520,36863,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-176355,208043,-15520,28672,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-179284,205990,-15520,16384,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-182268,208218,-15520,4096,0,1},
		{TUMOR_OF_DEATH_ALIVE_81,-182069,211140,-15520,61439,0,1}
	};
	
	public static final int[][] SOULWAGON_SPAWN_HID =
	{
		{SOUL_DEVOURER_3,-180003,206703,-15520,0,0,-1},
		{SOUL_DEVOURER_3,-180056,216162,-15511,0,0,-1},
		{SOUL_DEVOURER_3,-179586,205657,-15499,0,0,-1},
		{SOUL_DEVOURER_3,-179029,205991,-15518,0,0,-1},
		{SOUL_DEVOURER_3,-178960,206658,-15526,0,0,-1},
		{SOUL_DEVOURER_3,-179350,206484,-15524,0,0,-1},
		{SOUL_DEVOURER_3,-179526,206900,-15520,0,0,-1},
		{SOUL_DEVOURER_3,-179329,205916,-15532,0,0,-1}
	};
	
	public static final int[][] ROOM_1_MOBS_HOSA =
	{
		{FANATIC_OF_INFINITY, -186296, 208200, -9544},
		{FANATIC_OF_INFINITY, -186161, 208345, -9544},
		{FANATIC_OF_INFINITY, -186296, 208403, -9544},
		{ROTTEN_MESSENGER, -186107, 208113, -9528},
		{ROTTEN_MESSENGER, -186350, 208200, -9544}
	};
	
	public static final int[][] ROOM_2_MOBS_HOSA =
	{
		{ZEALOT_OF_INFINITY, -184433, 210953, -9536},
		{ZEALOT_OF_INFINITY, -184406, 211301, -9536},
		{FANATIC_OF_INFINITY, -184541, 211272, -9544},
		{ROTTEN_MESSENGER, -184244, 211098, -9536},
		{ROTTEN_MESSENGER, -184352, 211243, -9536},
		{ROTTEN_MESSENGER, -184298, 211330, -9528}
	};
	
	public static final int[][] ROOM_3_MOBS_HOSA =
	{
		{BODY_SEVERER_1, -182611, 213984, -9520},
		{BODY_SEVERER_1, -182908, 214071, -9520},
		{BODY_SEVERER_1, -182962, 213868, -9512},
		{FANATIC_OF_INFINITY, -182881, 213955, -9512},
		{ZEALOT_OF_INFINITY, -182827, 213781, -9504},
		{ZEALOT_OF_INFINITY, -182530, 213984, -9528},
		{ROTTEN_MESSENGER, -182935, 213723, -9512},
		{ROTTEN_MESSENGER, -182557, 213868, -9520}
	};
	
	public static final int[][] ROOM_4_MOBS_HOSA =
	{
		{SOUL_EXPLOITER, -180958, 216860, -9544},
		{SOUL_EXPLOITER, -181012, 216628, -9536},
		{SOUL_EXPLOITER, -181120, 216715, -9536},
		{BODY_HARVESTER, -180661, 216599, -9536},
		{BODY_HARVESTER, -181039, 216599, -9536},
		{ZEALOT_OF_INFINITY, -180715, 216599, -9536},
		{ZEALOT_OF_INFINITY, -181012, 216889, -9536},
		{BODY_SEVERER_1, -180931, 216918, -9536},
		{BODY_SEVERER_1, -180742, 216628, -9536}
	};
	
	public static final int[][] ROOM_5_MOBS_HOSA =
	{
		{BODY_SEVERER_1, -177372, 217854, -9536},
		{BODY_SEVERER_1, -177237, 218140, -9536},
		{BODY_SEVERER_1, -177021, 217647, -9528},
		{BODY_HARVESTER, -177372, 217792, -9544},
		{BODY_HARVESTER, -177372, 218053, -9536},
		{SOUL_EXPLOITER, -177291, 217734, -9544},
		{SOUL_EXPLOITER, -177264, 217792, -9544},
		{SOUL_EXPLOITER, -177264, 218053, -9536},
		{SOUL_DEVOURER_1, -177156, 217792, -9536},
		{SOUL_DEVOURER_1, -177075, 217647, -9528}
	};
	
	public static final int[][] TUMOR_SPAWNS_HOSA =
	{
		{-186327,208286,-9544},
		{-184429,211155,-9544},
		{-182811,213871,-9496},
		{-181039,216633,-9528},
		{-177264,217760,-9544}
	};
	
	public static final int[][] TWIN_SPAWNS_HOSA = 
	{
		{KLODEKUS,-173727,218169,-9536},
		{KLANIKUS,-173727,218049,-9536}
	};
	
	public static final int[] TEPIOS_SPAWN_HOSA = {-173727,218109,-9536};
	
	public static final int[][] ROOM_1_MOBS_HOSD =
	{
		{FANATIC_OF_INFINITY,-173712,217838,-9559},
		{FANATIC_OF_INFINITY,-173489,218281,-9557},
		{FANATIC_OF_INFINITY,-173824,218389,-9558},
		{ROTTEN_MESSENGER,-174018,217970,-9559},
		{ROTTEN_MESSENGER,-173382,218198,-9547}
	};
	
	public static final int[][] ROOM_2_MOBS_HOSD =
	{
		{ZEALOT_OF_INFINITY,-173456,217976,-9556},
		{ZEALOT_OF_INFINITY,-173673,217951,-9547},
		{FANATIC_OF_INFINITY,-173622,218233,-9547},
		{ROTTEN_MESSENGER,-173775,218218,-9545},
		{ROTTEN_MESSENGER,-173660,217980,-9542},
		{ROTTEN_MESSENGER,-173712,217838,-9559}
	};
	
	public static final int[][] ROOM_3_MOBS_HOSD =
	{
		{BODY_SEVERER_1,-173489,218281,-9557},
		{BODY_SEVERER_1,-173824,218389,-9558},
		{BODY_SEVERER_1,-174018,217970,-9559},
		{FANATIC_OF_INFINITY,-173382,218198,-9547},
		{ZEALOT_OF_INFINITY,-173456,217976,-9556},
		{ZEALOT_OF_INFINITY,-173673,217951,-9547},
		{ROTTEN_MESSENGER,-173622,218233,-9547},
		{ROTTEN_MESSENGER,-173775,218218,-9545}
	};
	
	public static final int[][] ROOM_4_MOBS_HOSD =
	{
		{SOUL_EXPLOITER,-173660,217980,-9542},
		{SOUL_EXPLOITER,-173712,217838,-9559},
		{SOUL_EXPLOITER,-173489,218281,-9557},
		{BODY_HARVESTER,-173824,218389,-9558},
		{BODY_HARVESTER,-174018,217970,-9559},
		{ZEALOT_OF_INFINITY,-173382,218198,-9547},
		{ZEALOT_OF_INFINITY,-173456,217976,-9556},
		{BODY_SEVERER_1,-173673,217951,-9547},
		{BODY_SEVERER_1,-173622,218233,-9547}
	};
	
	public static final int[][] ROOM_5_MOBS_HOSD =
	{
		{BODY_SEVERER_1,-173775,218218,-9545},
		{BODY_SEVERER_1,-173660,217980,-9542},
		{BODY_SEVERER_1,-173712,217838,-9559},
		{BODY_HARVESTER,-173489,218281,-9557},
		{BODY_HARVESTER,-173824,218389,-9558},
		{SOUL_EXPLOITER,-174018,217970,-9559},
		{SOUL_EXPLOITER,-173382,218198,-9547},
		{SOUL_EXPLOITER,-173456,217976,-9556},
		{SOUL_DEVOURER_1,-173673,217951,-9547},
		{SOUL_DEVOURER_1,-173622,218233,-9547}
	};
	
	public static final int[][] TUMOR_SPAWNS_HOSD =
	{
		{-173727,218109,-9536},
		{-173727,218109,-9536},
		{-173727,218109,-9536},
		{-173727,218109,-9536},
		{-173727,218109,-9536}
	};
	
	public static final int[][] TWIN_SPAWNS_HOSD =
	{
		{KLODEKUS,-173727,218169,-9536},
		{KLANIKUS,-173727,218049,-9536}
	};
	
	public static final int[] TEPIOS_SPAWN_HOSD = {-173727, 218109, -9536};
	
	
	public static final void removeBuffs(L2Character character)
	{
		character.stopAllEffectsExceptThoseThatLastThroughDeath();
		if (character.hasSummon())
		{
			character.getSummon().stopAllEffectsExceptThoseThatLastThroughDeath();
		}
		
	}
}
