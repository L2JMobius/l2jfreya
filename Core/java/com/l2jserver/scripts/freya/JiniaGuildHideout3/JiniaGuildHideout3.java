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
package com.l2jserver.scripts.freya.JiniaGuildHideout3;

import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.instancemanager.InstanceManager.InstanceWorld;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.scripts.L2AttackableAIScript;
import com.l2jserver.scripts.quests.Q10286_ReunionWithSirra;

/**
 * Jinia Guild Hideout instance zone.
 * @author Adry_85
 */
public final class JiniaGuildHideout3 extends L2AttackableAIScript
{
	protected class JGH3World extends InstanceWorld
	{
		
	}
	
	// NPC
	private static final int RAFFORTY = 32020;
	// Location
	private static final Location START_LOC = new Location(-23530, -8963, -5413, 0, 0);
	// Misc
	private static final int TEMPLATE_ID = 145;
	
	public JiniaGuildHideout3()
	{
		super(-1, JiniaGuildHideout3.class.getSimpleName(), "freya");
		StartNpcs(RAFFORTY);
		TalkNpcs(RAFFORTY);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		final QuestState qs = talker.getQuestState(Q10286_ReunionWithSirra.class.getSimpleName());
		if ((qs != null) && qs.isMemoState(1))
		{
			enterInstance(talker, new JGH3World(), "JiniaGuildHideout3.xml", TEMPLATE_ID);
			qs.setCond(2, true);
		}
		return super.onTalk(npc, talker);
	}
	
	protected int enterInstance(L2PcInstance player, InstanceWorld instance, String template, int templateId)
	{
		int instanceId = 0;
		InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
		
		if (world != null)
		{
			if (!(world instanceof JGH3World))
			{
				player.sendPacket(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER);
				return 0;
			}
			managePlayerEnter(player, (JGH3World) world);
			player.setInstanceId(instanceId);
			return world.instanceId;
		}
		else
		{
			
			instanceId = InstanceManager.getInstance().createDynamicInstance(template);
			world = new JGH3World();
			world.templateId = TEMPLATE_ID;
			world.instanceId = instanceId;
			world.status = 0;
			player.setInstanceId(instanceId);
			InstanceManager.getInstance().addWorld(world);
			_log.info("Jinia Hideout 3 " + template + " Instance: " + instanceId + " created by player: " + player.getName());
			managePlayerEnter(player, (JGH3World) world);
			return instanceId;
		}
	}
	
	private void managePlayerEnter(L2PcInstance player, JGH3World world)
	{
		world.allowed.add(player.getObjectId());
		teleportPlayer(player, START_LOC, world.getInstanceId(), false);
	}
}