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
 * this program. If not, see <http://l2jpsproject.eu/>.
 */
package com.l2jserver.gameserver.communitybbs.Managers;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ShowBoard;

public class AdminBBSManager extends BaseBBSManager
{
	public static AdminBBSManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private AdminBBSManager()
	{
	}
	
	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
		if (!activeChar.isGM())
		{
			return;
		}
		if (command.startsWith("admin_bbs"))
		{
			separateAndSend("<html><body><br><br><center>This Page is only an exemple :)<br><br>command=" + command + "</center></body></html>", activeChar);
		}
		else
		{
			
			ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + command + " is not implemented yet</center><br><br></body></html>", "101");
			activeChar.sendPacket(sb);
			activeChar.sendPacket(new ShowBoard(null, "102"));
			activeChar.sendPacket(new ShowBoard(null, "103"));
		}
		
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
		if (!activeChar.isGM())
		{
			return;
		}
		
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AdminBBSManager _instance = new AdminBBSManager();
	}
}