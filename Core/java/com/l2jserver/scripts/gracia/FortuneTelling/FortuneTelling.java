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
package com.l2jserver.scripts.gracia.FortuneTelling;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.scripts.L2AttackableAIScript;

/**
 * Fortune Telling AI.<br>
 * Original Jython script by Kerberos.
 * @author Nyaran
 */
public class FortuneTelling extends L2AttackableAIScript
{
	// NPC
	private static final int MINE = 32616;
	
	public FortuneTelling()
	{
		super(-1, FortuneTelling.class.getSimpleName(), "gracia");
		StartNpcs(MINE);
		TalkNpcs(MINE);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		if (player.getAdena() < 1000)
		{
			htmltext = "lowadena.htm";
		}
		else
		{
			takeItems(player, Inventory.ADENA_ID, 1000);
			htmltext = getHtm(player, "fortune.htm").replace("%fortune%", String.valueOf(getRandom(1800309, 1800695)));
		}
		return htmltext;
	}
}