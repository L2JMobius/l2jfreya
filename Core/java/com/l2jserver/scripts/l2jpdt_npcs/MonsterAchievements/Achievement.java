package com.l2jserver.scripts.l2jpdt_npcs.MonsterAchievements;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

public class Achievement
{
	static String SQL = "INSERT INTO character_premium_items (charId,itemNum,itemId,itemCount,itemSender) VALUES(?,?,?,?,?)";
	static String SENDER_NAME = "Server";
	
	public int startLevel;
	public int mobId; // key
	public String huntLocation;
	public int[] maxkills;
	public int expRewards[];
	public int spRewards[];
	public int fameRewards[];
	public int[][][] itemRewards;
	
	public int getMaxStage()
	{
		return maxkills.length;
	}
	
	public int getStage(int progress)
	{
		for (int i = 0; i < maxkills.length; i++)
		{
			if (progress < maxkills[i])
			{
				return i;
			}
		}
		return maxkills.length;
	}
	
	public boolean hasGotReward(L2PcInstance player, int stage)
	{
		int mask = 1 << stage;
		int flag = player.getVariables().getInt(getRewardVarName(), 0);
		return ((flag & mask) == mask);
	}
	
	public int availableAdward(L2PcInstance player)
	{
		int process = player.getVariables().getInt(getKillProgressVarName(), 0);
		for (int i = 0; i < maxkills.length; i++)
		{
			if (hasGotReward(player, i))
			{
				continue;
			}
			
			if (process >= maxkills[i])
			{
				return i;
			}
		}
		player.store();
		return -1;
	}
	
	public int getCurrentProgress(L2PcInstance player)
	{
		return player.getVariables().getInt(getKillProgressVarName(), 0);
	}
	
	public int getMaxProgerss(L2PcInstance player)
	{
		int progress = getCurrentProgress(player);
		for (int maxkill : maxkills)
		{
			if (progress < maxkill)
			{
				return maxkill;
			}
		}
		return maxkills[maxkills.length - 1];
	}
	
	public void checkProgress(L2PcInstance player)
	{
		synchronized (player)
		{
			if (player.getLevel() < startLevel)
			{
				return;
			}
			
			int process = player.getVariables().getInt(getKillProgressVarName(), 0);
			int stage = getStage(process);
			if (stage == maxkills.length)// finished!
			{
				return;
			}
			
			process++;
			if ((process == maxkills[stage]))
			{
				player.sendPacket(new ExShowScreenMessage("Shauma: for completing my achievements I giving you some gifts.# Visit me !!!", 5000));
				player.store();
			}
			
			player.getVariables().set(getKillProgressVarName(), process);
		}
	}
	
	public String getRewardVarName()
	{
		return "Achive_Reward" + "@" + mobId;
	}
	
	public String getKillProgressVarName()
	{
		return "Achive_Kill" + "@" + mobId;
	}
	
	public void takeRewards(L2PcInstance activeChar, int stage)
	{
		if (hasGotReward(activeChar, stage))
		{
			return;
		}
		
		int exp = 0, sp = 0, fame = 0;
		if ((expRewards != null) && (expRewards.length > stage))
		{
			exp = expRewards[stage];
		}
		
		if ((spRewards != null) && (spRewards.length > stage))
		{
			sp = spRewards[stage];
		}
		
		if ((exp > 0) || (sp > 0))
		{
			activeChar.addExpAndSp(exp, sp);
		}
		
		if ((fameRewards != null) && (fameRewards.length > stage))
		{
			fame = fameRewards[stage];
			activeChar.setFame(activeChar.getFame() + fame);
		}
		
		if ((itemRewards != null) && (itemRewards.length > stage))
		{
			for (int[] items : itemRewards[stage])
			{
				activeChar.addItem("AchievementReward", items[0], items[1], activeChar, true);
			}
		}
		int flag = activeChar.getVariables().getInt(getRewardVarName(), 0);
		flag |= (1 << stage);
		activeChar.getVariables().set(getRewardVarName(), flag);
		activeChar.store();
	}
}