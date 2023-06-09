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
package com.l2jserver.scripts.quests;

import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.skills.SkillHolder;

/**
 * @author L2jPDT
 */
public class Q00247_PossessorOfAPreciousSoul4 extends Quest
{
	// NPCs
	private static final int CARADINE = 31740;
	private static final int LADY_OF_LAKE = 31745;
	// Items
	private static final int CARADINE_LETTER_LAST = 7679;
	private static final int NOBLESS_TIARA = 7694;
	// Location
	private static final Location CARADINE_LOC = new Location(143209, 43968, -3038);
	// Skill
	private static SkillHolder MIMIRS_ELIXIR = new SkillHolder(4339, 1);
	
	public Q00247_PossessorOfAPreciousSoul4()
	{
		super(247, Q00247_PossessorOfAPreciousSoul4.class.getSimpleName(), "Possessor Of A Precious Soul 4");
		StartNpcs(CARADINE);
		TalkNpcs(CARADINE, LADY_OF_LAKE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return getNoQuestMsg(player);
		}
		if (!player.isSubClassActive())
		{
			return "no_sub.html";
		}
		
		switch (event)
		{
			case "31740-3.html":
				st.startQuest();
				st.takeItems(CARADINE_LETTER_LAST, -1);
				break;
			case "31740-5.html":
				if (st.isCond(1))
				{
					st.setCond(2, true);
					player.teleToLocation(CARADINE_LOC, 0);
				}
				break;
			case "31745-5.html":
				if (st.isCond(2))
				{
					player.setNoble(true);
					st.addExpAndSp(93836, 0);
					player.broadcastPacket(new SocialAction(player, 3));
					st.giveItems(NOBLESS_TIARA, 1);
					npc.setTarget(player);
					npc.doCast(MIMIRS_ELIXIR.getSkill());
					player.sendPacket(new SocialAction(player, 3));
					st.exitQuest(false, true);
				}
				break;
		}
		return event;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		if (st.isStarted() && !player.isSubClassActive())
		{
			return "no_sub.html";
		}
		
		switch (npc.getNpcId())
		{
			case CARADINE:
			{
				switch (st.getState())
				{
					case State.CREATED:
						final QuestState qs = player.getQuestState(Q00246_PossessorOfAPreciousSoul3.class.getSimpleName());
						if ((qs != null) && qs.isCompleted())
						{
							htmltext = ((player.getLevel() >= 75) ? "31740-1.htm" : "31740-2.html");
						}
						break;
					case State.STARTED:
						if (st.isCond(1))
						{
							htmltext = "31740-6.html";
						}
						break;
					case State.COMPLETED:
						htmltext = getAlreadyCompletedMsg(player);
						break;
				}
				break;
			}
			case LADY_OF_LAKE:
			{
				if (st.isCond(2))
				{
					htmltext = "31745-1.html";
				}
				break;
			}
		}
		return htmltext;
	}
}
