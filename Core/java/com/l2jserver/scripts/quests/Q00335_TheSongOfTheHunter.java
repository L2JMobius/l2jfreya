package com.l2jserver.scripts.quests;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

/**
 * @author L2jPDT
 */
public class Q00335_TheSongOfTheHunter extends Quest
{
	// NPCs
	private static final int Grey = 30744;
	private static final int Tor = 30745;
	private static final int Cybellin = 30746;
	// Mobs
	private static final int Breka_Orc_Warrior = 20271;
	private static final int Windsus = 20553;
	private static final int Tarlk_Bugbear_Warrior = 20571;
	private static final int Breka_Overlord_Haka = 27140;
	private static final int Breka_Overlord_Jaka = 27141;
	private static final int Breka_Overlord_Marka = 27142;
	private static final int Windsus_Aleph = 27143;
	private static final int Tarlk_Raider_Athu = 27144;
	private static final int Tarlk_Raider_Lanka = 27145;
	private static final int Tarlk_Raider_Triska = 27146;
	private static final int Tarlk_Raider_Motura = 27147;
	private static final int Tarlk_Raider_Kalath = 27148;
	// Items
	private static final short ADENA = 57;
	private static final short Cybellins_Dagger = 3471;
	private static final short _1st_Circle_Hunter_License = 3692;
	private static final short _2nd_Circle_Hunter_License = 3693;
	private static final short Laurel_Leaf_Pin = 3694;
	private static final short _1st_Test_Instructions = 3695;
	private static final short _2nd_Test_Instructions = 3696;
	private static final short Cybellins_Request = 3697;
	private static final short Guardian_Basilisk_Scale = 3709;
	private static final short Karut_Weed = 3710;
	private static final short Hakas_Head = 3711;
	private static final short Jakas_Head = 3712;
	private static final short Markas_Head = 3713;
	private static final short Windsus_Aleph_Skin = 3714;
	private static final short Indigo_Runestone = 3715;
	private static final short Sporesea_Seed = 3716;
	private static final short Timak_Orc_Totem = 3717;
	private static final short Trisalim_Silk = 3718;
	private static final short Ambrosius_Fruit = 3719;
	private static final short Balefire_Crystal = 3720;
	private static final short Imperial_Arrowhead = 3721;
	private static final short Athus_Head = 3722;
	private static final short Lankas_Head = 3723;
	private static final short Triskas_Head = 3724;
	private static final short Moturas_Head = 3725;
	private static final short Kalaths_Head = 3726;
	
	private static final int[] q_blood_crystal =
	{
		3708,
		3698,
		3699,
		3700,
		3701,
		3702,
		3703,
		3704,
		3705,
		3706,
		3707
	};
	private static final int[] q_blood_crystal_lizardmen =
	{
		20578,
		20579,
		20580,
		20581,
		20582,
		20641,
		20642,
		20643,
		20644,
		20645
	};
	private static final int[][][] Items_1st_Circle =
	{
		{
			{
				Guardian_Basilisk_Scale
			},
			{
				40
			},
			{
				20550,
				75
			}
		},
		{
			{
				Karut_Weed
			},
			{
				20
			},
			{
				20581,
				50
			}
		},
		{
			{
				Hakas_Head,
				Jakas_Head,
				Markas_Head
			},
			{
				3
			}
		},
		{
			{
				Windsus_Aleph_Skin
			},
			{
				1
			},
			{
				Windsus_Aleph,
				100
			}
		},
		{
			{
				Indigo_Runestone
			},
			{
				20
			},
			{
				20563,
				50
			},
			{
				20565,
				50
			}
		},
		{
			{
				Sporesea_Seed
			},
			{
				30
			},
			{
				20555,
				70
			}
		}
	};
	private static final int[][][] Items_2nd_Circle =
	{
		{
			{
				Timak_Orc_Totem
			},
			{
				20
			},
			{
				20586,
				50
			}
		},
		{
			{
				Trisalim_Silk
			},
			{
				20
			},
			{
				20560,
				50
			},
			{
				20561,
				50
			}
		},
		{
			{
				Ambrosius_Fruit
			},
			{
				30
			},
			{
				20591,
				75
			},
			{
				20597,
				75
			}
		},
		{
			{
				Balefire_Crystal
			},
			{
				20
			},
			{
				20675,
				50
			}
		},
		{
			{
				Imperial_Arrowhead
			},
			{
				20
			},
			{
				20660,
				50
			}
		},
		{
			{
				Athus_Head,
				Lankas_Head,
				Triskas_Head,
				Moturas_Head,
				Kalaths_Head
			},
			{
				5
			}
		}
	};
	private static final Request[] Requests1 =
	{
		new Request(3727, 3769, 40, 2090, "C: 40 Totems of Kadesh").addDrop(20578, 80).addDrop(20579, 83),
		new Request(3728, 3770, 50, 6340, "C: 50 Jade Necklaces of Timak").addDrop(20586, 89).addDrop(20588, 100),
		new Request(3729, 3771, 50, 9480, "C: 50 Enchanted Golem Shards").addDrop(20565, 100),
		new Request(3730, 3772, 30, 9110, "C: 30 Pieces Monster Eye Meat").addDrop(20556, 50),
		new Request(3731, 3773, 40, 8690, "C: 40 Eggs of Dire Wyrm").addDrop(20557, 80),
		new Request(3732, 3774, 100, 9480, "C: 100 Claws of Guardian Basilisk").addDrop(20550, 150),
		new Request(3733, 3775, 50, 11280, "C: 50 Revenant Chains").addDrop(20552, 100),
		new Request(3734, 3776, 30, 9640, "C: 30 Windsus Tusks").addDrop(Windsus, 50),
		new Request(3735, 3777, 100, 9180, "C: 100 Skulls of Grandis").addDrop(20554, 200),
		new Request(3736, 3778, 50, 5160, "C: 50 Taik Obsidian Amulets").addDrop(20631, 100).addDrop(20632, 93),
		new Request(3737, 3779, 30, 3140, "C: 30 Heads of Karul Bugbear").addDrop(20600, 50),
		new Request(3738, 3780, 40, 3160, "C: 40 Ivory Charms of Tamlin").addDrop(20601, 62).addDrop(20602, 80),
		new Request(3739, 3781, 1, 6370, "B: Situation Preparation - Leto Chief").addSpawn(20582, 27157, 10).addDrop(27157, 100), // leto_chief_narak
		new Request(3740, 3782, 50, 19080, "B: 50 Enchanted Gargoyle Horns").addDrop(20567, 50),
		new Request(3741, 3783, 50, 17730, "B: 50 Coiled Serpent Totems").addDrop(20269, 93).addDrop(Breka_Orc_Warrior, 100),
		new Request(3742, 3784, 1, 5790, "B: Situation Preparation - Sorcerer Catch of Leto").addSpawn(20581, 27156, 10).addDrop(27156, 100), // leto_shaman_ketz
		new Request(3743, 3785, 1, 8560, "B: Situation Preparation - Timak Raider Kaikee").addSpawn(20586, 27158, 10).addDrop(27158, 100), // timak_raider_kaikee
		new Request(3744, 3786, 30, 8320, "B: 30 Kronbe Venom Sacs").addDrop(20603, 50),
		new Request(3745, 3787, 30, 30310, "A: 30 Eva's Charm").addDrop(20562, 50),
		new Request(3746, 3788, 1, 27540, "A: Titan's Tablet").addSpawn(20554, 27160, 10).addDrop(27160, 100), // grandis_chief_gok_magok
		new Request(3747, 3789, 1, 20560, "A: Book of Shunaiman").addSpawn(20600, 27164, 10).addDrop(27164, 100)
	}; // karul_chief_orooto
	private static final Request[] Requests2 =
	{
		new Request(3748, 3790, 40, 6850, "C: 40 Rotting Tree Spores").addDrop(20558, 67),
		new Request(3749, 3791, 40, 7250, "C: 40 Trisalim Venom Sacs").addDrop(20560, 66).addDrop(20561, 75),
		new Request(3750, 3792, 50, 7160, "C: 50 Totems of Taik Orc").addDrop(20633, 53).addDrop(20634, 99),
		new Request(3751, 3793, 40, 6580, "C: 40 Harit Barbed Necklaces").addDrop(20641, 88).addDrop(20642, 88).addDrop(20643, 91),
		new Request(3752, 3794, 20, 10100, "C: 20 Coins of Ancient Empire").addDrop(20661, 50).addSpawn(20661, 27149, 5).addDrop(20662, 52).addSpawn(20662, 27149, 5).addDrop(27149, 300), // gremlin_filcher
		new Request(3753, 3795, 30, 13000, "C: 30 Skins of Farkran").addDrop(20667, 90),
		new Request(3754, 3796, 40, 7660, "C: 40 Tempest Shards").addDrop(20589, 49).addSpawn(20589, 27149, 5).addDrop(27149, 500), // gremlin_filcher
		new Request(3755, 3797, 40, 7660, "C: 40 Tsunami Shards").addDrop(20590, 51).addSpawn(20590, 27149, 5).addDrop(27149, 500), // gremlin_filcher
		new Request(3756, 3798, 40, 11260, "C: 40 Manes of Pan Ruem").addDrop(20592, 80).addDrop(20598, 100),
		new Request(3757, 3799, 40, 7660, "C: 40 Hamadryad Shard").addDrop(20594, 64).addSpawn(20594, 27149, 5).addDrop(27149, 500), // gremlin_filcher
		new Request(3758, 3800, 30, 8810, "C: 30 Manes of Vanor Silenos").addDrop(20682, 70).addDrop(20683, 85).addDrop(20684, 90),
		new Request(3759, 3801, 30, 7350, "C: 30 Totems of Tarlk Bugbears").addDrop(Tarlk_Bugbear_Warrior, 63),
		new Request(3760, 3802, 1, 8760, "B: Situation Preparation - Overlord Okun of Timak").addSpawn(20588, 27159, 10).addDrop(27159, 100), // timak_overlord_okun
		new Request(3761, 3803, 1, 9380, "B: Situation Preparation - Overlord Kakran of Taik").addSpawn(20634, 27161, 10).addDrop(27161, 100), // taik_overlord_kakran
		new Request(3762, 3804, 40, 17820, "B: 40 Narcissus Soulstones").addDrop(20639, 86).addSpawn(20639, 27149, 5).addDrop(27149, 500), // gremlin_filcher
		new Request(3763, 3805, 20, 17540, "B: 20 Eyes of Deprived").addDrop(20664, 77),
		new Request(3764, 3806, 20, 14160, "B: 20 Unicorn Horns").addDrop(20593, 68).addDrop(20599, 86),
		new Request(3765, 3807, 1, 15960, "B: Golden Mane of Silenos").addSpawn(20686, 27163, 10).addDrop(27163, 100), // vanor_elder_kerunos
		new Request(3766, 3808, 20, 39100, "A: 20 Skulls of Executed Person").addDrop(20659, 73),
		new Request(3767, 3809, 1, 39550, "A: Bust of Travis").addSpawn(20662, 27162, 10).addDrop(27162, 100), // hatar_chieftain_kubel
		new Request(3768, 3810, 10, 41200, "A: 10 Swords of Cadmus").addDrop(20676, 64)
	};
	
	public Q00335_TheSongOfTheHunter()
	{
		super(335, Q00335_TheSongOfTheHunter.class.getSimpleName(), "The Song of the Hunter");
		
		StartNpcs(Grey);
		TalkNpcs(Cybellin);
		TalkNpcs(Tor);
		
		KillNpcs(Breka_Overlord_Haka);
		KillNpcs(Breka_Overlord_Jaka);
		KillNpcs(Breka_Overlord_Marka);
		KillNpcs(Tarlk_Raider_Athu);
		KillNpcs(Tarlk_Raider_Lanka);
		KillNpcs(Tarlk_Raider_Triska);
		KillNpcs(Tarlk_Raider_Motura);
		KillNpcs(Tarlk_Raider_Kalath);
		KillNpcs(q_blood_crystal_lizardmen);
		
		for (int[][] ItemsCond : Items_1st_Circle)
		{
			registerQuestItems(ItemsCond[0]);
			for (int i = 2; i < ItemsCond.length; i++)
			{
				KillNpcs(ItemsCond[i][0]);
			}
		}
		
		for (int[][] ItemsCond : Items_2nd_Circle)
		{
			registerQuestItems(ItemsCond[0]);
			for (int i = 2; i < ItemsCond.length; i++)
			{
				KillNpcs(ItemsCond[i][0]);
			}
		}
		
		for (Request r : Requests1)
		{
			registerQuestItems(r.request_id);
			registerQuestItems(r.request_item);
			for (int id : r.droplist.keySet())
			{
				KillNpcs(id);
			}
			for (int id : r.spawnlist.keySet())
			{
				KillNpcs(id);
			}
		}
		
		for (Request r : Requests2)
		{
			registerQuestItems(r.request_id);
			registerQuestItems(r.request_item);
			for (int id : r.droplist.keySet())
			{
				KillNpcs(id);
			}
			for (int id : r.spawnlist.keySet())
			{
				KillNpcs(id);
			}
		}
		
		registerQuestItems(_1st_Circle_Hunter_License);
		registerQuestItems(_2nd_Circle_Hunter_License);
		registerQuestItems(Laurel_Leaf_Pin);
		registerQuestItems(_1st_Test_Instructions);
		registerQuestItems(_2nd_Test_Instructions);
		registerQuestItems(Cybellins_Request);
		registerQuestItems(Cybellins_Dagger);
		registerQuestItems(q_blood_crystal);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		byte _state = st.getState();
		if (event.equals("30744-03.htm") && (_state == State.CREATED))
		{
			if (st.getQuestItemsCount(_1st_Test_Instructions) == 0)
			{
				st.giveItems(_1st_Test_Instructions, 1);
			}
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound(QuestSound.ITEMSOUND_QUEST_ACCEPT);
		}
		else if (event.equals("30744-09.htm") && (_state == State.STARTED))
		{
			if (GetCurrentRequest(st, Requests1) != null)
			{
				return "30744-09a.htm";
			}
			if (st.getQuestItemsCount(_2nd_Test_Instructions) == 0)
			{
				st.playSound(QuestSound.ITEMSOUND_QUEST_MIDDLE);
				st.giveItems(_2nd_Test_Instructions, 1);
			}
		}
		else if (event.equals("30744-16.htm") && (_state == State.STARTED))
		{
			if (st.getQuestItemsCount(Laurel_Leaf_Pin) >= 20)
			{
				st.giveItems(ADENA, 20000);
				event = "30744-17.htm";
			}
			st.playSound(QuestSound.ITEMSOUND_QUEST_FINISH);
			st.exitQuest(true);
		}
		else if (event.equals("30746-03.htm") && (_state == State.STARTED))
		{
			if ((st.getQuestItemsCount(_1st_Circle_Hunter_License) == 0) && (st.getQuestItemsCount(_2nd_Circle_Hunter_License) == 0))
			{
				return null;
			}
			if (st.getQuestItemsCount(Cybellins_Dagger) == 0)
			{
				st.giveItems(Cybellins_Dagger, 1);
			}
			if (st.getQuestItemsCount(Cybellins_Request) == 0)
			{
				st.giveItems(Cybellins_Request, 1);
			}
			st.takeAllItems(q_blood_crystal);
			st.playSound(QuestSound.ITEMSOUND_QUEST_MIDDLE);
			st.giveItems(q_blood_crystal[1], 1);
		}
		else if (event.equals("30746-06.htm") && (_state == State.STARTED))
		{
			if (!Blood_Crystal2Adena(st, Get_Blood_Crystal_Level(st)))
			{
				return null;
			}
		}
		else if (event.equals("30746-10.htm") && (_state == State.STARTED))
		{
			st.takeItems(Cybellins_Dagger, -1);
			st.takeItems(Cybellins_Request, -1);
			st.takeAllItems(q_blood_crystal);
		}
		else if (event.equals("30745-02.htm") && (_state == State.STARTED))
		{
			if (st.getQuestItemsCount(_2nd_Test_Instructions) > 0)
			{
				return "30745-03.htm";
			}
		}
		else if (event.equals("30745-05b.htm") && (_state == State.STARTED))
		{
			if (st.getQuestItemsCount(Laurel_Leaf_Pin) > 0)
			{
				st.takeItems(Laurel_Leaf_Pin, 1);
			}
			for (Request r : Requests1)
			{
				st.takeItems(r.request_id, -1);
				st.takeItems(r.request_item, -1);
			}
			for (Request r : Requests2)
			{
				st.takeItems(r.request_id, -1);
				st.takeItems(r.request_item, -1);
			}
		}
		else if (event.equals("30745-list1") && (_state == State.STARTED))
		{
			GenList(st);
			return FormatList(st, Requests1);
		}
		else if (event.equals("30745-list2") && (_state == State.STARTED))
		{
			GenList(st);
			return FormatList(st, Requests2);
		}
		else if (event.startsWith("30745-request-") && (_state == State.STARTED))
		{
			event = event.replaceFirst("30745-request-", "");
			int request_id;
			try
			{
				request_id = Integer.valueOf(event);
			}
			catch (Exception e)
			{
				return null;
			}
			if (!isValidRequest(request_id))
			{
				return null;
			}
			st.giveItems(request_id, 1);
			return "30745-" + request_id + ".htm";
		}
		
		return event;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		String htmltext = getNoQuestMsg(talker);
		final QuestState st = getQuestState(talker, true);
		if (st == null)
		{
			return htmltext;
		}
		
		byte _state = st.getState();
		int npcId = npc.getNpcId();
		
		if (_state == State.CREATED)
		{
			if (npcId != Grey)
			{
				return "noquest";
			}
			if (st.getPlayer().getLevel() < 35)
			{
				st.exitQuest(true);
				return "30744-01.htm";
			}
			st.set("cond", "0");
			st.unset("list");
			return "30744-02.htm";
		}
		
		if (_state != State.STARTED)
		{
			return "noquest";
		}
		
		if (npcId == Grey)
		{
			if (st.getQuestItemsCount(_1st_Test_Instructions) > 0)
			{
				if (CalcItemsConds(st, Items_1st_Circle) < 3)
				{
					return "30744-05.htm";
				}
				DelItemsConds(st, Items_1st_Circle);
				st.takeItems(_1st_Test_Instructions, -1);
				st.playSound(QuestSound.ITEMSOUND_QUEST_MIDDLE);
				st.giveItems(_1st_Circle_Hunter_License, 1);
				st.set("cond", "2");
				return "30744-06.htm";
			}
			if (st.getQuestItemsCount(_1st_Circle_Hunter_License) > 0)
			{
				if (st.getPlayer().getLevel() < 45)
				{
					return "30744-07.htm";
				}
				if (st.getQuestItemsCount(_2nd_Test_Instructions) == 0)
				{
					return "30744-08.htm";
				}
			}
			if (st.getQuestItemsCount(_2nd_Test_Instructions) > 0)
			{
				if (CalcItemsConds(st, Items_2nd_Circle) < 3)
				{
					return "30744-11.htm";
				}
				DelItemsConds(st, Items_2nd_Circle);
				st.takeItems(_2nd_Test_Instructions, -1);
				st.takeItems(_1st_Circle_Hunter_License, -1);
				st.playSound(QuestSound.ITEMSOUND_QUEST_MIDDLE);
				st.giveItems(_2nd_Circle_Hunter_License, 1);
				st.set("cond", "3");
				return "30744-12.htm";
			}
			if (st.getQuestItemsCount(_2nd_Circle_Hunter_License) > 0)
			{
				return "30744-14.htm";
			}
		}
		
		if (npcId == Cybellin)
		{
			if ((st.getQuestItemsCount(_1st_Circle_Hunter_License) == 0) && (st.getQuestItemsCount(_2nd_Circle_Hunter_License) == 0))
			{
				return "30746-01.htm";
			}
			if (st.getQuestItemsCount(Cybellins_Request) == 0)
			{
				return "30746-02.htm";
			}
			
			int Blood_Crystal_Level = Get_Blood_Crystal_Level(st);
			if (Blood_Crystal_Level == -1)
			{
				return "30746-08.htm";
			}
			if (Blood_Crystal_Level == 0)
			{
				return "30746-09.htm";
			}
			if (Blood_Crystal_Level == 1)
			{
				return "30746-04.htm";
			}
			if ((Blood_Crystal_Level > 1) && (Blood_Crystal_Level < 10))
			{
				return "30746-05.htm";
			}
			if ((Blood_Crystal_Level == 10) && Blood_Crystal2Adena(st, Blood_Crystal_Level))
			{
				return "30746-05a.htm";
			}
		}
		
		if (npcId == Tor)
		{
			if ((st.getQuestItemsCount(_1st_Circle_Hunter_License) == 0) && (st.getQuestItemsCount(_2nd_Circle_Hunter_License) == 0))
			{
				return "30745-01a.htm";
			}
			if (st.getQuestItemsCount(_1st_Circle_Hunter_License) > 0)
			{
				Request request = GetCurrentRequest(st, Requests1);
				if (request == null)
				{
					if (st.getPlayer().getLevel() < 45)
					{
						return "30745-01b.htm";
					}
					return st.getQuestItemsCount(_2nd_Test_Instructions) > 0 ? "30745-03.htm" : "30745-03a.htm";
				}
				return request.Complete(st) ? "30745-06a.htm" : "30745-05.htm";
			}
			if (st.getQuestItemsCount(_2nd_Circle_Hunter_License) > 0)
			{
				Request request = GetCurrentRequest(st, Requests2);
				if (request == null)
				{
					return "30745-03b.htm";
				}
				return request.Complete(st) ? "30745-06b.htm" : "30745-05.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		QuestState st = killer.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		if (st.getState() != State.STARTED)
		{
			return null;
		}
		int npcId = npc.getNpcId();
		
		int[][][] Items_Circle = null;
		if (st.getQuestItemsCount(_1st_Test_Instructions) > 0)
		{
			Items_Circle = Items_1st_Circle;
		}
		else if (st.getQuestItemsCount(_2nd_Test_Instructions) > 0)
		{
			Items_Circle = Items_2nd_Circle;
		}
		if (Items_Circle != null)
		{
			for (int[][] ItemsCond : Items_Circle)
			{
				for (int i = 2; i < ItemsCond.length; i++)
				{
					if (npcId == ItemsCond[i][0])
					{
						st.dropQuestItems(ItemsCond[0][0], 1, 1, ItemsCond[1][0], ItemsCond[i][1], true);
					}
				}
			}
			if (st.getQuestItemsCount(_1st_Test_Instructions) > 0)
			{
				long Hakas_Head_count = st.getQuestItemsCount(Hakas_Head);
				long Jakas_Head_count = st.getQuestItemsCount(Jakas_Head);
				long Markas_Head_count = st.getQuestItemsCount(Markas_Head);
				if (npcId == Breka_Orc_Warrior)
				{
					if ((Hakas_Head_count == 0) && calcChance(10))
					{
						st.addSpawn(Breka_Overlord_Haka, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
					else if ((Jakas_Head_count == 0) && calcChance(10))
					{
						st.addSpawn(Breka_Overlord_Jaka, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
					else if ((Markas_Head_count == 0) && calcChance(10))
					{
						st.addSpawn(Breka_Overlord_Marka, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
				}
				else if (npcId == Breka_Overlord_Haka)
				{
					if (Hakas_Head_count == 0)
					{
						st.dropQuestItems(Hakas_Head, 1, 1, 1, 100, false);
					}
				}
				else if (npcId == Breka_Overlord_Jaka)
				{
					if (Jakas_Head_count == 0)
					{
						st.dropQuestItems(Jakas_Head, 1, 1, 1, 100, false);
					}
				}
				else if (npcId == Breka_Overlord_Marka)
				{
					if (Markas_Head_count == 0)
					{
						st.dropQuestItems(Markas_Head, 1, 1, 1, 100, false);
					}
				}
				else if (npcId == Windsus)
				{
					if ((st.getQuestItemsCount(Windsus_Aleph_Skin) == 0) && calcChance(10))
					{
						st.addSpawn(Windsus_Aleph, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
				}
			}
			else if (st.getQuestItemsCount(_2nd_Test_Instructions) > 0)
			{
				long Athus_Head_count = st.getQuestItemsCount(Athus_Head);
				long Lankas_Head_count = st.getQuestItemsCount(Lankas_Head);
				long Triskas_Head_count = st.getQuestItemsCount(Triskas_Head);
				long Moturas_Head_count = st.getQuestItemsCount(Moturas_Head);
				long Kalaths_Head_count = st.getQuestItemsCount(Kalaths_Head);
				if (npcId == Tarlk_Bugbear_Warrior)
				{
					if ((Athus_Head_count == 0) && calcChance(10))
					{
						st.addSpawn(Tarlk_Raider_Athu, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
					else if ((Lankas_Head_count == 0) && calcChance(10))
					{
						st.addSpawn(Tarlk_Raider_Lanka, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
					else if ((Triskas_Head_count == 0) && calcChance(10))
					{
						st.addSpawn(Tarlk_Raider_Triska, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
					else if ((Moturas_Head_count == 0) && calcChance(10))
					{
						st.addSpawn(Tarlk_Raider_Motura, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
					else if ((Kalaths_Head_count == 0) && calcChance(10))
					{
						st.addSpawn(Tarlk_Raider_Kalath, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
					}
				}
				else if (npcId == Tarlk_Raider_Athu)
				{
					if (Athus_Head_count == 0)
					{
						st.dropQuestItems(Athus_Head, 1, 1, 1, 100, false);
					}
				}
				else if (npcId == Tarlk_Raider_Lanka)
				{
					if (Lankas_Head_count == 0)
					{
						st.dropQuestItems(Lankas_Head, 1, 1, 1, 100, false);
					}
				}
				else if (npcId == Tarlk_Raider_Triska)
				{
					if (Triskas_Head_count == 0)
					{
						st.dropQuestItems(Triskas_Head, 1, 1, 1, 100, false);
					}
				}
				else if (npcId == Tarlk_Raider_Motura)
				{
					if (Moturas_Head_count == 0)
					{
						st.dropQuestItems(Moturas_Head, 1, 1, 1, 100, false);
					}
				}
				else if (npcId == Tarlk_Raider_Kalath)
				{
					if (Kalaths_Head_count == 0)
					{
						st.dropQuestItems(Kalaths_Head, 1, 1, 1, 100, false);
					}
				}
			}
		}
		
		if ((st.getQuestItemsCount(_1st_Circle_Hunter_License) > 0) || (st.getQuestItemsCount(_2nd_Circle_Hunter_License) > 0))
		{
			if ((st.getQuestItemsCount(Cybellins_Request) > 0) && (st.getPlayer().getActiveWeaponItem() != null) && (st.getPlayer().getActiveWeaponItem().getItemId() == Cybellins_Dagger))
			{
				int Blood_Crystal_Level = Get_Blood_Crystal_Level(st);
				if ((Blood_Crystal_Level > 0) && (Blood_Crystal_Level < 10))
				{
					for (int lizardmen_id : q_blood_crystal_lizardmen)
					{
						if (npcId == lizardmen_id)
						{
							if (calcChance(50))
							{
								st.takeItems(q_blood_crystal[Blood_Crystal_Level], -1);
								st.playSound(Blood_Crystal_Level < 6 ? QuestSound.ITEMSOUND_QUEST_MIDDLE : QuestSound.ITEMSOUND_QUEST_JACKPOT);
								st.giveItems(q_blood_crystal[Blood_Crystal_Level + 1], 1);
							}
							else
							{
								st.takeAllItems(q_blood_crystal);
								st.giveItems(q_blood_crystal[0], 1);
							}
						}
					}
				}
			}
			
			Request request = GetCurrentRequest(st, Requests1);
			if (request == null)
			{
				request = GetCurrentRequest(st, Requests2);
			}
			if (request != null)
			{
				if (request.droplist.containsKey(npcId))
				{
					st.dropQuestItems(request.request_item, 1, 1, request.request_count, request.droplist.get(npcId), true);
				}
				
				if (request.spawnlist.containsKey(npcId) && (st.getQuestItemsCount(request.request_item) < request.request_count))
				{
					int[] spawn_n_chance = Util.unpackLong(request.spawnlist.get(npcId), 16);
					if (calcChance(spawn_n_chance[1]))
					{
						st.addSpawn(spawn_n_chance[0], npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000);
						if (spawn_n_chance[0] == 27149)
						{
							npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 1, npc.getName(), "Show me the pretty sparkling things! They're all mine!"));
						}
					}
				}
			}
		}
		
		if (((npcId == 27160) || (npcId == 27162) || (npcId == 27164)) && calcChance(50))
		{
			npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 1, npc.getName(), "We'll take the property of the ancient empire!"));
			st.addSpawn(27150, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000); // b_legion_stormtrooper
			st.addSpawn(27150, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true, 300000); // b_legion_stormtrooper
		}
		
		return null;
	}
	
	private static final int CalcItemsConds(QuestState st, int[][][] ItemsConds)
	{
		int result = 0;
		for (int[][] ItemsCond : ItemsConds)
		{
			if (st.getQuestItemsCount(ItemsCond[0]) >= ItemsCond[1][0])
			{
				result++;
			}
		}
		return result;
	}
	
	private static void DelItemsConds(QuestState st, int[][][] ItemsConds)
	{
		for (int[][] ItemsCond : ItemsConds)
		{
			st.takeAllItems(ItemsCond[0]);
		}
	}
	
	private static final int Get_Blood_Crystal_Level(QuestState st)
	{
		for (int i = q_blood_crystal.length - 1; i >= 0; i--)
		{
			if (st.getQuestItemsCount(q_blood_crystal[i]) > 0)
			{
				return i;
			}
		}
		return -1;
	}
	
	private static boolean Blood_Crystal2Adena(QuestState st, int Blood_Crystal_Level)
	{
		if (Blood_Crystal_Level < 2)
		{
			return false;
		}
		st.takeAllItems(q_blood_crystal);
		st.giveItems(ADENA, 3400 * (int) Math.pow(2, Blood_Crystal_Level - 2));
		return true;
	}
	
	private static void GenList(QuestState st)
	{
		final int grade_c = 12;
		final int grade_b = 6;
		final int grade_a = 3;
		if (st.get("list") == null /* || st.get("list").isEmpty() */)
		{
			long Laurel_Leaf_Pin_count = st.getQuestItemsCount(Laurel_Leaf_Pin);
			int[] list = new int[5];
			if (Laurel_Leaf_Pin_count < 4)
			{
				if ((Laurel_Leaf_Pin_count == 0) || calcChance(80))
				{
					for (int i = 0; i < 5; i++)
					{
						list[i] = Rnd.get(grade_c);
					}
				}
				else
				{
					list[0] = grade_c + Rnd.get(grade_b);
					list[1] = Rnd.get(grade_c);
					list[2] = Rnd.get(grade_c / 2);
					list[3] = (grade_c / 2) + Rnd.get(grade_c / 2);
					list[4] = Rnd.get(grade_c);
				}
			}
			else if (calcChance(20))
			{
				list[0] = grade_c + Rnd.get(grade_b);
				list[1] = calcChance(5) ? grade_c + grade_b + Rnd.get(grade_a) : Rnd.get(grade_c);
				list[2] = Rnd.get(grade_c / 2);
				list[3] = (grade_c / 2) + Rnd.get(grade_c / 2);
				list[4] = Rnd.get(grade_c);
			}
			else
			{
				list[0] = Rnd.get(grade_c);
				list[1] = calcChance(5) ? grade_c + grade_b + Rnd.get(grade_a) : Rnd.get(grade_c);
				list[2] = Rnd.get(grade_c / 2);
				list[3] = (grade_c / 2) + Rnd.get(grade_c / 2);
				list[4] = Rnd.get(grade_c);
			}
			for (;;)
			{
				boolean sort_flag = false;
				for (int i = 1; i < list.length; i++)
				{
					if (list[i] < list[i - 1])
					{
						int tmp = list[i];
						list[i] = list[i - 1];
						list[i - 1] = tmp;
						sort_flag = true;
					}
				}
				if (!sort_flag)
				{
					break;
				}
			}
			int packedlist = 0;
			try
			{
				packedlist = Util.packInt(list, 5);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			st.set("list", String.valueOf(packedlist));
		}
	}
	
	private static String FormatList(QuestState st, Request[] requests)
	{
		String result = "<html><head><body>Guild Member Tor:<br>%reply%<br>%reply%<br>%reply%<br>%reply%<br>%reply%<br></body></html>";
		int[] listpacked = Util.unpackInt(st.getInt("list"), 5);
		for (int i = 0; i <= 5; i++)
		{
			String s = "<a action=\"bypass -h Quest TheSongOfTheHunter 30745-request-" + requests[listpacked[i]].request_id + "\">" + requests[listpacked[i]].text + "</a>";
			result = result.replaceFirst("%reply%", s);
		}
		return result;
	}
	
	public void onLoad()
	{
		System.out.println("Loaded Quest: 335: The Song of the Hunter");
	}
	
	public void onReload()
	{
	}
	
	public void onShutdown()
	{
	}
	
	private static class Request
	{
		protected final int request_id, request_item, request_count, reward_adena;
		protected final String text;
		protected final Map<Integer, Integer> droplist = new HashMap<>();
		protected final Map<Integer, Integer> spawnlist = new HashMap<>();
		
		protected Request(int requestid, int requestitem, int requestcount, int rewardadena, String txt)
		{
			request_id = requestid;
			request_item = requestitem;
			request_count = requestcount;
			reward_adena = rewardadena;
			text = txt;
		}
		
		public Request addDrop(int kill_mob_id, int chance)
		{
			droplist.put(kill_mob_id, chance);
			return this;
		}
		
		protected Request addSpawn(int kill_mob_id, int spawn_mob_id, int chance)
		{
			try
			{
				spawnlist.put(kill_mob_id, Util.packInt(new int[]
				{
					spawn_mob_id,
					chance
				}, 16));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return this;
		}
		
		protected boolean Complete(QuestState st)
		{
			if (st.getQuestItemsCount(request_item) < request_count)
			{
				return false;
			}
			st.takeItems(request_id, -1);
			st.takeItems(request_item, -1);
			st.playSound(QuestSound.ITEMSOUND_QUEST_MIDDLE);
			st.giveItems(Laurel_Leaf_Pin, 1);
			st.giveItems(ADENA, reward_adena);
			st.unset("list");
			return true;
		}
	}
	
	private static Request GetCurrentRequest(QuestState st, Request[] requests)
	{
		for (Request r : requests)
		{
			if (st.getQuestItemsCount(r.request_id) > 0)
			{
				return r;
			}
		}
		return null;
	}
	
	private static boolean isValidRequest(int id)
	{
		for (Request r : Requests1)
		{
			if (r.request_id == id)
			{
				return true;
			}
		}
		for (Request r : Requests2)
		{
			if (r.request_id == id)
			{
				return true;
			}
		}
		return false;
	}
}