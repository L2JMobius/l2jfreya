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

import java.util.Calendar;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.util.Rnd;

/**
 * @author L2jPDT
 */
public class Q00461_RumbleInTheBase extends Quest
{
	// NPC
	public static final int Stan = 30200;
	public static final int[] Monsters =
	{
		22780,
		22781,
		22782,
		2278,
		22784,
		22785,
		18908
	};
	
	// Item
	public static final int ShinySalmon = 15503;
	public static final int ShoesStringOfSelMahum = 16382;
	
	// Reset
	private static final int ResetHour = 6;
	private static final int ResetMin = 30;
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(Q00461_RumbleInTheBase.class.getSimpleName());
		
		if (st == null)
		{
			return htmltext;
		}
		
		if (event.equalsIgnoreCase("30200-05.htm"))
		{
			st.set("cond", "1");
			st.setState(State.STARTED);
			st.playSound("ItemSound.quest_accept");
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		QuestState st = player.getQuestState(Q00461_RumbleInTheBase.class.getSimpleName());
		QuestState prev = player.getQuestState(Q00252_ItSmellsDelicious.class.getSimpleName());
		if (st == null)
		{
			return htmltext;
		}
		
		switch (st.getState())
		{
			case State.CREATED:
				if ((player.getLevel() >= 82) && (prev != null) && prev.isCompleted())
				{
					htmltext = "30200-01.htm";
				}
				else
				{
					htmltext = "30200-02.htm";
				}
				break;
			case State.STARTED:
				if (st.getInt("cond") == 1)
				{
					htmltext = "30200-06.html";
				}
				else
				{
					st.takeItems(ShinySalmon, -1);
					st.takeItems(ShoesStringOfSelMahum, -1);
					st.addExpAndSp(224784, 342528);
					st.playSound("ItemSound.quest_finish");
					htmltext = "30200-07.html";
					st.unset("cond");
					st.exitQuest(false);
					
					Calendar time = Calendar.getInstance();
					time.set(Calendar.MINUTE, ResetMin);
					if (time.get(Calendar.HOUR_OF_DAY) >= ResetHour)
					{
						time.add(Calendar.DATE, 1);
					}
					time.set(Calendar.HOUR_OF_DAY, ResetHour);
					st.set("time", String.valueOf(time.getTimeInMillis()));
				}
				break;
			case State.COMPLETED:
				Long time = Long.parseLong(st.get("time"));
				if (time > System.currentTimeMillis())
				{
					htmltext = "30200-03.htm";
				}
				else
				{
					st.setState(State.CREATED);
					if ((player.getLevel() >= 82) && (prev != null) && (prev.getState() == State.COMPLETED))
					{
						htmltext = "30200-01.htm";
					}
					else
					{
						htmltext = "30200-02.htm";
					}
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		L2PcInstance partyMember = getRandomPartyMember(player, "1");
		if (partyMember == null)
		{
			return null;
		}
		final QuestState st = partyMember.getQuestState(Q00461_RumbleInTheBase.class.getSimpleName());
		
		int chance = Rnd.get(1000);
		boolean giveItem = false;
		
		switch (npc.getNpcId())
		{
			case 22780:
				if (chance < 581)
				{
					giveItem = true;
				}
				break;
			case 22781:
				if (chance < 772)
				{
					giveItem = true;
				}
				break;
			case 22782:
				if (chance < 581)
				{
					giveItem = true;
				}
				break;
			case 22783:
				if (chance < 563)
				{
					giveItem = true;
				}
				break;
			case 22784:
				if (chance < 581)
				{
					giveItem = true;
				}
				break;
			case 22785:
				if (chance < 271)
				{
					giveItem = true;
				}
				break;
			case 18908:
				if ((chance < 271) && (st.getQuestItemsCount(ShinySalmon) < 5))
				{
					st.giveItems(ShinySalmon, 1);
					st.playSound("ItemSound.quest_itemget");
				}
				break;
		}
		
		if (giveItem && (st.getQuestItemsCount(ShoesStringOfSelMahum) < 10))
		{
			st.giveItems(ShoesStringOfSelMahum, 1);
			st.playSound("ItemSound.quest_itemget");
		}
		
		if ((st.getQuestItemsCount(ShinySalmon) == 5) && (st.getQuestItemsCount(ShoesStringOfSelMahum) == 10))
		{
			st.set("cond", "2");
			st.playSound("ItemSound.quest_middle");
		}
		return null;
	}
	
	public Q00461_RumbleInTheBase()
	{
		super(461, Q00461_RumbleInTheBase.class.getSimpleName(), "Rumble in the Base");
		StartNpcs(Stan);
		TalkNpcs(Stan);
		KillNpcs(Monsters);
		
		questItemIds = new int[]
		{
			ShinySalmon,
			ShoesStringOfSelMahum
		};
	}
}