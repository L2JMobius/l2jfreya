package com.l2jserver.scripts.village_master.OrcOccupationChange2;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.base.Race;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.util.Util;

import com.l2jserver.scripts.L2AttackableAIScript;

public class OrcOccupationChange2 extends L2AttackableAIScript
{
	// NPCs
	private static int[] NPCS =
	{
		30513,
		30681,
		30704,
		30865,
		30913,
		31288,
		31326,
		31977
	};
	
	// Items
	private static int MARK_OF_CHALLENGER = 2627;
	private static int MARK_OF_PILGRIM = 2721;
	private static int MARK_OF_DUELIST = 2762;
	private static int MARK_OF_WARSPIRIT = 2879;
	private static int MARK_OF_GLORY = 3203;
	private static int MARK_OF_CHAMPION = 3276;
	private static int MARK_OF_LORD = 3390;
	
	private static int[][] CLASSES =
	{
		{
			48,
			47,
			16,
			17,
			18,
			19,
			MARK_OF_CHALLENGER,
			MARK_OF_GLORY,
			MARK_OF_DUELIST
		},
		{
			46,
			45,
			20,
			21,
			22,
			23,
			MARK_OF_CHALLENGER,
			MARK_OF_GLORY,
			MARK_OF_CHAMPION
		},
		{
			51,
			50,
			24,
			25,
			26,
			27,
			MARK_OF_PILGRIM,
			MARK_OF_GLORY,
			MARK_OF_LORD
		},
		{
			52,
			50,
			28,
			29,
			30,
			31,
			MARK_OF_PILGRIM,
			MARK_OF_GLORY,
			MARK_OF_WARSPIRIT
		}
	};
	
	public OrcOccupationChange2()
	{
		super(-1, OrcOccupationChange2.class.getSimpleName(), "village_master");
		StartNpcs(NPCS);
		TalkNpcs(NPCS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return getNoQuestMsg(player);
		}
		
		if (Util.isDigit(event))
		{
			int i = Integer.valueOf(event);
			final ClassId cid = player.getClassId();
			if ((cid.getRace() == Race.Orc) && (cid.getId() == CLASSES[i][1]))
			{
				int suffix;
				final boolean item1 = st.hasQuestItems(CLASSES[i][6]);
				final boolean item2 = st.hasQuestItems(CLASSES[i][7]);
				final boolean item3 = st.hasQuestItems(CLASSES[i][8]);
				if (player.getLevel() < 40)
				{
					suffix = (!item1 || !item2 || !item3) ? CLASSES[i][2] : CLASSES[i][3];
				}
				else
				{
					if (!item1 || !item2 || !item3)
					{
						suffix = CLASSES[i][4];
					}
					else
					{
						suffix = CLASSES[i][5];
						st.takeItems(CLASSES[i][6], -1);
						st.takeItems(CLASSES[i][7], -1);
						st.takeItems(CLASSES[i][8], -1);
						playSound(player, QuestSound.ITEMSOUND_QUEST_FANFARE_2);
						player.setClassId(CLASSES[i][0]);
						player.setBaseClass(CLASSES[i][0]);
						player.broadcastUserInfo();
						st.exitQuest(false);
					}
				}
				event = "30513-" + suffix + ".htm";
			}
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
			st = newQuestState(player);
		}
		
		if (player.isSubClassActive())
		{
			return htmltext;
		}
		
		final ClassId cid = player.getClassId();
		if (cid.getRace() == Race.Orc)
		{
			switch (cid)
			{
				case orcMonk:
				{
					htmltext = "30513-01.htm";
					break;
				}
				case orcRaider:
				{
					htmltext = "30513-05.htm";
					break;
				}
				case orcShaman:
				{
					htmltext = "30513-09.htm";
					break;
				}
				default:
				{
					if (cid.level() == 0)
					{
						htmltext = "30513-33.htm";
					}
					else if (cid.level() >= 2)
					{
						htmltext = "30513-32.htm";
					}
					else
					{
						htmltext = "30513-34.htm";
					}
				}
			}
		}
		else
		{
			htmltext = "30513-34.htm";
		}
		return htmltext;
	}
}