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
package com.l2jserver.scripts.quests;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * @author L2jPDT
 */
public class Q00016_TheComingDarkness extends Quest
{
	// NPCs
	private static final int HIERARCH = 31517;
	private static final int EVIL_ALTAR_1 = 31512;
	private static final int EVIL_ALTAR_2 = 31513;
	private static final int EVIL_ALTAR_3 = 31514;
	private static final int EVIL_ALTAR_4 = 31515;
	private static final int EVIL_ALTAR_5 = 31516;
	// Item
	private static final int CRYSTAL_OF_SEAL = 7167;
	
	public Q00016_TheComingDarkness()
	{
		super(16, Q00016_TheComingDarkness.class.getSimpleName(), "The Coming Darkness");
		StartNpcs(HIERARCH);
		TalkNpcs(HIERARCH, EVIL_ALTAR_1, EVIL_ALTAR_2, EVIL_ALTAR_3, EVIL_ALTAR_4, EVIL_ALTAR_5);
		registerQuestItems(CRYSTAL_OF_SEAL);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		final int cond = st.getCond();
		switch (event)
		{
			case "31517-02.htm":
				st.startQuest();
				st.giveItems(CRYSTAL_OF_SEAL, 5);
				break;
			case "31512-01.html":
			case "31513-01.html":
			case "31514-01.html":
			case "31515-01.html":
			case "31516-01.html":
				final int npcId = Integer.parseInt(event.replace("-01.html", ""));
				if ((cond == (npcId - 31511)) && st.hasQuestItems(CRYSTAL_OF_SEAL))
				{
					st.takeItems(CRYSTAL_OF_SEAL, 1);
					st.setCond(cond + 1, true);
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		final QuestState st2 = player.getQuestState(Q00017_LightAndDarkness.class.getSimpleName());
		if ((st2 != null) && !st2.isCompleted())
		{
			return "31517-04.html";
		}
		
		switch (st.getState())
		{
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg(player);
				break;
			case State.CREATED:
				htmltext = (player.getLevel() >= 62) ? "31517-00.htm" : "31517-05.html";
				break;
			case State.STARTED:
				final int npcId = npc.getNpcId();
				if (npcId == HIERARCH)
				{
					if (st.isCond(6))
					{
						st.addExpAndSp(865187, 69172);
						st.exitQuest(false, true);
						htmltext = "31517-03.html";
					}
					else
					{
						htmltext = "31517-02a.html";
					}
				}
				else if ((npcId - 31511) == st.getCond())
				{
					htmltext = npcId + "-00.html";
				}
				else
				{
					htmltext = npcId + "-01.html";
				}
				break;
		}
		return htmltext;
	}
}
