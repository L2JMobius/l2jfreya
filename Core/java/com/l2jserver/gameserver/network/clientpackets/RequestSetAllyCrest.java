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
package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Logger;

import com.l2jserver.gameserver.datatables.ClanTable;
import com.l2jserver.gameserver.datatables.CrestTable;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2Crest;
import com.l2jserver.gameserver.model.L2Crest.CrestType;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * Client packet for setting ally crest.
 */
public final class RequestSetAllyCrest extends L2GameClientPacket
{
	private static final String _C__87_REQUESTSETALLYCREST = "[C] 87 RequestSetAllyCrest";
	static Logger _log = Logger.getLogger(RequestSetAllyCrest.class.getName());
	
	private int _length;
	private byte[] _data = null;
	
	@Override
	protected void readImpl()
	{
		_length = readD();
		if (_length > 192)
		{
			return;
		}
		
		_data = new byte[_length];
		readB(_data);
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (_length < 0)
		{
			activeChar.sendMessage("File transfer error.");
			return;
		}
		
		if (_length > 192)
		{
			activeChar.sendPacket(SystemMessageId.ADJUST_IMAGE_8_12);
			return;
		}
		
		if (activeChar.getAllyId() == 0)
		{
			activeChar.sendPacket(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
			return;
		}
		
		final L2Clan leaderClan = ClanTable.getInstance().getClan(activeChar.getAllyId());
		
		if ((activeChar.getClanId() != leaderClan.getClanId()) || !activeChar.isClanLeader())
		{
			activeChar.sendPacket(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
			return;
		}
		
		if (_length == 0)
		{
			if (leaderClan.getAllyCrestId() != 0)
			{
				leaderClan.changeAllyCrest(0, false);
			}
		}
		else
		{
			final L2Crest crest = CrestTable.getInstance().createCrest(_data, CrestType.ALLY);
			if (crest != null)
			{
				leaderClan.changeAllyCrest(crest.getId(), false);
				activeChar.sendMessage("The crest was successfully registered.");
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__87_REQUESTSETALLYCREST;
	}
}
