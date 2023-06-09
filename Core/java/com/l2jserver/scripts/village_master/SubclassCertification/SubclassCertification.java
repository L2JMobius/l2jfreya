/*
 * Copyright (C) 2004-2015 L2J DataPack
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
package com.l2jserver.scripts.village_master.SubclassCertification;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.datatables.CharTemplateTable;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2VillageMasterInstance;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * Subclass certification
 * @author xban1x, jurchiks
 */
public final class SubclassCertification extends L2AttackableAIScript
{
	// @formatter:off
	private static final int[] NPCS =
	{
		30026, 30031, 30037, 30066, 30070, 30109, 30115, 30120, 30154, 30174,
		30175, 30176, 30187, 30191, 30195, 30288, 30289, 30290, 30297, 30358,
		30373, 30462, 30474, 30498, 30499, 30500, 30503, 30504, 30505, 30508,
		30511, 30512, 30513, 30520, 30525, 30565, 30594, 30595, 30676, 30677,
		30681, 30685, 30687, 30689, 30694, 30699, 30704, 30845, 30847, 30849,
		30854, 30857, 30862, 30865, 30894, 30897, 30900, 30905, 30910, 30913,
		31269, 31272, 31276, 31279, 31285, 31288, 31314, 31317, 31321, 31324,
		31326, 31328, 31331, 31334, 31336, 31755, 31958, 31961, 31965, 31968,
		31974, 31977, 31996, 32092, 32093, 32094, 32095, 32096, 32097, 32098,
		32145, 32146, 32147, 32150, 32153, 32154, 32157, 32158, 32160, 32171,
		32193, 32199, 32202, 32213, 32214, 32221, 32222, 32229, 32230, 32233,
		32234
	};
	// @formatter:on
	private static final int CERTIFICATE_EMERGENT_ABILITY = 10280;
	private static final int CERTIFICATE_MASTER_ABILITY = 10612;
	private static final Map<Integer, Integer> ABILITY_CERTIFICATES = new HashMap<>();
	private static final Map<Integer, Integer> TRANSFORMATION_SEALBOOKS = new HashMap<>();
	
	static
	{
		ABILITY_CERTIFICATES.put(0, 10281); // Certificate - Warrior Ability
		ABILITY_CERTIFICATES.put(1, 10283); // Certificate - Rogue Ability
		ABILITY_CERTIFICATES.put(2, 10282); // Certificate - Knight Ability
		ABILITY_CERTIFICATES.put(3, 10286); // Certificate - Summoner Ability
		ABILITY_CERTIFICATES.put(4, 10284); // Certificate - Wizard Ability
		ABILITY_CERTIFICATES.put(5, 10285); // Certificate - Healer Ability
		ABILITY_CERTIFICATES.put(6, 10287); // Certificate - Enchanter Ability
		
		TRANSFORMATION_SEALBOOKS.put(0, 10289); // Transformation Sealbook: Divine Warrior
		TRANSFORMATION_SEALBOOKS.put(1, 10290); // Transformation Sealbook: Divine Rogue
		TRANSFORMATION_SEALBOOKS.put(2, 10288); // Transformation Sealbook: Divine Knight
		TRANSFORMATION_SEALBOOKS.put(3, 10294); // Transformation Sealbook: Divine Summoner
		TRANSFORMATION_SEALBOOKS.put(4, 10292); // Transformation Sealbook: Divine Wizard
		TRANSFORMATION_SEALBOOKS.put(5, 10291); // Transformation Sealbook: Divine Healer
		TRANSFORMATION_SEALBOOKS.put(6, 10293); // Transformation Sealbook: Divine Enchanter
	}
	
	private static final int MIN_LVL = 65;
	
	public SubclassCertification()
	{
		super(-1, SubclassCertification.class.getSimpleName(), "village_master");
		StartNpcs(NPCS);
		TalkNpcs(NPCS);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st != null)
		{
			st.setState(State.STARTED);
			htmltext = "Main.html";
		}
		return htmltext;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = null;
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "GetCertified":
			{
				if (!player.isSubClassActive())
				{
					htmltext = "NotSubclass.html";
				}
				else if (player.getLevel() < MIN_LVL)
				{
					htmltext = "NotMinLevel.html";
				}
				else if (((L2VillageMasterInstance) npc).checkVillageMaster(player.getActiveClass()))
				{
					htmltext = "CertificationList.html";
				}
				else
				{
					htmltext = "WrongVillageMaster.html";
				}
				break;
			}
			case "Obtain65":
			{
				htmltext = replaceHtml(player, "EmergentAbility.html", true, null).replace("%level%", "65").replace("%skilltype%", "common skill").replace("%event%", "lvl65Emergent");
				break;
			}
			case "Obtain70":
			{
				htmltext = replaceHtml(player, "EmergentAbility.html", true, null).replace("%level%", "70").replace("%skilltype%", "common skill").replace("%event%", "lvl70Emergent");
				break;
			}
			case "Obtain75":
			{
				htmltext = replaceHtml(player, "ClassAbility.html", true, null);
				break;
			}
			case "Obtain80":
			{
				htmltext = replaceHtml(player, "EmergentAbility.html", true, null).replace("%level%", "80").replace("%skilltype%", "transformation skill").replace("%event%", "lvl80Class");
				break;
			}
			case "lvl65Emergent":
			{
				htmltext = doCertification(player, st, "EmergentAbility", CERTIFICATE_EMERGENT_ABILITY, 65);
				break;
			}
			case "lvl70Emergent":
			{
				htmltext = doCertification(player, st, "EmergentAbility", CERTIFICATE_EMERGENT_ABILITY, 70);
				break;
			}
			case "lvl75Master":
			{
				htmltext = doCertification(player, st, "ClassAbility", CERTIFICATE_MASTER_ABILITY, 75);
				break;
			}
			case "lvl75Class":
			{
				htmltext = doCertification(player, st, "ClassAbility", ABILITY_CERTIFICATES.get(getClassIndex(player)), 75);
				break;
			}
			case "lvl80Class":
			{
				htmltext = doCertification(player, st, "ClassAbility", TRANSFORMATION_SEALBOOKS.get(getClassIndex(player)), 80);
				break;
			}
			case "Main.html":
			case "Explanation.html":
			case "NotObtain.html":
			{
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	private String replaceHtml(L2PcInstance player, String htmlFile, boolean replaceClass, String levelToReplace)
	{
		String htmltext = getHtm(player, htmlFile);
		if (replaceClass)
		{
			htmltext = htmltext.replace("%class%", String.valueOf(CharTemplateTable.getInstance().getClassNameById(player.getActiveClass())));
			// htmltext = htmltext.replace("%class%", String.valueOf(ClassList.getInstance().getClass(player.getActiveClass()).getClientCode()));
		}
		if (levelToReplace != null)
		{
			htmltext = htmltext.replace("%level%", levelToReplace);
		}
		return htmltext;
	}
	
	private static int getClassIndex(L2PcInstance player)
	{
		if ((player.getClassId().getId() == 3) || (player.getClassId().getId() == 127) || (player.getClassId().getId() == 128) || (player.getClassId().getId() == 129) || (player.getClassId().getId() == 46) || (player.getClassId().getId() == 55) || (player.getClassId().getId() == 57) || (player.getClassId().getId() == 48) || (player.getClassId().getId() == 2) || (player.getClassId().getId() == 89) || (player.getClassId().getId() == 131) || (player.getClassId().getId() == 113) || (player.getClassId().getId() == 117) || (player.getClassId().getId() == 118) || (player.getClassId().getId() == 132) || (player.getClassId().getId() == 133) || (player.getClassId().getId() == 114) || (player.getClassId().getId() == 88))
		{
			return 0;
		}
		else if ((player.getClassId().getId() == 8) || (player.getClassId().getId() == 23) || (player.getClassId().getId() == 36) || (player.getClassId().getId() == 93) || (player.getClassId().getId() == 101) || (player.getClassId().getId() == 108) || (player.getClassId().getId() == 9) || (player.getClassId().getId() == 24) || (player.getClassId().getId() == 37) || (player.getClassId().getId() == 92) || (player.getClassId().getId() == 102) || (player.getClassId().getId() == 109) || (player.getClassId().getId() == 130) || (player.getClassId().getId() == 134))
		{
			return 1;
		}
		else if ((player.getClassId().getId() == 5) || (player.getClassId().getId() == 6) || (player.getClassId().getId() == 33) || (player.getClassId().getId() == 20) || (player.getClassId().getId() == 90) || (player.getClassId().getId() == 91) || (player.getClassId().getId() == 106) || (player.getClassId().getId() == 99))
		{
			return 2;
		}
		else if ((player.getClassId().getId() == 14) || (player.getClassId().getId() == 28) || (player.getClassId().getId() == 41) || (player.getClassId().getId() == 96) || (player.getClassId().getId() == 104) || (player.getClassId().getId() == 111))
		{
			return 3;
		}
		else if ((player.getClassId().getId() == 12) || (player.getClassId().getId() == 27) || (player.getClassId().getId() == 40) || (player.getClassId().getId() == 13) || (player.getClassId().getId() == 94) || (player.getClassId().getId() == 103) || (player.getClassId().getId() == 110) || (player.getClassId().getId() == 95))
		{
			return 4;
		}
		else if ((player.getClassId().getId() == 16) || (player.getClassId().getId() == 30) || (player.getClassId().getId() == 43) || (player.getClassId().getId() == 97) || (player.getClassId().getId() == 105) || (player.getClassId().getId() == 112))
		{
			return 5;
		}
		else if ((player.getClassId().getId() == 17) || (player.getClassId().getId() == 51) || (player.getClassId().getId() == 52) || (player.getClassId().getId() == 21) || (player.getClassId().getId() == 34) || (player.getClassId().getId() == 135) || (player.getClassId().getId() == 98) || (player.getClassId().getId() == 115) || (player.getClassId().getId() == 116) || (player.getClassId().getId() == 100) || (player.getClassId().getId() == 107) || (player.getClassId().getId() == 136))
		{
			return 6;
		}
		
		return -1;
	}
	
	private String doCertification(L2PcInstance player, QuestState qs, String variable, Integer itemId, int level)
	{
		if (itemId == null)
		{
			return null;
		}
		
		String htmltext;
		String tmp = variable + level + "-" + player.getClassIndex();
		String globalVariable = qs.getGlobalQuestVar(tmp);
		
		if (!globalVariable.equals("") && !globalVariable.equals("0"))
		{
			htmltext = "AlreadyReceived.html";
		}
		else if (player.getLevel() < level)
		{
			htmltext = replaceHtml(player, "LowLevel.html", false, Integer.toString(level));
		}
		else
		{
			// Add items to player's inventory
			final L2ItemInstance item = player.getInventory().addItem("Quest", itemId, 1, player, player.getTarget());
			if (item == null)
			{
				return null;
			}
			
			final SystemMessage smsg = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
			smsg.addItemName(item);
			player.sendPacket(smsg);
			
			qs.saveGlobalQuestVar(tmp, String.valueOf(item.getObjectId()));
			htmltext = "GetAbility.html";
		}
		return htmltext;
	}
}