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
package com.l2jserver.gameserver.network.serverpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.4.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public final class CreatureSay extends L2GameServerPacket
{
	// ddSS
	private final int _objectId;
	private final int _textType;
	private String _charName = null;
	private int _charId = 0;
	private String _text = null;
	private int _msgId = 0;
	
	/**
	 * @param _characters
	 */
	public CreatureSay(int objectId, int messageType, String charName, String text)
	{
		_objectId = objectId;
		_textType = messageType;
		_charName = charName;
		_text = text;
	}
	
	public CreatureSay(int objectId, int messageType, int charId, int msgId)
	{
		_objectId = objectId;
		_textType = messageType;
		_charId = charId;
		_msgId = msgId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4a);
		writeD(_objectId);
		writeD(_textType);
		if (_charName != null)
		{
			writeS(_charName);
		}
		else
		{
			writeD(_charId);
		}
		if (_text != null)
		{
			writeS(_text);
		}
		else
		{
			writeD(_msgId);
		}
	}
	
	@Override
	public final void runImpl()
	{
		L2PcInstance _pci = getClient().getActiveChar();
		if (_pci != null)
		{
			_pci.broadcastSnoop(_textType, _charName, _text);
		}
	}
}
