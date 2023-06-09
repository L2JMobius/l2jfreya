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
package com.l2jserver.scripts.hellbound.Kanaf;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.util.Rnd;

/**
 * @author GKR
 */
public class Kanaf extends Quest
{
	private static final int KANAF = 32346;
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("info"))
		{
			return "32346-0" + Rnd.get(1, 3) + ".htm";
		}
		
		return null;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		return "32346-01.htm";
	}
	
	public Kanaf()
	{
		super(-1, Kanaf.class.getSimpleName(), "hellbound");
		StartNpcs(KANAF);
		TalkNpcs(KANAF);
		FirstTalkNpcs(KANAF);
		TalkNpcs(KANAF);
	}
}
