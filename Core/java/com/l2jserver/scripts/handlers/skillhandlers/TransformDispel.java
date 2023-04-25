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
package com.l2jserver.scripts.handlers.skillhandlers;

import com.l2jserver.gameserver.handler.interfaces.ISkillHandler;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.templates.skills.L2SkillType;

/**
 * @author Ahmed
 */
public class TransformDispel implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.TRANSFORMDISPEL
	};
	
	/**
	 * @see com.l2jserver.gameserver.handler.interfaces.ISkillHandler#useSkill(com.l2jserver.gameserver.model.actor.L2Character, com.l2jserver.gameserver.model.L2Skill, com.l2jserver.gameserver.model.L2Object[])
	 */
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		if (!(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance pc = (L2PcInstance) activeChar;
		
		if (pc.isAlikeDead() || pc.isCursedWeaponEquipped())
		{
			return;
		}
		
		if (pc.isTransformed() || pc.isInStance())
		{
			if (pc.isFlyingMounted() && !pc.isInsideZone(L2Character.ZONE_LANDING))
			{
				pc.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.BOARD_OR_CANCEL_NOT_POSSIBLE_HERE));
			}
			else
			{
				pc.stopTransformation(true);
			}
		}
	}
	
	/**
	 * @see com.l2jserver.gameserver.handler.interfaces.ISkillHandler#getSkillIds()
	 */
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}