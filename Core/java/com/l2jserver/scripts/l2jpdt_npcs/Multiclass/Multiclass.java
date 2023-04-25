/*
 * Copyright (C) 2004-2018 L2J Server
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
package com.l2jserver.scripts.l2jpdt_npcs.Multiclass;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.Race;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.variables.PlayerVariables;
import com.l2jserver.gameserver.network.serverpackets.Earthquake;
import com.l2jserver.gameserver.network.serverpackets.ExRedSky;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;

/**
 * @author L2jPDT
 * @TODO: Create one variables where CLASS_COUNT ++ from 0 to configurable number
 */
public class Multiclass extends Quest
{
	private static final int NPC_ID = Config.MULTICLASS_NPC_ID;
	private static final int MIN_LEVEL = Config.MULTICLASS_MIN_LEVEL_TO_TRANSFER;
	private static final boolean DISABLED_KAMAELS_OTHERS_RACES = Config.DISABLED_KAMAELS_OTHERS_RACES;
	
	// Variables
	public static final String CLASS_COUNT_1 = Quest.CLASS_COUNT_1;
	public static final String CLASS_COUNT_2 = Quest.CLASS_COUNT_2;
	public static final String CLASS_COUNT_3 = Quest.CLASS_COUNT_3;
	
	public Multiclass()
	{
		super(-1, Multiclass.class.getSimpleName(), "l2jpdt_npcs");
		StartNpcs(NPC_ID);
		TalkNpcs(NPC_ID);
		FirstTalkNpcs(NPC_ID);
		if (Config.USE_MULTICLASS_NPC)
		{
			_log.info("L2jPDT: MultiClass NPC is enabled, NPC loaded.");
		}
		else
		{
			_log.info("L2jPDT: MultiClass NPC is disabled, NPC will not work.");
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final PlayerVariables vars = player.getVariables();
		String htmltext = "";
		switch (event)
		{
			case "html13":
				if (vars.getInt(CLASS_COUNT_3, 0) == 1)
				{
					maxiumClasses(player);
					htmltext = "NoMoreClasses.htm";
				}
				else
				{
					htmltext = "13.htm";
				}
				break;
			case "html15":
				htmltext = "15.htm";
				break;
			case "back":
				htmltext = "13.htm";
				break;
			case "human":
				if (player.getRace() != Race.Kamael)
				{
					htmltext = "human.htm";
				}
				else
				{
					if (DISABLED_KAMAELS_OTHERS_RACES)
					{
						noKamael(player);
					}
					else
					{
						htmltext = "human.htm";
					}
				}
				break;
			case "elf":
				if (player.getRace() != Race.Kamael)
				{
					htmltext = "elf.htm";
				}
				else
				{
					if (DISABLED_KAMAELS_OTHERS_RACES)
					{
						noKamael(player);
					}
					else
					{
						htmltext = "elf.htm";
					}
				}
				break;
			case "darkelf":
				if (player.getRace() != Race.Kamael)
				{
					htmltext = "darkelf.htm";
				}
				else
				{
					if (DISABLED_KAMAELS_OTHERS_RACES)
					{
						noKamael(player);
					}
					else
					{
						htmltext = "darkelf.htm";
					}
				}
				break;
			case "orc":
				if (player.getRace() != Race.Kamael)
				{
					htmltext = "orc.htm";
				}
				else
				{
					if (DISABLED_KAMAELS_OTHERS_RACES)
					{
						noKamael(player);
					}
					else
					{
						htmltext = "orc.htm";
					}
				}
				break;
			case "dwarf":
				if (player.getRace() != Race.Kamael)
				{
					htmltext = "dwarf.htm";
				}
				else
				{
					if (DISABLED_KAMAELS_OTHERS_RACES)
					{
						noKamael(player);
					}
					else
					{
						htmltext = "dwarf.htm";
					}
				}
				break;
			case "kamael":
				htmltext = "kamael.htm";
				break;
			// humans
			case "Hawkeye":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Hawkeye", 0) == 0)
						{
							vars.set("Hawkeye", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(9);
							player.setBaseClass(9);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "TreasureHunter":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("TreasureHunter", 0) == 0)
						{
							vars.set("TreasureHunter", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(8);
							player.setBaseClass(8);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Gladiator":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Gladiator", 0) == 0)
						{
							vars.set("Gladiator", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(2);
							player.setBaseClass(2);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Warlord":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Warlord", 0) == 0)
						{
							vars.set("Warlord", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(3);
							player.setBaseClass(3);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Paladin":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Paladin", 0) == 0)
						{
							vars.set("Paladin", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(5);
							player.setBaseClass(5);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "DarkAvenger":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("DarkAvenger", 0) == 0)
						{
							vars.set("DarkAvenger", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(6);
							player.setBaseClass(6);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Prophet":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Prophet", 0) == 0)
						{
							vars.set("Prophet", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(17);
							player.setBaseClass(17);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Bishop":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Bishop", 0) == 0)
						{
							vars.set("Bishop", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(16);
							player.setBaseClass(16);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Warlock":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Warlock", 0) == 0)
						{
							vars.set("Warlock", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(14);
							player.setBaseClass(14);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Necromancer":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Necromancer", 0) == 0)
						{
							vars.set("Necromancer", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(13);
							player.setBaseClass(13);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Sorcerer":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Sorcerer", 0) == 0)
						{
							vars.set("Sorcerer", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(12);
							player.setBaseClass(12);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			// elfs
			case "SilverRanger":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("SilverRanger", 0) == 0)
						{
							vars.set("SilverRanger", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(24);
							player.setBaseClass(24);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "PlainsWalker":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("PlainsWalker", 0) == 0)
						{
							vars.set("PlainsWalker", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(23);
							player.setBaseClass(23);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "SwordSinger":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("SwordSinger", 0) == 0)
						{
							vars.set("SwordSinger", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(21);
							player.setBaseClass(21);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "TempleKnight":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("TempleKnight", 0) == 0)
						{
							vars.set("TempleKnight", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(20);
							player.setBaseClass(20);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "ElvenElder":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("ElvenElder", 0) == 0)
						{
							vars.set("ElvenElder", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(30);
							player.setBaseClass(30);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "SpellSinger":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("SpellSinger", 0) == 0)
						{
							vars.set("SpellSinger", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(27);
							player.setBaseClass(27);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "ElementalSummoner":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("ElementalSummoner", 0) == 0)
						{
							vars.set("ElementalSummoner", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(28);
							player.setBaseClass(28);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			// Dark Elfs
			case "PhantomRanger":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("PhantomRanger", 0) == 0)
						{
							vars.set("PhantomRanger", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(37);
							player.setBaseClass(37);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "AbyssWalker":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("AbyssWalker", 0) == 0)
						{
							vars.set("AbyssWalker", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(36);
							player.setBaseClass(36);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "BladeDancer":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("BladeDancer", 0) == 0)
						{
							vars.set("BladeDancer", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(34);
							player.setBaseClass(34);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "ShillienKnight":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("ShillienKnight", 0) == 0)
						{
							vars.set("ShillienKnight", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(33);
							player.setBaseClass(33);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "ShilienElder":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("ShilienElder", 0) == 0)
						{
							vars.set("ShilienElder", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(43);
							player.setBaseClass(43);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Spellhowler":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Spellhowler", 0) == 0)
						{
							vars.set("Spellhowler", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(40);
							player.setBaseClass(40);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "PhantomSummoner":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("PhantomSummoner", 0) == 0)
						{
							vars.set("PhantomSummoner", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(41);
							player.setBaseClass(41);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			// Dwarf
			case "BountyHunter":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("BountyHunter", 0) == 0)
						{
							vars.set("BountyHunter", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(55);
							player.setBaseClass(55);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			// Orcs
			case "Tyrant":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Tyrant", 0) == 0)
						{
							vars.set("Tyrant", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(48);
							player.setBaseClass(48);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Destroyer":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Destroyer", 0) == 0)
						{
							vars.set("Destroyer", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(46);
							player.setBaseClass(46);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "WarCryer":
				if (player.getRace() == Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("WarCryer", 0) == 0)
						{
							vars.set("WarCryer", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(52);
							player.setBaseClass(52);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			// Kamael
			case "Berserker":
				if (player.getRace() != Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Berserker", 0) == 0)
						{
							vars.set("Berserker", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(127);
							player.setBaseClass(127);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "MaleSoulBreaker":
				if (player.getRace() != Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("MaleSoulBreaker", 0) == 0)
						{
							vars.set("MaleSoulBreaker", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(128);
							player.setBaseClass(128);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Arbalester":
				if (player.getRace() != Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Arbalester", 0) == 0)
						{
							vars.set("Arbalester", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(130);
							player.setBaseClass(130);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "FemaleSoulBreaker":
				if (player.getRace() != Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("FemaleSoulBreaker", 0) == 0)
						{
							vars.set("FemaleSoulBreaker", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(129);
							player.setBaseClass(129);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			case "Judicator":
				if (player.getRace() != Race.Kamael)
				{
					noKamael(player);
				}
				else
				{
					if (vars.getInt(CLASS_COUNT_3, 0) == 1)
					{
						maxiumClasses(player);
						htmltext = "NoMoreClasses.htm";
					}
					else
					{
						if (vars.getInt("Judicator", 0) == 0)
						{
							vars.set("Judicator", 1);
							checkTheClassCount(player);
							changelevel(player);
							addReward(player);
							player.setClassId(135);
							player.setBaseClass(135);
							statusUpdate(player);
							htmltext = "9.htm";
						}
						else
						{
							classDone(player);
							htmltext = "13.htm";
						}
					}
				}
				break;
			
		}
		_log.info(Multiclass.class.getSimpleName() + ": Event: " + event);
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		if (Config.USE_MULTICLASS_NPC)
		{
			if (npc.getNpcId() == NPC_ID)
			{
				if (player.getLevel() < MIN_LEVEL)
				{
					player.sendMessage("You must reach the " + MIN_LEVEL + " level with currently class.");
					player.sendPacket(new ExShowScreenMessage("You must reach the " + MIN_LEVEL + " level with currently class.", 5000));
					player.sendPacket(new Earthquake(player.getX(), player.getY(), player.getZ(), 25, 6));
					player.sendPacket(new ExRedSky(6));
					player.sendPacket(new SocialAction(player, 5));
				}
				else if (player.getRace() == Race.Kamael)
				{
					if ((player.getClassId().getId() == 131) || (player.getClassId().getId() == 132) || (player.getClassId().getId() == 133) || (player.getClassId().getId() == 134))
					{
						player.sendMessage("You can do it only with 2nd class characters.");
						player.sendPacket(new ExShowScreenMessage("You can do it only with 2nd class characters.", 5000));
						player.sendPacket(new Earthquake(player.getX(), player.getY(), player.getZ(), 25, 6));
						player.sendPacket(new ExRedSky(6));
						player.sendPacket(new SocialAction(player, 5));
					}
					else
					{
						htmltext = "start.htm";
					}
				}
				else if ((player.getClassId().getId() == 88) || (player.getClassId().getId() == 89) || (player.getClassId().getId() == 90) || (player.getClassId().getId() == 91) || (player.getClassId().getId() == 92) || (player.getClassId().getId() == 93) || (player.getClassId().getId() == 94) || (player.getClassId().getId() == 95) || (player.getClassId().getId() == 96) || (player.getClassId().getId() == 97) || (player.getClassId().getId() == 98) || (player.getClassId().getId() == 99) || (player.getClassId().getId() == 100) || (player.getClassId().getId() == 101) || (player.getClassId().getId() == 102) || (player.getClassId().getId() == 103) || (player.getClassId().getId() == 104) || (player.getClassId().getId() == 105) || (player.getClassId().getId() == 106) || (player.getClassId().getId() == 107) || (player.getClassId().getId() == 108) || (player.getClassId().getId() == 109) || (player.getClassId().getId() == 110) || (player.getClassId().getId() == 111) || (player.getClassId().getId() == 112) || (player.getClassId().getId() == 113) || (player.getClassId().getId() == 114) || (player.getClassId().getId() == 115) || (player.getClassId().getId() == 116) || (player.getClassId().getId() == 117) || (player.getClassId().getId() == 118) || (player.getClassId().getId() == 127) || (player.getClassId().getId() == 128) || (player.getClassId().getId() == 129) || (player.getClassId().getId() == 130) || (player.getClassId().getId() == 131) || (player.getClassId().getId() == 132) || (player.getClassId().getId() == 133) || (player.getClassId().getId() == 134) || (player.getClassId().getId() == 135) || (player.getClassId().getId() == 136))
				{
					player.sendMessage("You can do it only with 2nd class characters.");
					player.sendPacket(new ExShowScreenMessage("You can do it only with 2nd class characters.", 5000));
					player.sendPacket(new Earthquake(player.getX(), player.getY(), player.getZ(), 25, 6));
					player.sendPacket(new ExRedSky(6));
					player.sendPacket(new SocialAction(player, 5));
				}
				else
				{
					htmltext = "start.htm";
				}
			}
		}
		else
		{
			player.sendMessage("NPC Services disabled.");
		}
		return htmltext;
	}
}
