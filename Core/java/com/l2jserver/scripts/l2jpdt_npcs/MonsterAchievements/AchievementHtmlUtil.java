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

import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;
import com.l2jserver.util.StringUtil;

/**
 * @author YuKun (L2jPDT)
 */
public class AchievementHtmlUtil
{
	static String NPC_BUTTON_TEMPLATE = "<tr><td align=\"center\"><button value=\"%name% Lv.%level%\" action=\"bypass -h Quest AchievementEvent detail %id% %minLv% %maxLv%\" width=200 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
	static String REWARD_BUTTON_TEMPLATE = "<button value=\"Claim Reward for Stage %stage%\" action=\"bypass -h Quest AchievementEvent claim %id% %index%\" width=\"200\" height=\"25\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	
	public static String buildNpcButton(L2NpcTemplate template, int minLv, int maxLv)
	{
		int npcId = template.idTemplate;
		//@formatter:off 
		return NPC_BUTTON_TEMPLATE
			.replace("%name%", template.name)
			.replace("%level%", Integer.toString(template.level))
			.replace("%id%", Integer.toString(npcId))
			.replace("%minLv%", Integer.toString(minLv))
			.replace("%maxLv%", Integer.toString(maxLv));
		//@formatter:on 
	}
	
	public static String buildLists(L2PcInstance player)
	{
		String htm = HtmCache.getInstance().getHtm(player, "data/scripts/l2jpdt_npcs/MonsterAchievements/50023-list.htm");
		htm = htm.replace("%name%", player.getName());
		return htm;
	}
	
	public static String buildDetail(L2PcInstance player, int mobId, int minLv, int maxLv)
	{
		Achievement achievement = AchievementData.ACHIEVEMENT.get(mobId);
		L2NpcTemplate npcTemplate = NpcTable.getInstance().getTemplate(mobId);
		String htm = HtmCache.getInstance().getHtm(player, "data/scripts/l2jpdt_npcs/MonsterAchievements/50023-detail.htm");
		int currentProgress = achievement.getCurrentProgress(player);
		int maxProgress = achievement.getMaxProgerss(player);
		int stage = achievement.getStage(currentProgress);
		htm = htm.replace("%name%", npcTemplate.getName());
		htm = htm.replace("%level%", Integer.toString(npcTemplate.level));
		htm = htm.replace("%location%", achievement.huntLocation);
		htm = htm.replace("%currentCount%", Integer.toString(currentProgress));
		htm = htm.replace("%maxCount%", Integer.toString(maxProgress));
		htm = htm.replace("%stage%", Integer.toString(stage + 1));
		htm = htm.replace("%bar%", getFoodGauge(250, currentProgress, maxProgress, true));
		htm = htm.replace("%minLv%", Integer.toString(minLv));
		htm = htm.replace("%maxLv%", Integer.toString(maxLv));
		int rewardIndex = achievement.availableAdward(player);
		if (rewardIndex != -1)
		{
			htm = htm.replace("%rewardsButton%", REWARD_BUTTON_TEMPLATE.replace("%id%", Integer.toString(mobId)).replace("%stage%", Integer.toString(rewardIndex + 1)).replace("%index%", Integer.toString(rewardIndex)));
		}
		else
		{
			htm = htm.replace("%rewardsButton%", "");
		}
		return htm;
	}
	
	public static String buildNpcPages(L2PcInstance player, int minLv, int maxLv)
	{
		StringBuilder npcButtons = new StringBuilder(1024);
		//@formatter:off 
		AchievementData.ACHIEVEMENT.values().stream().filter(a ->
		{
			L2NpcTemplate template = NpcTable.getInstance().getTemplate(a.mobId);
			return (template.level >= minLv) && (template.level <= maxLv);
		})
		.map(a -> NpcTable.getInstance().getTemplate(a.mobId))
		.sorted((n1,n2)->Integer.compare(n1.level, n2.level))
		.forEach(n-> npcButtons.append(buildNpcButton(n,minLv,maxLv)));
		//@formatter:on
		String htm = HtmCache.getInstance().getHtm(player, "data/scripts/l2jpdt_npcs/MonsterAchievements/50023-level.htm");
		htm = htm.replace("%monsters%", npcButtons);
		htm = htm.replace("%name%", player.getName());
		return htm;
	}
	
	public static String getFoodGauge(int width, long current, long max, boolean displayAsPercentage)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_Food_Bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_Food_Center", 17, -13);
	}
	
	private static String getGauge(int width, long current, long max, boolean displayAsPercentage, String backgroundImage, String image, long imageHeight, long top)
	{
		current = Math.min(current, max);
		final StringBuilder sb = new StringBuilder();
		StringUtil.append(sb, "<table width=", String.valueOf(width), " cellpadding=0 cellspacing=0><tr><td background=\"" + backgroundImage + "\">");
		StringUtil.append(sb, "<img src=\"" + image + "\" width=", String.valueOf((long) (((double) current / max) * width)), " height=", String.valueOf(imageHeight), ">");
		StringUtil.append(sb, "</td></tr><tr><td align=center><table cellpadding=0 cellspacing=", String.valueOf(top), "><tr><td>");
		if (displayAsPercentage)
		{
			StringUtil.append(sb, "<table cellpadding=0 cellspacing=2><tr><td>", String.format("%.2f%%", ((double) current / max) * 100), "</td></tr></table>");
		}
		else
		{
			final String tdWidth = String.valueOf((width - 10) / 2);
			StringUtil.append(sb, "<table cellpadding=0 cellspacing=0><tr><td width=" + tdWidth + " align=right>", String.valueOf(current), "</td>");
			StringUtil.append(sb, "<td width=10 align=center>/</td><td width=" + tdWidth + ">", String.valueOf(max), "</td></tr></table>");
		}
		StringUtil.append(sb, "</td></tr></table></td></tr></table>");
		return sb.toString();
	}
}
