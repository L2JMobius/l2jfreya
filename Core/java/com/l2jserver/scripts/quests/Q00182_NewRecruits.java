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
package com.l2jserver.scripts.quests;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * @author L2jPDT
 */
public class Q00182_NewRecruits extends Quest
{
	// NPC's
	private static final int _kekropus = 32138;
	private static final int _nornil = 32258;
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(Q00182_NewRecruits.class.getSimpleName());
		
		if (st == null)
		{
			return htmltext;
		}
		
		if (npc.getNpcId() == _kekropus)
		{
			if (event.equalsIgnoreCase("32138-03.htm"))
			{
				st.setState(State.STARTED);
				st.set("cond", "1");
				st.playSound("ItemSound.quest_accept");
			}
		}
		else if (npc.getNpcId() == _nornil)
		{
			if (event.equalsIgnoreCase("32258-04.htm"))
			{
				st.giveItems(847, 2);
				st.playSound("ItemSound.quest_finish");
				st.exitQuest(false);
			}
			else if (event.equalsIgnoreCase("32258-05.htm"))
			{
				st.giveItems(890, 2);
				st.playSound("ItemSound.quest_finish");
				st.exitQuest(false);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		QuestState st = player.getQuestState(Q00182_NewRecruits.class.getSimpleName());
		if (st == null)
		{
			return htmltext;
		}
		
		if (player.getRace().ordinal() == 5)
		{
			htmltext = "32138-00.htm";
		}
		else
		{
			if (npc.getNpcId() == _kekropus)
			{
				switch (st.getState())
				{
					case State.CREATED:
						htmltext = "32138-01.htm";
						break;
					case State.STARTED:
						if (st.getInt("cond") == 1)
						{
							htmltext = "32138-03.htm";
						}
						break;
					case State.COMPLETED:
						htmltext = getAlreadyCompletedMsg(player);
						break;
				}
			}
			else if ((npc.getNpcId() == _nornil) && (st.getState() == State.STARTED))
			{
				htmltext = "32258-01.htm";
			}
		}
		
		return htmltext;
	}
	
	public Q00182_NewRecruits()
	{
		super(182, Q00182_NewRecruits.class.getSimpleName(), "New Recruits");
		StartNpcs(_kekropus);
		TalkNpcs(_kekropus);
		TalkNpcs(_nornil);
	}
}