/*
 * Copyright (C) 2004-2014 L2J Server
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
package com.l2jserver.gameserver.model.quest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.l2jserver.gameserver.model.holders.ItemHolder;

/**
 * @author L2jPDT
 */
public class QuestReward
{
	private final int exp;
	private final int sp;
	private final List<ItemHolder> rewards = new ArrayList<>(3);
	private final Map<Integer, List<ItemHolder>> randomRewards = new LinkedHashMap<>(1);
	
	public QuestReward(int exp, int sp)
	{
		this.exp = exp;
		this.sp = sp;
	}
	
	public QuestReward addReward(int itemId, int itemCount, int enchant)
	{
		rewards.add(new ItemHolder(itemId, itemCount, enchant));
		return this;
	}
	
	public Map<Integer, List<ItemHolder>> getRandomRewardsItem()
	{
		return randomRewards;
	}
	
	public List<ItemHolder> getRewardsItem()
	{
		return rewards;
	}
	
	/**
	 * this is use to add RandomReward
	 * @param chance 1~1000
	 * @param itemId
	 * @param itemCount
	 * @param enchant
	 * @return
	 */
	public QuestReward addReward(int chance, int itemId, int itemCount, int enchant)
	{
		List<ItemHolder> rrewards = randomRewards.get(chance);
		if (rrewards == null)
		{
			rrewards = new ArrayList<>(3);
			randomRewards.put(chance, rrewards);
		}
		
		rrewards.add(new ItemHolder(itemId, itemCount, enchant));
		
		return this;
	}
	
	/**
	 * @return the xp
	 */
	public int getExp()
	{
		return exp;
	}
	
	/**
	 * @return the sp
	 */
	public int getSp()
	{
		return sp;
	}
}
