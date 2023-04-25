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
package com.l2jserver.scripts.quests;

import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.util.Util;

/**
 * @author L2jPDT
 */
public final class Q00123_TheLeaderAndTheFollower extends Quest
{
	// NPC
	private static final int HEAD_BLACKSMITH_NEWYEAR = 31961;
	// Items
	private static final int CRYSTAL_D = 1458;
	private static final int BRUIN_LIZARDMAN_BLOOD = 8549;
	private static final int PICOT_ARANEIDS_LEG = 8550;
	// Reward
	private static final int CLAN_OATH_HELM = 7850;
	private static final int CLAN_OATH_ARMOR = 7851;
	private static final int CLAN_OATH_GAUNTLETS_HEAVY_ARMOR = 7852;
	private static final int CLAN_OATH_SABATON_HEAVY_ARMOR = 7853;
	private static final int CLAN_OATH_BRIGANDINE = 7854;
	private static final int CLAN_OATH_LEATHER_GLOVES_LIGHT_ARMOR = 7855;
	private static final int CLAN_OATH_BOOTS_LIGHT_ARMOR = 7856;
	private static final int CLAN_OATH_AKETON = 7857;
	private static final int CLAN_OATH_PADDED_GLOVES_ROBE = 7858;
	private static final int CLAN_OATH_SANDALS_ROBE = 7859;
	// Quest Monster
	private static final int BRUIN_LIZARDMAN = 27321;
	private static final int PICOT_ARANEID = 27322;
	// Misc
	private static final int MIN_LEVEL = 19;
	private static final int CRYSTAL_COUNT_1 = 922;
	private static final int CRYSTAL_COUNT_2 = 771;
	
	public Q00123_TheLeaderAndTheFollower()
	{
		super(123, Q00123_TheLeaderAndTheFollower.class.getSimpleName(), "The Leader And The Follower");
		StartNpcs(HEAD_BLACKSMITH_NEWYEAR);
		TalkNpcs(HEAD_BLACKSMITH_NEWYEAR);
		KillNpcs(BRUIN_LIZARDMAN, PICOT_ARANEID);
		registerQuestItems(BRUIN_LIZARDMAN_BLOOD, PICOT_ARANEIDS_LEG);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = null;
		// Manage Sponsor's quest events.
		if (player.getApprentice() > 0)
		{
			final L2PcInstance apprentice = L2World.getInstance().getPlayer(player.getApprentice());
			if (apprentice == null)
			{
				return htmltext;
			}
			
			final QuestState q123 = apprentice.getQuestState(Q00123_TheLeaderAndTheFollower.class.getSimpleName());
			switch (event)
			{
				case "sponsor":
				{
					if (!Util.checkIfInRange(1500, npc, apprentice, true))
					{
						htmltext = "31961-09.html";
					}
					else
					{
						if ((q123 == null) || (!q123.isMemoState(2) && !q123.isMemoState(3)))
						{
							htmltext = "31961-14.html";
						}
						else if (q123.isMemoState(2))
						{
							htmltext = "31961-08.html";
						}
						else if (q123.isMemoState(3))
						{
							htmltext = "31961-12.html";
						}
					}
					break;
				}
				case "31961-10.html":
				{
					if (Util.checkIfInRange(1500, npc, apprentice, true) && (q123 != null) && q123.isMemoState(2))
					{
						switch (q123.getMemoStateEx(1))
						{
							case 1:
							{
								if (getQuestItemsCount(player, CRYSTAL_D) >= CRYSTAL_COUNT_1)
								{
									takeItems(player, CRYSTAL_D, CRYSTAL_COUNT_1);
									q123.setMemoState(3);
									q123.setCond(6, true);
									htmltext = event;
								}
								else
								{
									htmltext = "31961-11.html";
								}
								break;
							}
							case 2:
							case 3:
							{
								if (getQuestItemsCount(player, CRYSTAL_D) >= CRYSTAL_COUNT_2)
								{
									takeItems(player, CRYSTAL_D, CRYSTAL_COUNT_2);
									q123.setMemoState(3);
									q123.setCond(6, true);
									htmltext = event;
								}
								else
								{
									htmltext = "31961-11a.html";
								}
								break;
							}
						}
					}
				}
					break;
			}
			return htmltext;
		}
		
		final QuestState qs = getQuestState(player, false);
		if (qs == null)
		{
			return null;
		}
		
		switch (event)
		{
			case "31961-03.htm":
			{
				if (qs.isCreated())
				{
					qs.startQuest();
					qs.setMemoState(1);
					htmltext = event;
				}
				break;
			}
			case "31961-05a.html":
			case "31961-05b.html":
			case "31961-05c.html":
			case "31961-05g.html":
			{
				htmltext = event;
				break;
			}
			case "31961-05d.html":
			{
				if (qs.isMemoState(1) && (getQuestItemsCount(player, BRUIN_LIZARDMAN_BLOOD) >= 10))
				{
					takeItems(player, BRUIN_LIZARDMAN_BLOOD, -1);
					qs.setMemoState(2);
					qs.setMemoStateEx(1, 1);
					qs.setCond(3, true);
					htmltext = event;
				}
				break;
			}
			case "31961-05e.html":
			{
				if (qs.isMemoState(1) && (getQuestItemsCount(player, BRUIN_LIZARDMAN_BLOOD) >= 10))
				{
					takeItems(player, BRUIN_LIZARDMAN_BLOOD, -1);
					qs.setMemoState(2);
					qs.setMemoStateEx(1, 2);
					qs.setCond(4, true);
					htmltext = event;
				}
				break;
			}
			case "31961-05f.html":
			{
				if (qs.isMemoState(1) && (getQuestItemsCount(player, BRUIN_LIZARDMAN_BLOOD) >= 10))
				{
					takeItems(player, BRUIN_LIZARDMAN_BLOOD, -1);
					qs.setMemoState(2);
					qs.setMemoStateEx(1, 3);
					qs.setCond(5, true);
					htmltext = event;
				}
				break;
			}
			
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && qs.isStarted())
		{
			switch (npc.getNpcId())
			{
				case BRUIN_LIZARDMAN:
				{
					if (qs.isMemoState(1))
					{
						if (giveItemRandomly(killer, npc, BRUIN_LIZARDMAN_BLOOD, 1, 10, 7, true))
						{
							qs.setCond(2);
						}
					}
					break;
				}
				case PICOT_ARANEID:
				{
					if (qs.isMemoState(4))
					{
						if (killer.getSponsor() > 0)
						{
							final L2PcInstance c0 = L2World.getInstance().getPlayer(killer.getSponsor());
							if ((c0 != null) && Util.checkIfInRange(1500, npc, c0, true))
							{
								if (giveItemRandomly(killer, npc, PICOT_ARANEIDS_LEG, 1, 8, 7, true))
								{
									qs.setCond(8);
								}
							}
						}
					}
					break;
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState qs = getQuestState(player, true);
		final QuestState q118 = player.getQuestState(Q00118_ToLeadAndBeLed.class.getSimpleName());
		String htmltext = getNoQuestMsg(player);
		
		switch (qs.getState())
		{
			case State.CREATED:
			{
				if ((q118 != null) && q118.isStarted())
				{
					htmltext = "31961-02b.htm";
				}
				else if ((q118 != null) && q118.isCompleted())
				{
					htmltext = "31961-02a.html";
				}
				else if ((player.getLevel() >= MIN_LEVEL) && (player.getPledgeType() == -1) && (player.getSponsor() > 0))
				{
					htmltext = "31961-01.htm";
				}
				else
				{
					htmltext = "31961-02.htm";
				}
				break;
			}
			case State.STARTED:
			{
				if (qs.isMemoState(1))
				{
					if (getQuestItemsCount(player, BRUIN_LIZARDMAN_BLOOD) < 10)
					{
						htmltext = "31961-04.html";
					}
					else
					{
						htmltext = "31961-05.html";
					}
				}
				else if (qs.isMemoState(2))
				{
					if (player.getSponsor() == 0)
					{
						if (qs.getMemoStateEx(1) == 1)
						{
							htmltext = "31961-06a.html";
						}
						else if (qs.getMemoStateEx(1) == 2)
						{
							htmltext = "31961-06b.html";
						}
						else if (qs.getMemoStateEx(1) == 3)
						{
							htmltext = "31961-06c.html";
						}
					}
					else
					{
						final L2PcInstance c0 = L2World.getInstance().getPlayer(player.getSponsor());
						if ((c0 != null) && Util.checkIfInRange(1500, npc, c0, true))
						{
							htmltext = "31961-07.html";
						}
						else
						{
							if (qs.getMemoStateEx(1) == 1)
							{
								htmltext = "31961-06.html";
							}
							else if (qs.getMemoStateEx(1) == 2)
							{
								htmltext = "31961-06d.html";
							}
							else if (qs.getMemoStateEx(1) == 3)
							{
								htmltext = "31961-06e.html";
							}
						}
					}
				}
				else if (qs.isMemoState(3))
				{
					qs.setMemoState(4);
					qs.setCond(7, true);
					htmltext = "31961-15.html";
				}
				else if (qs.isMemoState(4))
				{
					if (getQuestItemsCount(player, PICOT_ARANEIDS_LEG) < 8)
					{
						htmltext = "31961-16.html";
					}
					else
					{
						if (qs.getMemoStateEx(1) == 1)
						{
							giveItems(player, CLAN_OATH_HELM, 1);
							giveItems(player, CLAN_OATH_ARMOR, 1);
							giveItems(player, CLAN_OATH_GAUNTLETS_HEAVY_ARMOR, 1);
							giveItems(player, CLAN_OATH_SABATON_HEAVY_ARMOR, 1);
							takeItems(player, PICOT_ARANEIDS_LEG, -1);
						}
						else if (qs.getMemoStateEx(1) == 2)
						{
							giveItems(player, CLAN_OATH_HELM, 1);
							giveItems(player, CLAN_OATH_BRIGANDINE, 1);
							giveItems(player, CLAN_OATH_LEATHER_GLOVES_LIGHT_ARMOR, 1);
							giveItems(player, CLAN_OATH_BOOTS_LIGHT_ARMOR, 1);
							takeItems(player, PICOT_ARANEIDS_LEG, -1);
						}
						else if (qs.getMemoStateEx(1) == 3)
						{
							giveItems(player, CLAN_OATH_HELM, 1);
							giveItems(player, CLAN_OATH_AKETON, 1);
							giveItems(player, CLAN_OATH_PADDED_GLOVES_ROBE, 1);
							giveItems(player, CLAN_OATH_SANDALS_ROBE, 1);
							takeItems(player, PICOT_ARANEIDS_LEG, -1);
						}
						qs.exitQuest(false, true);
						htmltext = "31961-17.html";
					}
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		return htmltext;
	}
}