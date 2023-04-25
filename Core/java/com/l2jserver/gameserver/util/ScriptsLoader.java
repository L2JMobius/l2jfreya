/*
 * Copyright (C) 2004-2014 L2J DataPack
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
package com.l2jserver.gameserver.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import com.l2jserver.scripts.L2AttackableAIScript;
import com.l2jserver.scripts.ChamberOfDelusion.Aenkinel;
import com.l2jserver.scripts.ChamberOfDelusion.DelusionTeleport.DelusionTeleport;
import com.l2jserver.scripts.ChamberOfDelusion.East.East;
import com.l2jserver.scripts.ChamberOfDelusion.North.North;
import com.l2jserver.scripts.ChamberOfDelusion.South.South;
import com.l2jserver.scripts.ChamberOfDelusion.Tower.Tower;
import com.l2jserver.scripts.ChamberOfDelusion.West.West;
import com.l2jserver.scripts.ai.Barakiel;
import com.l2jserver.scripts.ai.BeastFarm;
import com.l2jserver.scripts.ai.Chests;
import com.l2jserver.scripts.ai.Core;
import com.l2jserver.scripts.ai.CryptsOfDisgrace;
import com.l2jserver.scripts.ai.DenOfEvil;
import com.l2jserver.scripts.ai.DrChaos;
import com.l2jserver.scripts.ai.FairyTrees;
import com.l2jserver.scripts.ai.FieldOfWhispersSilence;
import com.l2jserver.scripts.ai.FleeNpc;
import com.l2jserver.scripts.ai.FlyingNpcs;
import com.l2jserver.scripts.ai.FrozenLabyrinth;
import com.l2jserver.scripts.ai.GargosPailaka;
import com.l2jserver.scripts.ai.GiantScouts;
import com.l2jserver.scripts.ai.Golkonda;
import com.l2jserver.scripts.ai.GordonRaid;
import com.l2jserver.scripts.ai.GraveRobbers;
import com.l2jserver.scripts.ai.Hallate;
import com.l2jserver.scripts.ai.HolyBrazier;
import com.l2jserver.scripts.ai.HotSpringsBuffs;
import com.l2jserver.scripts.ai.KarulBugbear;
import com.l2jserver.scripts.ai.Kernon;
import com.l2jserver.scripts.ai.MithrilMinesLocation;
import com.l2jserver.scripts.ai.Monastery;
import com.l2jserver.scripts.ai.Orfen;
import com.l2jserver.scripts.ai.PavelArchaic;
import com.l2jserver.scripts.ai.PlainsOfDion;
import com.l2jserver.scripts.ai.PlainsOfLizardman;
import com.l2jserver.scripts.ai.PolymorphingAngel;
import com.l2jserver.scripts.ai.PolymorphingOnAttack;
import com.l2jserver.scripts.ai.QueenAnt;
import com.l2jserver.scripts.ai.QueenShyeed;
import com.l2jserver.scripts.ai.RagnaOrcFrightened;
import com.l2jserver.scripts.ai.RagnaOrcs;
import com.l2jserver.scripts.ai.SearchingMaster;
import com.l2jserver.scripts.ai.SeeThroughSilentMove;
import com.l2jserver.scripts.ai.SelMahumDrill;
import com.l2jserver.scripts.ai.SelMahumSquad;
import com.l2jserver.scripts.ai.SilentValley;
import com.l2jserver.scripts.ai.StakatoNestMonsters;
import com.l2jserver.scripts.ai.SummonMinions;
import com.l2jserver.scripts.ai.VanHalter;
import com.l2jserver.scripts.ai.WarriorFishingBlock;
import com.l2jserver.scripts.ai.Antharas.Antharas;
import com.l2jserver.scripts.ai.Baium.Baium;
import com.l2jserver.scripts.ai.Nons.NoAttacking;
import com.l2jserver.scripts.ai.Nons.NoChampion;
import com.l2jserver.scripts.ai.Nons.NoLethalable;
import com.l2jserver.scripts.ai.Nons.NoMoving;
import com.l2jserver.scripts.ai.Nons.NoRandomAnimation;
import com.l2jserver.scripts.ai.Nons.NoRandomWalk;
import com.l2jserver.scripts.ai.Nons.NoTalking;
import com.l2jserver.scripts.ai.Valakas.Valakas;
import com.l2jserver.scripts.ai.Zaken.PirateZaken;
import com.l2jserver.scripts.ai.fantasy_isle.HandysBlockCheckerEvent;
import com.l2jserver.scripts.ai.fantasy_isle.MC_Show;
import com.l2jserver.scripts.ai.npc.PcBangPoint.PcBangPoint;
import com.l2jserver.scripts.ai.npc.PriestOfBlessing.PriestOfBlessing;
import com.l2jserver.scripts.custom.EchoCrystals.EchoCrystals;
import com.l2jserver.scripts.custom.HeroCirclet.HeroCirclet;
import com.l2jserver.scripts.custom.HeroWeapon.HeroWeapon;
import com.l2jserver.scripts.custom.MissQueen.MissQueen;
import com.l2jserver.scripts.custom.NewbieCoupons.NewbieCoupons;
import com.l2jserver.scripts.custom.NpcLocationInfo.NpcLocationInfo;
import com.l2jserver.scripts.custom.PinsAndPouchUnseal.PinsAndPouchUnseal;
import com.l2jserver.scripts.custom.PurchaseBracelet.PurchaseBracelet;
import com.l2jserver.scripts.custom.RaidbossInfo.RaidbossInfo;
import com.l2jserver.scripts.custom.ShadowWeapons.ShadowWeapons;
import com.l2jserver.scripts.custom.Validators.SkillTransferValidator;
import com.l2jserver.scripts.custom.Validators.SubClassSkills;
import com.l2jserver.scripts.events.HideAndSeek.HideAndSeek;
import com.l2jserver.scripts.freya.FreyasSteward.FreyasSteward;
import com.l2jserver.scripts.freya.IceQueensCastle.IceQueensCastle;
import com.l2jserver.scripts.freya.IceQueensCastleNormalBattle.IceQueensCastleNormalBattle;
import com.l2jserver.scripts.freya.IceQueensCastleUltimateBattle.IceQueensCastleUltimateBattle;
import com.l2jserver.scripts.freya.Jinia.Jinia;
import com.l2jserver.scripts.freya.JiniaGuildHideout1.JiniaGuildHideout1;
import com.l2jserver.scripts.freya.JiniaGuildHideout2.JiniaGuildHideout2;
import com.l2jserver.scripts.freya.JiniaGuildHideout3.JiniaGuildHideout3;
import com.l2jserver.scripts.freya.JiniaGuildHideout4.JiniaGuildHideout4;
import com.l2jserver.scripts.freya.MithrilMine.MithrilMine;
import com.l2jserver.scripts.freya.Rafforty.Rafforty;
import com.l2jserver.scripts.freya.Sirra.Sirra;
import com.l2jserver.scripts.gracia.EnergySeeds;
import com.l2jserver.scripts.gracia.Enira;
import com.l2jserver.scripts.gracia.Lindvior;
import com.l2jserver.scripts.gracia.Maguen;
import com.l2jserver.scripts.gracia.StarStones;
import com.l2jserver.scripts.gracia.AbyssGaze.AbyssGaze;
import com.l2jserver.scripts.gracia.DestroyedTumors.DestroyedTumors;
import com.l2jserver.scripts.gracia.EkimusMouth.EkimusMouth;
import com.l2jserver.scripts.gracia.FortuneTelling.FortuneTelling;
import com.l2jserver.scripts.gracia.GeneralDilios.GeneralDilios;
import com.l2jserver.scripts.gracia.Lekon.Lekon;
import com.l2jserver.scripts.gracia.Nemo.Nemo;
import com.l2jserver.scripts.gracia.Nottingale.Nottingale;
import com.l2jserver.scripts.gracia.SecretArea.SecretArea;
import com.l2jserver.scripts.gracia.SeedOfAnnihilation.SeedOfAnnihilation;
import com.l2jserver.scripts.gracia.SeedOfDestruction.Stage1;
import com.l2jserver.scripts.gracia.SeedOfInfinity.HallOfErosionAttack;
import com.l2jserver.scripts.gracia.SeedOfInfinity.HallOfErosionDefence;
import com.l2jserver.scripts.gracia.SeedOfInfinity.HallOfSufferingAttack;
import com.l2jserver.scripts.gracia.SeedOfInfinity.HallOfSufferingDefence;
import com.l2jserver.scripts.gracia.SeedOfInfinity.HeartInfinityAttack;
import com.l2jserver.scripts.gracia.SeedOfInfinity.HeartInfinityDefence;
import com.l2jserver.scripts.gracia.Seyo.Seyo;
import com.l2jserver.scripts.hellbound.Amaskari;
import com.l2jserver.scripts.hellbound.Beleth;
import com.l2jserver.scripts.hellbound.Chimeras;
import com.l2jserver.scripts.hellbound.DemonPrinceRaid;
import com.l2jserver.scripts.hellbound.Engine;
import com.l2jserver.scripts.hellbound.Epidos;
import com.l2jserver.scripts.hellbound.HellboundCore;
import com.l2jserver.scripts.hellbound.Hellenark;
import com.l2jserver.scripts.hellbound.Keltas;
import com.l2jserver.scripts.hellbound.MasterZelos;
import com.l2jserver.scripts.hellbound.NaiaLock;
import com.l2jserver.scripts.hellbound.OutpostCaptain;
import com.l2jserver.scripts.hellbound.RandomSpawn;
import com.l2jserver.scripts.hellbound.RankuRaid;
import com.l2jserver.scripts.hellbound.Remnants;
import com.l2jserver.scripts.hellbound.Sandstorms;
import com.l2jserver.scripts.hellbound.SinWardens;
import com.l2jserver.scripts.hellbound.Slaves;
import com.l2jserver.scripts.hellbound.AnomicFoundry.AnomicFoundry;
import com.l2jserver.scripts.hellbound.BaseTower.BaseTower;
import com.l2jserver.scripts.hellbound.Bernarde.Bernarde;
import com.l2jserver.scripts.hellbound.Budenka.Budenka;
import com.l2jserver.scripts.hellbound.Buron.Buron;
import com.l2jserver.scripts.hellbound.DemonPrinceFloor.DemonPrinceFloor;
import com.l2jserver.scripts.hellbound.Falk.Falk;
import com.l2jserver.scripts.hellbound.HellboundTown.HellboundTown;
import com.l2jserver.scripts.hellbound.Hude.Hude;
import com.l2jserver.scripts.hellbound.Jude.Jude;
import com.l2jserver.scripts.hellbound.Kanaf.Kanaf;
import com.l2jserver.scripts.hellbound.Kief.Kief;
import com.l2jserver.scripts.hellbound.Natives.Natives;
import com.l2jserver.scripts.hellbound.Quarry.Quarry;
import com.l2jserver.scripts.hellbound.RankuFloor.RankuFloor;
import com.l2jserver.scripts.hellbound.Shadai.Shadai;
import com.l2jserver.scripts.hellbound.Solomon.Solomon;
import com.l2jserver.scripts.hellbound.TowerOfInfinitum.TowerOfInfinitum;
import com.l2jserver.scripts.hellbound.TowerOfNaia.TowerOfNaia;
import com.l2jserver.scripts.hellbound.TullyWorkshop.TullyWorkshop;
import com.l2jserver.scripts.hellbound.Warpgate.Warpgate;
import com.l2jserver.scripts.imperial_tomb.FinalEmperialTomb.FinalEmperialTomb;
import com.l2jserver.scripts.isle_of_prayer.DarkWaterDragon;
import com.l2jserver.scripts.isle_of_prayer.EvasGiftBoxes;
import com.l2jserver.scripts.isle_of_prayer.PrisonGuards;
import com.l2jserver.scripts.isle_of_prayer.CrystalCaverns.CrystalCaverns;
import com.l2jserver.scripts.isle_of_prayer.DarkCloudMansion.DarkCloudMansion;
import com.l2jserver.scripts.isle_of_prayer.IOPRace.IOPRace;
import com.l2jserver.scripts.isle_of_souls.NornilsGarden.NornilsGarden;
import com.l2jserver.scripts.kamaloka.BladeOtis;
import com.l2jserver.scripts.kamaloka.CrimsonHatuOtis;
import com.l2jserver.scripts.kamaloka.FollowerOfAllosce;
import com.l2jserver.scripts.kamaloka.FollowerOfMontagnar;
import com.l2jserver.scripts.kamaloka.KaimAbigore;
import com.l2jserver.scripts.kamaloka.Kamaloka;
import com.l2jserver.scripts.kamaloka.KelBilette;
import com.l2jserver.scripts.kamaloka.OlAriosh;
import com.l2jserver.scripts.kamaloka.PowderKeg;
import com.l2jserver.scripts.kamaloka.SeerFlouros;
import com.l2jserver.scripts.kamaloka.VenomousStorace;
import com.l2jserver.scripts.kamaloka.WeirdBunei;
import com.l2jserver.scripts.kamaloka.WhiteAllosce;
import com.l2jserver.scripts.l2jpdt_npcs.AutomaticTeleporterNPC.AutomaticTeleporterNPC;
import com.l2jserver.scripts.l2jpdt_npcs.GmShopNPC.GmShopNPC;
import com.l2jserver.scripts.l2jpdt_npcs.LevelChooseNpc.LevelChooseNpc;
import com.l2jserver.scripts.l2jpdt_npcs.MonsterAchievements.AchievementEvent;
import com.l2jserver.scripts.l2jpdt_npcs.Multiclass.Multiclass;
import com.l2jserver.scripts.l2jpdt_npcs.PremiumServiceNPC.PremiumServiceNPC;
import com.l2jserver.scripts.l2jpdt_npcs.SchemeBuffer.NpcBuffer;
import com.l2jserver.scripts.plains_of_the_lizardmen.SeerUgoros;
import com.l2jserver.scripts.primeval_isle.Sailren;
import com.l2jserver.scripts.primeval_isle.TRex;
import com.l2jserver.scripts.quests.*;
import com.l2jserver.scripts.seven_signs.HideoutOfTheDawn.HideoutOfTheDawn;
import com.l2jserver.scripts.seven_signs.SanctumOftheLordsOfDawn.SanctumOftheLordsOfDawn;
import com.l2jserver.scripts.teleports.CrumaTower.CrumaTower;
import com.l2jserver.scripts.teleports.ElrokiTeleporters.ElrokiTeleporters;
import com.l2jserver.scripts.teleports.GatekeeperSpirit.GatekeeperSpirit;
import com.l2jserver.scripts.teleports.HuntingGroundsTeleport.HuntingGroundsTeleport;
import com.l2jserver.scripts.teleports.Klein.Klein;
import com.l2jserver.scripts.teleports.MithrilMines.MithrilMines;
import com.l2jserver.scripts.teleports.NewbieTravelToken.NewbieTravelToken;
import com.l2jserver.scripts.teleports.NoblesseTeleport.NoblesseTeleport;
import com.l2jserver.scripts.teleports.OracleTeleport.OracleTeleport;
import com.l2jserver.scripts.teleports.PaganTeleporters.PaganTeleporters;
import com.l2jserver.scripts.teleports.StakatoNest.StakatoNest;
import com.l2jserver.scripts.teleports.SteelCitadelTeleport.SteelCitadelTeleport;
import com.l2jserver.scripts.teleports.StrongholdsTeleports.StrongholdsTeleports;
import com.l2jserver.scripts.teleports.Survivor.Survivor;
import com.l2jserver.scripts.teleports.TeleportToFantasy.TeleportToFantasy;
import com.l2jserver.scripts.teleports.TeleportToRaceTrack.TeleportToRaceTrack;
import com.l2jserver.scripts.teleports.TeleportToUndergroundColiseum.TeleportToUndergroundColiseum;
import com.l2jserver.scripts.teleports.TeleportWithCharm.TeleportWithCharm;
import com.l2jserver.scripts.teleports.ToIVortex.ToIVortex;
import com.l2jserver.scripts.vehicles.BoatGiranTalking;
import com.l2jserver.scripts.vehicles.BoatGludinRune;
import com.l2jserver.scripts.vehicles.BoatInnadrilTour;
import com.l2jserver.scripts.vehicles.BoatRunePrimeval;
import com.l2jserver.scripts.vehicles.BoatTalkingGludin;
import com.l2jserver.scripts.vehicles.AirShipGludioGracia.AirShipGludioGracia;
import com.l2jserver.scripts.vehicles.KeucereusNorthController.KeucereusNorthController;
import com.l2jserver.scripts.vehicles.KeucereusSouthController.KeucereusSouthController;
import com.l2jserver.scripts.vehicles.SoDController.SoDController;
import com.l2jserver.scripts.vehicles.SoIController.SoIController;
import com.l2jserver.scripts.village_master.Alliance.Alliance;
import com.l2jserver.scripts.village_master.Clan.Clan;
import com.l2jserver.scripts.village_master.DarkElvenChange1.DarkElvenChange1;
import com.l2jserver.scripts.village_master.DarkElvenChange2.DarkElvenChange2;
import com.l2jserver.scripts.village_master.DwarvenOccupationChange.DwarvenOccupationChange;
import com.l2jserver.scripts.village_master.ElvenHumanBuffers2.ElvenHumanBuffers2;
import com.l2jserver.scripts.village_master.ElvenHumanFighters1.ElvenHumanFighters1;
import com.l2jserver.scripts.village_master.ElvenHumanFighters2.ElvenHumanFighters2;
import com.l2jserver.scripts.village_master.ElvenHumanMystics1.ElvenHumanMystics1;
import com.l2jserver.scripts.village_master.ElvenHumanMystics2.ElvenHumanMystics2;
import com.l2jserver.scripts.village_master.FirstClassTransferTalk.FirstClassTransferTalk;
import com.l2jserver.scripts.village_master.KamaelChange1.KamaelChange1;
import com.l2jserver.scripts.village_master.KamaelChange2.KamaelChange2;
import com.l2jserver.scripts.village_master.OrcOccupationChange1.OrcOccupationChange1;
import com.l2jserver.scripts.village_master.OrcOccupationChange2.OrcOccupationChange2;
import com.l2jserver.scripts.village_master.SubclassCertification.SubclassCertification;

/**
 * @author L2jPDT
 */
public class ScriptsLoader
{
	protected final static Logger log = Logger.getLogger(ScriptsLoader.class.getName());
	protected static int successCount;
	
	static Class<?>[] CUSTOM =
	{
		EchoCrystals.class,
		HeroCirclet.class,
		HeroWeapon.class,
		MissQueen.class,
		NewbieCoupons.class,
		NpcLocationInfo.class,
		PinsAndPouchUnseal.class,
		PurchaseBracelet.class,
		RaidbossInfo.class,
		ShadowWeapons.class,
		SkillTransferValidator.class,
		SubClassSkills.class,
		HideAndSeek.class,
		// Unique NPC created for L2Winter. Contact us for test it or buy it.
		// Multiclass.class,
	};
	
	static Class<?>[] FREYA =
	{
		FreyasSteward.class,
		IceQueensCastle.class,
		IceQueensCastleNormalBattle.class,
		IceQueensCastleUltimateBattle.class,
		Jinia.class,
		JiniaGuildHideout1.class,
		JiniaGuildHideout2.class,
		JiniaGuildHideout3.class,
		JiniaGuildHideout4.class,
		MithrilMine.class,
		Rafforty.class,
		Sirra.class,
	};
	
	static Class<?>[] GRACIA =
	{
		EnergySeeds.class,
		Enira.class,
		Lindvior.class,
		Maguen.class,
		StarStones.class,
		AbyssGaze.class,
		DestroyedTumors.class,
		EkimusMouth.class,
		FortuneTelling.class,
		GeneralDilios.class,
		Lekon.class,
		Nemo.class,
		Nottingale.class,
		SecretArea.class,
		SeedOfAnnihilation.class,
		Stage1.class,
		HallOfSufferingAttack.class,
		HallOfSufferingDefence.class,
		HallOfErosionAttack.class,
		HallOfErosionDefence.class,
		HeartInfinityAttack.class,
		HeartInfinityDefence.class,
		Seyo.class,
	};
	
	static Class<?>[] HELLBOUND =
	{
		Amaskari.class,
		Beleth.class,
		DemonPrinceRaid.class,
		Engine.class,
		Epidos.class,
		HellboundCore.class,
		Hellenark.class,
		Chimeras.class,
		Keltas.class,
		MasterZelos.class,
		NaiaLock.class,
		OutpostCaptain.class,
		RandomSpawn.class,
		RankuRaid.class,
		Remnants.class,
		Sandstorms.class,
		SinWardens.class,
		Slaves.class,
		AnomicFoundry.class,
		BaseTower.class,
		Bernarde.class,
		Budenka.class,
		Buron.class,
		DemonPrinceFloor.class,
		Falk.class,
		HellboundTown.class,
		Hude.class,
		Jude.class,
		Kanaf.class,
		Kief.class,
		Natives.class,
		Quarry.class,
		RankuFloor.class,
		Shadai.class,
		Solomon.class,
		TowerOfInfinitum.class,
		TowerOfNaia.class,
		TullyWorkshop.class,
		Warpgate.class,
	};
	
	static Class<?>[] CHAMBER_OF_DELUSION =
	{
		Aenkinel.class,
		DelusionTeleport.class,
		East.class,
		North.class,
		South.class,
		West.class,
		Tower.class,
	};
	
	static Class<?>[] IMPERIAL_TOMB =
	{
		FinalEmperialTomb.class,
	};
	
	static Class<?>[] ISLE_OF_PRAYER =
	{
		DarkWaterDragon.class,
		EvasGiftBoxes.class,
		PrisonGuards.class,
		CrystalCaverns.class,
		DarkCloudMansion.class,
		IOPRace.class,
	};
	
	static Class<?>[] ISLE_OF_SOULS =
	{
		NornilsGarden.class,
	};
	
	static Class<?>[] L2JPDT_NPCS =
	{
		AutomaticTeleporterNPC.class,
		GmShopNPC.class,
		LevelChooseNpc.class,
		AchievementEvent.class,
		Multiclass.class,
		PremiumServiceNPC.class,
		// RevolutionToINpc.class, Temp. disabled
		NpcBuffer.class,
		// StatsEnchanterNPC.class,
	};
	
	static Class<?>[] NONS =
	{
		NoAttacking.class,
		NoChampion.class,
		NoLethalable.class,
		NoMoving.class,
		NoRandomAnimation.class,
		NoRandomWalk.class,
		NoTalking.class,
	};
	
	static Class<?>[] KAMALOKA =
	{
		BladeOtis.class,
		CrimsonHatuOtis.class,
		FollowerOfAllosce.class,
		FollowerOfMontagnar.class,
		KaimAbigore.class,
		Kamaloka.class,
		KelBilette.class,
		OlAriosh.class,
		PowderKeg.class,
		SeerFlouros.class,
		VenomousStorace.class,
		WeirdBunei.class,
		WhiteAllosce.class,
	};
	
	static Class<?>[] PLAINS_OF_THE_LIZARDMEN =
	{
		SeerUgoros.class,
	};
	
	static Class<?>[] PRIMEVAL_ISLE =
	{
		Sailren.class,
		TRex.class,
	};
	
	static Class<?>[] SSQ =
	{
		HideoutOfTheDawn.class,
		SanctumOftheLordsOfDawn.class,
	};
	
	static Class<?>[] TELEPORTS =
	{
		CrumaTower.class,
		ElrokiTeleporters.class,
		GatekeeperSpirit.class,
		HuntingGroundsTeleport.class,
		Klein.class,
		MithrilMines.class,
		NewbieTravelToken.class,
		NoblesseTeleport.class,
		OracleTeleport.class,
		PaganTeleporters.class,
		StakatoNest.class,
		SteelCitadelTeleport.class,
		StrongholdsTeleports.class,
		Survivor.class,
		TeleportToFantasy.class,
		TeleportToRaceTrack.class,
		TeleportToUndergroundColiseum.class,
		TeleportWithCharm.class,
		ToIVortex.class,
	};
	
	static Class<?>[] QUESTS =
	{
		Q00001_LettersOfLove.class,
		Q00002_WhatWomenWant.class,
		Q00003_WillTheSealBeBroken.class,
		Q00004_LongLiveThePaagrioLord.class,
		Q00005_MinersFavor.class,
		Q00006_StepIntoTheFuture.class,
		Q00007_ATripBegins.class,
		Q00008_AnAdventureBegins.class,
		Q00009_IntoTheCityOfHumans.class,
		Q00010_IntoTheWorld.class,
		Q00011_SecretMeetingWithKetraOrcs.class,
		Q00012_SecretMeetingWithVarkaSilenos.class,
		Q00013_ParcelDelivery.class,
		Q00014_WhereaboutsOfTheArchaeologist.class,
		Q00015_SweetWhispers.class,
		Q00016_TheComingDarkness.class,
		Q00017_LightAndDarkness.class,
		Q00018_MeetingWithTheGoldenRam.class,
		Q00019_GoToThePastureland.class,
		Q00020_BringUpWithLove.class,
		Q00021_HiddenTruth.class,
		Q00022_TragedyInVonHellmannForest.class,
		Q00023_LidiasHeart.class,
		Q00024_InhabitantsOfTheForestOfTheDead.class,
		Q00025_HidingBehindTheTruth.class,
		Q00027_ChestCaughtWithABaitOfWind.class,
		Q00028_ChestCaughtWithABaitOfIcyAir.class,
		Q00029_ChestCaughtWithABaitOfEarth.class,
		Q00030_ChestCaughtWithABaitOfFire.class,
		Q00031_SecretBuriedInTheSwamp.class,
		Q00032_AnObviousLie.class,
		Q00033_MakeAPairOfDressShoes.class,
		Q00034_InSearchOfCloth.class,
		Q00035_FindGlitteringJewelry.class,
		Q00036_MakeASewingKit.class,
		Q00037_MakeFormalWear.class,
		Q00038_DragonFangs.class,
		Q00039_RedEyedInvaders.class,
		Q00040_ASpecialOrder.class,
		Q00042_HelpTheUncle.class,
		Q00043_HelpTheSister.class,
		Q00044_HelpTheSon.class,
		Q00045_ToTalkingIsland.class,
		Q00046_OnceMoreInTheArmsOfTheMotherTree.class,
		Q00047_IntoTheDarkElvenForest.class,
		Q00048_ToTheImmortalPlateau.class,
		Q00049_TheRoadHome.class,
		Q00050_LanoscosSpecialBait.class,
		Q00051_OFullesSpecialBait.class,
		Q00052_WilliesSpecialBait.class,
		Q00053_LinnaeusSpecialBait.class,
		Q00060_GoodWorksReward.class,
		Q00061_LawEnforcement.class,
		Q00062_PathOfTheTrooper.class,
		Q00063_PathOfTheWarder.class,
		Q00064_CertifiedBerserker.class,
		Q00065_CertifiedSoulBreaker.class,
		Q00066_CertifiedArbalester.class,
		// load by another script0067_SagaOfTheDoombringer.class,
		// load by another script0068_SagaOfTheSoulHound.class,
		// load by another script0069_SagaOfTheTrickster.class,
		// load by another script0070_SagaOfThePhoenixKnight.class,
		// load by another script0071_SagaOfEvasTemplar.class,
		// load by another script0072_SagaOfTheSwordMuse.class,
		// load by another script0073_SagaOfTheDuelist.class,
		// load by another script0074_SagaOfTheDreadnoughts.class,
		// load by another script0075_SagaOfTheTitan.class,
		// load by another script0076_SagaOfTheGrandKhavatari.class,
		// load by another script0077_SagaOfTheDominator.class,
		// load by another script0078_SagaOfTheDoomcryer.class,
		// load by another script0079_SagaOfTheAdventurer.class,
		// load by another script0080_SagaOfTheWindRider.class,
		// load by another script0081_SagaOfTheGhostHunter.class,
		// load by another script0082_SagaOfTheSagittarius.class,
		// load by another script0083_SagaOfTheMoonlightSentinel.class,
		// load by another script0084_SagaOfTheGhostSentinel.class,
		// load by another script0085_SagaOfTheCardinal.class,
		// load by another script0086_SagaOfTheHierophant.class,
		// load by another script0087_SagaOfEvasSaint.class,
		// load by another script0088_SagaOfTheArchmage.class,
		// load by another script0089_SagaOfTheMysticMuse.class,
		// load by another script0090_SagaOfTheStormScreamer.class,
		// load by another script0091_SagaOfTheArcanaLord.class,
		// load by another script0092_SagaOfTheElementalMaster.class,
		// load by another script0093_SagaOfTheSpectralMaster.class,
		// load by another script0094_SagaOfTheSoultaker.class,
		// load by another script0095_SagaOfTheHellKnight.class,
		// load by another script0096_SagaOfTheSpectralDancer.class,
		// load by another script0097_SagaOfTheShillienTemplar.class,
		// load by another script0098_SagaOfTheShillienSaint.class,
		// load by another script0099_SagaOfTheFortuneSeeker.class,
		// load by another script0100_SagaOfTheMaestro.class,
		Q00101_SwordOfSolidarity.class,
		Q00102_SeaOfSporesFever.class,
		Q00103_SpiritOfCraftsman.class,
		Q00104_SpiritOfMirrors.class,
		Q00105_SkirmishWithOrcs.class,
		Q00106_ForgottenTruth.class,
		Q00107_MercilessPunishment.class,
		Q00108_JumbleTumbleDiamondFuss.class,
		Q00109_InSearchOfTheNest.class,
		Q00110_ToThePrimevalIsle.class,
		Q00111_ElrokianHuntersProof.class,
		Q00112_WalkOfFate.class,
		Q00113_StatusOfTheBeaconTower.class,
		Q00114_ResurrectionOfAnOldManager.class,
		Q00115_TheOtherSideOfTruth.class,
		Q00116_BeyondTheHillsOfWinter.class,
		Q00117_TheOceanOfDistantStars.class,
		Q00118_ToLeadAndBeLed.class,
		Q00119_LastImperialPrince.class,
		Q00120_PavelsLastResearch.class,
		Q00121_PavelTheGiant.class,
		Q00122_OminousNews.class,
		Q00123_TheLeaderAndTheFollower.class,
		Q00124_MeetingTheElroki.class,
		Q00125_TheNameOfEvil1.class,
		Q00126_TheNameOfEvil2.class,
		Q00128_PailakaSongOfIceAndFire.class,
		Q00129_PailakaDevilsLegacy.class,
		Q00130_PathToHellbound.class,
		Q00131_BirdInACage.class,
		Q00132_MatrasCuriosity.class,
		Q00133_ThatsBloodyHot.class,
		Q00134_TempleMissionary.class,
		Q00135_TempleExecutor.class,
		Q00136_MoreThanMeetsTheEye.class,
		Q00137_TempleChampionPart1.class,
		Q00138_TempleChampionPart2.class,
		Q00139_ShadowFoxPart1.class,
		Q00140_ShadowFoxPart2.class,
		Q00141_ShadowFoxPart3.class,
		Q00142_FallenAngelRequestOfDawn.class,
		Q00143_FallenAngelRequestOfDusk.class,
		Q00144_PailakaInjuredDragon.class,
		Q00146_TheZeroHour.class,
		Q00147_PathtoBecominganEliteMercenary.class,
		Q00148_PathtoBecominganExaltedMercenary.class,
		Q00151_CureForFever.class,
		Q00152_ShardsOfGolem.class,
		Q00153_DeliverGoods.class,
		Q00154_SacrificeToTheSea.class,
		Q00155_FindSirWindawood.class,
		Q00156_MillenniumLove.class,
		Q00157_RecoverSmuggledGoods.class,
		Q00158_SeedOfEvil.class,
		Q00159_ProtectTheWaterSource.class,
		Q00160_NerupasRequest.class,
		Q00161_FruitOfTheMotherTree.class,
		Q00162_CurseOfTheUndergroundFortress.class,
		Q00163_LegacyOfThePoet.class,
		Q00164_BloodFiend.class,
		Q00165_ShilensHunt.class,
		Q00166_MassOfDarkness.class,
		Q00167_DwarvenKinship.class,
		Q00168_DeliverSupplies.class,
		Q00169_OffspringOfNightmares.class,
		Q00170_DangerousSeduction.class,
		Q00171_ActsOfEvil.class,
		Q00172_NewHorizons.class,
		Q00173_ToTheIsleOfSouls.class,
		Q00174_SupplyCheck.class,
		Q00175_TheWayOfTheWarrior.class,
		Q00176_StepsForHonor.class,
		Q00178_IconicTrinity.class,
		Q00179_IntoTheLargeCavern.class,
		Q00182_NewRecruits.class,
		Q00183_RelicExploration.class,
		Q00184_ArtOfPersuasion.class,
		Q00185_NikolasCooperation.class,
		Q00186_ContractExecution.class,
		Q00187_NikolasHeart.class,
		Q00188_SealRemoval.class,
		Q00189_ContractCompletion.class,
		Q00190_LostDream.class,
		Q00191_VainConclusion.class,
		Q00192_SevenSignsSeriesOfDoubt.class,
		Q00193_SevenSignsDyingMessage.class,
		Q00194_SevenSignsMammonsContract.class,
		Q00195_SevenSignsSecretRitualOfThePriests.class,
		Q00196_SevenSignSealOfTheEmperor.class,
		Q00197_SevenSignsTheSacredBookOfSeal.class,
		Q00198_SevenSignsEmbryo.class,
		Q00211_TrialOfTheChallenger.class,
		Q00212_TrialOfDuty.class,
		Q00213_TrialOfTheSeeker.class,
		Q00214_TrialOfTheScholar.class,
		Q00215_TrialOfThePilgrim.class,
		Q00216_TrialOfTheGuildsman.class,
		Q00217_TestimonyOfTrust.class,
		Q00218_TestimonyOfLife.class,
		Q00219_TestimonyOfFate.class,
		Q00220_TestimonyOfGlory.class,
		Q00221_TestimonyOfProsperity.class,
		Q00222_TestOfTheDuelist.class,
		Q00223_TestOfTheChampion.class,
		Q00224_TestOfSagittarius.class,
		Q00225_TestOfTheSearcher.class,
		Q00226_TestOfTheHealer.class,
		Q00227_TestOfTheReformer.class,
		Q00228_TestOfMagus.class,
		Q00229_TestOfWitchcraft.class,
		Q00230_TestOfTheSummoner.class,
		Q00231_TestOfTheMaestro.class,
		Q00232_TestOfTheLord.class,
		Q00233_TestOfTheWarSpirit.class,
		Q00234_FatesWhisper.class,
		Q00235_MimirsElixir.class,
		Q00236_SeedsOfChaos.class,
		Q00237_WindsOfChange.class,
		Q00238_SuccessFailureOfBusiness.class,
		Q00239_WontYouJoinUs.class,
		Q00240_ImTheOnlyOneYouCanTrust.class,
		Q00241_PossessorOfAPreciousSoul1.class,
		Q00242_PossessorOfAPreciousSoul2.class,
		Q00246_PossessorOfAPreciousSoul3.class,
		Q00247_PossessorOfAPreciousSoul4.class,
		Q00249_PoisonedPlainsOfTheLizardmen.class,
		Q00250_WatchWhatYouEat.class,
		Q00251_NoSecrets.class,
		Q00252_ItSmellsDelicious.class,
		Q00255_Tutorial.class,
		Q00257_TheGuardIsBusy.class,
		Q00258_BringWolfPelts.class,
		Q00259_RequestFromTheFarmOwner.class,
		Q00260_OrcHunting.class,
		Q00261_CollectorsDream.class,
		Q00262_TradeWithTheIvoryTower.class,
		Q00263_OrcSubjugation.class,
		Q00264_KeenClaws.class,
		Q00265_BondsOfSlavery.class,
		Q00266_PleasOfPixies.class,
		Q00267_WrathOfVerdure.class,
		Q00268_TracesOfEvil.class,
		Q00269_InventionAmbition.class,
		Q00270_TheOneWhoEndsSilence.class,
		Q00271_ProofOfValor.class,
		Q00272_WrathOfAncestors.class,
		Q00273_InvadersOfTheHolyLand.class,
		Q00274_SkirmishWithTheWerewolves.class,
		Q00275_DarkWingedSpies.class,
		Q00276_TotemOfTheHestui.class,
		Q00277_GatekeepersOffering.class,
		Q00278_HomeSecurity.class,
		Q00279_TargetOfOpportunity.class,
		Q00280_TheFoodChain.class,
		Q00281_HeadForTheHills.class,
		Q00283_TheFewTheProudTheBrave.class,
		Q00284_MuertosFeather.class,
		Q00286_FabulousFeathers.class,
		Q00287_FiguringItOut.class,
		Q00288_HandleWithCare.class,
		Q00289_NoMoreSoupForYou.class,
		Q00290_ThreatRemoval.class,
		Q00291_RevengeOfTheRedbonnet.class,
		Q00292_BrigandsSweep.class,
		Q00293_TheHiddenVeins.class,
		Q00294_CovertBusiness.class,
		Q00295_DreamingOfTheSkies.class,
		Q00296_TarantulasSpiderSilk.class,
		Q00297_GatekeepersFavor.class,
		Q00298_LizardmensConspiracy.class,
		Q00299_GatherIngredientsForPie.class,
		Q00300_HuntingLetoLizardman.class,
		Q00303_CollectArrowheads.class,
		Q00306_CrystalOfFireAndIce.class,
		Q00307_ControlDeviceOfTheGiants.class,
		Q00308_ReedFieldMaintenance.class,
		Q00309_ForAGoodCause.class,
		Q00310_OnlyWhatRemains.class,
		Q00311_ExpulsionOfEvilSpirits.class,
		Q00312_TakeAdvantageOfTheCrisis.class,
		Q00313_CollectSpores.class,
		Q00316_DestroyPlagueCarriers.class,
		Q00317_CatchTheWind.class,
		Q00319_ScentOfDeath.class,
		Q00320_BonesTellTheFuture.class,
		Q00324_SweetestVenom.class,
		Q00325_GrimCollector.class,
		Q00326_VanquishRemnants.class,
		Q00327_RecoverTheFarmland.class,
		Q00328_SenseForBusiness.class,
		Q00329_CuriosityOfADwarf.class,
		Q00330_AdeptOfTaste.class,
		Q00331_ArrowOfVengeance.class,
		Q00333_HuntOfTheBlackLion.class,
		Q00334_TheWishingPotion.class,
		Q00335_TheSongOfTheHunter.class,
		Q00336_CoinsOfMagic.class,
		Q00337_AudienceWithTheLandDragon.class,
		Q00338_AlligatorHunter.class,
		Q00340_SubjugationOfLizardmen.class,
		Q00341_HuntingForWildBeasts.class,
		Q00343_UnderTheShadowOfTheIvoryTower.class,
		Q00344_1000YearsTheEndOfLamentation.class,
		Q00345_MethodToRaiseTheDead.class,
		Q00347_GoGetTheCalculator.class,
		Q00348_ArrogantSearch.class,
		Q00350_EnhanceYourWeapon.class,
		Q00351_BlackSwan.class,
		Q00352_HelpRoodRaiseANewPet.class,
		Q00354_ConquestOfAlligatorIsland.class,
		Q00355_FamilyHonor.class,
		Q00356_DigUpTheSeaOfSpores.class,
		Q00357_WarehouseKeepersAmbition.class,
		Q00358_IllegitimateChildOfTheGoddess.class,
		Q00359_ForASleeplessDeadman.class,
		Q00360_PlunderTheirSupplies.class,
		Q00362_BardsMandolin.class,
		Q00363_SorrowfulSoundOfFlute.class,
		Q00364_JovialAccordion.class,
		Q00365_DevilsLegacy.class,
		Q00366_SilverHairedShaman.class,
		Q00367_ElectrifyingRecharge.class,
		Q00368_TrespassingIntoTheHolyGround.class,
		Q00369_CollectorOfJewels.class,
		Q00370_AnElderSowsSeeds.class,
		Q00371_ShrieksOfGhosts.class,
		Q00372_LegacyOfInsolence.class,
		Q00373_SupplierOfReagents.class,
		Q00376_ExplorationOfTheGiantsCavePart1.class,
		Q00377_ExplorationOfTheGiantsCavePart2.class,
		Q00378_GrandFeast.class,
		Q00379_FantasyWine.class,
		Q00380_BringOutTheFlavorOfIngredients.class,
		Q00381_LetsBecomeARoyalMember.class,
		Q00382_KailsMagicCoin.class,
		Q00383_TreasureHunt.class,
		Q00384_WarehouseKeepersPastime.class,
		Q00385_YokeOfThePast.class,
		Q00386_StolenDignity.class,
		Q00401_PathToWarrior.class,
		Q00402_PathOfTheHumanKnight.class,
		Q00403_PathOfTheRogue.class,
		Q00404_PathOfTheHumanWizard.class,
		Q00405_PathOfTheCleric.class,
		Q00406_PathOfTheElvenKnight.class,
		Q00407_PathOfTheElvenScout.class,
		Q00408_PathOfTheElvenWizard.class,
		Q00409_PathOfTheElvenOracle.class,
		Q00410_PathOfThePalusKnight.class,
		Q00411_PathOfTheAssassin.class,
		Q00412_PathOfTheDarkWizard.class,
		Q00413_PathOfTheShillienOracle.class,
		Q00414_PathOfTheOrcRaider.class,
		Q00415_PathOfTheOrcMonk.class,
		Q00416_PathOfTheOrcShaman.class,
		Q00417_PathOfTheScavenger.class,
		Q00418_PathOfTheArtisan.class,
		Q00419_GetAPet.class,
		Q00420_LittleWing.class,
		Q00421_LittleWingsBigAdventure.class,
		Q00422_RepentYourSins.class,
		Q00423_TakeYourBestShot.class,
		Q00426_QuestForFishingShot.class,
		Q00431_WeddingMarch.class,
		Q00432_BirthdayPartySong.class,
		Q00450_GraveRobberRescue.class,
		Q00451_LuciensAltar.class,
		Q00452_FindingtheLostSoldiers.class,
		Q00453_NotStrongEnoughAlone.class,
		Q00457_LostAndFound.class,
		Q00458_PerfectForm.class,
		Q00461_RumbleInTheBase.class,
		Q00463_IMustBeaGenius.class,
		Q00464_Oath.class,
		Q00501_ProofOfClanAlliance.class,
		Q00503_PursuitClanAmbition.class,
		Q00508_AClansReputation.class,
		Q00509_AClansFame.class,
		Q00510_AClansPrestige.class,
		Q00511_AwlUnderFoot.class,
		Q00601_WatchingEyes.class,
		Q00602_ShadowOfLight.class,
		Q00603_DaimonTheWhiteEyedPart1.class,
		Q00604_DaimonTheWhiteEyedPart2.class,
		Q00605_AllianceWithKetraOrcs.class,
		Q00606_BattleAgainstVarkaSilenos.class,
		Q00607_ProveYourCourageKetra.class,
		Q00608_SlayTheEnemyCommanderKetra.class,
		Q00609_MagicalPowerOfWaterPart1.class,
		Q00610_MagicalPowerOfWaterPart2.class,
		Q00611_AllianceWithVarkaSilenos.class,
		Q00612_BattleAgainstKetraOrcs.class,
		Q00613_ProveYourCourageVarka.class,
		Q00614_SlayTheEnemyCommanderVarka.class,
		Q00615_MagicalPowerOfFirePart1.class,
		Q00616_MagicalPowerOfFirePart2.class,
		Q00617_GatherTheFlames.class,
		Q00618_IntoTheFlame.class,
		Q00619_RelicsOfTheOldEmpire.class,
		Q00620_FourGoblets.class,
		Q00621_EggDelivery.class,
		Q00622_SpecialtyLiquorDelivery.class,
		Q00623_TheFinestFood.class,
		Q00624_TheFinestIngredientsPart1.class,
		Q00625_TheFinestIngredientsPart2.class,
		Q00626_ADarkTwilight.class,
		Q00627_HeartInSearchOfPower.class,
		Q00628_HuntGoldenRam.class,
		Q00629_CleanUpTheSwampOfScreams.class,
		Q00631_DeliciousTopChoiceMeat.class,
		Q00632_NecromancersRequest.class,
		Q00633_InTheForgottenVillage.class,
		Q00634_InSearchOfFragmentsOfDimension.class,
		Q00635_IntoTheDimensionalRift.class,
		Q00636_TruthBeyond.class,
		Q00637_ThroughOnceMore.class,
		Q00638_SeekersOfTheHolyGrail.class,
		Q00641_AttackSailren.class,
		Q00642_APowerfulPrimevalCreature.class,
		Q00643_RiseAndFallOfTheElrokiTribe.class,
		Q00644_GraveRobberAnnihilation.class,
		Q00645_GhostsOfBatur.class,
		Q00646_SignsOfRevolt.class,
		Q00647_InfluxOfMachines.class,
		Q00648_AnIceMerchantsDream.class,
		Q00649_ALooterAndARailroadMan.class,
		Q00650_ABrokenDream.class,
		Q00651_RunawayYouth.class,
		Q00652_AnAgedExAdventurer.class,
		Q00653_WildMaiden.class,
		Q00654_JourneyToASettlement.class,
		Q00660_AidingTheFloranVillage.class,
		Q00661_MakingTheHarvestGroundsSafe.class,
		Q00662_AGameOfCards.class,
		Q00663_SeductiveWhispers.class,
		Q00688_DefeatTheElrokianRaiders.class,
		Q00690_JudesRequest.class,
		Q00691_MatrasSuspiciousRequest.class,
		Q00692_HowtoOpposeEvil.class,
		Q00693_DefeatingDragonkinRemnants.class,
		Q00694_BreakThroughTheHallOfSuffering.class,
		Q00695_DefendtheHallofSuffering.class,
		Q00696_ConquertheHallofErosion.class,
		Q00697_DefendtheHallofErosion.class,
		Q00698_BlocktheLordsEscape.class,
		Q00699_GuardianOfTheSkies.class,
		Q00700_CursedLife.class,
		Q00701_ProofOfExistence.class,
		Q00998_FallenAngelSelect.class,
		Q00999_T1Tutorial.class,
		Q10267_JourneyToGracia.class,
		Q10268_ToTheSeedOfInfinity.class,
		Q10269_ToTheSeedOfDestruction.class,
		Q10270_BirthOfTheSeed.class,
		Q10271_TheEnvelopingDarkness.class,
		Q10272_LightFragment.class,
		Q10273_GoodDayToFly.class,
		Q10274_CollectingInTheAir.class,
		Q10275_ContainingTheAttributePower.class,
		Q10276_MutatedKaneusGludio.class,
		Q10277_MutatedKaneusDion.class,
		Q10278_MutatedKaneusHeine.class,
		Q10279_MutatedKaneusOren.class,
		Q10280_MutatedKaneusSchuttgart.class,
		Q10281_MutatedKaneusRune.class,
		Q10282_ToTheSeedOfAnnihilation.class,
		Q10283_RequestOfIceMerchant.class,
		Q10284_AcquisitionOfDivineSword.class,
		Q10285_MeetingSirra.class,
		Q10286_ReunionWithSirra.class,
		Q10287_StoryOfThoseLeft.class,
		Q10288_SecretMission.class,
		Q10289_FadeToBlack.class,
		Q10290_LandDragonConqueror.class,
		Q10291_FireDragonDestroyer.class,
	};
	
	static Class<?>[] VEHICLES =
	{
		BoatTalkingGludin.class,
		BoatGiranTalking.class,
		BoatInnadrilTour.class,
		BoatGludinRune.class,
		BoatRunePrimeval.class,
		AirShipGludioGracia.class,
		KeucereusNorthController.class,
		KeucereusSouthController.class,
		SoIController.class,
		SoDController.class,
	};
	
	static Class<?>[] VILLAGEMASTERS =
	{
		Alliance.class,
		Clan.class,
		DarkElvenChange1.class,
		DarkElvenChange2.class,
		DwarvenOccupationChange.class,
		ElvenHumanBuffers2.class,
		ElvenHumanFighters1.class,
		ElvenHumanFighters2.class,
		ElvenHumanMystics1.class,
		ElvenHumanMystics2.class,
		FirstClassTransferTalk.class,
		KamaelChange1.class,
		KamaelChange2.class,
		OrcOccupationChange1.class,
		OrcOccupationChange2.class,
		SubclassCertification.class,
	};
	
	static Class<?>[] AI =
	{
		L2AttackableAIScript.class,
		// Need to add in signle AI
		FlyingNpcs.class,
		//
		Antharas.class,
		Baium.class,
		Barakiel.class,
		BeastFarm.class,
		Core.class,
		CryptsOfDisgrace.class,
		DenOfEvil.class,
		DrChaos.class,
		FairyTrees.class,
		FieldOfWhispersSilence.class,
		FleeNpc.class,
		FrozenLabyrinth.class,
		GargosPailaka.class,
		GiantScouts.class,
		Golkonda.class,
		GordonRaid.class,
		GraveRobbers.class,
		Hallate.class,
		HolyBrazier.class,
		HotSpringsBuffs.class,
		Chests.class,
		KarulBugbear.class,
		Kernon.class,
		MithrilMinesLocation.class,
		Monastery.class,
		Orfen.class,
		PavelArchaic.class,
		PlainsOfDion.class,
		PlainsOfLizardman.class, // move to correct folder
		PolymorphingAngel.class,
		PolymorphingOnAttack.class,
		QueenAnt.class,
		QueenShyeed.class,
		RagnaOrcFrightened.class,
		RagnaOrcs.class,
		SearchingMaster.class,
		SeeThroughSilentMove.class,
		SelMahumDrill.class,
		SelMahumSquad.class,
		SilentValley.class,
		StakatoNestMonsters.class,
		SummonMinions.class,
		HandysBlockCheckerEvent.class,
		MC_Show.class,
		PriestOfBlessing.class,
		PcBangPoint.class,
		VanHalter.class,
		Valakas.class,
		PirateZaken.class,
		WarriorFishingBlock.class,
	};
	
	public static void loadAllScripts()
	{
		loadAllClass("Artificial Intelligence", AI);
		loadAllClass("Customs", CUSTOM);
		loadAllClass("L2jPDT NPCs", L2JPDT_NPCS);
		loadAllClass("Nons", NONS);
		loadAllClass("Freya", FREYA);
		loadAllClass("Gracia", GRACIA);
		loadAllClass("Hellbound", HELLBOUND);
		loadAllClass("Chamber of Delusion", CHAMBER_OF_DELUSION);
		loadAllClass("Imperial Tomb", IMPERIAL_TOMB);
		loadAllClass("Isle of Prayer", ISLE_OF_PRAYER);
		loadAllClass("Isle of Souls", ISLE_OF_SOULS);
		loadAllClass("Kamaloka", KAMALOKA);
		loadAllClass("Lizardmen Plains", PLAINS_OF_THE_LIZARDMEN);
		loadAllClass("Primeval Isle", PRIMEVAL_ISLE);
		loadAllClass("SSQ", SSQ);
		loadAllClass("Teleports", TELEPORTS);
		loadAllClass("Quest", QUESTS);
		loadAllClass("Vehicles", VEHICLES);
		loadAllClass("Village Master", VILLAGEMASTERS);
	}
	
	private static void loadAllClass(String type, Class<?>... files)
	{
		Constructor<?> constructor;
		successCount = 0;
		for (Class<?> file : files)
		{
			if (file == null)
			{
				continue;
			}
			
			try
			{
				constructor = file.getDeclaredConstructor();
				constructor.setAccessible(true);
				constructor.newInstance();
				successCount++;
			}
			catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
	}
}
