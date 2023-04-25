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
package com.l2jserver.scripts.l2jpdt_npcs.MonsterAchievements;

import com.l2jserver.Config;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;

/**
 * @author L2jPDT
 */
public class AchievementEvent extends Quest
{
	// CONFIGS
	private static int NPC_ID = Config.ACHIEVEMENT_NPC_ID;// 50023
	public static boolean USE_ACHIEVEMENT_NPC = Config.USE_ACHIEVEMENT_NPC;
	
	public AchievementEvent()
	{
		super(NPC_ID, AchievementEvent.class.getSimpleName(), "l2jpdt_npcs");
		
		if (USE_ACHIEVEMENT_NPC)
		{
			FirstTalkNpcs(NPC_ID);
			StartNpcs(NPC_ID);
			TalkNpcs(NPC_ID);
			AchievementData.loadData();
			KillNpcs(AchievementData.ACHIEVEMENT.keySet());
			_log.info("L2jPDT: Monsters Achievements NPC is enabled, NPC loaded.");
		}
		else
		{
			_log.info("L2jPDT: Monsters Achievements NPC is disabled, NPC will not work.");
		}
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String html = HtmCache.getInstance().getHtm(player, "data/scripts/l2jpdt_npcs/MonsterAchievements/50023.htm");
		html = html.replace("%player%", player.getName());
		return html;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String[] events = event.split(" ");
		switch (events[0].toLowerCase())
		{
			case "back":
			{
				return this.onFirstTalk(npc, player);
			}
			case "lists":
			{
				return AchievementHtmlUtil.buildLists(player);
			}
			case "list":
			{
				int minLv = Integer.parseInt(events[1]);
				int maxLv = Integer.parseInt(events[2]);
				return AchievementHtmlUtil.buildNpcPages(player, minLv, maxLv);
			}
			case "detail":
			{
				int mobId = Integer.parseInt(events[1]);
				int minLv = Integer.parseInt(events[2]);
				int maxLv = Integer.parseInt(events[3]);
				return AchievementHtmlUtil.buildDetail(player, mobId, minLv, maxLv);
			}
			case "claim":
			{
				int mobId = Integer.parseInt(events[1]);
				int stage = Integer.parseInt(events[2]);
				AchievementData.ACHIEVEMENT.get(mobId).takeRewards(player, stage);
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		int mobId = npc.getNpcId();
		Achievement achievement = AchievementData.ACHIEVEMENT.get(mobId);
		achievement.checkProgress(killer);
		return super.onKill(npc, killer, isPet);
	}
}