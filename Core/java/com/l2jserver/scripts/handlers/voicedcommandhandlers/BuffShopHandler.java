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
package com.l2jserver.scripts.handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.interfaces.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.buffshop.BuffShopManager;

/**
 * @author Administrator
 */
public class BuffShopHandler implements IVoicedCommandHandler
{
	private static String[] _voicedCommands =
	{
		"buffshop"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("buffshop"))
		{
			if (BuffShopManager.ENABLE_BUFF_SHOP_MODE)
			{
				BuffShopManager.getInstance().setShop(activeChar);
			}
			else
			{
				activeChar.sendMessage("Buff Shop features is currently disabled.");
			}
		}
		
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}