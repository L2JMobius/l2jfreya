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
package com.l2jserver.scripts.handlers.bypasshandlers;

import java.util.Base64;

import com.l2jserver.Config;
import com.l2jserver.gameserver.DressMeData;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.communitybbs.Managers.BaseBBSManager;
import com.l2jserver.gameserver.communitybbs.Managers.DressMeBBSManager;
import com.l2jserver.gameserver.communitybbs.Managers.TopBBSManager;
import com.l2jserver.gameserver.handler.interfaces.IBypassHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.Race;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.variables.PlayerVariables;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.templates.item.L2WeaponType;

/**
 * @author L2jPDT
 * @TODO: correct transformation height and radius now int fix to double (retail vaules from transforms .javas)
 */
public class DressMeBypasses implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"changedressmestatus",
		"gettarget",
		// setup
		"setupmyarmor",
		"setupmyweapon",
		"setupmycloak",
		"setupmyagathion",
		"setupmytransform",
		// menu - weapons
		"s84triumph",
		"s84freya",
		"s84vesper",
		"s80icarus",
		"s80dynasty",
		"sgrade",
		"cursed",
		// reset
		"resetmyarmor",
		"resetmyweapon",
		"resetmycloak",
		"resetmyagathion",
		"resetmytransform",
		// Agathions
		"Little_Angel",
		"Little_Devil",
		"Rudolph",
		"Juju",
		"Oink",
		"Majo",
		"Gold_Majo",
		"Black_Majo",
		"Plaipitak",
		"Baby_Panda",
		"Bamboo_Panda",
		"Sexy_Panda",
		"Charming_Cupid",
		"Naughty_Cupid",
		"White_Maneki_Neko",
		"Black_Maneki_Neko",
		"Brown_Maneki_Neko",
		"One_Eyed_Bat_Drove",
		"Pegasus",
		"Yellow_Robed_Tojigong",
		"Blue_Robed_Tojigong",
		"Green_Robed_Tojigon",
		"Bugbear",
		"Love",
		"Red_Sumo_Wrestler",
		"Blue_Sumo_Wrestler",
		"Great_Sumo_Match",
		"Button_Eyed_Bear_Doll",
		"God_of_Fortune",
		"Dryad",
		"Wonboso",
		"Daewoonso",
		"Pomona",
		"Weaver",
		"Chon_cho",
		"Tang_tan",
		"Monkey_King",
		"Utanka_Agathion",
		"Bonus_B_Agathion",
		"Zombie",
		"Baekyi_Hwamae",
		"Kwanwoo_Hwamae",
		"Opera",
		"Miss_Chipa",
		"Nepal_Snow",
		"Round_Ball_Snow",
		"Ladder_Snow",
		"Iken",
		"Lana",
		"Gnocian",
		"Orodriel",
		"Lakinos",
		"Mortia",
		"Hayance",
		"Meruril",
		"Taman_ze_Lapatui",
		"Kaurin",
		"Ahertbein",
		"Naonin",
		// Armors
		"vorpal_heavy",
		"vorpal_light",
		"vorpal_robe",
		"elegia_heavy",
		"elegia_light",
		"elegia_robe",
		"vesper_heavy",
		"vesper_light",
		"vesper_robe",
		"vesper_noble_heavy",
		"vesper_noble_light",
		"vesper_noble_robe",
		"dynasty_heavy",
		"dynasty_light",
		"dynasty_robe",
		"moirai_heavy",
		"moirai_light",
		"moirai_robe",
		// Demonic Weapons
		"zariche",
		"akamanah",
		// S- grade wea //TODO: KAMAEL weapons
		"imperial",
		"draconic",
		"major_arcana",
		"forgottenblade",
		"basalthammer",
		"imperialstaff",
		"angelslayer",
		"shiningbow",
		"dragonaxe",
		"saintspear",
		"demonsplinter",
		"heavensdivider",
		"arcanamace",
		"sduals",
		"dracobow",
		// Dynasty //TODO: KAMAEL weapons
		"dyn_sword",
		"dyn_blade",
		"dyn_phantom",
		"dyn_bow",
		"dyn_knife",
		"dyn_halbert",
		"dyn_cudgel",
		"dyn_mace",
		"dyn_baghnahk",
		"dyn_dual_s",
		"dyn_dual_d",
		"dyn_crusher",
		"dyn_staff",
		// Icarus
		"ica_sawsword",
		"ica_disperser",
		"ica_spririt",
		"ica_heavy_arms",
		"ica_trident",
		"ica_hammer",
		"ica_hand",
		"ica_hall",
		"ica_spitter",
		"ica_stinger",
		"ica_wingblade",
		"ica_shotter",
		"ica_dual_s",
		"ica_dual_d",
		// Vesper //TODO: only Kamael
		"Vesper_Cutter",
		"Vesper_Slasher",
		"Vesper_Buster",
		"Vesper_Shaper",
		"Vesper_Fighter",
		"Vesper_Stormer",
		"Vesper_Avenger",
		"Vesper_Retributer",
		"Vesper_Caster",
		"Vesper_Singer",
		"Vesper_Thrower",
		"Vesper_Pincer",
		"Vesper_Sheutjeh",
		"Vesper_Nagan",
		"Vesper_Dual_Daggers",
		"Vesper_Dual_Sword",
		// Freya
		"Eternal_Core_Sword",
		"Mamba_Edge",
		"Eversor_Mace",
		"Contristo_Hammer",
		"Lava_Saw",
		"Jade_Claw",
		"Demitelum",
		"Sacredium",
		"Cyclic_Cane",
		"Archangel_Sword",
		"Recurve_Thorne_Bow",
		"Heavenstare_Rapier",
		"Pyseal_Blade",
		"Thorne_Crossbow",
		"Periel_Sword",
		"Skull_Edge",
		"Vigwik_Axe",
		"Devilish_Maul",
		"Feather_Eye_Blade",
		"Octo_Claw",
		"Doubletop_Spear",
		"Rising_Star",
		"Blackvisage",
		"Veniplant_Sword",
		"Skull_Carnium_Bow",
		"Gemtail_Rapier",
		"Finale_Blade",
		"Dominion_Crossbow",
		"Skull_Edge_Dual_Daggers",
		"Periel_Dual_Sword",
		"Mamba_Edge_Dual_Daggers",
		"Eternal_Core_Dual_Sword",
		// Triumph
		"Triumph_Blade",
		"Triumph_Dagger",
		"Triumph_Hammer",
		"Triumph_Crusher",
		"Triumph_Two_Hand_Sword",
		"Triumph_Jamadhr",
		"Triumph_Spear",
		"Triumph_Staff",
		"Triumph_Two_Hand_Staff",
		"Triumph_Magic_Sword",
		"Triumph_Bow",
		"Triumph_Rapier",
		"Triumph_Ancientsword",
		"Triumph_Crossbow",
		// Transformations
		"transform_me_257",
		"transform_me_255",
		"transform_me_252",
		"transform_me_254",
		"transform_me_258",
		"transform_me_253",
		"transform_me_256",
		"transform_me_219",
		"transform_me_220",
		"transform_me_221",
		"transform_me_105",
		"transform_me_104",
		"transform_me_304",
		"transform_me_102",
		"transform_me_1",
		"transform_me_103",
		"transform_me_2",
		"transform_me_7",
		"transform_me_303",
		"transform_me_3",
		"transform_me_6",
		"transform_me_5",
		"transform_me_4",
		"transform_me_322",
		"transform_me_321",
		"transform_me_111",
		"transform_me_320",
		"transform_me_217",
		"transform_me_211",
		"transform_me_202",
		"transform_me_214",
		"transform_me_208",
		"transform_me_205",
		"transform_me_204",
		"transform_me_206",
		"transform_me_108",
		"transform_me_114",
		"transform_me_107",
		"transform_me_121",
		"transform_me_122",
		"transform_me_319",
		"transform_me_115",
		"transform_me_116",
		"transform_me_112",
		"transform_me_117",
		"transform_me_120",
		"transform_me_118",
		"transform_me_119",
		"transform_me_125",
		"transform_me_20000",
	};
	
	@Override
	public boolean useBypass(String bypass, L2PcInstance activeChar, L2Character target)
	{
		if (Config.DRESS_ME_ONLY_FOR_PREMIUM)
		{
			if (activeChar.isPremiumAccount() == false)
			{
				activeChar.sendMessage(getText("WW91IG11c3QgYmUgYSBwcmVtaXVtIHVzZXIgZm9yIHVzZSAuZHJlc3NtZS4="));
				return true;
			}
		}
		switch (bypass)
		{
			case "gettarget":
			{
				L2PcInstance t = (L2PcInstance) activeChar.getTarget();
				if ((activeChar.getRace() == Race.Kamael) && (t.getRace() != Race.Kamael))
				{
					activeChar.sendMessage(getText("SWYgeW91IGFyZSBhIGthbWFlbCwgeW91IG11c3QgdGFyZ2V0IGthbWFlbC4="));
				}
				else
				{
					stealTarget(activeChar);
					activeChar.setDressMeEnabled(true);
					DressMeBBSManager.consumeForTarget(activeChar);
				}
				break;
			}
			// Vesper
			case "Vesper_Dual_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUAL))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(52);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Cutter":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13457);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Slasher":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGSWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13458);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Buster":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13459);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Shaper":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13460);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Fighter":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALFIST))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13461);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Stormer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.POLE))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13462);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Avenger":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13463);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Retributer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13464);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Caster":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13465);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Singer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13466);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Thrower":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13467);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Pincer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.RAPIER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13468);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Sheutjeh":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.CROSSBOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13469);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Nagan":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.ANCIENTSWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13470);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vesper_Dual_Daggers":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALDAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13884);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendVesperWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			// Freya
			case "Eternal_Core_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15544);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Mamba_Edge":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15545);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Eversor_Mace":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15546);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Contristo_Hammer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15547);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Lava_Saw":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGSWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15548);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Jade_Claw":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALFIST))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15549);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Demitelum":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.POLE))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15550);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Sacredium":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15551);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Cyclic_Cane":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15552);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Archangel_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15553);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Recurve_Thorne_Bow":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15554);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Heavenstare_Rapier":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.RAPIER))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15555);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "Pyseal_Blade":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.ANCIENTSWORD))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15556);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "Thorne_Crossbow":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.CROSSBOW))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15557);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "Periel_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15558);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Skull_Edge":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15559);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Vigwik_Axe":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15560);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Devilish_Maul":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15561);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Feather_Eye_Blade":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGSWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15562);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Octo_Claw":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALFIST))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15563);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Doubletop_Spear":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.POLE))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15564);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Rising_Star":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15565);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Blackvisage":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15566);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Veniplant_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15567);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Skull_Carnium_Bow":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15568);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Gemtail_Rapier":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.RAPIER))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15569);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "Finale_Blade":
			{
				
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.ANCIENTSWORD))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15570);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "Dominion_Crossbow":
			{
				
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.CROSSBOW))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15571);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "Skull_Edge_Dual_Daggers":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALDAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(16152);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Periel_Dual_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUAL))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(16154);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Mamba_Edge_Dual_Daggers":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALDAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(16156);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Eternal_Core_Dual_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUAL))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(16158);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendFreyaWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			// Triumph
			case "Triumph_Blade":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15676);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Dagger":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15677);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Hammer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15678);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Crusher":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15679);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Two_Hand_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGSWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15680);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Jamadhr":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.FIST))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15681);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Spear":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.POLE))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15682);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Staff":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15683);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Two_Hand_Staff":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15684);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Magic_Sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15685);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Bow":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(15686);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "Triumph_Rapier":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
				}
				else
				{
					
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.RAPIER))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15687);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
				
			}
			case "Triumph_Ancientsword":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.ANCIENTSWORD))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15688);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "Triumph_Crossbow":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.CROSSBOW))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(15689);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendTriumphWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			// Icarus
			case "ica_sawsword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10215);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_disperser":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10216);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_spririt":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10217);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_heavy_arms":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGSWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10218);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_trident":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.POLE))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10219);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_hammer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10220);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_hand":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALFIST))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10221);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_hall":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10222);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_spitter":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10223);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_stinger":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.RAPIER))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(10224);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "ica_wingblade":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.ANCIENTSWORD))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(10225);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "ica_shotter":
			{
				if (activeChar.getRace() != Race.Kamael)
				{
					activeChar.sendMessage(getText("WW91IGFyZSBub3QgYWxsb3dlZCB0byB1c2UgS2FtYWVsIHdlYXBvbnMu"));
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
				}
				else
				{
					if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.CROSSBOW))
					{
						activeChar.sendMessage("You must has equiped same type of weapon");
					}
					else
					{
						DressMeBBSManager.newDressData(activeChar);
						activeChar.getDressMeData().setWeapId(10226);
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
						DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
						DressMeBBSManager.consumeForWeapon(activeChar);
					}
				}
				break;
			}
			case "ica_dual_s":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUAL))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10415);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "ica_dual_d":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALDAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13883);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendIcarusWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			
			case "dyn_crusher":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10253);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_staff":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10252);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_sword":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9442);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
					break;
				}
			}
			case "dyn_blade":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGSWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9443);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
					break;
				}
			}
			case "dyn_phantom":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9444);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_bow":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9445);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_knife":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9446);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_halbert":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.POLE))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9447);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_cudgel":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9448);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_mace":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9449);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_baghnahk":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALFIST))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(9450);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_dual_s":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUAL))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(10004);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dyn_dual_d":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUALDAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(13882);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendDynWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "forgottenblade":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.SWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6364);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "basalthammer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6365);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "imperialstaff":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6366);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "angelslayer":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DAGGER))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6367);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "shiningbow":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6368);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dragonaxe":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGBLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6369);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "saintspear":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.POLE))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6370);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "demonsplinter":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.FIST))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6371);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "heavensdivider":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BIGSWORD))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6372);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "arcanamace":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BLUNT))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6579);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "sduals":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.DUAL))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(6580);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "dracobow":
			{
				if (!(activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
				{
					activeChar.sendMessage("You must has equiped same type of weapon");
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setWeapId(7575);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendSWeaponMenu(activeChar);
					DressMeBBSManager.consumeForWeapon(activeChar);
				}
				break;
			}
			case "zariche":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setWeapId(8190);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendCursedWeaponMenu(activeChar);
				DressMeBBSManager.consumeForWeapon(activeChar);
				break;
			}
			case "akamanah":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setWeapId(8689);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendCursedWeaponMenu(activeChar);
				DressMeBBSManager.consumeForWeapon(activeChar);
				break;
			}
			case "s84triumph":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/wea_s84_triumph.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "s84freya":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/wea_s84_freya.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "s84vesper":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/wea_s84_vesper.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "s80icarus":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/wea_s80_icarus.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "s80dynasty":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/wea_s80_dynasty.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "sgrade":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/wea_s.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "cursed":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/wea_demonic.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "setupmyarmor":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/setupmyarmor.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "setupmyweapon":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/setupmyweapon.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "setupmycloak":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setCloakId(13890);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.consumeForCloak(activeChar);
				DressMeBBSManager.sendMainWindow(activeChar);
				break;
			}
			case "setupmyagathion":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/setupmyagathion.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "setupmytransform":
			{
				NpcHtmlMessage file = new NpcHtmlMessage(0);
				file.setFile(activeChar, "./data/html/CommunityBoard/setupmytransform.htm");
				PlayerVariables vars = activeChar.getVariables();
				int traders = TopBBSManager.getTraders();
				int onlineplayers = TopBBSManager.getOnline();
				file.replace("%playername%", activeChar.getName());
				file.replace("%servername%", Config.SERVER_NAME);
				file.replace("%online%", "<font color=LEVEL>" + String.valueOf(onlineplayers - traders) + " </font>");
				file.replace("%traders%", "<font color=LEVEL>" + String.valueOf(traders) + " </font>");
				file.replace("%total%", "<font color=LEVEL>" + String.valueOf(onlineplayers) + " </font>");
				file.replace("%XP%", "XP: <font color=LEVEL>" + Config.RATE_XP + " </font>");
				file.replace("%DROP%", "Drop: <font color=LEVEL>" + Config.RATE_DROP_ITEMS + " </font>");
				file.replace("%SP%", "SP: <font color=LEVEL>" + Config.RATE_SP + " </font>");
				file.replace("%ADENA%", "Adena: <font color=LEVEL>" + Config.RATE_DROP_ITEMS_ID.get(57) + " </font>");
				file.replace("%PXP%", "Party XP: <font color=LEVEL>" + Config.RATE_PARTY_XP + " </font>");
				file.replace("%SPOIL%", "Spoil: <font color=LEVEL>" + Config.RATE_DROP_SPOIL + " </font>");
				file.replace("%PSP%", "Party SP: <font color=LEVEL>" + Config.RATE_PARTY_SP + " </font>");
				file.replace("%QR%", "Quest Drop: <font color=LEVEL>" + Config.RATE_QUEST_DROP + " </font>");
				// Dress Me
				file.replace("%enabled%", activeChar.isDressMeEnabled() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				for (String var : TopBBSManager.VAR_NAMES)
				{
					file.replace("%" + var + "%", vars.getBoolean(var, false) ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");
				}
				BaseBBSManager.separateAndSend(file.getHtm(), activeChar);
				break;
			}
			case "resetmyarmor":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setChestId(0);
				activeChar.getDressMeData().setBootsId(0);
				activeChar.getDressMeData().setLegsId(0);
				activeChar.getDressMeData().setGlovesId(0);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendMainWindow(activeChar);
				activeChar.sendMessage(getText("WW91IGhhdmUganVzdCByZXNldCB5b3VyIGFybW9yIHNldHRpbmdzLg=="));
				break;
			}
			case "resetmyweapon":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setWeapId(0);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendMainWindow(activeChar);
				activeChar.sendMessage(getText("WW91IGhhdmUganVzdCByZXNldCB5b3VyIHdlYXBvbiBzZXR0aW5ncy4="));
				break;
			}
			case "resetmycloak":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setCloakId(0);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendMainWindow(activeChar);
				activeChar.sendMessage(getText("WW91IGhhdmUganVzdCByZXNldCB5b3VyIGNsb2FrIHNldHRpbmdzLg=="));
				break;
			}
			case "resetmyagathion":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(0);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendMainWindow(activeChar);
				activeChar.sendMessage(getText("WW91IGhhdmUganVzdCByZXNldCB5b3VyIGFnYXRoaW9uIHNldHRpbmdzLg=="));
				break;
			}
			case "resetmytransform":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setTransformId(0);
				activeChar.getDressMeData().setRadius(activeChar.getCollisionRadius());
				activeChar.getDressMeData().setHeight(activeChar.getCollisionHeight());
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendMainWindow(activeChar);
				activeChar.sendMessage(getText("WW91IGhhdmUganVzdCByZXNldCB5b3VyIHRyYW5zZm9ybWF0aW9uIHNldHRpbmdzLg=="));
				break;
			}
			// Transformations
			case "transform_me_257":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(257);
					activeChar.getDressMeData().setRadius(8);
					activeChar.getDressMeData().setHeight(18);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
				}
				break;
			}
			case "transform_me_255":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(255);
					activeChar.getDressMeData().setRadius(10);
					activeChar.getDressMeData().setHeight(25);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
				}
				break;
			}
			case "transform_me_252":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(252);
					activeChar.getDressMeData().setRadius(16);
					activeChar.getDressMeData().setHeight(30);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_254":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(254);
					activeChar.getDressMeData().setRadius(10);
					activeChar.getDressMeData().setHeight(28);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_258":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(258);
					activeChar.getDressMeData().setRadius(10);
					activeChar.getDressMeData().setHeight(25);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_253":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(253);
					activeChar.getDressMeData().setRadius(15);
					activeChar.getDressMeData().setHeight(29);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_256":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(256);
					activeChar.getDressMeData().setRadius(10);
					activeChar.getDressMeData().setHeight(26);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_219":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(219);
					activeChar.getDressMeData().setRadius(10);
					activeChar.getDressMeData().setHeight(23);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_220":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(220);
					activeChar.getDressMeData().setRadius(8);
					activeChar.getDressMeData().setHeight(30);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_221":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(221);
					activeChar.getDressMeData().setRadius(11);
					activeChar.getDressMeData().setHeight(27);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_105":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(105);
					activeChar.getDressMeData().setRadius(5);
					activeChar.getDressMeData().setHeight(5);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_104":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(104);
					activeChar.getDressMeData().setRadius(15);
					activeChar.getDressMeData().setHeight(18);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_304":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(304);
					activeChar.getDressMeData().setRadius(5);
					activeChar.getDressMeData().setHeight(25);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_102":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(102);
					activeChar.getDressMeData().setRadius(15);
					activeChar.getDressMeData().setHeight(27);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_1":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(1);
					activeChar.getDressMeData().setRadius(14);
					activeChar.getDressMeData().setHeight(15);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_103":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(103);
					activeChar.getDressMeData().setRadius(22);
					activeChar.getDressMeData().setHeight(31);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_2":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(2);
					activeChar.getDressMeData().setRadius(22);
					activeChar.getDressMeData().setHeight(31);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_7":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(7);
					activeChar.getDressMeData().setRadius(6);
					activeChar.getDressMeData().setHeight(12);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_303":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(303);
					activeChar.getDressMeData().setRadius(11);
					activeChar.getDressMeData().setHeight(25);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_3":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(3);
					activeChar.getDressMeData().setRadius(8);
					activeChar.getDressMeData().setHeight(28);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_6":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(6);
					activeChar.getDressMeData().setRadius(23);
					activeChar.getDressMeData().setHeight(61);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_5":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(5);
					activeChar.getDressMeData().setRadius(34);
					activeChar.getDressMeData().setHeight(28);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_4":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(4);
					activeChar.getDressMeData().setRadius(12);
					activeChar.getDressMeData().setHeight(40);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_322":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(322);
					activeChar.getDressMeData().setRadius(22);
					activeChar.getDressMeData().setHeight(45);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_321":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(321);
					activeChar.getDressMeData().setRadius(23);
					activeChar.getDressMeData().setHeight(90);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_111":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(111);
					activeChar.getDressMeData().setRadius(20);
					activeChar.getDressMeData().setHeight(10);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_320":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(320);
					activeChar.getDressMeData().setRadius(21);
					activeChar.getDressMeData().setHeight(40);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_217":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(217);
					activeChar.getDressMeData().setRadius(16);
					activeChar.getDressMeData().setHeight(24);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_211":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(211);
					activeChar.getDressMeData().setRadius(13);
					activeChar.getDressMeData().setHeight(25);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_202":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(202);
					activeChar.getDressMeData().setRadius(10);
					activeChar.getDressMeData().setHeight(35);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_214":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(214);
					activeChar.getDressMeData().setRadius(15);
					activeChar.getDressMeData().setHeight(24);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_208":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(208);
					activeChar.getDressMeData().setRadius(12);
					activeChar.getDressMeData().setHeight(26);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_205":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(205);
					activeChar.getDressMeData().setRadius(15);
					activeChar.getDressMeData().setHeight(28);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_108":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(108);
					activeChar.getDressMeData().setRadius(12);
					activeChar.getDressMeData().setHeight(18);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_114":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(114);
					activeChar.getDressMeData().setRadius(28);
					activeChar.getDressMeData().setHeight(30);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_107":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(107);
					activeChar.getDressMeData().setRadius(8);
					activeChar.getDressMeData().setHeight(25);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_121":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(121);
					activeChar.getDressMeData().setRadius(9);
					activeChar.getDressMeData().setHeight(28);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_122":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(122);
					activeChar.getDressMeData().setRadius(9);
					activeChar.getDressMeData().setHeight(28);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_319":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(319);
					activeChar.getDressMeData().setRadius(8);
					activeChar.getDressMeData().setHeight(25);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_115":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(115);
					activeChar.getDressMeData().setRadius(13);
					activeChar.getDressMeData().setHeight(30);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_116":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(116);
					activeChar.getDressMeData().setRadius(13);
					activeChar.getDressMeData().setHeight(19);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_112":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(112);
					activeChar.getDressMeData().setRadius(5);
					activeChar.getDressMeData().setHeight(12);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_117":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(117);
					activeChar.getDressMeData().setRadius(4);
					activeChar.getDressMeData().setHeight(8);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_120":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(120);
					activeChar.getDressMeData().setRadius(4);
					activeChar.getDressMeData().setHeight(8);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_118":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(118);
					activeChar.getDressMeData().setRadius(4);
					activeChar.getDressMeData().setHeight(8);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_119":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(119);
					activeChar.getDressMeData().setRadius(4);
					activeChar.getDressMeData().setHeight(8);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_125":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(125);
					activeChar.getDressMeData().setRadius(12);
					activeChar.getDressMeData().setHeight(28);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_20000":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(20000);
					activeChar.getDressMeData().setRadius(25);
					activeChar.getDressMeData().setHeight(14);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_17":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(17);
					activeChar.getDressMeData().setRadius(10);
					activeChar.getDressMeData().setHeight(24);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_21":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(21);
					activeChar.getDressMeData().setRadius(10);
					activeChar.getDressMeData().setHeight(27);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_19":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(19);
					activeChar.getDressMeData().setRadius(13);
					activeChar.getDressMeData().setHeight(23);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_16":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(16);
					activeChar.getDressMeData().setRadius(8);
					activeChar.getDressMeData().setHeight(24);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_18":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(18);
					activeChar.getDressMeData().setRadius(9);
					activeChar.getDressMeData().setHeight(24);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_113":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(113);
					activeChar.getDressMeData().setRadius(8);
					activeChar.getDressMeData().setHeight(24);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			case "transform_me_20":
			{
				if (DressMeBBSManager._transformed == true)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(() ->
					{
						DressMeBBSManager.untransform(activeChar);
					}, 100);
					return true;
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setTransformId(20);
					activeChar.getDressMeData().setRadius(12);
					activeChar.getDressMeData().setHeight(25);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendTransformMenu(activeChar);
					DressMeBBSManager.consumeForTransform(activeChar);
					DressMeBBSManager._transformed = true;
					break;
				}
			}
			// Agathions
			case "Little_Angel":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(16031);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Little_Devil":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(16032);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Rudolph":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(16033);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Juju":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(16064);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Oink":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(16065);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Majo":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1501);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Gold_Majo":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1502);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Black_Majo":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1503);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Plaipitak":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1504);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Baby_Panda":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1505);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Bamboo_Panda":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1506);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Sexy_Panda":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1507);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Charming_Cupid":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1508);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Naughty_Cupid":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1509);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "White_Maneki_Neko":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1510);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Black_Maneki_Neko":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1511);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Brown_Maneki_Neko":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1512);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "One_Eyed_Bat_Drove":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1513);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Pegasus":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1514);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Yellow_Robed_Tojigong":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1515);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Blue_Robed_Tojigong":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1516);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Green_Robed_Tojigon":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1517);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Bugbear":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1518);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Love":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(16049);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Red_Sumo_Wrestler":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1519);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Blue_Sumo_Wrestler":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1520);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Great_Sumo_Match":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1521);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Button_Eyed_Bear_Doll":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1522);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "God_of_Fortune":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1523);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Dryad":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1524);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Wonboso":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1525);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Daewoonso":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1526);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Pomona":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1527);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Weaver":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1528);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Chon_cho":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1529);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Tang_tan":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1530);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Monkey_King":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1532);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Utanka_Agathion":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1533);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Bonus_B_Agathion":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1534);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Zombie":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1535);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Baekyi_Hwamae":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1536);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Kwanwoo_Hwamae":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1537);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Opera":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1555);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Miss_Chipa":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1556);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Nepal_Snow":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1557);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Round_Ball_Snow":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1558);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Ladder_Snow":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1559);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Iken":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1539);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Lana":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1540);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Gnocian":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1541);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Orodriel":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1542);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Lakinos":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1543);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Mortia":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1544);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Hayance":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1545);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Meruril":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1546);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Taman_ze_Lapatui":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1547);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Kaurin":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1548);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Ahertbein":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1549);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			case "Naonin":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setAgathionId(1550);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendAgathionMenu(activeChar);
				DressMeBBSManager.consumeForAgathion(activeChar);
				break;
			}
			// Change Status
			case "changedressmestatus":
			{
				if (activeChar.getDressMeData() == null)
				{
					activeChar.sendMessage("You must something to wear up.");
					DressMeBBSManager.sendMainWindow(activeChar);
				}
				else
				{
					if (activeChar.isDressMeEnabled())
					{
						activeChar.setDressMeEnabled(false);
						activeChar.broadcastUserInfo();
					}
					else
					{
						activeChar.setDressMeEnabled(true);
						activeChar.broadcastUserInfo();
					}
					DressMeBBSManager.sendMainWindow(activeChar);
				}
				break;
			}
			case "dynasty_heavy":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(9416);
					activeChar.getDressMeData().setLegsId(9421);
					activeChar.getDressMeData().setBootsId(9424);
					activeChar.getDressMeData().setGlovesId(9423);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "dynasty_light":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setChestId(9425);
				activeChar.getDressMeData().setLegsId(9428);
				activeChar.getDressMeData().setBootsId(9431);
				activeChar.getDressMeData().setGlovesId(9430);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendArmorMenu(activeChar);
				DressMeBBSManager.consumeForArmor(activeChar);
				break;
			}
			case "dynasty_robe":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(9432);
					activeChar.getDressMeData().setLegsId(9437);
					activeChar.getDressMeData().setBootsId(9440);
					activeChar.getDressMeData().setGlovesId(9439);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "moirai_heavy":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(15609);
					activeChar.getDressMeData().setLegsId(15612);
					activeChar.getDressMeData().setBootsId(15618);
					activeChar.getDressMeData().setGlovesId(15615);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "moirai_light":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setChestId(15610);
				activeChar.getDressMeData().setLegsId(15613);
				activeChar.getDressMeData().setBootsId(15619);
				activeChar.getDressMeData().setGlovesId(15616);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendArmorMenu(activeChar);
				DressMeBBSManager.consumeForArmor(activeChar);
				break;
			}
			case "moirai_robe":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(15611);
					activeChar.getDressMeData().setLegsId(15614);
					activeChar.getDressMeData().setBootsId(15620);
					activeChar.getDressMeData().setGlovesId(15617);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "imperial":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(6373);
					activeChar.getDressMeData().setLegsId(6374);
					activeChar.getDressMeData().setBootsId(6376);
					activeChar.getDressMeData().setGlovesId(6375);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "draconic":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setChestId(6379);
				activeChar.getDressMeData().setLegsId(6379);
				activeChar.getDressMeData().setBootsId(6381);
				activeChar.getDressMeData().setGlovesId(6380);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendArmorMenu(activeChar);
				DressMeBBSManager.consumeForArmor(activeChar);
				break;
			}
			case "major_arcana":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(6383);
					activeChar.getDressMeData().setLegsId(6383);
					activeChar.getDressMeData().setBootsId(6385);
					activeChar.getDressMeData().setGlovesId(6384);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "vorpal_heavy":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(15592);
					activeChar.getDressMeData().setLegsId(15595);
					activeChar.getDressMeData().setBootsId(15601);
					activeChar.getDressMeData().setGlovesId(15598);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "vorpal_light":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setChestId(15593);
				activeChar.getDressMeData().setLegsId(15596);
				activeChar.getDressMeData().setBootsId(15602);
				activeChar.getDressMeData().setGlovesId(15599);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendArmorMenu(activeChar);
				DressMeBBSManager.consumeForArmor(activeChar);
				break;
			}
			case "vorpal_robe":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(15594);
					activeChar.getDressMeData().setLegsId(15597);
					activeChar.getDressMeData().setBootsId(15603);
					activeChar.getDressMeData().setGlovesId(15600);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "elegia_heavy":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(15575);
					activeChar.getDressMeData().setLegsId(15578);
					activeChar.getDressMeData().setBootsId(15584);
					activeChar.getDressMeData().setGlovesId(15581);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "elegia_light":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setChestId(15576);
				activeChar.getDressMeData().setLegsId(15579);
				activeChar.getDressMeData().setBootsId(15585);
				activeChar.getDressMeData().setGlovesId(15582);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendArmorMenu(activeChar);
				DressMeBBSManager.consumeForArmor(activeChar);
				break;
			}
			case "elegia_robe":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(15577);
					activeChar.getDressMeData().setLegsId(15580);
					activeChar.getDressMeData().setBootsId(15586);
					activeChar.getDressMeData().setGlovesId(15583);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "vesper_heavy":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(13432);
					activeChar.getDressMeData().setLegsId(13438);
					activeChar.getDressMeData().setBootsId(13440);
					activeChar.getDressMeData().setGlovesId(13439);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "vesper_light":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setChestId(13433);
				activeChar.getDressMeData().setLegsId(13441);
				activeChar.getDressMeData().setBootsId(13443);
				activeChar.getDressMeData().setGlovesId(13442);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendArmorMenu(activeChar);
				DressMeBBSManager.consumeForArmor(activeChar);
				break;
			}
			case "vesper_robe":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(13434);
					activeChar.getDressMeData().setLegsId(13444);
					activeChar.getDressMeData().setBootsId(13446);
					activeChar.getDressMeData().setGlovesId(13445);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "vesper_noble_heavy":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(13435);
					activeChar.getDressMeData().setLegsId(13448);
					activeChar.getDressMeData().setBootsId(13450);
					activeChar.getDressMeData().setGlovesId(13449);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
			case "vesper_noble_light":
			{
				DressMeBBSManager.newDressData(activeChar);
				activeChar.getDressMeData().setChestId(13436);
				activeChar.getDressMeData().setLegsId(13451);
				activeChar.getDressMeData().setBootsId(13453);
				activeChar.getDressMeData().setGlovesId(13452);
				activeChar.setDressMeEnabled(true);
				activeChar.broadcastUserInfo();
				DressMeBBSManager.sendArmorMenu(activeChar);
				DressMeBBSManager.consumeForArmor(activeChar);
				break;
			}
			case "vesper_noble_robe":
			{
				if (activeChar.getRace() == Race.Kamael)
				{
					activeChar.sendMessage(getText("U29ycnksIGJ1dCBrYW1hZWwgcmFjZSBjYW4gd2VhciBvbmx5IGxpZ2h0IGFybW9ycy4="));
					DressMeBBSManager.sendArmorMenu(activeChar);
				}
				else
				{
					DressMeBBSManager.newDressData(activeChar);
					activeChar.getDressMeData().setChestId(13437);
					activeChar.getDressMeData().setLegsId(13454);
					activeChar.getDressMeData().setBootsId(13456);
					activeChar.getDressMeData().setGlovesId(13455);
					activeChar.setDressMeEnabled(true);
					activeChar.broadcastUserInfo();
					DressMeBBSManager.sendArmorMenu(activeChar);
					DressMeBBSManager.consumeForArmor(activeChar);
				}
				break;
			}
		}
		return true;
		
	}
	
	public void stealTarget(L2PcInstance activeChar)
	{
		if ((activeChar.getTarget() == null) || !(activeChar.getTarget() instanceof L2PcInstance))
		{
			activeChar.sendMessage("Invalid target.");
			return;
		}
		
		L2PcInstance target = (L2PcInstance) activeChar.getTarget();
		
		if (target.getDressMeData() != null)
		{
			activeChar.sendMessage("You cannot take target visual, because he using .dressme.");
			return;
		}
		
		if (activeChar.getDressMeData() == null)
		{
			DressMeData dmd = new DressMeData();
			activeChar.setDressMeData(dmd);
		}
		
		if (target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST) == null)
		{
			activeChar.getDressMeData().setChestId(0);
		}
		else
		{
			activeChar.getDressMeData().setChestId(target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST).getItemId());
		}
		if (target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEGS) == null)
		{
			activeChar.getDressMeData().setLegsId(0);
		}
		else
		{
			activeChar.getDressMeData().setLegsId(target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEGS).getItemId());
		}
		if (target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_GLOVES) == null)
		{
			activeChar.getDressMeData().setGlovesId(0);
		}
		else
		{
			activeChar.getDressMeData().setGlovesId(target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_GLOVES).getItemId());
		}
		if (target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FEET) == null)
		{
			activeChar.getDressMeData().setBootsId(0);
		}
		else
		{
			activeChar.getDressMeData().setBootsId(target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FEET).getItemId());
		}
		if (target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND) == null)
		{
			activeChar.getDressMeData().setWeapId(0);
		}
		else
		{
			activeChar.getDressMeData().setWeapId(target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND).getItemId());
		}
		
		activeChar.broadcastUserInfo();
		DressMeBBSManager.sendMainWindow(activeChar);
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}