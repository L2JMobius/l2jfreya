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

/**
 * @author L2jPDT
 */
public class Q00646_SignsOfRevolt extends Quest
{
	// NPC
	private static final int TORRANT = 32016;
	// Misc
	private static final int MIN_LEVEL = 80;
	
	public Q00646_SignsOfRevolt()
	{
		super(646, Q00646_SignsOfRevolt.class.getSimpleName(), "Signs of Revolt");
		StartNpcs(TORRANT);
		TalkNpcs(TORRANT);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if (st != null)
		{
			st.exitQuest(true);
		}
		return (player.getLevel() >= MIN_LEVEL) ? "32016-01.html" : "32016-02.html";
	}
}