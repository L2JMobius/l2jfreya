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

import com.l2jserver.gameserver.datatables.CrestTable;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2Crest;
import com.l2jserver.gameserver.model.L2Crest.CrestType;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;

public final class RequestSetPledgeCrest extends L2GameClientPacket
{
	private static final String _C__53_REQUESTSETPLEDGECREST = "[C] 53 RequestSetPledgeCrest";
	static Logger _log = Logger.getLogger(RequestSetPledgeCrest.class.getName());
	
	private int _length;
	private byte[] _data;
	
	@Override
	protected void readImpl()
	{
		_length = readD();
		if (_length > 256)
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
		
		if ((_length < 0))
		{
			activeChar.sendMessage("The size of the uploaded crest or insignia does not meet the standard requirements.");
			return;
		}
		
		if (_length > 256)
		{
			activeChar.sendMessage("The size of the image file is inappropriate. Please adjust to 16x12 pixels.");
			return;
		}
		
		final L2Clan clan = activeChar.getClan();
		if (clan == null)
		{
			return;
		}
		
		if (clan.getDissolvingExpiryTime() > System.currentTimeMillis())
		{
			activeChar.sendPacket(SystemMessageId.CANNOT_SET_CREST_WHILE_DISSOLUTION_IN_PROGRESS);
			return;
		}
		
		if (!((activeChar.getClanPrivileges() & L2Clan.CP_CL_REGISTER_CREST) == L2Clan.CP_CL_REGISTER_CREST))
		{
			activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		if (_length == 0)
		{
			if (clan.getCrestId() != 0)
			{
				clan.changeClanCrest(0);
				activeChar.sendPacket(SystemMessageId.CLAN_CREST_HAS_BEEN_DELETED);
			}
		}
		else
		{
			if (clan.getLevel() < 3)
			{
				activeChar.sendPacket(SystemMessageId.CLAN_LVL_3_NEEDED_TO_SET_CREST);
				return;
			}
			
			final L2Crest crest = CrestTable.getInstance().createCrest(_data, CrestType.PLEDGE);
			if (crest != null)
			{
				clan.changeClanCrest(crest.getId());
				activeChar.sendMessage("The crest was successfully registered.");
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__53_REQUESTSETPLEDGECREST;
	}
}
