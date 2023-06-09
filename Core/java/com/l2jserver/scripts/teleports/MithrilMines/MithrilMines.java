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
package com.l2jserver.scripts.teleports.MithrilMines;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

public class MithrilMines extends Quest
{
	//@formatter:off
	private static final int[][] LOCATIONS =
	{
		{171946,-173352,3440},
		{175499,-181586,-904},
		{173462,-174011,3480},
		{179299,-182831,-224},
		{178591,-184615,360},
		{175499,-181586,-904}
	};
	//@formatter:on
	
	private final static int NPC_ID = 32652;
	
	public MithrilMines()
	{
		super(-1, MithrilMines.class.getSimpleName(), "teleports");
		StartNpcs(NPC_ID);
		FirstTalkNpcs(NPC_ID);
		TalkNpcs(NPC_ID);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		
		int loc = Integer.parseInt(event) - 1;
		if (LOCATIONS.length > loc)
		{
			int x = LOCATIONS[loc][0];
			int y = LOCATIONS[loc][1];
			int z = LOCATIONS[loc][2];
			
			player.teleToLocation(x, y, z);
			st.exitQuest(true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		
		if (npc.isInsideRadius(173147, -173762, Config.INTERACTION_DISTANCE_NPC, true))
		{
			htmltext = "32652-01.htm";
		}
		else if (npc.isInsideRadius(181941, -174614, Config.INTERACTION_DISTANCE_NPC, true))
		{
			htmltext = "32652-02.htm";
		}
		else if (npc.isInsideRadius(179560, -182956, Config.INTERACTION_DISTANCE_NPC, true))
		{
			htmltext = "32652-03.htm";
		}
		
		return htmltext;
	}
}